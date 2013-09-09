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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Map;

/**
 * LogPreparedStatement
 * @version $Rev$
 */
public class LogPreparedStatement implements PreparedStatement {

    private PreparedStatement embedded;
    private Connection conn;
    private String sql;
    private Map bindParams;
    private static Logger log = Logger.getLogger(LogPreparedStatement.class);
    
    LogPreparedStatement(PreparedStatement ps, Connection c, String s) {
        embedded = ps;
        conn = c;
        sql = s;
        // we want to have the bind parameters print out in order
        // otherwise it is difficult to match the parameters with
        // the question marks (?) in the query.
        bindParams = new TreeMap();
    }
    
    // This looks useless, but it isn't.  I have centralized the logging in
    // this class so that I can easily replace all of the '?'s with the actual
    // values.
    private void logStatement() {
        logStatement(sql);
    }

    private void logStatement(String sql) {
        String replaceBind = System.getProperty("replace.bindParams", "0");
        
        if (replaceBind.equals("1") || replaceBind.equals("true")) {
        	String logStr = this.bindParamsToSql(sql, this.bindParams);
        	log.debug("executing PreparedStatement: " + logStr);
            return;
        }
        log.debug("executing PreparedStatement: '" + sql + "' with bind " +
                  "parameters: " + bindParams);    
    }

    /**
     * Replaces bind parameters of an SQL query
     *  
     * @param strSQL SQL query containing ?'s wherever a bind param should be
     * @param mapBindParams one-based index of bind parameters
     * @return SQL statement containing bind parameters 
     */
	private String bindParamsToSql(String strSQL, Map<Integer,Object> mapBindParams)
	{
		String[] astrSQL;
		StringBuilder oLogLineBuilder = new StringBuilder();
		
		// sanity check
		if ( strSQL == null || mapBindParams == null )
			return strSQL;
	
		// split the sql around the ?'s
		astrSQL = strSQL.split("\\?");
		
		// add the split chunks of SQL to oLogLineBuilder, separated by data from mapBindParams
		int i = 1;
		for ( String strChunk : astrSQL )
		{
			oLogLineBuilder.append(strChunk);
			if ( mapBindParams.containsKey(i) )
				oLogLineBuilder.append(
					mapBindParams.get(i) != null ?
						mapBindParams.get(i).toString() :
						"NULL"
					);
			i++;
		}
		
		return oLogLineBuilder.toString();
	}
    
