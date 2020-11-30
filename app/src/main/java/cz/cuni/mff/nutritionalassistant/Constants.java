package cz.cuni.mff.nutritionalassistant;

final class Constants {

    //REQUESTS
    static final int VALUES_REQUEST = 1;
    static final int MANUAL_REQUEST = 2;
    static final int AUTOMATIC_REQUEST = 3;
    static final int FOOD_REQUEST = 4;
    static final int PARAMETERS_REQUEST = 5;

    //RESULTS
    static final int RESULT_AUTOMATIC_FAILURE = 2;

    //USER PARAMETERS
    static enum Sex {MALE, FEMALE}

    static enum Lifestyle {SEDENTARY, MILD_ACTIVITY, MEDIUM_ACTIVITY, HIGH_ACTIVITY}

    static enum Goal {GAIN, LOSE, MAINTAIN}

    private Constants() {
    }
}
