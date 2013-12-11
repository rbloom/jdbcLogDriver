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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * LogConnection is a wrapper class around the JDBC Connection. It will log the
 * methods being executed, then forward the calls to the embedded
 * JDBC Connection. It also replaces JDBC classes with equivalent Logging
 * versions, for example, returns LogStatement for Statement.
 * @version $Rev$
 */
public class LogConnection implements Connection {

    private Connection embedded;
    private static Logger log = Logger.getLogger(LogConnection.class);
    
    LogConnection(Connection conn) {
        embedded = conn;
        log.debug("Opening connection: " + embedded);
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
        log.debug("Closing connection: " + embedded);
        embedded.close();
    }

    /**
     * {@inheritDoc}
     */
    public void commit() throws SQLException {
        log.debug("Committing connection: " + embedded);
        embedded.commit();
    }

    /**
     * {@inheritDoc}
     */
    public void rollback() throws SQLException {
        log.debug("Rolling back connection: " + embedded);
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
        log.debug("Creating a new statement");
        return new LogStatement(embedded.createStatement(), this);
    }

    /**
     * {@inheritDoc}
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
        throws SQLException {
        log.debug("Creating a new statement");
        return new LogStatement(embedded.createStatement(resultSetType, 
                                                         resultSetConcurrency),
                                this);
    }

    /**
     * {@inheritDoc}
     */
    public Statement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        log.debug("Creating a new statement");
        return new LogStatement(embedded.createStatement(resultSetType, 
                                        resultSetConcurrency,
                                        resultSetHoldability),
                                this);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String,Class<?>> getTypeMap() throws SQLException {
        return embedded.getTypeMap();
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
        return new LogCallableStatement(embedded.prepareCall(sql), this, sql);
    }

    /**
     * {@inheritDoc}
     */
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return new LogCallableStatement(
                embedded.prepareCall(sql, resultSetType,
                        resultSetConcurrency), this, sql);
    }

    /**
     * {@inheritDoc}
     */
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return new LogCallableStatement(
                embedded.prepareCall(sql, resultSetType, resultSetConcurrency,
                     resultSetHoldability), this, sql);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new LogPreparedStatement(embedded.prepareStatement(sql),
                                        this, sql);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
        throws SQLException {
        return new LogPreparedStatement(
                           embedded.prepareStatement(sql, autoGeneratedKeys),
                           this, sql);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return new LogPreparedStatement(embedded.prepareStatement(sql, resultSetType, 
                                         resultSetConcurrency), this, sql);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return new LogPreparedStatement(embedded.prepareStatement(sql, 
                                        resultSetType, resultSetConcurrency, 
                                        resultSetHoldability), this, sql);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
        throws SQLException {
        return new LogPreparedStatement(embedded.prepareStatement(sql, 
                                        columnIndexes), this, sql);
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
        return new LogPreparedStatement(embedded.prepareStatement(sql, 
                                        columnNames), this, sql);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return embedded.unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return embedded.isWrapperFor(iface);
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        embedded.setTypeMap(map);
    }

    public Clob createClob() throws SQLException {
        return embedded.createClob();
    }

    public Blob createBlob() throws SQLException {
        return embedded.createBlob();
    }

    public NClob createNClob() throws SQLException {
        return embedded.createNClob();
    }

    public SQLXML createSQLXML() throws SQLException {
        return embedded.createSQLXML();
    }

    public boolean isValid(int timeout) throws SQLException {
        return embedded.isValid(timeout);
    }

    public void setClientInfo(String name, String value)
        throws SQLClientInfoException {
        embedded.setClientInfo(name, value);
    }

    public void setClientInfo(Properties properties)
        throws SQLClientInfoException {
        embedded.setClientInfo(properties);
    }

    public String getClientInfo(String name) throws SQLException {
        return embedded.getClientInfo(name);
    }

    public Properties getClientInfo() throws SQLException {
        return embedded.getClientInfo();
    }

    public Array createArrayOf(String typeName, Object[] elements)
        throws SQLException {
        return embedded.createArrayOf(typeName, elements);
    }

    public Struct createStruct(String typeName, Object[] attributes)
        throws SQLException {
        return embedded.createStruct(typeName, attributes);
    }

    public void setSchema(String schema) throws SQLException {
        embedded.setSchema(schema);
    }

    public String getSchema() throws SQLException {
        return embedded.getSchema();
    }

    public void abort(Executor executor) throws SQLException {
        embedded.abort(executor);
    }

    public void setNetworkTimeout(Executor executor, int milliseconds)
        throws SQLException {
        embedded.setNetworkTimeout(executor, milliseconds);
    }

    public int getNetworkTimeout() throws SQLException {
        return embedded.getNetworkTimeout();
    }
}
