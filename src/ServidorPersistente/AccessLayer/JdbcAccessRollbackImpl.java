/*
 * Created on 10/Dez/2004
 */
package ServidorPersistente.AccessLayer;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.accesslayer.JdbcAccessImpl;
import org.apache.ojb.broker.accesslayer.ResultSetAndStatement;
import org.apache.ojb.broker.core.ValueContainer;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.query.Query;

import Util.FileUtils;
import _development.PropertiesManager;

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
        //super.executeDelete(arg0, arg1);
    }

    public void executeInsert(ClassDescriptor arg0, Object arg1) throws PersistenceBrokerException {
        concatSQLInstructionDelete(arg0, arg1);
        super.executeInsert(arg0, arg1);
    }

    public void executeUpdate(ClassDescriptor arg0, Object arg1) throws PersistenceBrokerException {
        concatSQLInstructionUpdate(arg0, arg1);

        super.executeUpdate(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     */
    private void concatSQLInstructionUpdate(ClassDescriptor arg0, Object arg1) {
        ResultSetAndStatement resultSetAndStatement = getOriginalResultSet(arg0, arg1);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("update ");
        stringBuffer.append(arg0.getFullTableName());
        stringBuffer.append(" set ");
        try {

            FieldDescriptor[] fieldDescriptors = arg0.getFieldDescriptions();
            for (int i = 0; i < fieldDescriptors.length; i++) {
                String columnName = fieldDescriptors[i].getColumnName();
                resultSetAndStatement.m_rs.next();
                String property = resultSetAndStatement.m_rs.getString(columnName);
                stringBuffer.append(columnName);
                stringBuffer.append("='");
                stringBuffer.append(property);
                stringBuffer.append("'");
                if (i != fieldDescriptors.length - 1) {
                    stringBuffer.append(", ");
                }

            }
            addCriteria(arg0, fieldDescriptors, stringBuffer, arg1, ", ");

            stringBuffer.append(" where ");

            FieldDescriptor[] fieldDescriptorsPK = arg0.getPkFields();
            addCriteria(arg0, fieldDescriptorsPK, stringBuffer, arg1, " and ");
            
            FileUtils.writeFile(PropertiesManager.getProperty("storageFileName"), stringBuffer.toString(), true);
            
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
        StringBuffer selectInstructionSQL = new StringBuffer();
        selectInstructionSQL.append("select * from ");
        selectInstructionSQL.append(arg0.getFullTableName());
        selectInstructionSQL.append(" where ");
        addCriteria(arg0, arg0.getPkFields(), selectInstructionSQL, arg1, " and ");
        selectInstructionSQL.append(" ; ");
        ResultSetAndStatement resultSetAndStatement = executeSQL(selectInstructionSQL.toString(), arg0, false);
        return resultSetAndStatement;
    }

    public int executeUpdateSQL(String arg0, ClassDescriptor arg1, ValueContainer[] arg2,
            ValueContainer[] arg3) throws PersistenceBrokerException {
        // TODO Auto-generated method stub
        return super.executeUpdateSQL(arg0, arg1, arg2, arg3);
    }

    public int executeUpdateSQL(String arg0, ClassDescriptor arg1) throws PersistenceBrokerException {
        // TODO Auto-generated method stub
        return super.executeUpdateSQL(arg0, arg1);
    }

    public Object materializeObject(ClassDescriptor arg0, Identity arg1)
            throws PersistenceBrokerException {
        // TODO Auto-generated method stub
        return super.materializeObject(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     */
    private void concatSQLInstructionInsert(ClassDescriptor arg0, Object arg1) {
        StringBuffer insertInstruction = new StringBuffer();
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
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("delete from ");
        stringBuffer.append(arg0.getFullTableName());
        stringBuffer.append(" where ");

        FieldDescriptor[] fieldDescriptors = arg0.getFieldDescriptions();
        addCriteria(arg0, fieldDescriptors, stringBuffer, arg1, " and ");
    }

    /**
     * @param arg0
     * @param arg1
     * @param stringBuffer
     * @param arg1
     */
    private void addCriteria(ClassDescriptor arg0, FieldDescriptor[] fieldDescriptors,
            StringBuffer stringBuffer, Object arg1, String separator) {
        try {
            for (int i = 0; i < fieldDescriptors.length; i++) {
                String columnName = fieldDescriptors[i].getColumnName();
                String property = getPropertyValue(fieldDescriptors[i], arg1);
                stringBuffer.append(columnName);
                stringBuffer.append("='");
                stringBuffer.append(property);
                stringBuffer.append("'");
                if (i != fieldDescriptors.length - 1) {
                    stringBuffer.append(separator);
                }
            }
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
     * @param insertInstruction
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void constructSQLInstructionBody(ClassDescriptor arg0, Object arg1,
            StringBuffer insertInstruction) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        StringBuffer insertInstructionValues = new StringBuffer();
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

}
