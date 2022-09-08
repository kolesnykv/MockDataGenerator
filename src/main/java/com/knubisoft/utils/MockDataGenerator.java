package com.knubisoft.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Supplier;

public class MockDataGenerator {
    static Map<Class<?>, Supplier<Object>> generator = new LinkedHashMap<>();

    static {
        generator.put(Integer.class, () -> new Random().nextInt(100));
        generator.put(Double.class, () -> new Random().nextDouble(100));
        generator.put(Boolean.class, () -> new Random().nextBoolean());
        generator.put(String.class, () -> RandomStringUtils.randomAlphabetic(5));
    }

    @SneakyThrows
    public Object populate(Type type, int initialCapacity) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type incomeRawType = parameterizedType.getRawType();
            if (List.class.isAssignableFrom((Class<?>) incomeRawType)) {
                return generateList(parameterizedType, initialCapacity);
            }
            if (Map.class.isAssignableFrom((Class<?>) incomeRawType)) {
                return generateMap(parameterizedType, initialCapacity);
            }
        }
        if (isSimpleType(type)) {
            return generator.get(type).get();
        } else {
            return generateCustomClassInstance((Class) type, initialCapacity);
        }
    }
    public Type unpackGenericClass(Type type) {
        ParameterizedType params = (ParameterizedType) type;
        return params.getRawType().equals(GenericClass.class) ? params.getActualTypeArguments()[0] : type;
    }

    @SneakyThrows
    private Object generateCustomClassInstance(Class type, int initialCapacity) {
        Class<?> cls = Class.forName(type.getName());
        Field[] fields = cls.getDeclaredFields();
        Object instance = cls.getDeclaredConstructor().newInstance();
        for (Field f : fields) {
            f.setAccessible(true);
            f.set(instance, populate(f.getGenericType(), initialCapacity));
        }
        return instance;
    }

    private Map<Object, Object> generateMap(ParameterizedType parameterizedType, int initialCapacity) {
        Map<Object, Object> resultMap = new LinkedHashMap();
        Type[] nestedMapTypes = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < initialCapacity; i++) {
            resultMap.put(populate(nestedMapTypes[0], initialCapacity), populate(nestedMapTypes[1], initialCapacity));
        }
        return resultMap;
    }

    private List<Object> generateList(ParameterizedType parameterizedType, int initialCapacity) {
        Type nestedListType = parameterizedType.getActualTypeArguments()[0];
        List<Object> resultList = new ArrayList<>();
        for (int i = 0; i < initialCapacity; i++) {
            resultList.add(populate(nestedListType, initialCapacity));
        }
        return resultList;
    }

    private boolean isSimpleType(Object x) {
        return generator.containsKey(x);
    }

}
