package asia.fourtitude.interviewq.jumble.util;

public class Validator {

    public static boolean isValidChar(Character c) {
        if (null != c) {
            c = Character.toLowerCase(c);
            return c != null && c >= 'a' && c <= 'z'; // Check if character is a lowercase letter
        }
        return true;
    }
}
