/**
 * 
 */
package net.sourceforge.fenixedu.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Modifier;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.framework.FenixPersistentField;
import net.sourceforge.fenixedu.stm.OJBFunctionalSetWrapper;
import net.sourceforge.fenixedu.util.StringFormatter;

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

    private static Formatter changeTablesCommands = null;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String[] dmlFilesArray = { args[0] };
        if (args.length == 2) {
            classToDebug = args[1];
        }

        DomainModel domainModel = DmlCompiler.getDomainModel(dmlFilesArray);
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

//        if (changeTablesCommands != null) {
//            changeTablesCommands.flush();
//        }

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
            final Class clazz = Class.forName(domClass.getFullName());

            if (!Modifier.isAbstract(clazz.getModifiers()) && !domClass.getFullName().equals("net.sourceforge.fenixedu.domain.RootDomainObject")) {
                final ClassDescriptor classDescriptor = (ClassDescriptor) ojbMetadata.get(domClass
                        .getFullName());
                if (classDescriptor != null) {
                    // add keyRootDomainObject field
                    addKeyRootDomainObjectField(clazz, classDescriptor);
                }
            }
        }        
        
        for (final Iterator iterator = domainModel.getClasses(); iterator.hasNext();) {
            final DomainClass domClass = (DomainClass) iterator.next();
            final Class clazz = Class.forName(domClass.getFullName());

            if (!Modifier.isAbstract(clazz.getModifiers())) {
                final ClassDescriptor classDescriptor = (ClassDescriptor) ojbMetadata.get(domClass
                        .getFullName());

                if (classDescriptor != null) {

                    update(classDescriptor, domClass, ojbMetadata, clazz);
                    if (classToDebug != null
                            && classDescriptor.getClassNameOfObject().contains(classToDebug)) {
                        System.out.println(classDescriptor.toXML());
                    }
                }
            }
        }
    }

    private static void addKeyRootDomainObjectField(final Class clazz,
            final ClassDescriptor classDescriptor) throws FileNotFoundException {

        if (classDescriptor.getFieldDescriptorByName("keyRootDomainObject") == null) {
            int maxFieldID = 0;
            for (FieldDescriptor descriptor : classDescriptor.getFieldDescriptions()) {
                if (descriptor.getColNo() > maxFieldID) {
                    maxFieldID = descriptor.getColNo();
                }
            }
            FieldDescriptor rootDomainObjectFieldDescriptor = new FieldDescriptor(classDescriptor, ++maxFieldID);
            rootDomainObjectFieldDescriptor.setColumnName("KEY_ROOT_DOMAIN_OBJECT");
            rootDomainObjectFieldDescriptor.setColumnType("INTEGER");
            rootDomainObjectFieldDescriptor.setAccess("readwrite");
            PersistentField persistentField = new FenixPersistentField(clazz, "keyRootDomainObject");
            rootDomainObjectFieldDescriptor.setPersistentField(persistentField);
            classDescriptor.addFieldDescriptor(rootDomainObjectFieldDescriptor);

//            if (changeTablesCommands == null) {
//                changeTablesCommands = new Formatter(new File("addRootDomainObjectKeys.sql"));
//            }
//            changeTablesCommands.format(
//                    "alter table %s add column KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1;\n",
//                    classDescriptor.getFullTableName());
        }
    }

    protected static void update(final ClassDescriptor classDescriptor, final DomainClass domClass,
            Map ojbMetadata, Class persistentFieldClass) throws Exception {

        DomainEntity domEntity = domClass;
        while (domEntity instanceof DomainClass) {
            DomainClass dClass = (DomainClass) domEntity;

            // roles
            Iterator roleSlots = dClass.getRoleSlots();
            while (roleSlots.hasNext()) {
                Role role = (Role) roleSlots.next();
                String roleName = role.getName();

                if (domClass.getFullName().equals("net.sourceforge.fenixedu.domain.RootDomainObject")
                		&& roleName != null && (roleName.equals("rootDomainObject") || roleName.equals("rootDomainObjects"))) {
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
                            
                            if(otherClassDescriptor == null){
                                System.out.println("Ignoring " + ((DomainClass) role.getType()).getFullName());
                                continue;
                            }
                            
                            
                            if (otherClassDescriptor.getFieldDescriptorByName(foreignKeyField) == null) {
                                Class classToVerify = Class.forName(otherClassDescriptor
                                        .getClassNameOfObject());
                                if (!Modifier.isAbstract(classToVerify.getModifiers())) {
                                    unmappedCollectionReferenceAttributesInOJB.add(foreignKeyField
                                            + " -> " + otherClassDescriptor.getClassNameOfObject()
                                            + " | " + classDescriptor.getClassNameOfObject());
                                    continue;
                                }

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
        referenceDescriptor.setCascadingStore(ObjectReferenceDescriptor.CASCADE_LINK);
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
