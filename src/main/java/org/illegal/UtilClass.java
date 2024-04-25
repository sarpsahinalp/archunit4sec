package org.illegal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UtilClass {

    public static void getMethods() {
        Method method = null;
        try {
            method = Class.forName("java.lang.Runtime")
                    .getDeclaredMethod("getRuntime");
            method.invoke(null);
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        getMethods();
    }
}
