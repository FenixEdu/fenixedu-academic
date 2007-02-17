package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.lang.reflect.Method;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.stm.Transaction;
import net.sourceforge.fenixedu.stm.TransactionChangeLogs;
import net.sourceforge.fenixedu.stm.VBox;

import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.accesslayer.RowReaderDefaultImpl;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.util.ClassHelper;


public class FenixRowReader extends RowReaderDefaultImpl {
    private static final Object[] NO_ARGS = {};

    public FenixRowReader(ClassDescriptor cld) {
        super(cld);
    }

    protected Object buildOrRefreshObject(Map row, ClassDescriptor targetClassDescriptor, Object targetObject) {
        Object result = targetObject;
        FieldDescriptor fmd = null;

        if (targetObject == null) {
	    result = ClassHelper.buildNewObjectInstance(targetClassDescriptor);

	    // if it is a domain object
	    if (targetClassDescriptor.getFactoryClass() == DomainAllocator.class) {
                // the following may be used for debugging
                //Throwable t = new Throwable("Using FenixRowReader");
                //t.setStackTrace(Thread.currentThread().getStackTrace());
                //t.printStackTrace();

                // read idInternal
                fmd = targetClassDescriptor.getFieldDescriptorByName("idInternal");
                fmd.getPersistentField().set(result, row.get(fmd.getColumnName()));
                
                // cache object
                result = Transaction.getCache().cache((DomainObject)result);
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
}
