package com.example.flowable_web.enums;

public enum SexEnum {
    MAN("男","1"),
    WOMAN("女","2"),
    NO("第三性别","0");

    private SexEnum(String key,String value) {
        this.key = key;
        this.value = value;
    }

    private String value;
    private String key;
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public static SexEnum getByValue(String val) {
        SexEnum[] vals = values();
        for (SexEnum v : vals) {
            if (v.getValue().equals( val)) {
                return v;
            }
        }
        return SexEnum.NO;
    }

    public static SexEnum getByKey(String key) {
        SexEnum[] vals = values();
        for (SexEnum v : vals) {
            if (v.getKey().equals( key)) {
                return v;
            }
        }
        return SexEnum.NO;
    }
}
