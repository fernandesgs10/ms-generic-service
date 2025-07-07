package br.com.generic.service.enums;

public enum OperationEnum {
    SAVE,
    UPDATE,
    DELETE;

    public static OperationEnum fromString(String type) {
        try {
            return OperationEnum.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown operation type: " + type, e);
        }
    }
}

