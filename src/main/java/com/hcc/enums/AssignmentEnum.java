package com.hcc.enums;

public enum AssignmentEnum {

    ASSIGNMENT_ONE,
    ASSIGNMENT_TWO,
    ASSIGNMENT_THREE,
    ASSIGNMENT_FOUR,
    ASSIGNMENT_FIVE,
    ASSIGNMENT_SIX,
    ASSIGNMENT_SEVEN,
    ASSIGNMENT_EIGHT,
    ASSIGNMENT_NINE;

    public static boolean isValidName(String name) {
        for (AssignmentEnum value : AssignmentEnum.values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
