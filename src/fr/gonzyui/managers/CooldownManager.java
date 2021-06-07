package fr.gonzyui.managers;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String replaceOnce(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    private static String padding(final int repeat, final char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Negative amount does not works!" + repeat);
        }
        final char[] buf = new char[repeat];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = padChar;
        }
        return new String(buf);
    }

    public static String leftPad(final String str, final int size) {
        return leftPad(str, size, ' ');
    }

    public static String leftPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return padding(pads, padChar).concat(str);
    }

    public static String leftPad(final String str, final int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        final int padLen = padStr.length();
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        if (padLen == 1 && pads <= 8192) {
            return leftPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return padStr.concat(str);
        }
        if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        }
        final char[] padding = new char[pads];
        final char[] padChars = padStr.toCharArray();
        for (int i = 0; i < pads; ++i) {
            padding[i] = padChars[i % padLen];
        }
        return new String(padding).concat(str);
    }
    public static String replace(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, -1);
    }
    public static String replace(final String text, final String searchString, final String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = ((increase < 0) ? 0 : increase);
        increase *= ((max < 0) ? 16 : ((max > 64) ? 64 : max));
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static void createCooldown(String k) {
        if (cooldown.containsKey(k)) {
            throw new IllegalArgumentException("This cooldown is already in use!");
        }
        cooldown.put(k, new HashMap<UUID, Long>());
    }

    public static HashMap<UUID, Long> getCooldownMap(String k) {
        if (cooldown.containsKey(k)) {
            return cooldown.get(k);
        }
        return null;
    }

    public static void addCooldown(String k, ProxiedPlayer p, int seconds) {
        if (!cooldown.containsKey(k)) {
            throw new IllegalArgumentException(k + " does not exist");
        }
        long next = System.currentTimeMillis() + seconds * 1000L;
        (cooldown.get(k)).put(p.getUniqueId(), Long.valueOf(next));
    }

    public static boolean isOnCooldown(String k, ProxiedPlayer p) {
        return (cooldown.containsKey(k)) && ((cooldown.get(k)).containsKey(p.getUniqueId())) && (System
                .currentTimeMillis() <= ((Long) ((HashMap<?, ?>) cooldown.get(k)).get(p.getUniqueId())).longValue());
    }

    public static int getCooldownForPlayerInt(String k, ProxiedPlayer p) {
        return (int) (((Long) (cooldown.get(k)).get(p.getUniqueId())).longValue() - System.currentTimeMillis()) / 1000;
    }

    public static long getCooldownForPlayerLong(String k, ProxiedPlayer p) {
        return (int) (((Long) (cooldown.get(k)).get(p.getUniqueId())).longValue() - System.currentTimeMillis());
    }

    public static void removeCooldown(String k, ProxiedPlayer p) {
        if (!cooldown.containsKey(k)) {
            throw new IllegalArgumentException(k + " does not exists!");
        }
        (cooldown.get(k)).remove(p.getUniqueId());
    }

    private static HashMap<String, HashMap<UUID, Long>> cooldown = new HashMap<String, HashMap<UUID, Long>>();
}
