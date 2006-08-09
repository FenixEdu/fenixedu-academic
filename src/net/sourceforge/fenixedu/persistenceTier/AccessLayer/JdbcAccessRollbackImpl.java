/*
 * Created on 10/Dez/2004
 */
package net.sourceforge.fenixedu.persistenceTier.AccessLayer;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSetMetaData;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.accesslayer.JdbcAccessImpl;
import org.apache.ojb.broker.accesslayer.ResultSetAndStatement;
import org.apache.ojb.broker.core.ValueContainer;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.query.Query;

import pt.utl.ist.fenix.tools.util.FileUtils;

/**
 * @author Pedro Santos & Rita Carvalho
 */
public class JdbcAccessRollbackImpl extends JdbcAccessImpl {

    /**
     * @param arg0
     */
    public JdbcAccessRollbackImpl(PersistenceBroker arg0) {
        super(arg0);
    }

    public void executeDelete(ClassDescriptor arg0, Object arg1) throws PersistenceBrokerException {
        concatSQLInstructionInsert(arg0, arg1);
        super.executeDelete(arg0, arg1);
    }

    public void executeDelete(Query arg0, ClassDescriptor arg1) throws PersistenceBrokerException {
        throw new RuntimeException("Method not suported");
        // super.executeDelete(arg0, arg1);
    }

    public void executeInsert(ClassDescriptor arg0, Object arg1) throws PersistenceBrokerException {
        super.executeInsert(arg0, arg1);
        concatSQLInstructionDelete(arg0, arg1);
    }

    public void executeUpdate(ClassDescriptor arg0, Object arg1) throws PersistenceBrokerException {
        concatSQLInstructionUpdate(arg0, arg1);
        super.executeUpdate(arg0, arg1);
    }

    public int executeUpdateSQL(String sqlStatement, ClassDescriptor classDescriptor,
            ValueContainer[] valueContainers1, ValueContainer[] valueContainers2)
            throws PersistenceBrokerException {

        concatInverseSQLInstruction(sqlStatement, classDescriptor, valueContainers1, valueContainers2);

        return super.executeUpdateSQL(sqlStatement, classDescriptor, valueContainers1, valueContainers2);
    }

