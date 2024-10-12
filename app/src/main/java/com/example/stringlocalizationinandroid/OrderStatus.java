package com.example.stringlocalizationinandroid;

//Constraining string values in Java when you can't use enums because values have to be saved in a database by using Using a custom Enum-like class.
public class OrderStatus {
    public static final OrderStatus COMPLETE = new OrderStatus("Complete");
    public static final OrderStatus INCOMPLETE = new OrderStatus("Incomplete");
    public static final OrderStatus DEFAULT = new OrderStatus("");

    private final String value;
    private OrderStatus(String value) {
        this.value = value;
    }
}
