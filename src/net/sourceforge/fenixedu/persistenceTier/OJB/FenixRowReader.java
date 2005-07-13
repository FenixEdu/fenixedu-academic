package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.ojb.broker.accesslayer.RowReaderDefaultImpl;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.PersistenceBrokerException;


public class FenixRowReader extends RowReaderDefaultImpl {

    public FenixRowReader(ClassDescriptor cld) {
        super(cld);
    }

    private void forceFactoryClassAndMethod(ClassDescriptor cld) {
        if (cld.getFactoryClass() != DomainAllocator.class) {
            String className = cld.getClassOfObject().getName();
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

    protected ClassDescriptor selectClassDescriptor(Map row) throws PersistenceBrokerException {
        ClassDescriptor result = super.selectClassDescriptor(row);
        forceFactoryClassAndMethod(result);
        return result;
    }
}
