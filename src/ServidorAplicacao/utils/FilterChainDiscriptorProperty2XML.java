/*
 * Created on Nov 20, 2003
 *  
 */
package ServidorAplicacao.utils;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

/**
 * @author Luis Cruz
 *  
 */
public class FilterChainDiscriptorProperty2XML extends FileUtil {

    protected static String implementationClassPackage;

    protected static HashMap filterChainIDs;

    /**
     *  
     */
    public FilterChainDiscriptorProperty2XML() {
        super();
        filterChainIDs = new HashMap();
    }

    /**
     * args[0] input filename args[0] output filename
     *  
     */
    public static void main(String[] args) {
        FilterChainDiscriptorProperty2XML instance = new FilterChainDiscriptorProperty2XML();
        exec(args[0], args[1], args[2], instance);
    }

    public static HashMap exec(String inputFile, String outputFile, String implementationClassPackage,
            FilterChainDiscriptorProperty2XML instance) {
        FileUtil.inputFile = inputFile;
        FileUtil.outputFile = outputFile;
        FilterChainDiscriptorProperty2XML.implementationClassPackage = implementationClassPackage;
        instance.processFile();
        return filterChainIDs;
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
                String implementationClassName = "AccessControlFilter";
                String discription = "";
                String invocationTiming = "1";
                String filterDescriptor = "\t<filterChain>\n";
                filterDescriptor += "\t\t<idInternal>" + i + "</idInternal>\n";
                filterDescriptor += "\t\t<name>" + filterAlias + "</name>\n";
                filterDescriptor += "\t\t<expression>" + filterAlias + "</expression>\n";
                filterDescriptor += "\t\t<description>" + discription + "</description>\n";
                filterDescriptor += "\t\t<invocationTiming>" + invocationTiming
                        + "</invocationTiming>\n";
                // CONFIRM THIS!!!!!!
                filterDescriptor += "\t\t<filterClass>" + implementationClassPackage + "."
                        + implementationClassName + "</filterClass>\n";
                filterDescriptor += "\t</filterChain>\n";

                filterChainIDs.put(filterAlias, i);

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
        String header = "<filterChainsDefinitions>\n";
        return header;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.utils.FileUtil#generateFooter()
     */
    protected String generateFooter() {
        String footer = "</filterChainsDefinitions>\n";
        return footer;
    }

    /**
     * @return Returns the filterIDs.
     */
    public static HashMap getFilterIDs() {
        return filterChainIDs;
    }

    /**
     * @param filterIDs
     *            The filterIDs to set.
     */
    public static void setFilterIDs(HashMap filterChainIDs) {
        FilterChainDiscriptorProperty2XML.filterChainIDs = filterChainIDs;
    }

}