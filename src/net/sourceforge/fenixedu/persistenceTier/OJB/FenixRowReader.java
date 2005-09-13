package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.ojb.broker.accesslayer.RowReaderDefaultImpl;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.util.ClassHelper;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.stm.TransactionChangeLogs;
import net.sourceforge.fenixedu.stm.Transaction;
import net.sourceforge.fenixedu.stm.VBox;


public class FenixRowReader extends RowReaderDefaultImpl {
    private static final Object[] NO_ARGS = {};

    public FenixRowReader(ClassDescriptor cld) {
        super(cld);
    }

    protected Object buildOrRefreshObject(Map row, ClassDescriptor targetClassDescriptor, Object targetObject) {
	boolean previous = VBox.setLoading(true);
	try {
	    return origBuildOrRefreshObject(row, targetClassDescriptor, targetObject);
	} finally {
	    VBox.setLoading(previous);
	}
    }

    protected Object origBuildOrRefreshObject(Map row, ClassDescriptor targetClassDescriptor, Object targetObject) {
        Object result = targetObject;
        FieldDescriptor fmd = null;

        if (targetObject == null) {
	    result = ClassHelper.buildNewObjectInstance(targetClassDescriptor);

	    // if it is a domain object
	    if (targetClassDescriptor.getFactoryClass() == DomainAllocator.class) {
		// read idInternal
		fmd = targetClassDescriptor.getFieldDescriptorByName("idInternal");
		fmd.getPersistentField().set(result, row.get(fmd.getColumnName()));

		// we need to lock on the CHANGE_LOGS to avoid concurrent updates
		// while the object is being constructed and not in the cache
		synchronized (TransactionChangeLogs.CHANGE_LOGS) {
		    // cache object
		    Object cached = Transaction.getCache().cache((DomainObject)result);

		    if (cached == result) {
			// only initialize versions if it was not cached already
			((DomainObject)result).addKnownVersionsFromLogs();
		    }
		    result = cached;
		}
	    }
        }

        // 2. fill all scalar attributes of the new object
        FieldDescriptor[] fields = targetClassDescriptor.getFieldDescriptions();
        for (int i = 0; i < fields.length; i++) {
            fmd = fields[i];
            fmd.getPersistentField().set(result, row.get(fmd.getColumnName()));
        }

        if(targetObject == null) {
            // 3. for new build objects, invoke the initialization method for the class if one is provided
            Method initializationMethod = targetClassDescriptor.getInitializationMethod();
            if (initializationMethod != null) {
		try {
                    initializationMethod.invoke(result, NO_ARGS);
                } catch (Exception ex) {
                    throw new PersistenceBrokerException("Unable to invoke initialization method:" 
							 + initializationMethod.getName() 
							 + " for class:" 
							 + targetClassDescriptor.getClassOfObject(), 
							 ex);
                }
            }
        }
        return result;
    }


    private void forceFactoryClassAndMethod(ClassDescriptor cld) {
        if (cld.getFactoryClass() != DomainAllocator.class) {
            String className = cld.getClassOfObject().getName();
	    if (! "org.apache.ojb.broker.util.sequence.HighLowSequence".equals(className)) {
		try {
		    Method m = DomainAllocator.class.getMethod("allocate_" + className.replace('.', '_'));
		    if (m != null) {
			//System.out.println("FenixRowReader: setting factoryMethod for class " + className + " to " + m.getName());
			cld.setFactoryClass(DomainAllocator.class);
			cld.setFactoryMethod(m.getName());
		    }
		} catch (Exception e) {
		    System.out.println("FenixRowReader: NOT setting factoryMethod for class " + className + " because " + e);
		}
	    }
        }
    }

    protected ClassDescriptor selectClassDescriptor(Map row) throws PersistenceBrokerException {
        ClassDescriptor result = super.selectClassDescriptor(row);
        forceFactoryClassAndMethod(result);
        return result;
    }
}
