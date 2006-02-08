/**
 * 
 */
package pt.utl.ist.codeGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import dml.DmlCompiler;
import dml.DomainClass;
import dml.DomainModel;
import dml.Role;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RootDomainObjectGenerator {

    private static final String CLASS_NAME = "net.sourceforge.fenixedu.domain.RootDomainObject";

    private DomainModel domainModel;

    private String dmlFile;

    private String outputFolder;

    private String sourceSuffix = "_Base.java";

    private DomainModel getModel() {
        if (domainModel == null) {
            if (dmlFile.length() == 0) {
                throw new Error("No DML files specified");
            } else {
                try {
                    String[] dmlFilesArray = { dmlFile };
                    domainModel = DmlCompiler.getDomainModel(dmlFilesArray, true);
                } catch (antlr.ANTLRException ae) {
                    System.err.println("Error parsing the DML files, leaving the domain empty");
                }
            }
        }
        return domainModel;
    }

    private void appendMethodsInTheRootDomainObject() throws IOException {

        String rootObjectSourceCodeFilePath = outputFolder + "/" + CLASS_NAME.replace('.', '/')
                + sourceSuffix;

        String rootObjectSourceCode = readFile(rootObjectSourceCodeFilePath);
        int lastBrace = rootObjectSourceCode.lastIndexOf('}');
        if (lastBrace > 0) {
            StringBuilder resultSourceCode = new StringBuilder();
            resultSourceCode.append(rootObjectSourceCode.substring(0, lastBrace));

            Formatter methods = new Formatter(resultSourceCode);
            DomainClass rootDomainObjectClass = getModel().findClass(CLASS_NAME);
            for (Iterator<Role> iter = rootDomainObjectClass.getRoleSlots(); iter.hasNext();) {
                Role roleSlot = iter.next();
                if (roleSlot.getMultiplicityUpper() != 1) {
                    String slotName = StringUtils.capitalize(roleSlot.getName());
                    DomainClass otherDomainClass = (DomainClass) roleSlot.getType();
                    methods.format("\n\tpublic %s read%sByOID(Integer idInternal){\n", otherDomainClass
                            .getFullName(), otherDomainClass.getName());
                    methods.format("\t\tfor (%s iter%s : get%s()) {\n", otherDomainClass.getFullName(),
                            otherDomainClass.getName(), slotName);
                    methods.format("\t\t\tif(iter%s.getIdInternal().equals(idInternal)){\n",
                            otherDomainClass.getName());
                    methods.format("\t\t\t\treturn iter%s;\n\t\t\t}\n\t\t}\n\t\treturn null;\n\t}\n",
                            otherDomainClass.getName());
                }
            }

            resultSourceCode.append("\n\n}\n");
            //System.out.println(resultSourceCode.toString());
            writeFile(rootObjectSourceCodeFilePath, resultSourceCode.toString(), false);
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Invalid Number of Arguments");
            System.exit(1);
        }

        try {
            RootDomainObjectGenerator rootDomainObjectGenerator = new RootDomainObjectGenerator();
            rootDomainObjectGenerator.outputFolder = args[0];
            rootDomainObjectGenerator.dmlFile = args[1];
            rootDomainObjectGenerator.appendMethodsInTheRootDomainObject();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    public static String readFile(final String filename) throws IOException {
        
        final StringBuilder fileContents = new StringBuilder();
        String str= null;
        BufferedReader in = new BufferedReader(new FileReader(filename));
        while ((str = in.readLine()) != null) {
            fileContents.append(str);
            fileContents.append("\n");
        }
        in.close();
        
        return fileContents.toString();
        
    }

    public static void writeFile(final String filename, final String fileContents, final boolean append)
            throws IOException {
       
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(fileContents);
            out.close();
        
    }

}
