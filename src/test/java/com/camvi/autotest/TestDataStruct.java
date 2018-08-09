package com.camvi.autotest;

public class TestDataStruct {
    public String[] items;

    public TestDataStruct(String... items) {
        this.items = items; // should probably make a defensive copy
    }

    public String get(int x) {
        return items[x];
    }
}
