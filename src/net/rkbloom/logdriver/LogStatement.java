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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * LogStatement is a wrapper class around the JDBC Statement. It will log the
 * SQL statements being executed, then forward the calls to the embedded
 * JDBC Statement.
 * @version $Rev$
 */
public class LogStatement implements Statement {

    private Statement embedded;
    private Connection conn;
    private static Logger log = Logger.getLogger(LogStatement.class);
    
    LogStatement(Statement stmt, Connection c) {
        embedded = stmt;
        conn = c;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getFetchDirection() throws SQLException {
        return embedded.getFetchDirection();
    }

    /**
     * {@inheritDoc}
     */
    public int getFetchSize() throws SQLException {
        return embedded.getFetchSize();
    }

    /**
     * {@inheritDoc}
     */
    public int getMaxFieldSize() throws SQLException {
        return embedded.getMaxFieldSize();
    }

    /**
     * {@inheritDoc}
     */
    public int getMaxRows() throws SQLException {
        return embedded.getMaxRows();
    }

    /**
     * {@inheritDoc}
     */
    public int getQueryTimeout() throws SQLException {
        return embedded.getQueryTimeout();
    }

    /**
     * {@inheritDoc}
     */
    public int getResultSetConcurrency() throws SQLException {
        return embedded.getResultSetConcurrency();
    }

    /**
     * {@inheritDoc}
     */
    public int getResultSetHoldability() throws SQLException {
        return embedded.getResultSetHoldability();
    }

    /**
     * {@inheritDoc}
     */
    public int getResultSetType() throws SQLException {
        return embedded.getResultSetType();
    }

    /**
     * {@inheritDoc}
     */
    public int getUpdateCount() throws SQLException {
        return embedded.getUpdateCount();
    }

    /**
     * {@inheritDoc}
     */
    public void cancel() throws SQLException {
        embedded.cancel();
    }

    /**
     * {@inheritDoc}
     */
    public void clearBatch() throws SQLException {
        embedded.clearBatch();
    }

    /**
     * {@inheritDoc}
     */
    public void clearWarnings() throws SQLException {
        embedded.clearWarnings();
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws SQLException {
        embedded.close();
    }

    /**
     * {@inheritDoc}
     */
    public boolean getMoreResults() throws SQLException {
        return embedded.getMoreResults();
    }

    /**
     * {@inheritDoc}
     */
    public int[] executeBatch() throws SQLException {
        log.debug("Executing the entire batch");
        return embedded.executeBatch();
    }

    /**
     * {@inheritDoc}
     */
    public void setFetchDirection(int direction) throws SQLException {
        embedded.setFetchDirection(direction);
    }

    /**
     * {@inheritDoc}
     */
    public void setFetchSize(int rows) throws SQLException {
        embedded.setFetchSize(rows);
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxFieldSize(int max) throws SQLException {
        embedded.setMaxFieldSize(max);
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxRows(int max) throws SQLException {
        embedded.setMaxRows(max);
    }

    /**
     * {@inheritDoc}
     */
    public void setQueryTimeout(int seconds) throws SQLException {
        embedded.setQueryTimeout(seconds);
    }

    /**
     * {@inheritDoc}
     */
    public boolean getMoreResults(int current) throws SQLException {
        return embedded.getMoreResults(current);
    }

    /**
     * {@inheritDoc}
     */
    public void setEscapeProcessing(boolean enable) throws SQLException {
        embedded.setEscapeProcessing(enable);
    }

    /**
     * {@inheritDoc}
     */
    public int executeUpdate(String sql) throws SQLException {
        log.debug("Executing Update: " + sql);
        return embedded.executeUpdate(sql);
    }

    /**
     * {@inheritDoc}
     */
    public void addBatch(String sql) throws SQLException {
        log.debug("Adding '" + sql + "' to the batch");
        embedded.addBatch(sql);
    }

    /**
     * {@inheritDoc}
     */
    public void setCursorName(String name) throws SQLException {
        embedded.setCursorName(name);
    }

    /**
     * {@inheritDoc}
     */
    public boolean execute(String sql) throws SQLException {
        log.debug("Executing: " + sql);
        return embedded.execute(sql);
    }

    /**
     * {@inheritDoc}
     */
    public int executeUpdate(String sql, int autoGeneratedKeys)
        throws SQLException {
        log.debug("Executing Update: " + sql);
        return embedded.executeUpdate(sql, autoGeneratedKeys);
    }

    /**
     * {@inheritDoc}
     */
    public boolean execute(String sql, int autoGeneratedKeys)
        throws SQLException {
        log.debug("Executing: " + sql);
        return embedded.execute(sql, autoGeneratedKeys);
    }

    /**
     * {@inheritDoc}
     */
    public int executeUpdate(String sql, int[] columnIndexes)
        throws SQLException {
        log.debug("Executing Update: " + sql);
        return embedded.executeUpdate(sql, columnIndexes);
    }

    /**
     * {@inheritDoc}
     */
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        log.debug("Executing: " + sql);
        return embedded.execute(sql, columnIndexes);
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection() throws SQLException {
        return conn;
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet getGeneratedKeys() throws SQLException {
        return embedded.getGeneratedKeys();
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet getResultSet() throws SQLException {
        return embedded.getResultSet();
    }

    /**
     * {@inheritDoc}
     */
    public SQLWarning getWarnings() throws SQLException {
        return embedded.getWarnings();
    }

    /**
     * {@inheritDoc}
     */
    public int executeUpdate(String sql, String[] columnNames)
        throws SQLException {
        log.debug("Executing Update: " + sql);
        return embedded.executeUpdate(sql, columnNames);
    }

    /**
     * {@inheritDoc}
     */
    public boolean execute(String sql, String[] columnNames)
        throws SQLException {
        log.debug("Executing: " + sql);
        return embedded.execute(sql, columnNames);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        log.debug("Executing Query: " + sql);
        return embedded.executeQuery(sql);
    }

    /**
     * {@inheritDoc}
     */
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return embedded.unwrap(iface);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return embedded.isWrapperFor(iface);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isClosed() throws SQLException {
        return embedded.isClosed();
    }

    /**
     * {@inheritDoc}
     */
    public void setPoolable(boolean poolable) throws SQLException {
        embedded.setPoolable(poolable);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPoolable() throws SQLException {
        return embedded.isPoolable();
    }

    /**
     * {@inheritDoc}
     */
    public void closeOnCompletion() throws SQLException {
        embedded.closeOnCompletion();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCloseOnCompletion() throws SQLException {
        return embedded.isCloseOnCompletion();
    }
}
