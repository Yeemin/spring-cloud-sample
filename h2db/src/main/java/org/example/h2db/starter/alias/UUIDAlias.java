package org.example.h2db.starter.alias;

import java.util.UUID;

public class UUIDAlias {

    public static String uuid32() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String capture(String str) {
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

}
