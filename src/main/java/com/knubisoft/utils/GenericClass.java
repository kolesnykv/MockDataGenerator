package com.knubisoft.utils;

import lombok.Getter;

import java.lang.reflect.Type;

@Getter
public abstract class GenericClass<T> {
    private final T t;
    private final Type type;

    public GenericClass(T t) {
        this.t = t;
        this.type = this.getClass().getGenericSuperclass();
    }
}