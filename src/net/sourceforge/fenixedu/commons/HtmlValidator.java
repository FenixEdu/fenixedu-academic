package net.sourceforge.fenixedu.commons;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import org.w3c.tidy.Node;
import org.w3c.tidy.Out;
import org.w3c.tidy.OutImpl;
import org.w3c.tidy.PPrint;
import org.w3c.tidy.StreamIn;
import org.w3c.tidy.TagTable;
import org.w3c.tidy.Tidy;

/**
 * @author mrsp and jdnf
 */
public class HtmlValidator {
    
    private Tidy tidy = null;
    private OutputStream outputStreamError = null; 
    private String errors;
    
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
        return errors;
    }
    
    public String validateHTMLString (String htmlInput) {
        
        InputStream inputStreamData = new java.io.ByteArrayInputStream(htmlInput.getBytes());
        OutputStream outputStreamData = new ByteArrayOutputStream();
        reset(this.outputStreamError);
        
        Node node = tidy.parse(inputStreamData, outputStreamData);
        reset(outputStreamData);
        
        removeMissingTitle();        
        
        Node body = node.findBody(new TagTable());               
        pprintNode(body, outputStreamData);        
        String teste = outputStreamData.toString();
        
        if(teste.length() >= 6) {
            return teste.substring(6, teste.indexOf("</body>"));
        }
        return "";
    }
    
    /**
     * 
     */
    protected void removeMissingTitle() {
        String errors_aux = this.outputStreamError.toString();
        String titleString = "missing 'title' element";
        
        int index = errors_aux.indexOf(titleString);
        
        if(index != -1){         
            String firstString="", lastString="";
            int firstIndex, index_aux = index, lastIndex;
            
            for(char c = errors_aux.charAt(index_aux); (c != '\n') && (index_aux > 0); index_aux--){}
            firstIndex = index_aux;       
            
            lastIndex = errors_aux.indexOf(titleString) + titleString.length();
            
            firstString = errors_aux.substring(0, firstIndex);
            lastString = errors_aux.substring(lastIndex+1);
            
            this.errors = firstString.concat(lastString);
        }
    }
    
    public String validateHTMLFile (String htmlInput) {
        
        InputStream inputStreamData = new java.io.ByteArrayInputStream(htmlInput.getBytes());
        OutputStream outputStreamData = new ByteArrayOutputStream();
        reset(this.outputStreamError);
        
        tidy.parse(inputStreamData, outputStreamData);
        
        return outputStreamData.toString();
    }
    
    private void reset(OutputStream outputStream) {
        ((ByteArrayOutputStream) outputStream).reset();
    }
    
    /**
     * Pretty-prints a Node.
     */
    
    private void pprintNode(Node document, OutputStream out)
    {
        Out o = new OutImpl();
        PPrint pprint;
        
        o.state = StreamIn.FSM_ASCII;
        o.encoding = tidy.getCharEncoding();
        
        if (out != null)
        {
            pprint = new PPrint(tidy.getConfiguration());
            o.out = out;
            
            if (tidy.getXmlTags())
                pprint.printXMLTree(o, (short)0, 0, null, document);
            else
                pprint.printTree(o, (short)0, 0, null, document);
            
            pprint.flushLine(o, 0);
        }
    }
}
