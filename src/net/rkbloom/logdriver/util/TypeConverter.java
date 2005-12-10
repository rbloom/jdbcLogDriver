/**
 * Copyright (c) 2004-2005 Red Hat, Inc.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Red Hat, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Red Hat.
 */
package net.rkbloom.logdriver.util;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;


/**
 * TypeConverter
 * @version $Rev$
 */
public class TypeConverter {

    private static Map types;
    
    static {
        types = new HashMap();
        types.put(new Integer(Types.ARRAY), "ARRAY");
        types.put(new Integer(Types.BIGINT), "BIGINT");
        types.put(new Integer(Types.BINARY), "BINARY");
        types.put(new Integer(Types.BIT), "BIT");
        types.put(new Integer(Types.BLOB), "BLOB");
        types.put(new Integer(Types.BOOLEAN), "BOOLEAN");
        types.put(new Integer(Types.CHAR), "CHAR");
        types.put(new Integer(Types.CLOB), "CLOB");
        types.put(new Integer(Types.DATALINK), "DATALINK");
        types.put(new Integer(Types.DATE), "DATE");
        types.put(new Integer(Types.DECIMAL), "DECIMAL");
        types.put(new Integer(Types.DISTINCT), "DISTINCT");
        types.put(new Integer(Types.DOUBLE), "DOUBLE");
        types.put(new Integer(Types.FLOAT), "FLOAT");
        types.put(new Integer(Types.INTEGER), "INTEGER");
        types.put(new Integer(Types.JAVA_OBJECT), "JAVA_OBJECT");
        types.put(new Integer(Types.LONGVARBINARY), "LONGVARBINARY");
        types.put(new Integer(Types.LONGVARCHAR), "LONGVARCHAR");
        types.put(new Integer(Types.NULL), "NULL");
        types.put(new Integer(Types.NUMERIC), "NUMERIC");
        types.put(new Integer(Types.OTHER), "OTHER");
        types.put(new Integer(Types.REAL), "REAL");
        types.put(new Integer(Types.REF), "REF");
        types.put(new Integer(Types.SMALLINT), "SMALLINT");
        types.put(new Integer(Types.STRUCT), "STRUCT");
        types.put(new Integer(Types.TIME), "TIME");
        types.put(new Integer(Types.TIMESTAMP), "TIMESTAMP");
        types.put(new Integer(Types.TINYINT), "TINYINT");
        types.put(new Integer(Types.VARCHAR), "VARCHAR");
        types.put(new Integer(Types.VARBINARY), "VARBINARY");
    }
    
    private TypeConverter() {
    }

    /**
     * Converts the given type into a String useful for logging.
     * @param type a value from {@link java.sql.Types}
     * @return the type converted to a String for logging.
     */
    public static String convert(int type) {
        String sType = (String) types.get(new Integer(type));
        return (sType == null ? "" : sType);
    }
}
