/*
 * Created on Nov 20, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.utils;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

/**
 * @author Luis Cruz
 *  
 */
public class FilterDiscriptorProperty2XML extends FileUtil {

    protected static String implementationClassPackage;

    protected static HashMap filterIDs;

    /**
     *  
     */
    public FilterDiscriptorProperty2XML() {
        super();
        filterIDs = new HashMap();
    }

    /**
     * args[0] input filename args[0] output filename
     *  
     */
    public static void main(String[] args) {
        FilterDiscriptorProperty2XML instance = new FilterDiscriptorProperty2XML();
        exec(args[0], args[1], args[2], instance);
    }

    public static HashMap exec(String inputFile, String outputFile, String implementationClassPackage,
            FilterDiscriptorProperty2XML instance) {
        FileUtil.inputFile = inputFile;
        FileUtil.outputFile = outputFile;
        FilterDiscriptorProperty2XML.implementationClassPackage = implementationClassPackage;
        instance.processFile();
        return filterIDs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.utils.FileUtil#processLine(java.lang.String)
     */
    protected String processLine(Integer i, String line) {
        if (line != null) {
            if (i.intValue() == 1) {
                // What do we do with the filter order???
            } else {
                String filterAlias = StringUtils.trim(StringUtils.chomp(line, "="));
                String implementationClassName = StringUtils.trim(StringUtils.substringAfter(line, "="));
                String discription = "";
                String isTransactional = "false";

                String filterDescriptor = "\t<filter>\n";
                filterDescriptor += "\t\t<idInternal>" + i + "</idInternal>\n";
                filterDescriptor += "\t\t<name>" + filterAlias + "</name>\n";
                filterDescriptor += "\t\t<implementationClass>" + implementationClassPackage + "."
                        + implementationClassName + "</implementationClass>\n";
                filterDescriptor += "\t\t<description>" + discription + "</description>\n";
                filterDescriptor += "\t\t<isTransactional>" + isTransactional + "</isTransactional>\n";
                filterDescriptor += "\t</filter>\n";

                filterIDs.put(filterAlias, i);

                return filterDescriptor;
            }
        }
        return new String();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.utils.FileUtil#generateHeader()
     */
    protected String generateHeader() {
        String header = "<filterDefinitions>\n";
        return header;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.utils.FileUtil#generateFooter()
     */
    protected String generateFooter() {
        String footer = "</filterDefinitions>\n";
        return footer;
    }

    /**
     * @return Returns the filterIDs.
     */
    public static HashMap getFilterIDs() {
        return filterIDs;
    }

    /**
     * @param filterIDs
     *            The filterIDs to set.
     */
    public static void setFilterIDs(HashMap filterIDs) {
        FilterDiscriptorProperty2XML.filterIDs = filterIDs;
    }

}