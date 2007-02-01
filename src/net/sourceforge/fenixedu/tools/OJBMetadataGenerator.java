/**
 * 
 */
package net.sourceforge.fenixedu.tools;

import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.framework.FenixPersistentField;
import net.sourceforge.fenixedu.stm.OJBFunctionalSetWrapper;
import net.sourceforge.fenixedu.util.StringFormatter;

import net.sourceforge.fenixedu.persistenceTier.OJB.DomainAllocator;


import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;
import org.apache.ojb.broker.metadata.fieldaccess.PersistentField;

import dml.DmlCompiler;
import dml.DomainClass;
import dml.DomainEntity;
import dml.DomainModel;
import dml.Role;
import dml.Slot;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class OJBMetadataGenerator {

    private static final Set<String> unmappedObjectReferenceAttributesInDML = new TreeSet<String>();

    private static final Set<String> unmappedCollectionReferenceAttributesInDML = new TreeSet<String>();

    private static final Set<String> unmappedObjectReferenceAttributesInOJB = new TreeSet<String>();

    private static final Set<String> unmappedCollectionReferenceAttributesInOJB = new TreeSet<String>();

    private static String classToDebug = null;

    private static ResourceBundle rbConversors = ResourceBundle.getBundle("dataTypeConversors");

    private static ResourceBundle rbJDBCTypes = ResourceBundle.getBundle("dataTypesJDBCTypes");

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String[] dmlFilesArray = { args[0] };
        if (args.length == 2) {
            classToDebug = args[1];
        }

        DomainModel domainModel = DmlCompiler.getFenixDomainModel(dmlFilesArray);
        Map ojbMetadata = MetadataManager.getInstance().getGlobalRepository().getDescriptorTable();

        updateOJBMappingFromDomainModel(ojbMetadata, domainModel);

        printUnmmapedAttributes(unmappedObjectReferenceAttributesInDML,
                "UnmappedObjectReferenceAttributes in DML:");
        printUnmmapedAttributes(unmappedCollectionReferenceAttributesInDML,
                "UnmappedCollectionReferenceAttributes in DML:");
        printUnmmapedAttributes(unmappedObjectReferenceAttributesInOJB,
                "UnmappedObjectReferenceAttributes in OJB:");
        printUnmmapedAttributes(unmappedCollectionReferenceAttributesInOJB,
                "UnmappedCollectionReferenceAttributes in OJB:");

        System.exit(0);

    }

    private static void printUnmmapedAttributes(Set<String> unmappedAttributesSet, String title) {
        if (!unmappedAttributesSet.isEmpty()) {
            System.out.println();
            System.out.println(title);
            for (String objectReference : unmappedAttributesSet) {
                System.out.println(objectReference);
            }
        }
    }

    public static void updateOJBMappingFromDomainModel(Map ojbMetadata, DomainModel domainModel)
            throws Exception {

        for (final Iterator iterator = domainModel.getClasses(); iterator.hasNext();) {
            final DomainClass domClass = (DomainClass) iterator.next();

            final ClassDescriptor classDescriptor = (ClassDescriptor) ojbMetadata.get(domClass
                    .getFullName());

            if (classDescriptor != null) {

                setFactoryMethodAndClass(classDescriptor);

                final Class clazz = Class.forName(domClass.getFullName());
                updateFields(classDescriptor, domClass, ojbMetadata, clazz);
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    updateRelations(classDescriptor, domClass, ojbMetadata, clazz);
                }
                
                if (classToDebug != null && classDescriptor.getClassNameOfObject().contains(classToDebug)) {
                    System.out.println(classDescriptor.toXML());
                }
            }

        }

    }

    private static void setFactoryMethodAndClass(ClassDescriptor cld) {
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


    protected static void updateFields(final ClassDescriptor classDescriptor,
            final DomainClass domClass, Map ojbMetadata, Class persistentFieldClass) throws Exception {

        DomainEntity domEntity = domClass;
        int fieldID = 1;

        addFieldDescriptor("idInternal", "java.lang.Integer", fieldID++, classDescriptor, persistentFieldClass);

        while (domEntity instanceof DomainClass) {
            DomainClass dClass = (DomainClass) domEntity;

            Iterator<Slot> slots = dClass.getSlots();
            while (slots.hasNext()) {
                Slot slot = slots.next();

                addFieldDescriptor(slot.getName(), slot.getType(), fieldID++, classDescriptor, persistentFieldClass);
            }

            domEntity = dClass.getSuperclass();
        }

    }

    protected static void addFieldDescriptor(String slotName,
                                             String slotType,
                                             int fieldID, 
                                             ClassDescriptor classDescriptor,
                                             Class persistentFieldClass) {
        if (classDescriptor.getFieldDescriptorByName(slotName) == null){
            FieldDescriptor fieldDescriptor = new FieldDescriptor(classDescriptor, fieldID);
            fieldDescriptor.setColumnName(StringFormatter.convertToDBStyle(slotName));
            // System.out.println(">> " + slotType);
            // System.out.println("\t" +
            // java2JdbcConversion.get(slotType));
            fieldDescriptor.setColumnType(rbJDBCTypes.getString(slotType).trim());

            fieldDescriptor.setAccess("readwrite");

            try {
                String conversor = rbConversors.getString(slotType).trim();
                fieldDescriptor.setFieldConversionClassName(conversor);
            } catch (MissingResourceException e) {
            }

            if (slotName.equals("idInternal")) {
                fieldDescriptor.setPrimaryKey(true);
                fieldDescriptor.setAutoIncrement(true);
            }
            PersistentField persistentField = new FenixPersistentField(persistentFieldClass, slotName);
            fieldDescriptor.setPersistentField(persistentField);
            classDescriptor.addFieldDescriptor(fieldDescriptor);
        }
    }

    protected static void updateRelations(final ClassDescriptor classDescriptor,
            final DomainClass domClass, Map ojbMetadata, Class persistentFieldClass) throws Exception {

        DomainEntity domEntity = domClass;
        while (domEntity instanceof DomainClass) {
            DomainClass dClass = (DomainClass) domEntity;

            // roles
            Iterator roleSlots = dClass.getRoleSlots();
            while (roleSlots.hasNext()) {
                Role role = (Role) roleSlots.next();
                String roleName = role.getName();

                if (domClass.getFullName().equals("net.sourceforge.fenixedu.domain.RootDomainObject")
                        && roleName != null
                        && (roleName.equals("rootDomainObject") || roleName.equals("rootDomainObjects"))) {
                    continue;
                }

                if (role.getMultiplicityUpper() == 1) {

                    // reference descriptors
                    if (classDescriptor.getObjectReferenceDescriptorByName(roleName) == null) {

                        String foreignKeyField = "key" + StringUtils.capitalize(roleName);

                        if (findSlotByName(dClass, foreignKeyField) == null) {
                            unmappedObjectReferenceAttributesInDML.add(foreignKeyField + " -> "
                                    + dClass.getName());
                            continue;
                        }

                        if (classDescriptor.getFieldDescriptorByName(foreignKeyField) == null) {
                            Class classToVerify = Class.forName(dClass.getFullName());
                            if (!Modifier.isAbstract(classToVerify.getModifiers())) {
                                unmappedObjectReferenceAttributesInOJB.add(foreignKeyField + " -> "
                                        + dClass.getName());
                                continue;
                            }
                        }

                        generateReferenceDescriptor(classDescriptor, persistentFieldClass, role,
                                roleName, foreignKeyField);

                    }
                } else {

                    // collection descriptors
                    if (classDescriptor.getCollectionDescriptorByName(roleName) == null) {

                        CollectionDescriptor collectionDescriptor = new CollectionDescriptor(
                                classDescriptor);

                        if (role.getOtherRole().getMultiplicityUpper() == 1) {

                            String foreignKeyField = "key"
                                    + StringUtils.capitalize(role.getOtherRole().getName());

                            if (findSlotByName((DomainClass) role.getType(), foreignKeyField) == null) {
                                unmappedCollectionReferenceAttributesInDML.add(foreignKeyField + " | "
                                        + ((DomainClass) role.getType()).getName() + " -> "
                                        + dClass.getName());
                                continue;
                            }

                            ClassDescriptor otherClassDescriptor = (ClassDescriptor) ojbMetadata
                                    .get(((DomainClass) role.getType()).getFullName());

                            if (otherClassDescriptor == null) {
                                System.out.println("Ignoring "
                                        + ((DomainClass) role.getType()).getFullName());
                                continue;
                            }

                            generateOneToManyCollectionDescriptor(collectionDescriptor, foreignKeyField);

                        } else {
                            generateManyToManyCollectionDescriptor(collectionDescriptor, role);

                        }
                        updateCollectionDescriptorWithCommonSettings(classDescriptor,
                                persistentFieldClass, role, roleName, collectionDescriptor);
                    }
                }
            }

            domEntity = dClass.getSuperclass();
        }
    }

    private static void updateCollectionDescriptorWithCommonSettings(
            final ClassDescriptor classDescriptor, Class persistentFieldClass, Role role,
            String roleName, CollectionDescriptor collectionDescriptor) throws ClassNotFoundException {
        collectionDescriptor.setItemClass(Class.forName(role.getType().getFullName()));
        collectionDescriptor.setPersistentField(persistentFieldClass, roleName);
        collectionDescriptor.setRefresh(false);
        collectionDescriptor.setCollectionClass(OJBFunctionalSetWrapper.class);
        collectionDescriptor.setCascadeRetrieve(false);
        collectionDescriptor.setLazy(false);
        classDescriptor.addCollectionDescriptor(collectionDescriptor);
    }

    private static void generateManyToManyCollectionDescriptor(
            CollectionDescriptor collectionDescriptor, Role role) {

        String indirectionTableName = StringFormatter.splitCamelCaseString(role.getRelation().getName())
                .replace(' ', '_').toUpperCase();
        String fkToItemClass = "KEY_"
                + StringFormatter.splitCamelCaseString(role.getType().getName()).replace(' ', '_')
                        .toUpperCase();
        String fkToThisClass = "KEY_"
                + StringFormatter.splitCamelCaseString(role.getOtherRole().getType().getName()).replace(
                        ' ', '_').toUpperCase();

        if (fkToItemClass.equals(fkToThisClass)) {
            fkToItemClass = fkToItemClass
                    + "_"
                    + StringFormatter.splitCamelCaseString(role.getName()).replace(' ', '_')
                            .toUpperCase();
            fkToThisClass = fkToThisClass
                    + "_"
                    + StringFormatter.splitCamelCaseString(role.getOtherRole().getName()).replace(' ',
                            '_').toUpperCase();
        }

        collectionDescriptor.setIndirectionTable(indirectionTableName);
        collectionDescriptor.addFkToItemClass(fkToItemClass);
        collectionDescriptor.addFkToThisClass(fkToThisClass);
        collectionDescriptor.setCascadingStore(ObjectReferenceDescriptor.CASCADE_NONE);
        collectionDescriptor.setCascadingDelete(ObjectReferenceDescriptor.CASCADE_NONE);
    }

    private static void generateOneToManyCollectionDescriptor(CollectionDescriptor collectionDescriptor,
            String foreignKeyField) {
        collectionDescriptor.setCascadingStore(ObjectReferenceDescriptor.CASCADE_NONE);
        collectionDescriptor.addForeignKeyField(foreignKeyField);
    }

    private static void generateReferenceDescriptor(final ClassDescriptor classDescriptor,
            Class persistentFieldClass, Role role, String roleName, String foreignKeyField)
            throws ClassNotFoundException {
        ObjectReferenceDescriptor referenceDescriptor = new ObjectReferenceDescriptor(classDescriptor);
        referenceDescriptor.setItemClass(Class.forName(role.getType().getFullName()));
        referenceDescriptor.addForeignKeyField(foreignKeyField);
        referenceDescriptor.setPersistentField(persistentFieldClass, roleName);
        referenceDescriptor.setCascadeRetrieve(false);
        referenceDescriptor.setCascadingStore(ObjectReferenceDescriptor.CASCADE_NONE);
        referenceDescriptor.setLazy(false);

        classDescriptor.addObjectReferenceDescriptor(referenceDescriptor);
    }

    private static Slot findSlotByName(DomainClass domainClass, String slotName) {
        DomainClass domainClassIter = domainClass;
        while (domainClassIter != null) {

            for (Iterator<Slot> slotsIter = domainClassIter.getSlots(); slotsIter.hasNext();) {
                Slot slot = (Slot) slotsIter.next();
                if (slot.getName().equals(slotName)) {
                    return slot;
                }
            }

            domainClassIter = (DomainClass) domainClassIter.getSuperclass();
        }

        return null;
    }

}
