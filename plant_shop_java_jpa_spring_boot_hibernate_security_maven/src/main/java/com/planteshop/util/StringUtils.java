package com.planteshop.util;

public class StringUtils {
    /**
     * Convertit une chaîne en Capital Case (première lettre de chaque mot en majuscule).
     * Exemple : "jean dupont" devient "Jean Dupont".
     *
     * @param text String le texte à convertir
     * @return String le texte en Capital Case ou null/vide si entrée null/vide
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
