package tarleton;

/**
 *
 * @author Richard
 */
public class StringIndexer {

    private String targetString;
    private String searchString;
    private int currentPosition;
    private boolean searchWrap = false;
    private boolean found = false;

    public StringIndexer(String text,
            String searchString,
            int startingPosition,
            boolean searchWrap,
            boolean ignoreCase) {
        if (ignoreCase) {
            this.targetString = text.toLowerCase();
            this.searchString = searchString.toLowerCase();
        } else {
            this.targetString = text;
            this.searchString = searchString;
        }
        this.currentPosition = startingPosition;
        this.searchWrap = searchWrap;
    }

    public int nextStringIndex() {
        int position;
        if (currentPosition >= targetString.length()) {
            // End of target string reached
            if (searchWrap) {
                currentPosition = 0;
            }
        }
        position = targetString.indexOf(searchString, currentPosition);
        if (position == -1) {
            // searchString was not found
            if (found && searchWrap) {
                currentPosition = 0;
                position = targetString.indexOf(searchString, currentPosition);
            } else {
                return -1;
            }
        } else {
            found = true;
        }
        currentPosition = position + 1;

        return position;
    }

    public int previousStringIndex() {
        int position;
        int targetStringLength = targetString.length();
        int endOfStringIndex = targetStringLength - 1;

        if (currentPosition >= targetStringLength) {
            // End of target string reached
            if (searchWrap) {
                currentPosition = endOfStringIndex;
            }
        }
        position = targetString.lastIndexOf(searchString, currentPosition);
        if (position == -1) {
            // searchString was not found
            if (found && searchWrap) {
                currentPosition = endOfStringIndex;
                position = targetString.lastIndexOf(searchString, currentPosition);
            } else {
                return -1;
            }
        } else {
            found = true;
        }
        currentPosition = position - 1;

        return position;
    }
}
