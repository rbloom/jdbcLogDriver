/**
 * Copyright (c) 2004 Ryan Bloom
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Ryan Bloom. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Ryan Bloom.
 */
package net.rkbloom.jdbcLogDriver;

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
        String realUrl = parseUrl(url);
        log.debug("Trying to find: " + realUrl);
        return new LogConnection(DriverManager.getConnection(realUrl, info));
    }

    /**
     * {@inheritDoc}
     */
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
        throws SQLException {
        return new DriverPropertyInfo[0];
    }

    private String parseUrl(String url) {
        String newUrl = url.replaceFirst(":log", "");
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
