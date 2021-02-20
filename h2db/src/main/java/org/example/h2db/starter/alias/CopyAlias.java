package org.example.h2db.starter.alias;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

public class CopyAlias {

    private static final Log log = LogFactory.get();

    public static String copy(String database) {
        log.info("copy alias for database: {}", database);
        return AliasUtil.createAlias(database);
    }

}
