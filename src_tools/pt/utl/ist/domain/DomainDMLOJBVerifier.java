package pt.utl.ist.domain;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainObject_Base;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;

public class DomainDMLOJBVerifier {

    private static final Logger logger = Logger.getLogger(DomainDMLOJBVerifier.class);

    private static final Set<Class> unmappedClasses = new TreeSet<Class>(new Comparator() {
        public int compare(Object o1, Object o2) {
            final Class class1 = (Class) o1;
            final Class class2 = (Class) o2;
            return class1.getName().compareTo(class2.getName());
        }
    });

    private static final Set<String> unmappedAttributes = new TreeSet<String>();

    private static final Set<String> unmappedObjectReferenceAttributes = new TreeSet<String>();

    private static final Set<String> unmappedCollectionReferenceAttributes = new TreeSet<String>();

    public static void main(final String[] args) throws SecurityException, NoSuchMethodException {
        final Map ojbMetadata = getDescriptorTable();
        verifyDMLMappingFromOJBMetadata(ojbMetadata);

        logVerificationResults();
        logger.info("\nVerification complete.");
        System.exit(0);
    }

    protected static Map getDescriptorTable()
    {
        final MetadataManager metadataManager = MetadataManager.getInstance();
        final DescriptorRepository descriptorRepository = metadataManager.getGlobalRepository();
        return descriptorRepository.getDescriptorTable();
    }

    protected static void verifyDMLMappingFromOJBMetadata(final Map ojbMetadata) throws SecurityException, NoSuchMethodException {
        for (final Iterator iterator = ojbMetadata.values().iterator(); iterator.hasNext(); ) {
            final ClassDescriptor classDescriptor = (ClassDescriptor) iterator.next();
            final Class mappedClass = classDescriptor.getClassOfObject();

            if (isDomainObject(mappedClass)) {
                final Class baseClass = mappedClass.getSuperclass();
                if (baseClass.getName().endsWith("_Base")) {
                    verify(classDescriptor, baseClass);
                } else {
                    unmappedClasses.add(mappedClass);
                }
            } else {
                // These can be ignored, they are classes used by OJB.
                logger.debug("Ignoreing non-domain class " + mappedClass.getName());
            }
        }
    }

    protected static boolean isDomainObject(final Class mappedClass) {
        return mappedClass.getName().equals(DomainObject.class.getName())
            || (!mappedClass.getName().equals(Object.class.getName())
                    && isDomainObject(mappedClass.getSuperclass()));
    }

    protected static void verify(final ClassDescriptor classDescriptor, final Class baseClass) throws SecurityException, NoSuchMethodException {
        final FieldDescriptor[] fieldDescriptors = classDescriptor.getFieldDescriptions();
        if (fieldDescriptors != null) {
            for (int i = 0; i < fieldDescriptors.length; i++) {
                final String attributeName = fieldDescriptors[i].getAttributeName();
                if (!hasGetterAndSetter(baseClass, attributeName, fieldDescriptors[i].getPersistentField().getType())) {
                    unmappedAttributes.add(classDescriptor.getClassNameOfObject() + "." + attributeName);
                }
            }
        }

        final Vector referenceDescriptors = classDescriptor.getObjectReferenceDescriptors();
        for (final Iterator iterator = referenceDescriptors.iterator(); iterator.hasNext(); ) {
            final ObjectReferenceDescriptor referenceDescriptor = (ObjectReferenceDescriptor) iterator.next();
            if (!hasGetterAndSetter(baseClass, referenceDescriptor.getAttributeName(), referenceDescriptor.getItemClass())) {
                unmappedObjectReferenceAttributes.add(classDescriptor.getClassNameOfObject() + "." + referenceDescriptor.getAttributeName());
            }
        }

        final Vector collectionDescriptors = classDescriptor.getCollectionDescriptors();
        for (final Iterator iterator = collectionDescriptors.iterator(); iterator.hasNext(); ) {
            final CollectionDescriptor collectionDescriptor = (CollectionDescriptor) iterator.next();
            if (!hasGetterAndSetter(baseClass, collectionDescriptor.getAttributeName(), List.class)) {
                unmappedCollectionReferenceAttributes.add(classDescriptor.getClassNameOfObject() + "." + collectionDescriptor.getAttributeName());
            }
        }
    }

    protected static boolean hasGetterAndSetter(final Class baseClass, final String attributeName, final Class attributeClass) throws SecurityException, NoSuchMethodException {
        final String capitalizedAttributeName = StringUtils.capitalise(attributeName);
        final String getMethodName = "get" + capitalizedAttributeName;
        final String setMethodName = "set" + capitalizedAttributeName;

        Method getter;
        Method setter;
        try {
             getter = baseClass.getDeclaredMethod(getMethodName, null);
        } catch (Exception ex) {
            getter = null;
        }
        try {
            setter = baseClass.getDeclaredMethod(setMethodName, new Class[] { attributeClass });
       } catch (Exception ex) {
           setter = null;
       }
       if (setter == null
               && !baseClass.getName().equals(DomainObject_Base.class.getName())) {
           try {
               setter = baseClass.getDeclaredMethod(setMethodName, new Class[] { attributeClass.getSuperclass().getInterfaces()[0] } );
           } catch (Exception ex) {
               setter = null;
           }
       }

        return (getter != null && setter != null)
                || (!baseClass.getName().equals(DomainObject_Base.class.getName()) && hasGetterAndSetter(
                        baseClass.getSuperclass(), attributeName, attributeClass));
    }

    protected static void logVerificationResults() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nNo DML mapping found for ");
        stringBuilder.append(unmappedClasses.size());
        stringBuilder.append(" classes: \n");
        for (final Iterator iterator = unmappedClasses.iterator(); iterator.hasNext(); ) {
            final Class clazz = (Class) iterator.next();
            stringBuilder.append(clazz.getName());
            stringBuilder.append("\n");
        }

        stringBuilder.append("\nFound ");
        stringBuilder.append(unmappedAttributes.size());
        stringBuilder.append(" attributes not mapped in DML mapping: \n");
        logCollectionOfStrings(stringBuilder, unmappedAttributes);
        stringBuilder.append("\n");

        stringBuilder.append("\nFound ");
        stringBuilder.append(unmappedObjectReferenceAttributes.size());
        stringBuilder.append(" object reference attributes not mapped in DML mapping: \n");
        logCollectionOfStrings(stringBuilder, unmappedObjectReferenceAttributes);
        stringBuilder.append("\n");

        stringBuilder.append("\nFound ");
        stringBuilder.append(unmappedCollectionReferenceAttributes.size());
        stringBuilder.append(" colletion reference attributes not mapped in DML mapping: \n");
        logCollectionOfStrings(stringBuilder, unmappedCollectionReferenceAttributes);
        stringBuilder.append("\n");

        logger.warn(stringBuilder.toString());
    }

    protected static void logCollectionOfStrings(final StringBuilder stringBuilder, final Collection collection) {
        for (final Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
            final String attribute = (String) iterator.next();
            stringBuilder.append(attribute);
            stringBuilder.append("\n");
        }
    }
}
