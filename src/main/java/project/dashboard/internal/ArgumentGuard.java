package project.dashboard.internal;

public class ArgumentGuard {
    public static void assertStringNotNullOrEmpty(String value) throws IllegalArgumentException
    {
        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Given string value can't be null or empty string");
    }

    public static void assertEmailIsValid(String value) throws IllegalArgumentException {
        if (!EmailValidator.isValid(value))
            throw new IllegalArgumentException("Given email address is not a valid email address");
    }

    public static <T> void assertNotNull(T item) {
        if (item == null)
            throw new IllegalArgumentException("Given object must nut be a null pointer");
    }

    @SafeVarargs
    public static <T> void assertNotNull(T... items) {
        for (var item : items)
            assertNotNull(item);
    }
}
