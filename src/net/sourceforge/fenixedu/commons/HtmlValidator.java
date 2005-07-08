package net.sourceforge.fenixedu.commons;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import org.w3c.tidy.Tidy;

/**
 * @author mrsp and jdnf
 */
public class HtmlValidator {
    
    private Tidy tidy = null;
    private OutputStream outputStreamError = null; 
    
    public HtmlValidator() {
        init();
    }
    
    private void init() {
        tidy = new Tidy();
        Properties properties = new Properties();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("jtidy");
        
        convertPropertyResourceBundleToProperties(resourceBundle, properties);
        tidy.setConfigurationFromProps(properties);
        
        outputStreamError = new ByteArrayOutputStream();
        tidy.setErrout(new PrintWriter(outputStreamError, true));
    }
    
    private static void convertPropertyResourceBundleToProperties(ResourceBundle resourceBundle, Properties properties) {
        for (Enumeration enumeration = resourceBundle.getKeys(); enumeration.hasMoreElements();) {
            String key = (String) enumeration.nextElement();
            properties.setProperty(key, resourceBundle.getString(key));
        }   
    }
    
    public String getErrors() {
        return this.outputStreamError.toString();
    }
    
    public String validateHTMLString (String htmlInput) {
        
        InputStream inputStreamData = new java.io.ByteArrayInputStream(htmlInput.getBytes());
        OutputStream outputStreamData = new ByteArrayOutputStream();
        reset(this.outputStreamError);
        
        tidy.parse(inputStreamData, outputStreamData);
        
        return outputStreamData.toString();
    }
    
    private void reset(OutputStream outputStream) {
        ((ByteArrayOutputStream) outputStream).reset();
    }
}
