/**
 * Creates a deep clone of the specified sheet with a new name. The cloned sheet
 * reproduces the original content and relationships, with adjustments for unsupported
 * features (e.g. legacy drawing and page setup). This method is broken into smaller
 * helper functions for improved clarity and maintainability.
 *
 * @param sheetNum the index of the sheet to clone
 * @param newName the name for the cloned sheet; if null, a unique name is generated
 * @return the cloned XSSFSheet object
 * @throws IllegalArgumentException if the sheet index or provided name is invalid
 * @throws POIXMLException if an error occurs during cloning
 */
public XSSFSheet cloneSheetRefactored(int sheetNum, String newName) {
    validateSheetIndex(sheetNum);
    XSSFSheet sourceSheet = sheets.get(sheetNum);
    String cloneSheetName = determineCloneSheetName(sourceSheet, newName);
    XSSFSheet clonedSheet = createSheet(cloneSheetName);

    // Clone non-drawing relationships and capture the drawing (if any) for later processing.
    XSSFDrawing sourceDrawing = cloneSheetRelations(sourceSheet, clonedSheet);

    // Clone external relationships from the source sheet.
    cloneExternalRelationships(sourceSheet, clonedSheet);

    // Clone the actual sheet content by serializing and deserializing.
    cloneSheetContent(sourceSheet, clonedSheet);

    // Perform post-processing on the cloned sheet (e.g. unsetting legacy drawing, page setup).
    postProcessClonedSheet(clonedSheet);

    // If a drawing exists in the source sheet, clone it along with its relationships.
    if (sourceDrawing != null) {
        cloneDrawingIfNeeded(sourceSheet, clonedSheet, sourceDrawing);
    }
    return clonedSheet;
}

/**
 * Determines the appropriate name for the cloned sheet.
 * If a new name is not provided, generates a unique name based on the source sheet's name.
 *
 * @param srcSheet the source sheet being cloned
 * @param newName the desired new name; may be null
 * @return a valid, unique sheet name for the clone
 */
private String determineCloneSheetName(XSSFSheet srcSheet, String newName) {
    if (newName == null) {
        return getUniqueSheetName(srcSheet.getSheetName());
    } else {
        validateSheetName(newName);
        return newName;
    }
}

/**
 * Clones all non-drawing relationships from the source sheet to the cloned sheet.
 * If a drawing is found, it is returned for later processing.
 *
 * @param srcSheet the source sheet
 * @param destSheet the cloned sheet
 * @return the XSSFDrawing from the source sheet, or null if none exists
 */
private XSSFDrawing cloneSheetRelations(XSSFSheet srcSheet, XSSFSheet destSheet) {
    XSSFDrawing sourceDrawing = null;
    List<RelationPart> relations = srcSheet.getRelationParts();
    for (RelationPart rp : relations) {
        POIXMLDocumentPart part = rp.getDocumentPart();
        if (part instanceof XSSFDrawing) {
            // Do not copy drawing relation now; handle it separately.
            sourceDrawing = (XSSFDrawing) part;
        } else {
            addRelation(rp, destSheet);
        }
    }
    return sourceDrawing;
}

/**
 * Clones the external relationships from the source sheet to the cloned sheet.
 *
 * @param srcSheet the source sheet
 * @param destSheet the cloned sheet
 * @throws POIXMLException if an InvalidFormatException occurs during cloning
 */
private void cloneExternalRelationships(XSSFSheet srcSheet, XSSFSheet destSheet) {
    try {
        for (PackageRelationship pr : srcSheet.getPackagePart().getRelationships()) {
            if (pr.getTargetMode() == TargetMode.EXTERNAL) {
                destSheet.getPackagePart().addExternalRelationship(
                        pr.getTargetURI().toASCIIString(), pr.getRelationshipType(), pr.getId());
            }
        }
    } catch (InvalidFormatException e) {
        throw new POIXMLException("Failed to clone external relationships", e);
    }
}

/**
 * Clones the sheet content by writing the source sheet to a byte array and reading it into the clone.
 *
 * @param srcSheet the source sheet
 * @param destSheet the cloned sheet
 * @throws POIXMLException if an IOException occurs during cloning
 */
private void cloneSheetContent(XSSFSheet srcSheet, XSSFSheet destSheet) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        srcSheet.write(baos);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray())) {
            destSheet.read(bais);
        }
    } catch (IOException e) {
        throw new POIXMLException("Failed to clone sheet content", e);
    }
}

/**
 * Performs post-processing on the cloned sheet such as clearing legacy drawing and page setup.
 *
 * @param sheet the cloned sheet to process
 */
private void postProcessClonedSheet(XSSFSheet sheet) {
    CTWorksheet ctWorksheet = sheet.getCTWorksheet();
    if (ctWorksheet.isSetLegacyDrawing()) {
        logger.log(POILogger.WARN, "Cloning sheets with comments is not yet supported.");
        ctWorksheet.unsetLegacyDrawing();
    }
    if (ctWorksheet.isSetPageSetup()) {
        logger.log(POILogger.WARN, "Cloning sheets with page setup is not yet supported.");
        ctWorksheet.unsetPageSetup();
    }
    sheet.setSelected(false);
}

/**
 * Clones the drawing from the source sheet to the cloned sheet and replicates its relationships.
 *
 * @param srcSheet the source sheet containing the drawing
 * @param destSheet the cloned sheet
 * @param sourceDrawing the drawing to clone
 */
private void cloneDrawingIfNeeded(XSSFSheet srcSheet, XSSFSheet destSheet, XSSFDrawing sourceDrawing) {
    CTWorksheet ct = destSheet.getCTWorksheet();
    if (ct.isSetDrawing()) {
        // Remove existing drawing reference to ensure new drawing is created.
        ct.unsetDrawing();
    }
    XSSFDrawing clonedDrawing = destSheet.createDrawingPatriarch();
    // Copy the drawing contents.
    clonedDrawing.getCTDrawing().set(sourceDrawing.getCTDrawing());
    
    // Clone drawing relationships from the source drawing.
    List<RelationPart> drawingRelations = srcSheet.createDrawingPatriarch().getRelationParts();
    for (RelationPart rp : drawingRelations) {
        addRelation(rp, clonedDrawing);
    }
}
