package org.example.h2db.starter.alias;

import cn.hutool.db.Db;
import org.example.h2db.starter.server.JdbcConnectionPoolWrapper;
import org.h2.util.StringUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class AliasUtil {

    public static void createAlias(String database) {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("alias.factories");
            if (properties.isEmpty()) {
                return;
            }
            DataSource dataSource = JdbcConnectionPoolWrapper.getInstance().get(database);
            properties.entrySet().parallelStream().forEach(entry -> {
                String k = (String) entry.getKey();
                String v = (String) entry.getValue();
                String dropAliasSQL = "DROP ALIAS IF EXISTS " + k;
                try {
                    Db.use(dataSource).execute(dropAliasSQL);
                } catch (SQLException ignored) {
                }
                if (!StringUtils.isNullOrEmpty(v)) {
                    String createAliasSQL = "CREATE ALIAS " + k + " FOR \"" + v + "\"";
                    try {
                        Db.use(dataSource).execute(createAliasSQL);
                    } catch (SQLException ignored) {
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
