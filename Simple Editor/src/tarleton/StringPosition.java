/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tarleton;

/**
 *
 * @author Richard
 */
public class StringPosition {

    private static String target;

    public StringPosition(String target) {
        this.target = target;
    }

    public static int getPosition(int num) {
        if (num < 0 || num > target.length()) {
            // End of string
        } else {
            if (num == 1) {
                return 0;
            } else {
                int count = 1;
                int oldPosition = 0;
                for (int i = 0; i < target.length(); i++) {
                    if (target.charAt(i) == '\n') {
                        if (count == num) {
                            return oldPosition;
                        } else {
                            count++;
                            oldPosition = i + 1;
                        }
                    } else {
                        // Check next character
                    }
                    if (i == target.length() - 1) {
                        if (count == num) {
                            return oldPosition;
                        }
                    }
                }
            }

        }

    }
}
