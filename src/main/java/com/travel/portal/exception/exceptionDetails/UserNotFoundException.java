package com.travel.portal.exception.exceptionDetails;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(int userId) {
        super("User not found with id: " + userId);
    }
}
