package com.charles.demo;

/*
 *   Created by Charles on 08/25/2020.
 *   多例模式-懒汉式
 */
public class Multiton {
    private Multiton(String value) {this.value = value;}

    private String value;

    private static Multiton red, green, blue;

    public static Multiton getInstance(String value) {
        switch (value) {
            case "red":
                if (red == null) {
                    red = new Multiton(value);
                }
                return red;
            case "green":
                if (green == null) {
                    green = new Multiton(value);
                }
                return green;
            case "blue":
                if (blue == null) {
                    blue = new Multiton(value);
                }
                return blue;
            default:
                return null;
        }
    }

    public String getValue() {
        return this.value;
    }
}
