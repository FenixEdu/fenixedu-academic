package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.PBFactoryException;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.PersistenceBrokerSQLException;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.accesslayer.JdbcAccessImpl;
import org.apache.ojb.broker.util.ClassHelper;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.stm.Transaction;


public class FenixJdbcAccessImpl extends JdbcAccessImpl {

    public FenixJdbcAccessImpl(PersistenceBroker broker) {
        super(broker);
    }

    private DomainObject allocateObject(ClassDescriptor cld, ResultSet rs) throws SQLException {
        DomainObject result = (DomainObject)ClassHelper.buildNewObjectInstance(cld);

        result.setIdInternal(rs.getInt("ID_INTERNAL"));
        return result;
    }

    // copied and adapted from the superclass
    public Object materializeObject(ClassDescriptor cld, Identity oid) throws PersistenceBrokerException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try
        {
            stmt = broker.serviceStatementManager().getSelectByPKStatement(cld);
            if (stmt == null)
            {
                logger.error("getSelectByPKStatement returned a null statement");
                throw new PersistenceBrokerException("getSelectByPKStatement returned a null statement");
            }
            broker.serviceStatementManager().bindSelect(stmt, oid, cld);
            rs = stmt.executeQuery();
            // data available read object, else return null
            if (rs.next())
            {
                ClassDescriptor targetClassDescriptor = findCorrectClassDescriptor(cld, rs);

                // if it is a domain object
                if (targetClassDescriptor.getFactoryClass() == DomainAllocator.class) {
                    DomainObject materializedObject = allocateObject(targetClassDescriptor, rs);

                    // cache object
                    materializedObject = (DomainObject)Transaction.getCache().cache(materializedObject);

                    materializedObject.readFromResultSet(rs);
                    return materializedObject;
                } else {
                    java.util.Map row = new java.util.HashMap();
                    cld.getRowReader().readObjectArrayFrom(rs, row);
                    return cld.getRowReader().readObjectFrom(row);
                }
            }
            else
            {
                return null;
            }
        }
        catch (PersistenceBrokerException e)
        {
            logger.error(
                "PersistenceBrokerException during the execution of materializeObject: "
                    + e.getMessage(),
                e);
            throw e;
        }
        catch (SQLException e)
        {
            logger.error(
                "SQLException during the execution of materializeObject (for a "
                    + cld.getClassOfObject().getName()
                    + "): "
                    + e.getMessage(),
                e);
            throw new PersistenceBrokerSQLException(e);
        }
        finally
        {
            broker.serviceStatementManager().closeResources(stmt, rs);
        }
    }

    public static ClassDescriptor findCorrectClassDescriptor(ClassDescriptor cld, ResultSet rs) 
        throws PersistenceBrokerException, SQLException
    {
        // check if there is an attribute which tells us which concrete class is to be instantiated
        FieldDescriptor concreteClassFD = cld.getOjbConcreteClassField();

        if (concreteClassFD == null)
            return cld;
        else
        {
            try
            {
                String concreteClass = rs.getString(concreteClassFD.getColumnName());
                if (concreteClass == null || concreteClass.trim().length() == 0)
                {
                    throw new PersistenceBrokerException(
                            "ojbConcreteClass field returned null or 0-length string");
                }
                else
                {
                    concreteClass = concreteClass.trim();
                }
                ClassDescriptor result = cld.getRepository().getDescriptorFor(concreteClass);
                if (result == null) {
                    result = cld;
                }
                return result;
            }
            catch (PBFactoryException e)
            {
                throw new PersistenceBrokerException(e);
            }
        }
    }
}
