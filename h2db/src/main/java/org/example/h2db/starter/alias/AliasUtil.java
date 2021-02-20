package org.example.h2db.starter.alias;

import cn.hutool.db.Db;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.example.h2db.starter.server.JdbcConnectionPoolWrapper;
import org.h2.util.StringUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class AliasUtil {

    private static final Log log = LogFactory.get();

    public static String createAlias(String database) {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("alias.factories");
            if (properties.isEmpty()) {
                log.warn("alias.factories is empty");
                return "alias.factories is empty";
            }
            DataSource dataSource = JdbcConnectionPoolWrapper.getInstance().get(database);
            properties.entrySet().parallelStream().forEach(entry -> {
                String k = (String) entry.getKey();
                String v = (String) entry.getValue();
                String dropAliasSQL = "DROP ALIAS IF EXISTS " + k;
                try {
                    Db.use(dataSource).execute(dropAliasSQL);
                } catch (SQLException e) {
                    log.error(e);
                }
                if (!StringUtils.isNullOrEmpty(v)) {
                    String createAliasSQL = "CREATE ALIAS " + k + " FOR \"" + v + "\"";
                    try {
                        Db.use(dataSource).execute(createAliasSQL);
                    } catch (SQLException e) {
                        log.error(e);
                    }
                }
            });
        } catch (IOException e) {
            log.error(e);
            return e.getMessage();
        }
        return "SUCCESS";
    }

}