    /**
     * {@inheritDoc}
     */
    public int executeUpdate() throws SQLException {
        logStatement();
        return embedded.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public void addBatch() throws SQLException {
        logStatement();
        embedded.addBatch();
    }

    /**
     * {@inheritDoc}
     */
    public void clearParameters() throws SQLException {
        embedded.clearParameters();
        bindParams.clear();
    }

    /**
     * {@inheritDoc}
     */
    public boolean execute() throws SQLException {
        logStatement();
        return embedded.execute();
    }

    /**
     * {@inheritDoc}
     */
    public void setByte(int i, byte x) throws SQLException {
        embedded.setByte(i, x);
        bindParams.put(new Integer(i), new Byte(x));
    }

    /**
     * {@inheritDoc}
     */
    public void setDouble(int i, double x) throws SQLException {
        embedded.setDouble(i, x);
        bindParams.put(new Integer(i), new Double(x));
    }

    /**
     * {@inheritDoc}
     */
    public void setFloat(int i, float x) throws SQLException {
        embedded.setFloat(i, x);
        bindParams.put(new Integer(i), new Float(x));
    }

    /**
     * {@inheritDoc}
     */
    public void setInt(int i, int x) throws SQLException {
        embedded.setInt(i, x);
        bindParams.put(new Integer(i), new Integer(x));
    }

    /**
     * {@inheritDoc}
     */
    public void setNull(int i, int sqlType) throws SQLException {
        embedded.setNull(i, sqlType);
        bindParams.put(new Integer(i), null);
    }

    /**
     * {@inheritDoc}
     */
    public void setLong(int i, long x) throws SQLException {
        embedded.setLong(i, x);
        bindParams.put(new Integer(i), new Long(x));
    }

    /**
     * {@inheritDoc}
     */
    public void setShort(int i, short x) throws SQLException {
        embedded.setShort(i, x);
        bindParams.put(new Integer(i), new Short(x));
    }

    /**
     * {@inheritDoc}
     */
    public void setBoolean(int i, boolean x) throws SQLException {
        embedded.setBoolean(i, x);
        bindParams.put(new Integer(i), new Boolean(x));
    }

    /**
     * {@inheritDoc}
     */
    public void setBytes(int i, byte[] x) throws SQLException {
        embedded.setBytes(i, x);
        // Should this be:
        // bindParams.put(new Integer(i), Arrays.asList(x));
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setAsciiStream(int i, InputStream x, int length)
        throws SQLException {
        embedded.setAsciiStream(i, x, length);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setBinaryStream(int i, InputStream x, int length)
        throws SQLException {
        embedded.setBinaryStream(i, x, length);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setUnicodeStream(int i, InputStream x, int length)
        throws SQLException {
        embedded.setUnicodeStream(i, x, length);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setCharacterStream(int i, Reader reader, int length)
        throws SQLException {
        embedded.setCharacterStream(i, reader, length);
        bindParams.put(new Integer(i), reader);
    }

    /**
     * {@inheritDoc}
     */
    public void setObject(int i, Object x) throws SQLException {
        embedded.setObject(i, x);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setObject(int i, Object x, int targetSqlType)
        throws SQLException {
        embedded.setObject(i, x, targetSqlType);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setObject(int i, Object x, int targetSqlType,
            int scale) throws SQLException {
        embedded.setObject(i, x, targetSqlType, scale);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setNull(int paramIndex, int sqlType, String typeName)
        throws SQLException {
        embedded.setNull(paramIndex, sqlType, typeName);
        bindParams.put(new Integer(paramIndex), null);
    }

    /**
     * {@inheritDoc}
     */
    public void setString(int i, String x) throws SQLException {
        embedded.setString(i, x);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setBigDecimal(int i, BigDecimal x)
        throws SQLException {
        embedded.setBigDecimal(i, x);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setURL(int i, URL x) throws SQLException {
        embedded.setURL(i, x);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setArray(int i, Array x) throws SQLException {
        embedded.setArray(i, x);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setBlob(int i, Blob x) throws SQLException {
        embedded.setBlob(i, x);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setClob(int i, Clob x) throws SQLException {
        embedded.setClob(i, x);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setDate(int i, Date x) throws SQLException {
        embedded.setDate(i, x);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return embedded.getParameterMetaData();
    }

    /**
     * {@inheritDoc}
     */
    public void setRef(int i, Ref x) throws SQLException {
        embedded.setRef(i, x);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet executeQuery() throws SQLException {
        logStatement();
        return embedded.executeQuery();
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return embedded.getMetaData();
    }

    /**
     * {@inheritDoc}
     */
    public void setTime(int i, Time x) throws SQLException {
        embedded.setTime(i, x);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setTimestamp(int i, Timestamp x)
        throws SQLException {
        embedded.setTimestamp(i, x);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setDate(int i, Date x, Calendar cal)
        throws SQLException {
        embedded.setDate(i, x, cal);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setTime(int i, Time x, Calendar cal)
        throws SQLException {
        embedded.setTime(i, x, cal);
        bindParams.put(new Integer(i), x);
    }

    /**
     * {@inheritDoc}
     */
    public void setTimestamp(int i, Timestamp x, Calendar cal)
        throws SQLException {
        embedded.setTimestamp(i, x, cal);
        bindParams.put(new Integer(i), x);
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
        logStatement(sql);
        return embedded.executeUpdate(sql);
    }

    /**
     * {@inheritDoc}
     */
    public void addBatch(String sql) throws SQLException {
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
        logStatement(sql);
        return embedded.execute(sql);
    }

    /**
     * {@inheritDoc}
     */
    public int executeUpdate(String sql, int autoGeneratedKeys)
        throws SQLException {
        logStatement(sql);
        return embedded.executeUpdate(sql, autoGeneratedKeys);
    }

    /**
     * {@inheritDoc}
     */
    public boolean execute(String sql, int autoGeneratedKeys)
        throws SQLException {
        logStatement(sql);
        return embedded.execute(sql, autoGeneratedKeys);
    }

    /**
     * {@inheritDoc}
     */
    public int executeUpdate(String sql, int[] columnIndexes)
        throws SQLException {
        logStatement(sql);
        return embedded.executeUpdate(sql, columnIndexes);
    }

    /**
     * {@inheritDoc}
     */
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        logStatement(sql);
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
        logStatement(sql);
        return embedded.executeUpdate(sql, columnNames);
    }

    /**
     * {@inheritDoc}
     */
    public boolean execute(String sql, String[] columnNames)
        throws SQLException {
        logStatement(sql);        
        return embedded.execute(sql, columnNames);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        logStatement(sql);
        return embedded.executeQuery(sql);
    }

    public boolean isClosed() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public void setPoolable(boolean poolable) throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public boolean isPoolable() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public void closeOnCompletion() throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public boolean isCloseOnCompletion() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public void setRowId(int i, RowId x) throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setNString(int i, String value)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setNCharacterStream(int i, Reader value,
            long length) throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setNClob(int i, NClob value) throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setClob(int i, Reader reader, long length)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setBlob(int i, InputStream inputStream, long length)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setNClob(int i, Reader reader, long length)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setSQLXML(int i, SQLXML xmlObject)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setAsciiStream(int i, InputStream x, long length)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setBinaryStream(int i, InputStream x, long length)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setCharacterStream(int i, Reader reader,
            long length) throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setAsciiStream(int i, InputStream x)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setBinaryStream(int i, InputStream x)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setCharacterStream(int i, Reader reader)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setNCharacterStream(int i, Reader value)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setClob(int i, Reader reader) throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setBlob(int i, InputStream inputStream)
        throws SQLException {
        // TODO Auto-generated method stub
        
    }

    public void setNClob(int i, Reader reader) throws SQLException {
        // TODO Auto-generated method stub
        
    }
}
