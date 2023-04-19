package project.dashboard.internal;

public class ArgumentGuard {
    public static void AssertStringNotNullOrEmpty(String value)
    {
        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Given string value can't be null or empty string");
    }
}
