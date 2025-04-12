for (i = 0; i < data.length; i++) {
    for (j = 0; j < data[0].length; j++) {
        // Precompute the constant value used in every iteration of the k-loop.
        double limitSquare = limit[i][j] * limit[i][j];
        
        // Pre-calculate the average for data[i][j] if used repeatedly.
        double avgDataIJ = average(data[i][j]);
        
        for (k = 0; k < data[0][0].length; k++) {
            // Calculate the new value once using the precomputed limitSquare.
            data2[i][j][k] = data[i][j][k] / d - limitSquare;
            
            // Compute average of data2[i][j] only once per iteration.
            double avgData2IJ = average(data2[i][j]);
            
            // Condition 1: If the average of data2 is within the desired range, break out.
            if (avgData2IJ > 10 && avgData2IJ < 50) {
                break;
            }
            // Condition 2: Check if the maximum between current data values exceeds data[i][j][k].
            else if (Math.max(data[i][j][k], data2[i][j][k]) > data[i][j][k]) {
                break;
            }
            // Condition 3: Use multiplication instead of Math.pow for cube comparisons.
            else if ((Math.abs(data[i][j][k]) * Math.abs(data[i][j][k]) * Math.abs(data[i][j][k]) < 
                      Math.abs(data2[i][j][k]) * Math.abs(data2[i][j][k]) * Math.abs(data2[i][j][k]))
                     && (avgDataIJ < data2[i][j][k])
                     && ((i + 1) * (j + 1) > 0)) {
                data2[i][j][k] *= 2;
            }
            else {
                continue;
            }
        }
    }
}
