package com.knubisoft;

import com.knubisoft.utils.GenericClass;
import com.knubisoft.utils.MockDataGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        List<List<String>> list = new ArrayList<>();
        Map<Map<String, Integer>, Integer> map = new HashMap<>();
        X x = new X();
        MockDataGenerator generator = new MockDataGenerator();
        System.out.println(generator.populate(generator.unpackGenericClass(new GenericClass<>(list) {
        }.getType()), 5));

    }

}
