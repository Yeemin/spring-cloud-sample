package org.example.h2db.starter.alias;

import cn.hutool.crypto.digest.DigestUtil;

public class DigestAlias {

    public static String md5(String input) {
        return DigestUtil.md5Hex(input);
    }

}
