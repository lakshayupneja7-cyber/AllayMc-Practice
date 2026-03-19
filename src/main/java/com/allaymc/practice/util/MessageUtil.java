package com.allaymc.practice.util;

public class MessageUtil {

    private MessageUtil() {
    }

    public static String color(String text) {
        return text == null ? "" : text.replace("&", "§");
    }
}
