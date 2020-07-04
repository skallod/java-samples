package ru.galuzin.mocksample;

import ru.galuzin.mocksample.kotlin.ExecutionService2;
import ru.galuzin.mocksample.kotlin.SettingsHelper2;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
//        {
//            final ExecutionService es = new ExecutionService();
//            es.settingsHelper = new SettingsHelper();
//            es.settingsHelper.postConstruct();
//            es.execute();
//        }
        {
            final ExecutionService2 es = new ExecutionService2();
            es.settingsHelper = new SettingsHelper2(new DataSource() {
                @Override
                public Connection getConnection() throws SQLException {
                    return null;
                }

                @Override
                public Connection getConnection(String username, String password) throws SQLException {
                    return null;
                }

                @Override
                public <T> T unwrap(Class<T> iface) throws SQLException {
                    return null;
                }

                @Override
                public boolean isWrapperFor(Class<?> iface) throws SQLException {
                    return false;
                }

                @Override
                public PrintWriter getLogWriter() throws SQLException {
                    return null;
                }

                @Override
                public void setLogWriter(PrintWriter out) throws SQLException {

                }

                @Override
                public void setLoginTimeout(int seconds) throws SQLException {

                }

                @Override
                public int getLoginTimeout() throws SQLException {
                    return 0;
                }

                @Override
                public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                    return null;
                }
            });
            es.settingsHelper.postConstruct();
            es.execute();
        }
    }
}
