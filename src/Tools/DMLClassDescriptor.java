/*
 * Created on Feb 27, 2005
 *
 */
package Tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;

/**
 * @author Luis Cruz
 * 
 */
public class DMLClassDescriptor {

    private ClassDescriptor classDescriptor;

    private Map attributeDescriptors;

    public class DMLAttributeDescriptor {

        private FieldDescriptor fieldDescriptor;

        public DMLAttributeDescriptor(final FieldDescriptor fieldDescriptor) {
            this.fieldDescriptor = fieldDescriptor;
        }

        public void appendDMLDescription(StringBuffer stringBuffer) {
            final Class fieldType = fieldDescriptor.getPersistentField().getType();
            if (isSupportedDMLField(fieldType)) {
                stringBuffer.append("\n\t");
                //stringBuffer.append(fieldType.getSimpleName());
                stringBuffer.append(" ");
                stringBuffer.append(getAttributeName());
                stringBuffer.append(";");
            }
        }

        protected boolean isSupportedDMLField(Class fieldType) {
            return fieldType.getName().startsWith("java.lang.");
        }

        public String getAttributeName() {
            return fieldDescriptor.getAttributeName();
        }
    }

    public DMLClassDescriptor(final String className, final ClassDescriptor classDescriptor) {
        this.classDescriptor = classDescriptor;

//        Vector vector = classDescriptor.getExtentClassNames();
//        if (vector != null) {
//            System.out.println("className= " + className);
//            for (final Iterator iterator = vector.iterator(); iterator.hasNext();) {
//                String extentClassName = (String) iterator.next();
//                System.out.println("extentClassName= " + extentClassName);
//            }
//        }

        final FieldDescriptor[] fieldDescriptors = classDescriptor.getFieldDescriptions();

        int numberFields = (fieldDescriptors != null) ? fieldDescriptors.length : 0;

        this.attributeDescriptors = new HashMap(numberFields);

        for (int i = 0; i < numberFields; i++) {
            addAttributeDescriptor(fieldDescriptors[i]);
        }
    }

    public void addAttributeDescriptor(final FieldDescriptor fieldDescriptor) {
        final DMLAttributeDescriptor dmlAttributeDescriptor = new DMLAttributeDescriptor(fieldDescriptor);

        final String attributeName = fieldDescriptor.getAttributeName();

        attributeDescriptors.put(attributeName, dmlAttributeDescriptor);
    }

    public void appendDMLDescription(final StringBuffer stringBuffer) {
        stringBuffer.append("\n\nclass ");
        stringBuffer.append(getClassName());
        stringBuffer.append(" {");
        for (final Iterator iterator = attributeDescriptors.values().iterator(); iterator.hasNext();) {
            final DMLAttributeDescriptor attributeDescriptor = (DMLAttributeDescriptor) iterator.next();
            attributeDescriptor.appendDMLDescription(stringBuffer);
        }
        stringBuffer.append("\n}");
    }

    public String getClassName() {
        return classDescriptor.getClassNameOfObject();
    }
}