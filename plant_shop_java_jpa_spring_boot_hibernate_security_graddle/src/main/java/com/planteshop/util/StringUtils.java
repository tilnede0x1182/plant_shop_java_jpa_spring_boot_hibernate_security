package com.planteshop.util;

public class StringUtils {
    /**
     * Convertit une chaine en Capital Case (premiere lettre de chaque mot en majuscule).
     *
     * @param text String le texte a convertir
     * @return String le texte converti
     */
    public static String toCapitalCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }

        return result.toString().trim();
    }
}
