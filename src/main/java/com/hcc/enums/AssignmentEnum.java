package com.hcc.enums;

public enum AssignmentEnum {

    ASSIGNMENT_ONE,
    ASSIGNMENT_TWO;

    public static boolean isValidName(String name) {
        for (AssignmentEnum value : AssignmentEnum.values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
