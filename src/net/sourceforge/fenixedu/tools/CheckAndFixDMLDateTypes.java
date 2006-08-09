/**
 * 
 */
package net.sourceforge.fenixedu.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CheckAndFixDMLDateTypes {

    /**
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {

        Map ojbMetadata = MetadataManager.getInstance().getGlobalRepository().getDescriptorTable();

        Formatter newFile = new Formatter("new_domain_model.dml");
        Set<String> alterCommands = new HashSet<String>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(args[0]));

            boolean insideClass = false;
            ClassDescriptor classDescriptor = null;
            String dmlFileLine;
            while ((dmlFileLine = in.readLine()) != null) {
                String dmlFileLineToAppend = dmlFileLine;
                dmlFileLine = dmlFileLine.trim();
                if (dmlFileLine.startsWith("class")) {
                    insideClass = true;
                    String className = dmlFileLine.split(" ")[1].replace(";", "");
                    classDescriptor = (ClassDescriptor) ojbMetadata.get(className);

                } else if (insideClass) {
                    if (dmlFileLine.contains("}")) {
                        insideClass = false;
                        classDescriptor = null;
                    } else if (classDescriptor != null) {
                        String[] lineSplitted = dmlFileLine.split(" ");
                        if (lineSplitted.length > 1) {
                            String fieldName = lineSplitted[1].replace(";", "");
                            FieldDescriptor fieldDescriptor = classDescriptor
                                    .getFieldDescriptorByName(fieldName);
                            if (fieldDescriptor != null) {
                                dmlFileLineToAppend = buildNewLineToAppend(dmlFileLineToAppend,
                                        fieldName, fieldDescriptor, alterCommands);
                            } else {
                                boolean foundInExtentClass = false;
                                for (String extentClassName : (Vector<String>) classDescriptor
                                        .getExtentClassNames()) {
                                    ClassDescriptor extentClassDescriptor = (ClassDescriptor) ojbMetadata
                                            .get(extentClassName);
                                    if (extentClassDescriptor != null) {
                                        fieldDescriptor = extentClassDescriptor
                                                .getFieldDescriptorByName(fieldName);
                                        if (fieldDescriptor != null) {
                                            dmlFileLineToAppend = buildNewLineToAppend(
                                                    dmlFileLineToAppend, fieldName, fieldDescriptor,
                                                    alterCommands);
                                            foundInExtentClass = true;
                                            break;
                                        }
                                    } else {
                                        System.out.println("WTF??? " + extentClassName);
                                    }
                                }
                                if (!foundInExtentClass) {
                                    System.out
                                            .println("ERROR: " + classDescriptor.getClassNameOfObject()
                                                    + " - " + fieldName);
                                }
                            }
                        }
                    }
                }

                newFile.format(dmlFileLineToAppend);
                newFile.format("\n");
            }
            in.close();
        } catch (IOException e) {
            newFile.close();
        }

        newFile.flush();
        newFile.close();

        Formatter renameColumnsFile = new Formatter("renameDateColumns.sql");
        for (String command : alterCommands) {
            renameColumnsFile.format("%s\n", command);
        }
        renameColumnsFile.flush();
        renameColumnsFile.close();

    }

    private static String buildNewLineToAppend(String dmlFileLineToAppend, String fieldName,
            FieldDescriptor fieldDescriptor, Set<String> alterCommands) {
        if (fieldDescriptor.getColumnType().equals("TIME")) {
            dmlFileLineToAppend = "\tHourMinuteSecond " + fieldName + "HourMinuteSecond;";
            buildColumnRenamer(alterCommands, fieldDescriptor, "_HOUR_MINUTE_SECOND", fieldName);
        } else if (fieldDescriptor.getColumnType().equals("DATE")) {
            dmlFileLineToAppend = "\tYearMonthDay " + fieldName + "YearMonthDay;";
            buildColumnRenamer(alterCommands, fieldDescriptor, "_YEAR_MONTH_DAY", fieldName);
        } else if (fieldDescriptor.getColumnType().equals("TIMESTAMP")) {
            dmlFileLineToAppend = "\tDateTime " + fieldName + "DateTime;";
            buildColumnRenamer(alterCommands, fieldDescriptor, "_DATE_TIME", fieldName);
        }
        return dmlFileLineToAppend;
    }

    private static void buildColumnRenamer(Set<String> alterCommands, FieldDescriptor descriptor,
            String columnNameSuffix, String fieldName) {

        ClassDescriptor classDescriptor = descriptor.getClassDescriptor();
        if (classDescriptor.getFullTableName() != null) {
            String renamedFieldName = StringFormatter.convertToDBStyle(fieldName);
            alterCommands.add("alter table "
                    + classDescriptor.getFullTableName()
                    + " change column "
                    + renamedFieldName
                    + " "
                    + renamedFieldName
                    + columnNameSuffix
                    + " "
                    + DBColumnInfo.getInfoWithType(classDescriptor.getFullTableName(), renamedFieldName,
                            true) + ";");
        }
    }

}
