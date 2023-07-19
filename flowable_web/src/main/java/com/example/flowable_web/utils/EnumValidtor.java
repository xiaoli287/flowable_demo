package com.example.flowable_web.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EnumValidtor implements ConstraintValidator<EnumValidAnnotation,String> {

    /**
     * 枚举类
     */
    Class<?>[] cls ;

    @Override
    public void initialize(EnumValidAnnotation enumValidAnnotation) {
        cls = enumValidAnnotation.target();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(cls.length>0 ){
            for (Class<?> cl : cls
            ) {
                try {
                    if(cl.isEnum()){
                        /**
                         * 	枚举类验证
                         */
                        Object[] objs = cl.getEnumConstants();
                        Method method = cl.getMethod("getValue");
                        for (Object obj : objs
                        ) {
                            Object code = method.invoke(obj,null);
                            if(value.equals(code.toString())){
                                return true;
                            }
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return true;
        }

        return false;
    }
}
