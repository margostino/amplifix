package org.gaussian.amplifix.toolkit.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class GenericsUtils {

    public static Field getField(Object event, String fieldName) {
        try {
            return event.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
            // TODO
            return null;
        }
    }

    public static boolean isInstance(Object event, Field field, Class clazz) {
        try {
            return clazz.isInstance(field.get(event));
        } catch (IllegalAccessException e) {
            // TODO
            return false;
        }
    }

    public static Boolean isList(Object event, Field field) {
        Type genericFieldType = field.getGenericType();
        return isInstance(event, field, List.class) && genericFieldType instanceof ParameterizedType;
    }

    public static List<Field> getFields(Object event, List<String> fieldNames) {
        return fieldNames.stream()
                         .map(fieldName -> getField(event, fieldName))
                         .collect(toList());
    }

}
