package cz.cuni.mff.nutritionalassistant;

final public class Constants {

    // Requests
    public static final int VALUES_REQUEST = 1;
    public static final int MANUAL_REQUEST = 2;
    public static final int AUTOMATIC_REQUEST = 3;
    public static final int FOOD_REQUEST = 4;
    public static final int PARAMETERS_REQUEST = 5;

    // Results
    public static final int RESULT_AUTOMATIC_FAILURE = 2;

    // User parameters
    public static enum Sex {MALE, FEMALE}

    public static enum Lifestyle {SEDENTARY, MILD_ACTIVITY, MEDIUM_ACTIVITY, HIGH_ACTIVITY}

    public static enum Goal {GAIN, LOSE, MAINTAIN}

    private Constants() {
    }
}
