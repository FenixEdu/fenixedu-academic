package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.lang.reflect.Method;

import org.apache.ojb.broker.accesslayer.RowReaderDefaultImpl;
import org.apache.ojb.broker.metadata.ClassDescriptor;


public class FenixRowReader extends RowReaderDefaultImpl {

    public FenixRowReader(ClassDescriptor cld) {
        super(cld);

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
