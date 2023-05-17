package com.charles.demo.test;

//  Created by Charles on 10/19/2020.
//  mail: zingon@163.com

import java.util.Arrays;

public class Car {
    private double price;
    private String colour;
    private double displacement;
    private byte[] bytes;

    public Car() {}

    public Car(double price, String colour, double displacement) {
        this.price = price;
        this.colour = colour;
        this.displacement = displacement;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public double getDisplacement() {return displacement;}

    public String toString() {
        return colour + "car costs $" + price;
    }

    public void newBytes() {
        bytes = new byte[1000 * 1000 * 2];
        Arrays.fill(bytes, (byte) 1);
    }

}

