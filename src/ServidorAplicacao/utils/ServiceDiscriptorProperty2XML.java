/*
 * Created on Nov 20, 2003
 *  
 */
package ServidorAplicacao.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import ServidorAplicacao.IServico;

/**
 * @author Luis Cruz
 *  
 */
public class ServiceDiscriptorProperty2XML extends FileUtil {

    protected static String implementationClassPackage;

    protected static HashMap filterChainIDs;

    /**
     *  
     */
    public ServiceDiscriptorProperty2XML() {
        super();
    }

    /**
     * args[0] input filename args[0] output filename
     *  
     */
    public static void main(String[] args) {
        filterChainIDs = FilterChainDiscriptorProperty2XML.exec(args[3], args[4], args[5],
                new FilterChainDiscriptorProperty2XML());
        inputFile = args[0];
        outputFile = args[1];
        implementationClassPackage = args[2];
        ServiceDiscriptorProperty2XML instance = new ServiceDiscriptorProperty2XML();
        instance.processFile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.utils.FileUtil#processLine(java.lang.String)
     */
    protected String processLine(Integer i, String line) {
        if (line != null && !StringUtils.trim(line).equals("")) {
            String implementationClassName = StringUtils.trim(StringUtils.chomp(line, "="));

            String serviceAlias = "";
            try {
                Class service = Class
                        .forName(implementationClassPackage + "." + implementationClassName);
                Method method = service.getDeclaredMethod("getService", null);
                IServico serviceInstance = (IServico) method.invoke(service, null);
                serviceAlias = serviceInstance.getNome();
            } catch (Exception e) {

                e.printStackTrace();
            }

            String filterToApply = StringUtils.trim(StringUtils.substringAfter(line, "="));
            String discription = "";

            String isTransactional = "false";
            try {
                String nextLine = bufferedReader.readLine();
                if (StringUtils.trim(StringUtils.substringAfter(nextLine, "=")).equals("1")) {
                    isTransactional = "true";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String serviceDescriptor = "\t<service>\n";
            serviceDescriptor += "\t\t<idInternal>" + i + "</idInternal>\n";
            serviceDescriptor += "\t\t<name>" + serviceAlias + "</name>\n";
            serviceDescriptor += "\t\t<implementationClass>" + implementationClassPackage + "."
                    + implementationClassName + "</implementationClass>\n";
            serviceDescriptor += "\t\t<description>" + discription + "</description>\n";
            serviceDescriptor += "\t\t<isTransactional>" + isTransactional + "</isTransactional>\n";
            serviceDescriptor += "\t\t<filterChains>";
            if (filterChainIDs.get(filterToApply) != null) {
                serviceDescriptor += "\n\t\t\t<chain id=\"" + filterChainIDs.get(filterToApply)
                        + "\"/>\n\t\t";
            }
            serviceDescriptor += "</filterChains>\n";
            serviceDescriptor += "\t</service>\n";

            return serviceDescriptor;
        }
        return new String();

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.utils.FileUtil#generateHeader()
     */
    protected String generateHeader() {
        String header = "<serviceDefinitions>\n";
        return header;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.utils.FileUtil#generateFooter()
     */
    protected String generateFooter() {
        String footer = "</serviceDefinitions>\n";
        return footer;
    }

}