package LocalData;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for parsing CSV lines.
 * Handles quoted fields and escaped double-quote characters.
 * @author Intisaar
 */
public final class Csv {

    private Csv() {
    }

    /**
     * Parses a single CSV line into a list of field values.
     * Supports quoted fields containing commas and escaped double quotes.
     * @param line the CSV line to parse
     * @return list of field values in the order they appear in the line
     */
    public static List<String> parseLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);

            if (quoted) {
                if (character == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++;
                    } else {
                        quoted = false;
                    }
                } else {
                    current.append(character);
                }
            } else {
                if (character == '"') {
                    quoted = true;
                } else if (character == ',') {
                    values.add(current.toString());
                    current.setLength(0);
                } else {
                    current.append(character);
                }
            }
        }

        values.add(current.toString());
        return values;
    }
}