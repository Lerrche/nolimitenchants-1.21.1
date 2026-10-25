package com.nolimitenchants.util;

/** Converts enchantment levels to standard uppercase Roman numerals. */
public final class RomanNumerals {
    private static final int[] VALUES = {
            1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1
    };
    private static final String[] SYMBOLS = {
            "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"
    };

    private RomanNumerals() {
    }

    /**
     * Formats a positive integer as a Roman numeral. Minecraft's stored
     * enchantment levels are limited to 255, but this supports standard values
     * up to 3999 as well. Values outside that range are returned as decimal.
     */
    public static String format(int value) {
        if (value <= 0 || value > 3999) {
            return Integer.toString(value);
        }

        StringBuilder result = new StringBuilder();
        int remaining = value;
        for (int i = 0; i < VALUES.length; i++) {
            while (remaining >= VALUES[i]) {
                result.append(SYMBOLS[i]);
                remaining -= VALUES[i];
            }
        }
        return result.toString();
    }
}
