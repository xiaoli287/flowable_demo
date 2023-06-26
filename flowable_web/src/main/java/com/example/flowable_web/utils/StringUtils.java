package com.example.flowable_web.utils;

import java.math.BigDecimal;
import java.util.Objects;

public class StringUtils {
    public static boolean equal(Object obj1, Object obj2) {
        return obj1 instanceof BigDecimal && obj2 instanceof BigDecimal ? equals((BigDecimal)obj1, (BigDecimal)obj2) : Objects.equals(obj1, obj2);
    }

    public static boolean equals(BigDecimal bigNum1, BigDecimal bigNum2) {
        if (bigNum1 == bigNum2) {
            return true;
        } else if (bigNum1 != null && bigNum2 != null) {
            return 0 == bigNum1.compareTo(bigNum2);
        } else {
            return false;
        }
    }
}
