package org.illegal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UtilClass {

    public static void getMethods() {
        Method method = null;
        try {
            method = Class.forName("java.lang.Runtime")
                    .getDeclaredMethod("getRuntime");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
