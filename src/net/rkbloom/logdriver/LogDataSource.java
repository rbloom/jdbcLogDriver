/*
 * Copyright 2006 Chris Wareham
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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * LogDataSource
 * @version $Rev$
 */
public class LogDataSource implements DataSource {
	/** The logger. */
	private static Logger logger = Logger.getLogger(LogDataSource.class);

	/** The data source to delegate to. */
	private DataSource dataSource;

	/**
	 * Creates a new instance of the LogDataSource class.
	 *
	 * @param ds the data source to delegate to.
	 */
	public LogDataSource(DataSource ds) {
		dataSource = ds;
	}

	/**
	 * {@inheritDoc}
	 */
	public Connection getConnection() throws SQLException {
		return new LogConnection(dataSource.getConnection());
	}

	/**
	 * {@inheritDoc}
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		return new LogConnection(dataSource.getConnection(username, password));
	}

	/**
	 * {@inheritDoc}
	 */
	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLoginTimeout(int seconds) throws SQLException {
		logger.debug("Setting login timeout to " + seconds + " seconds");
		dataSource.setLoginTimeout(seconds);
	}

	/**
	 * {@inheritDoc}
	 */
	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLogWriter(PrintWriter out) throws SQLException {
		logger.debug("Setting log writer to " + out);
		dataSource.setLogWriter(out);
	}
}