    public int executeUpdateSQL(String arg0, ClassDescriptor arg1) throws PersistenceBrokerException {
        throw new RuntimeException("Method not suported");
        // return super.executeUpdateSQL(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     */
    private void concatSQLInstructionUpdate(ClassDescriptor arg0, Object arg1) {
        ResultSetAndStatement resultSetAndStatement = getOriginalResultSet(arg0, arg1);

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("update ");
        stringBuffer.append(arg0.getFullTableName());
        stringBuffer.append(" set ");
        try {

            FieldDescriptor[] fieldDescriptors = arg0.getFieldDescriptions();
            for (int i = 0; i < fieldDescriptors.length; i++) {
                String columnName = fieldDescriptors[i].getColumnName();
                resultSetAndStatement.m_rs.first();
                String property = resultSetAndStatement.m_rs.getString(columnName);
                stringBuffer.append(columnName);

                if (property != null) {
                    stringBuffer.append("='");
                    stringBuffer.append(property);
                    stringBuffer.append("'");
                } else {
                    stringBuffer.append("= null");
                }

                if (i != fieldDescriptors.length - 1) {
                    stringBuffer.append(", ");
                }

            }
            stringBuffer.append(" where ");

            FieldDescriptor[] fieldDescriptorsPK = arg0.getPkFields();
            addCriteria(arg0, fieldDescriptorsPK, stringBuffer, arg1, " and ");
            stringBuffer.append(";\n");
            FileUtils.writeFile(PropertiesManager.getProperty("storageFileName"), stringBuffer
                    .toString(), true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param arg0
     * @param arg1
     * @return
     */
    private ResultSetAndStatement getOriginalResultSet(ClassDescriptor arg0, Object arg1) {
        StringBuilder selectInstructionSQL = new StringBuilder();
        selectInstructionSQL.append("select * from ");
        selectInstructionSQL.append(arg0.getFullTableName());
        selectInstructionSQL.append(" where ");
        addCriteria(arg0, arg0.getPkFields(), selectInstructionSQL, arg1, " and ");
        selectInstructionSQL.append(" ; ");
        ResultSetAndStatement resultSetAndStatement = executeSQL(selectInstructionSQL.toString(), arg0,
                false);

        return resultSetAndStatement;
    }

    /**
     * @param valueContainers2
     * @param valueContainers1
     * @param arg0
     * @param arg1
     * @return
     */
    private ResultSetAndStatement getOriginalResultSet(String sqlStatement,
            ClassDescriptor classDescriptors, ValueContainer[] valueContainers1,
            ValueContainer[] valueContainers2) {
        String[] tokens = sqlStatement.split(" ");

        ValueContainer[] valueContainers = joinValueContainers(valueContainers1, valueContainers2);

        StringBuilder selectInstructionSQL = new StringBuilder();
        selectInstructionSQL.append("select * from ");
        selectInstructionSQL.append(tokens[2]);
        selectInstructionSQL.append(" where ");

        for (int i = 4, j = 0; i < tokens.length; i++) {
            if (tokens[i].charAt(tokens[i].length() - 1) == '?') {
                selectInstructionSQL.append(tokens[i].substring(0, tokens[i].length() - 1));
                selectInstructionSQL.append("'");
                selectInstructionSQL.append(valueContainers[j++].getValue().toString());
                selectInstructionSQL.append("' ");
            } else {
                selectInstructionSQL.append(tokens[i]);
            }
            if (i + 2 < tokens.length) {
                selectInstructionSQL.append(" and ");
            }
            i++;
        }

        selectInstructionSQL.append(" ;");
        ResultSetAndStatement resultSetAndStatement = executeSQL(selectInstructionSQL.toString(),
                classDescriptors, false);

        return resultSetAndStatement;
    }

    /**
     * @param arg0
     * @param arg1
     */
    private void concatSQLInstructionInsert(ClassDescriptor arg0, Object arg1) {
        StringBuilder insertInstruction = new StringBuilder();
        insertInstruction.append("insert into ");

        try {
            constructSQLInstructionBody(arg0, arg1, insertInstruction);

            FileUtils.writeFile(PropertiesManager.getProperty("storageFileName"), insertInstruction
                    .toString(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param arg0
     * @param arg1
     */
    private void concatSQLInstructionDelete(ClassDescriptor arg0, Object arg1) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("delete from ");
        stringBuffer.append(arg0.getFullTableName());
        stringBuffer.append(" where ");

        FieldDescriptor[] fieldDescriptors = arg0.getFieldDescriptions();
        addCriteria(arg0, fieldDescriptors, stringBuffer, arg1, " and ");

        stringBuffer.append(";\n");

        try {
            FileUtils.writeFile(PropertiesManager.getProperty("storageFileName"), stringBuffer
                    .toString(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param arg0
     * @param arg1
     * @param stringBuffer
     * @param arg1
     */
    private void addCriteria(ClassDescriptor arg0, FieldDescriptor[] fieldDescriptors,
            StringBuilder stringBuffer, Object arg1, String separator) {
        try {
            for (int i = 0; i < fieldDescriptors.length; i++) {
                String columnName = fieldDescriptors[i].getColumnName();
                String property = getPropertyValue(fieldDescriptors[i], arg1);
                if (!fieldDescriptors[i].getPersistentField().getType().equals(Calendar.class)
                        && !fieldDescriptors[i].getPersistentField().getType().equals(Date.class)) {
                    stringBuffer.append(columnName);
                    stringBuffer.append("=");
                    if (property != null) {
                        stringBuffer.append("'");
                    }
                    stringBuffer.append(getSqlValidValue(property));
                    if (property != null) {
                        stringBuffer.append("'");
                    }
                    if (i != fieldDescriptors.length - 1) {
                        stringBuffer.append(separator);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Object getSqlValidValue(String property) {
        return property;
    }

    /**
     * @param arg0
     * @param arg1
     * @param insertInstruction
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void constructSQLInstructionBody(ClassDescriptor arg0, Object arg1,
            StringBuilder insertInstruction) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        StringBuilder insertInstructionValues = new StringBuilder();
        insertInstruction.append(arg0.getFullTableName());
        insertInstruction.append(" (");

        insertInstructionValues.append("values (");

        FieldDescriptor[] fieldDescriptors = arg0.getFieldDescriptions();

        for (int i = 0; i < fieldDescriptors.length; i++) {
            String columnName = fieldDescriptors[i].getColumnName();
            String property = getPropertyValue(fieldDescriptors[i], arg1);

            insertInstruction.append(columnName);
            insertInstructionValues.append("'");
            insertInstructionValues.append(property);
            insertInstructionValues.append("'");

            if (i != fieldDescriptors.length - 1) {
                insertInstruction.append(", ");
                insertInstructionValues.append(", ");
            }
        }
        insertInstruction.append(") ");
        insertInstructionValues.append(");\n");
        insertInstruction.append(insertInstructionValues);
    }

    /**
     * @param arg1
     * @param descriptor
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private String getPropertyValue(FieldDescriptor fieldDescriptor, Object arg1)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String attributeName = fieldDescriptor.getAttributeName();
        return BeanUtils.getProperty(arg1, attributeName);

    }

    private void concatInverseSQLInstruction(String sqlStatement, ClassDescriptor classDescriptor,
            ValueContainer[] valueContainers1, ValueContainer[] valueContainers2) {
        try {
            StringBuilder inverseSQLInstruction = new StringBuilder();

            // INSERT INTO PERSON_ROLE (KEY_PERSON,KEY_ROLE) VALUES (?,?)
            if (sqlStatement.startsWith("INSERT") || sqlStatement.startsWith("insert")) {
                Properties properties = getPropertyValuePairs(sqlStatement, valueContainers1,
                        valueContainers2);

                inverseSQLInstruction.append("delete from ");
                inverseSQLInstruction.append(getTableNameFromInsertInstruction(sqlStatement));
                inverseSQLInstruction.append(" where ");
                for (Iterator iterator = properties.entrySet().iterator(); iterator.hasNext();) {
                    Entry entry = (Entry) iterator.next();
                    inverseSQLInstruction.append(entry.getKey());
                    inverseSQLInstruction.append(" = ");
                    inverseSQLInstruction.append("'");
                    inverseSQLInstruction.append(entry.getValue());
                    inverseSQLInstruction.append("'");

                    if (iterator.hasNext()) {
                        inverseSQLInstruction.append(" and ");
                    }
                }
                inverseSQLInstruction.append(";\n");

                // DELETE FROM PERSON_ROLE WHERE KEY_PERSON=? AND KEY_ROLE=?
            } else if (sqlStatement.startsWith("DELETE") || sqlStatement.startsWith("delete")) {
                ResultSetAndStatement resultSetAndStatement = getOriginalResultSet(sqlStatement,
                        classDescriptor, valueContainers1, valueContainers2);
                if (resultSetAndStatement.m_rs.first()) {
                    ResultSetMetaData resultSetMetaData = resultSetAndStatement.m_rs.getMetaData();

                    inverseSQLInstruction.append("insert into ");
                    inverseSQLInstruction.append(getTableNameFromInsertInstruction(sqlStatement));
                    inverseSQLInstruction.append(" (");

                    StringBuilder values = new StringBuilder();
                    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                        inverseSQLInstruction.append(resultSetMetaData.getColumnName(i));
                        values.append("'");
                        values.append(resultSetAndStatement.m_rs.getString(i));
                        values.append("'");
                        if (i != resultSetMetaData.getColumnCount()) {
                            inverseSQLInstruction.append(", ");
                            values.append(", ");
                        }
                    }
                    inverseSQLInstruction.append(") values (");
                    inverseSQLInstruction.append(values);
                    inverseSQLInstruction.append(");\n");
                }
            } else {
                throw new RuntimeException("Method not suported");
            }

            FileUtils.writeFile(PropertiesManager.getProperty("storageFileName"), inverseSQLInstruction
                    .toString(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getTableNameFromInsertInstruction(String sqlStatement) {
        String[] tokens = sqlStatement.split(" ");
        return tokens[2];
    }

    private Properties getPropertyValuePairs(String sqlStatement, ValueContainer[] valueContainers1,
            ValueContainer[] valueContainers2) {
        final Properties properties = new Properties();

        ValueContainer[] valueContainers = joinValueContainers(valueContainers1, valueContainers2);

        final int startOfProperties = sqlStatement.indexOf('(') + 1;
        final int endOfProperties = sqlStatement.indexOf(')');
        final int startOfValues = endOfProperties + sqlStatement.substring(endOfProperties).indexOf('(')
                + 1;
        final int endOfValues = startOfValues + sqlStatement.substring(startOfValues).indexOf(')');

        final String[] propertyNames = sqlStatement.substring(startOfProperties, endOfProperties).split(
                ",");
        final String[] propertyValues = sqlStatement.substring(startOfValues, endOfValues).split(",");

        for (int i = 0, j = 0; i < propertyNames.length; i++) {
            String propertyValue;
            if (propertyValues[i].equals("?")) {
                propertyValue = valueContainers[j++].getValue().toString();
            } else {
                propertyValue = propertyValues[i];
            }
            properties.put(propertyNames[i], propertyValue);
        }

        return properties;
    }

    private ValueContainer[] joinValueContainers(ValueContainer[] valueContainers1,
            ValueContainer[] valueContainers2) {
        int arraySize = 0;
        if (valueContainers1 != null) {
            arraySize = valueContainers1.length;
        }
        if (valueContainers2 != null) {
            arraySize += valueContainers2.length;
        }

        final ValueContainer[] valueContainers = new ValueContainer[arraySize];
        if (valueContainers1 != null) {
            for (int i = 0; i < valueContainers1.length; i++) {
                valueContainers[i] = valueContainers1[i];
            }
        }
        if (valueContainers2 != null) {
            for (int i = 0; i < valueContainers2.length; i++) {
                valueContainers[i + valueContainers1.length] = valueContainers2[i];
            }
        }

        return valueContainers;
    }

}
