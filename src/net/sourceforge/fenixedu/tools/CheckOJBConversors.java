/**
 * 
 */
package net.sourceforge.fenixedu.tools;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;

import antlr.ANTLRException;
import dml.DmlCompiler;
import dml.DomainClass;
import dml.DomainModel;
import dml.Slot;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CheckOJBConversors {

    // public static Set<String> typeConversions = new HashSet<String>();
    // public static Map<String,Set<String>> typeConversions = new
    // HashMap<String, Set<String>>();

    public static void main(String[] args) throws ANTLRException, FileNotFoundException {

        String[] dmlFilesArray = { args[0] };
        DomainModel domainModel = DmlCompiler.getFenixDomainModel(dmlFilesArray);
        Map ojbMetadata = MetadataManager.getInstance().getGlobalRepository().getDescriptorTable();

        Map<String, Set<String>> typeConversions = checkDataTypes(ojbMetadata, domainModel);
        printResults(typeConversions);

        System.out.println("*******************************************");

        Map<String, Set<String>> converters = checkConversors(ojbMetadata, domainModel);
        printResults(converters);

    }

    private static void printResults(Map<String, Set<String>> typeConversions) {
        for (String key : typeConversions.keySet()) {
            Set<String> types = typeConversions.get(key);
            if (types.size() == 1) {
                continue;
            }
            System.out.println();
            System.out.println(key);
            for (String type : types) {
                System.out.println("\t- " + type);
                // System.out.println("primitiveJavaType2DataTypeConversor.put(\""
                // + key + "\", \"" + type + "\");");
            }
        }
    }

    private static Map<String, Set<String>> checkConversors(Map ojbMetadata, DomainModel domainModel) {

        Map<String, Set<String>> converters = new HashMap<String, Set<String>>();

        for (ClassDescriptor classDescriptor : (Collection<ClassDescriptor>) ojbMetadata.values()) {

            if (!classDescriptor.getClassNameOfObject().startsWith("org.apache.ojb")) {
                DomainClass domClass = getDomainClass(domainModel, classDescriptor);

                FieldDescriptor[] fieldDescriptions = classDescriptor.getFieldDescriptions();
                if (fieldDescriptions != null) {
                    for (FieldDescriptor descriptor : fieldDescriptions) {
                        Slot slot = domClass.findSlot(descriptor.getAttributeName());
                        if(slot == null){
                            continue;
                        }
                        Set<String> conversionClasses = converters.get(slot.getType());
                        if (conversionClasses == null) {
                            conversionClasses = new HashSet<String>();
                            converters.put(slot.getType(), conversionClasses);
                        }
                        conversionClasses.add(descriptor.getFieldConversion().getClass().getName());
                        // typeConversions.add(descriptor.getColumnType() + " ->
                        // " + slot.getType());
                    }
                }
            }
        }
        return converters;
    }

    private static Map<String, Set<String>> checkDataTypes(Map ojbMetadata, DomainModel domainModel) throws FileNotFoundException {

        Map<String, Set<String>> typeConversions = new HashMap<String, Set<String>>();
        Set<String> alterTypesCommands = new HashSet<String>();

        for (ClassDescriptor classDescriptor : (Collection<ClassDescriptor>) ojbMetadata.values()) {

            if (!classDescriptor.getClassNameOfObject().startsWith("org.apache.ojb")) {
                DomainClass domClass = getDomainClass(domainModel, classDescriptor);

                FieldDescriptor[] fieldDescriptions = classDescriptor.getFieldDescriptions();
                if (fieldDescriptions != null) {
                    
                    if(classDescriptor.getFullTableName() == null){
                        continue;
                    }
                    
                    for (FieldDescriptor descriptor : fieldDescriptions) {
                        Slot slot = domClass.findSlot(descriptor.getAttributeName());
                        if(slot == null){
//                            System.out.println("BITCHES here -> " + classDescriptor.getClassNameOfObject() + " @ "  + descriptor.getAttributeName());
                            continue;
                        }
                        Set<String> columnTypes = typeConversions.get(slot.getType());
                        if (columnTypes == null) {
                            columnTypes = new HashSet<String>();
                            typeConversions.put(slot.getType(), columnTypes);
                        }
                        columnTypes.add(descriptor.getColumnType());

                        if (slot.getType().equals("java.lang.Double")) {
                            if ((descriptor.getColumnType().equals("VARCHAR")
                                    || descriptor.getColumnType().equals("INTEGER") || descriptor
                                    .getColumnType().equals("FLOAT"))) {
                                alterTypesCommands.add("alter table " + classDescriptor.getFullTableName()
                                        + " change column " + descriptor.getColumnName() + " " + descriptor.getColumnName() + " double " 
                                        + DBColumnInfo.getInfoWithType(classDescriptor.getFullTableName(), descriptor.getColumnName(), false) + ";");
                            }
                        } else if (slot.getType().equals("java.util.Date")) {
                            if ((descriptor.getColumnType().equals("TIME") || descriptor.getColumnType()
                                    .equals("TIME"))) {
                                System.out.println("SHIT here -> "
                                        + classDescriptor.getClassNameOfObject() + " - " + descriptor.getAttributeName());
                                
                            }
                        } else if (slot.getType().equals("java.lang.Integer")) {
                            if (descriptor.getColumnType().equals("VARCHAR")) {
                                alterTypesCommands.add("alter table " + classDescriptor.getFullTableName()
                                        + " change column " + descriptor.getColumnName() + " " + descriptor.getColumnName() + " int " 
                                        + DBColumnInfo.getInfoWithType(classDescriptor.getFullTableName(), descriptor.getColumnName(), false) + ";");                                
                            }
                        } else if (slot.getType().equals("java.lang.Boolean")) {
                            if (descriptor.getColumnType().equals("INTEGER")) {
                                alterTypesCommands.add("alter table " + classDescriptor.getFullTableName()
                                        + " change column " + descriptor.getColumnName() + " " + descriptor.getColumnName() + " tinyint(1) " 
                                        + DBColumnInfo.getInfoWithType(classDescriptor.getFullTableName(), descriptor.getColumnName(), false) + ";");                                
                            }
                        }

                    }
                }
            }
        }

        java.util.Formatter alterTables = new java.util.Formatter("alterTablesTypes.sql");
        for (String command : alterTypesCommands) {
            alterTables.format("%s\n", command);
        }
        alterTables.flush();
        alterTables.close();
        
        return typeConversions;
    }

    private static DomainClass getDomainClass(DomainModel domainModel, ClassDescriptor classDescriptor) {
        DomainClass domClass = null;
        for (final Iterator iterator = domainModel.getClasses(); iterator.hasNext();) {
            DomainClass currentDomClass = (DomainClass) iterator.next();
            if (currentDomClass.getFullName().equals(classDescriptor.getClassNameOfObject())) {
                domClass = currentDomClass;
                break;
            }
        }
        return domClass;
    }

}
