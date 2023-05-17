package com.charles.demo.test;

import android.util.Log;

//工厂模式
public class CarBuilder {
    private double price;
    private String colour="red";
    private double displacement;

    static void job(){
        Log.d("William", "CarBuilder-job: " + new CarBuilder().price(15.6).displacement(2.0).buildCar().getColour());
    }

    public CarBuilder() {}

    public Car buildCar() {
        return new Car(price, colour,displacement);
    }

    public CarBuilder price(double price) {
        this.price = price;
        return this;
    }

    public CarBuilder colour(String colour) {
        this.colour = colour;
        return this;
    }

    public CarBuilder displacement(double displacement){
        this.displacement=displacement;
        return this;
    }

}
