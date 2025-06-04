package com.example.study.code.cacheAnnotation;

public enum ClassTypeEnum {
    SERVICE("SERVICE", "SERVICE"),
    DAO("DAO", "DAO"),
    ;

    final String code;
    final String msg;

    ClassTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public static int getIndex(String s){
        if(s == null || "".equals(s)){
            return -1;
        }else{
            for (ClassTypeEnum classTypeEnum: values()) {
                int indexOf = s.indexOf(classTypeEnum.getCode());
                if(indexOf != -1){
                    return indexOf;
                }
            }
            return -1;
        }
    }
}
