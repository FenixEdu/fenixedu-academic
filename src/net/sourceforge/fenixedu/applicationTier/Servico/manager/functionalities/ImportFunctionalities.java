package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.exceptions.InvalidStructureException;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class ImportFunctionalities extends Service {

    private static final Logger logger = Logger.getLogger(ImportFunctionalities.class);
    
    /**
     * Reads the given stream and interprets it as a XML document containing the
     * functionality structure to import. All the functionalities read will be
     * added to the given module. If the <tt>principalPreserved</tt> is
     * <code>false</code> then the value of <tt>principal</tt> is set to
     * <code>false</code> on all the functionalities imported.
     * 
     * <p>
     * NOTE: A new functionality will be created for every functionality
     * described in the document. No uuid will be used.
     * 
     * @param module
     *            the parent module for all imported functionalities
     * @param stream
     *            the stream containing the XML document describing a
     *            functionality structure
     * @param principalPreserved
     *            a flag indicating ifthe <tt>principal</tt> property should
     *            be preserved or if it should be set to <code>false</code>
     * 
     * @throws IOException
     *             when it's not possible to read from the stream
     */
    public void run(Module module, InputStream stream, boolean principalPreserved) throws IOException {
        Element root = getRootElement(stream);
        importFunctionalities(module, root.getChildren("functionality"), principalPreserved, false);
    }

    protected Element getRootElement(InputStream stream) throws IOException {
        SAXBuilder build = new SAXBuilder();

        build.setExpandEntities(true);
        build.setValidation(true);
        
        try {
            Document document = build.build(stream);
            return convertVersion(document.getRootElement());
        } catch (JDOMException e) {
            throw new InvalidStructureException("functionalities.import.structure.invalid", e);
        }
    }

    protected Element convertVersion(Element root) {
        // only supports 1.0 for now
        if (! "1.0".equals(root.getAttributeValue("version"))) {
            throw new InvalidStructureException("functionalities.import.version.notSupported");
        }
        
        return root;
    }

    protected void importFunctionalities(Module module, List children, boolean principalPreserved, boolean considerUUID) {
        for (Object element : children) {
            Element functionalityElement = (Element) element;

            UUID uuid = UUID.fromString(functionalityElement.getAttributeValue("uuid"));
            Functionality functionality = Functionality.getFunctionality(uuid);

            if (functionality == null) {
                String path = functionalityElement.getAttributeValue("path");
                String prefix = functionalityElement.getAttributeValue("prefix");
                String parameters = functionalityElement.getAttributeValue("parameters");
                
                MultiLanguageString name = importMultiLanguageString(functionalityElement.getChild("name"));
                MultiLanguageString title = importMultiLanguageString(functionalityElement.getChild("title"));
                MultiLanguageString description = importMultiLanguageString(functionalityElement.getChild("description"));
                
                Boolean relative  = new Boolean(functionalityElement.getAttributeValue("relative"));
                Boolean principal = new Boolean(functionalityElement.getAttributeValue("principal"));
                Boolean visible   = new Boolean(functionalityElement.getAttributeValue("visible"));
                Boolean maximized = new Boolean(functionalityElement.getAttributeValue("maximized"));
    
                String type = functionalityElement.getAttributeValue("type");
                if (type.equals(Module.class.getName())) {
                    functionality = new Module(uuid, name, prefix);
                }
                else {
                    functionality = new ConcreteFunctionality(uuid, name);
                }
    
                String expression = null;
                Element availabilityElement = functionalityElement.getChild("availability");
                if (availabilityElement != null) {
                    expression = availabilityElement.getTextNormalize();
                }
                
                functionality.setPathAndPrincipal(path, principalPreserved ? principal : false);
                functionality.setParameters(parameters);
                functionality.setName(name);
                functionality.setTitle(title);
                functionality.setDescription(description);
                functionality.setRelative(relative);
                functionality.setVisible(visible);
                functionality.setMaximized(maximized);
                
                if (expression != null) {
                    new GroupAvailability(functionality, expression);
                }
                
                functionality.setModule(module);
                
                logNewFunctionality(functionality);
            }
            
            Element childrenElement = functionalityElement.getChild("children");
            if (childrenElement != null && functionality instanceof Module) {
                importFunctionalities((Module) functionality, childrenElement.getChildren("functionality"), principalPreserved, considerUUID);
            }
        }
    }

    private void logNewFunctionality(Functionality functionality) {
        String path = getNamePath(functionality);
        
        logger.info(String.format("%s[%s]", path, functionality.getUuid()));
    }
    
    private String getNamePath(Functionality functionality) {
       if (functionality == null) {
           return "";
       }
       else {
           return getNamePath(functionality.getModule()) + "/" + functionality.getName().getContent();
       }
    }
    
    private MultiLanguageString importMultiLanguageString(Element child) {
        MultiLanguageString mlString = new MultiLanguageString();
        
        List values = child.getChildren("value");
        for (Object element : values) {
            Element valueElement = (Element) element;
            
            String language = valueElement.getAttributeValue("language");
            String text = valueElement.getText();
            
            mlString.setContent(Language.valueOf(language.toLowerCase()), text);
        }
        
        return mlString;
    }
    
}
