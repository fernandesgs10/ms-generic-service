package br.com.generic.service.enums;

public enum AuthEnum {
    ADMIN,
    USER;

    public static AuthEnum fromString(String type) {
        try {
            return AuthEnum.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown operation type: " + type, e);
        }
    }
}

