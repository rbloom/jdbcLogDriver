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
package src.net.rkbloom.jdbcLogDriver;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

/**
 * LogConnection
 * @version $Rev$
 */
public class LogConnection implements Connection {

    Connection embedded;
    
    LogConnection(Connection conn) {
        embedded = conn;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getHoldability() throws SQLException {
        return embedded.getHoldability();
    }

    /**
     * {@inheritDoc}
     */
    public int getTransactionIsolation() throws SQLException {
        return embedded.getTransactionIsolation();
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
    public void commit() throws SQLException {
        embedded.commit();
    }

    /**
     * {@inheritDoc}
     */
    public void rollback() throws SQLException {
        embedded.rollback();
    }

    /**
     * {@inheritDoc}
     */
    public boolean getAutoCommit() throws SQLException {
        return embedded.getAutoCommit();
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
    public boolean isReadOnly() throws SQLException {
        return embedded.isReadOnly();
    }

    /**
     * {@inheritDoc}
     */
    public void setHoldability(int holdability) throws SQLException {
        embedded.setHoldability(holdability);
    }

    /**
     * {@inheritDoc}
     */
    public void setTransactionIsolation(int level) throws SQLException {
        embedded.setTransactionIsolation(level);
    }

    /**
     * {@inheritDoc}
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        embedded.setAutoCommit(autoCommit);
    }

    /**
     * {@inheritDoc}
     */
    public void setReadOnly(boolean readOnly) throws SQLException {
        embedded.setReadOnly(readOnly);
    }

    /**
     * {@inheritDoc}
     */
    public String getCatalog() throws SQLException {
        return embedded.getCatalog();
    }

    /**
     * {@inheritDoc}
     */
    public void setCatalog(String catalog) throws SQLException {
        embedded.setCatalog(catalog);
    }

    /**
     * {@inheritDoc}
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        return embedded.getMetaData();
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
    public Savepoint setSavepoint() throws SQLException {
        return embedded.setSavepoint();
    }

    /**
     * {@inheritDoc}
     */
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        embedded.releaseSavepoint(savepoint);
    }

    /**
     * {@inheritDoc}
     */
    public void rollback(Savepoint savepoint) throws SQLException {
        embedded.rollback(savepoint);
    }

    /**
     * {@inheritDoc}
     */
    public Statement createStatement() throws SQLException {
        return embedded.createStatement();
    }

    /**
     * {@inheritDoc}
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
        throws SQLException {
        return embedded.createStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * {@inheritDoc}
     */
    public Statement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return embedded.createStatement(resultSetType, resultSetConcurrency,
                                        resultSetHoldability);
    }

    /**
     * {@inheritDoc}
     */
    public Map getTypeMap() throws SQLException {
        return embedded.getTypeMap();
    }

    /**
     * {@inheritDoc}
     */
    public void setTypeMap(Map map) throws SQLException {
        embedded.setTypeMap(map);
    }

    /**
     * {@inheritDoc}
     */
    public String nativeSQL(String sql) throws SQLException {
        return embedded.nativeSQL(sql);
    }

    /**
     * {@inheritDoc}
     */
    public CallableStatement prepareCall(String sql) throws SQLException {
        return embedded.prepareCall(sql);
    }

    /**
     * {@inheritDoc}
     */
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return embedded.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * {@inheritDoc}
     */
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return embedded.prepareCall(sql, resultSetType, resultSetConcurrency,
                                    resultSetHoldability);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return embedded.prepareStatement(sql);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
        throws SQLException {
        return embedded.prepareStatement(sql, autoGeneratedKeys);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return embedded.prepareStatement(sql, resultSetType, 
                                         resultSetConcurrency);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return embedded.prepareStatement(sql, resultSetType, 
                                         resultSetConcurrency, 
                                         resultSetHoldability);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
        throws SQLException {
        return embedded.prepareStatement(sql, columnIndexes);
    }

    /**
     * {@inheritDoc}
     */
    public Savepoint setSavepoint(String name) throws SQLException {
        return embedded.setSavepoint(name);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
        throws SQLException {
        return embedded.prepareStatement(sql, columnNames);
    }
}
