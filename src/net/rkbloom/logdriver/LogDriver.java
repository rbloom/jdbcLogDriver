/*
 * Copyright 2005 Ryan Bloom
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package net.rkbloom.logdriver;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

/**
 * LogDriver
 * @version $Rev$
 */
public class LogDriver implements Driver {

    private static Logger log = Logger.getLogger(LogDriver.class);
    
    /**
     * {@inheritDoc}
     */
    public int getMajorVersion() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    public int getMinorVersion() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean jdbcCompliant() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith("jdbc:log");
    }

    /**
     * {@inheritDoc}
     */
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            return null;
        }
        try {
            String realUrl = parseUrl(url);
            log.debug("Trying to find: " + realUrl);
            return new LogConnection(DriverManager.getConnection(realUrl, info));
        }
        catch (ClassNotFoundException e) {
            throw new SQLException("Couldn't load class for embedded driver: " +
                                   e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
        throws SQLException {
        return new DriverPropertyInfo[0];
    }

    private String parseUrl(String url) throws ClassNotFoundException {
        // Get the class for the real JDBC driver.
        int start = url.indexOf(":log:") + ":log:".length();
        int end = url.indexOf(":", start);
        String realClass = url.substring(start, end);
        log.debug("Real JDBC driver Class: " + realClass);
        Class.forName(realClass);
        
        String newUrl = url.replaceFirst(":log:" + realClass, "");
        log.debug("Real JDBC connection string: " + newUrl);
        return newUrl;
    }
    
    static {
        try {
            DriverManager.registerDriver(new LogDriver());
        }
        catch (SQLException e) {
            log.error("Couldn't register LogDriver.", e);
        }
    }
}
