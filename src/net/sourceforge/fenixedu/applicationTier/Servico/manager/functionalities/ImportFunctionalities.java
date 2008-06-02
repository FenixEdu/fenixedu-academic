package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.exceptions.InvalidStructureException;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
import net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class ImportFunctionalities extends Service {

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
     * @param uuidUsed
     *            indicates that new functionalities should be assigned the uuid
     *            defined in the document, that is, no new uuid will be
     *            generated
     * 
     * @throws IOException
     *             when it's not possible to read from the stream
     */
    public void run(Module module, InputStream stream, boolean principalPreserved, boolean uuidUsed) throws IOException {
        Element root = getRootElement(stream);
        importFunctionalities(module, root.getChildren("functionality"), principalPreserved, uuidUsed);
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

            Content content = null;
            String uuid = null;
            
            if (considerUUID) {
                uuid = functionalityElement.getAttributeValue("uuid");
                content  = Functionality.getFunctionality(uuid);
            }

            if (content == null) {
                String path = functionalityElement.getAttributeValue("path");
                String prefix = functionalityElement.getAttributeValue("prefix");
                String parameters = functionalityElement.getAttributeValue("parameters");
                
                MultiLanguageString name = importMultiLanguageString(functionalityElement.getChild("name"));
                MultiLanguageString title = importMultiLanguageString(functionalityElement.getChild("title"));
                MultiLanguageString description = importMultiLanguageString(functionalityElement.getChild("description"));
                
                Boolean relative  = new Boolean(functionalityElement.getAttributeValue("relative"));
                Boolean principal = new Boolean(functionalityElement.getAttributeValue("principal"));
                Boolean visible   = new Boolean(functionalityElement.getAttributeValue("visible"));
    
                Integer order = null;
                String orderValue = functionalityElement.getAttributeValue("order");
                if (orderValue != null) {
                    order = new Integer(orderValue);
                }
                
                String type = functionalityElement.getAttributeValue("type");
                if (type.equals(Module.class.getName())) {
                    content = new Module(name, prefix);
                }
                else {
                    content = considerUUID ? new Functionality(uuid, name) : new Functionality(name);
                }
    
                String expression = null;
                Element availabilityElement = functionalityElement.getChild("availability");
                if (availabilityElement != null) {
                    expression = availabilityElement.getTextNormalize();
                }
                
                content.setName(name);
                content.setTitle(title);
                content.setDescription(description);
                
                if (content instanceof Functionality) {
                    Functionality f = (Functionality) content;
                    f.setExecutionPath(path);
                    f.setParameters(parameters);
                    f.setModule(module);
                }
                else if (content instanceof Module) {
                    Module m = (Module) content;
                    m.setModule(module);
                }
                
                if (expression != null) {
                    new ExpressionGroupAvailability(content, expression);
                }
                
                ExplicitOrderNode node = (ExplicitOrderNode) content.getParentNode(module);
                if (order != null) {
                    node.setNodeOrder(order);
                }

                if (visible != null) {
                    node.setVisible(visible);
                }
            }
            
            Element childrenElement = functionalityElement.getChild("children");
            if (childrenElement != null && content instanceof Module) {
                importFunctionalities((Module) content, childrenElement.getChildren("functionality"), principalPreserved, considerUUID);
            }
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
