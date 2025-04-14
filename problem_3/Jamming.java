// Begin code jamming for lines 36–58 (integrated write call)
for (i = 0; i < data.length; i++) {
    for (j = 0; j < data[0].length; j++) {
        // Precompute constant values per row
        double limitSquare = limit[i][j] * limit[i][j];
        double avgDataIJ = average(data[i][j]);
        
        // Process each element in the current row (data[i][j])
        for (k = 0; k < data[0][0].length; k++) {
            // Compute adjusted data value using precomputed limitSquare
            data2[i][j][k] = data[i][j][k] / d - limitSquare;
            // Compute average for the current computed row from data2
            double avgData2IJ = average(data2[i][j]);
            
            // Condition 1: If average is within [10,50), stop processing further elements in this row.
            if (avgData2IJ > 10 && avgData2IJ < 50) {
                break;
            }
            // Condition 2: If maximum of current values exceeds original, stop processing.
            else if (Math.max(data[i][j][k], data2[i][j][k]) > data[i][j][k]) {
                break;
            }
            // Condition 3: Compare cubes via multiplication rather than Math.pow,
            // and if conditions met, multiply the current value by 2.
            else if ((Math.abs(data[i][j][k]) * Math.abs(data[i][j][k]) * Math.abs(data[i][j][k]) <
                      Math.abs(data2[i][j][k]) * Math.abs(data2[i][j][k]) * Math.abs(data2[i][j][k]))
                     && (avgDataIJ < data2[i][j][k])
                     && ((i + 1) * (j + 1) > 0)) {
                data2[i][j][k] *= 2;
            }
            else {
                continue;
            }
        } // End k-loop
        
        // After processing each row (i, j), write the row to the output.
        // The helper function converts the double array to a string.
        out.write(convertArrayToString(data2[i][j]) + "\t");
    } // End j-loop
} // End i-loop

/**
 * Helper method to convert an array of double values to a formatted string.
 * For example, {1.2, 3.4, 5.6} becomes "1.2,3.4,5.6".
 *
 * @param array the array of doubles to convert
 * @return a string representation of the array
 */
private String convertArrayToString(double[] array) {
    StringBuilder sb = new StringBuilder();
    for (int index = 0; index < array.length; index++) {
        sb.append(array[index]);
        if (index < array.length - 1) {
            sb.append(",");
        }
    }
    return sb.toString();
}
