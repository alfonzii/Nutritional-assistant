package cz.cuni.mff.nutritionalassistant.utils;

public class FormatUtil {

    public static float twoDecimalsRound(float num) {
        return Math.round(num * 100) / 100.0f;
    }

    // returns two digit decimal float number if num is decimal and returns integer if non-decimal
    public static String correctStringFormat(float num) {
        final float EPSILON = 0.001f;

        if (Math.abs(Math.round(num) - num) < EPSILON) {
            return String.valueOf(Math.round(num));
        } else { // number is decimal
            return String.valueOf(twoDecimalsRound(num));
        }
    }

    public static String roundedStringFormat(float num) {
        return String.valueOf(Math.round(num));
    }
}
