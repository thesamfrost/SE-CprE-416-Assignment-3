import java.util.Arrays;

public class IterativePermutationGenerator {

    /**
     * Generates the next lexicographic permutation of the string.
     * Returns null if the current permutation is the highest possible.
     */
    private static String nextPermutation(String str) {
        char[] arr = str.toCharArray();
        int i = arr.length - 2;
        // Find the first character that is smaller than its next character.
        while (i >= 0 && arr[i] >= arr[i + 1]) {
            i--;
        }
        if (i < 0) { // No further permutations.
            return null;
        }
        int j = arr.length - 1;
        // Find the smallest character on right side of arr[i] that is greater than arr[i].
        while (arr[j] <= arr[i]) {
            j--;
        }
        // Swap arr[i] and arr[j].
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        // Reverse the sub-array from i+1 to the end.
        int start = i + 1, end = arr.length - 1;
        while (start < end) {
            temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
        return new String(arr);
    }

    /**
     * Generates and prints all lexicographic permutations of the given input string.
     */
    public static void generatePermutations(String input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Invalid input string.");
            return;
        }
        // Sort input to start from the lexicographically smallest permutation.
        char[] sortedChars = input.toCharArray();
        Arrays.sort(sortedChars);
        String permutation = new String(sortedChars);
        System.out.println(permutation);
        while ((permutation = nextPermutation(permutation)) != null) {
            System.out.println(permutation);
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java IterativePermutationGenerator <input_string>");
            return;
        }
        generatePermutations(args[0]);
    }
}
