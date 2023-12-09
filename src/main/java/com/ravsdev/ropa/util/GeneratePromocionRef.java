package com.ravsdev.ropa.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneratePromocionRef {
    private static final String regex = "\\b[A-Za-z]{1}|\\d";
    public static String generateRef(String nombre){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nombre);

        StringBuilder ref= new StringBuilder();

        while (matcher.find()) {
            ref.append(matcher.group());
        }

        return ref.toString().toUpperCase();
    }
}
