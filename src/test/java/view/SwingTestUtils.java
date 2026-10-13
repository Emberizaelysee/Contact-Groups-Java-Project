package view;

import java.lang.reflect.Field;

/** Reflection helper for reading private Swing fields in view-layer tests without widening production visibility. */
final class SwingTestUtils {

    private SwingTestUtils() {
    }

    @SuppressWarnings("unchecked")
    static <T> T getField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not read field '" + fieldName + "' from " + target.getClass(), e);
        }
    }
}
