/*
 * Copyright (C) 2019. SakuraKooi(sakurakoi993519867@gmail.com) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package sakura.kooi.VirtualGraphicTablets.server.bootstrap.logger;

import java.util.HashMap;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@SuppressWarnings("SpellCheckingInspection")
final class MessageFormatter {
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    private static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    public static FormattingTuple format(final String messagePattern, final Object arg) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg});
    }

    public static FormattingTuple format(final String messagePattern, final Object arg1, final Object arg2) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg1, arg2});
    }

    private static Throwable getThrowableCandidate(final Object[] argArray) {
        if (argArray == null || argArray.length == 0)
			return null;
        final Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable)
			return (Throwable)lastEntry;
        return null;
    }

    public static FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray) {
        int L;
        final Throwable throwableCandidate = MessageFormatter.getThrowableCandidate(argArray);
        if (messagePattern == null)
			return new FormattingTuple(null, argArray, throwableCandidate);
        if (argArray == null)
			return new FormattingTuple(messagePattern);
        int i = 0;
        final StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        for (L = 0; L < argArray.length; ++L) {
            final int j = messagePattern.indexOf(DELIM_STR, i);
            if (j == -1) {
                if (i == 0)
					return new FormattingTuple(messagePattern, argArray, throwableCandidate);
                sbuf.append(messagePattern.substring(i));
                return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
            }
            if (MessageFormatter.isEscapedDelimeter(messagePattern, j)) {
                if (!MessageFormatter.isDoubleEscaped(messagePattern, j)) {
                    --L;
                    sbuf.append(messagePattern, i, j - 1);
                    sbuf.append('{');
                    i = j + 1;
                    continue;
                }
                sbuf.append(messagePattern, i, j - 1);
                MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap());
                i = j + 2;
                continue;
            }
            sbuf.append(messagePattern, i, j);
            MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap());
            i = j + 2;
        }
        sbuf.append(messagePattern.substring(i));
        if (L < argArray.length - 1)
			return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
        return new FormattingTuple(sbuf.toString(), argArray, null);
    }

    private static boolean isEscapedDelimeter(final String messagePattern, final int delimeterStartIndex) {
        if (delimeterStartIndex == 0)
			return false;
        final char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        return potentialEscape == '\\';
    }

    private static boolean isDoubleEscaped(final String messagePattern, final int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\';
    }

    private static void deeplyAppendParameter(final StringBuilder sbuf, final Object o, final Map<Object[], Object> seenMap) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            MessageFormatter.safeObjectAppend(sbuf, o);
        } else if (o instanceof boolean[]) {
            MessageFormatter.booleanArrayAppend(sbuf, (boolean[])o);
        } else if (o instanceof byte[]) {
            MessageFormatter.byteArrayAppend(sbuf, (byte[])o);
        } else if (o instanceof char[]) {
            MessageFormatter.charArrayAppend(sbuf, (char[])o);
        } else if (o instanceof short[]) {
            MessageFormatter.shortArrayAppend(sbuf, (short[])o);
        } else if (o instanceof int[]) {
            MessageFormatter.intArrayAppend(sbuf, (int[])o);
        } else if (o instanceof long[]) {
            MessageFormatter.longArrayAppend(sbuf, (long[])o);
        } else if (o instanceof float[]) {
            MessageFormatter.floatArrayAppend(sbuf, (float[])o);
        } else if (o instanceof double[]) {
            MessageFormatter.doubleArrayAppend(sbuf, (double[])o);
        } else {
            MessageFormatter.objectArrayAppend(sbuf, (Object[])o, seenMap);
        }
    }

    private static void safeObjectAppend(final StringBuilder sbuf, final Object o) {
        try {
            final String oAsString = o.toString();
            sbuf.append(oAsString);
        }
        catch (final Throwable t) {
            System.err.println("Failed toString() invocation on an object of type [" + o.getClass().getName() + "]");
            t.printStackTrace();
            sbuf.append("[FAILED toString()]");
        }
    }

    private static void objectArrayAppend(final StringBuilder sbuf, final Object[] a, final Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            final int len = a.length;
            for (int i = 0; i < len; ++i) {
                MessageFormatter.deeplyAppendParameter(sbuf, a[i], seenMap);
                if (i == len - 1) {
					continue;
				}
                sbuf.append(", ");
            }
            seenMap.remove(a);
        } else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }

    private static void booleanArrayAppend(final StringBuilder sbuf, final boolean[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) {
				continue;
			}
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void byteArrayAppend(final StringBuilder sbuf, final byte[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) {
				continue;
			}
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void charArrayAppend(final StringBuilder sbuf, final char[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) {
				continue;
			}
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void shortArrayAppend(final StringBuilder sbuf, final short[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) {
				continue;
			}
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void intArrayAppend(final StringBuilder sbuf, final int[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) {
				continue;
			}
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void longArrayAppend(final StringBuilder sbuf, final long[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) {
				continue;
			}
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void floatArrayAppend(final StringBuilder sbuf, final float[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) {
				continue;
			}
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void doubleArrayAppend(final StringBuilder sbuf, final double[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) {
				continue;
			}
            sbuf.append(", ");
        }
        sbuf.append(']');
    }
}
