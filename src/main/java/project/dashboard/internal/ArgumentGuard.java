package project.dashboard.internal;

public class ArgumentGuard {
    public static void AssertStringNotNullOrEmpty(String value) throws IllegalArgumentException
    {
        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Given string value can't be null or empty string");
    }

    public static void AssertEmailIsValid(String value) throws IllegalArgumentException {
        if (!EmailValidator.isValid(value))
            throw new IllegalArgumentException("Given email address is not a valid email address");
    }
}
