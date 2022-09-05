package sakura.kooi.VirtualGraphicTablets.server.bootstrap.logger;

public class FormattingTuple {
    public static FormattingTuple NULL = new FormattingTuple(null);
    private final String message;
    private final Throwable throwable;
    private final Object[] argArray;

    public FormattingTuple(final String message) {
        this(message, null, null);
    }

    public FormattingTuple(final String message, final Object[] argArray, final Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
        this.argArray = throwable == null ? argArray : FormattingTuple.trimmedCopy(argArray);
    }

    private static Object[] trimmedCopy(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) throw new IllegalStateException("non-sensical empty or null argument array");
        final int trimmedLen = argArray.length - 1;
        final Object[] trimmed = new Object[trimmedLen];
        System.arraycopy(argArray, 0, trimmed, 0, trimmedLen);
        return trimmed;
    }

    public String getMessage() {
        return message;
    }

    public Object[] getArgArray() {
        return argArray;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
