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
    //static final int MALE = 0;
    //static final int FEMALE = 1;

    static enum Lifestyle {SEDENTARY, MILD_ACTIVITY, MEDIUM_ACTIVITY, HIGH_ACTIVITY}
    /*static final int SEDENTARY = 0;
    static final int MILD_ACTIVITY = 1;
    static final int MEDIUM_ACTIVITY = 2;
    static final int HIGH_ACTIVITY = 3;*/

    static enum Goal {GAIN, LOSE, MAINTAIN}
    /*static final int GAIN = 0;
    static final int LOSE = 1;
    static final int MAINTAIN = 2;*/

    private Constants() {
    }
}
