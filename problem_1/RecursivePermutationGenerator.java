public class RecursivePermutationGenerator {

    /**
     * Recursively builds and prints all permutations by appending one character at a time.
     *
     * @param prefix    the current permutation constructed so far
     * @param remaining the remaining characters to permute
     */
    private static void permute(String prefix, String remaining) {
        if (remaining.isEmpty()) {
            System.out.println(prefix);
        } else {
            for (int i = 0; i < remaining.length(); i++) {
                // Create new prefix by appending the current character.
                String newPrefix = prefix + remaining.charAt(i);
                // Remove the used character from remaining.
                String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
                permute(newPrefix, newRemaining);
            }
        }
    }

    /**
     * Validates input and starts the recursive permutation generation.
     */
    public static void generatePermutations(String input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Invalid input string.");
            return;
        }
        permute("", input);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java RecursivePermutationGenerator <input_string>");
            return;
        }
        generatePermutations(args[0]);
    }
}
