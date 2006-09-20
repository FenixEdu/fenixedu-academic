package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.exceptions.InvalidStructureException;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.jdom.Element;

public class ImportStartupFunctionalities extends ImportFunctionalities {
    
    /**
     * Imports functionalities that don't yet exist in the current model from
     * the structure read from the given input stream. The <tt>uuid</tt>
     * present in the document are used to verify if a functionality was already
     * imported. If the functionality is already present in the system then it
     * is not imported but we try to import it's children, if any.
     * 
     * @param stream
     *            the input stream for the XMl document describing the
     *            functionalities to import
     * @throws IOException
     *             when it's not possible to read from the stream
     */
    public void run(InputStream stream) throws IOException {
        Element root = getRootElement(stream);
        
        Module parent;
        
        String uuidString = root.getAttributeValue("parent");
        if (uuidString != null) {
            try {
                UUID uuid = UUID.fromString(root.getAttributeValue("parent"));
                parent = (Module) Functionality.getFunctionality(uuid);

                if (parent == null) {
                    // the specified parent does not exist, create an artificial one
                    MultiLanguageString name = new MultiLanguageString();
                    name.setContent(String.format("[%1$tF %1$tT] %2$s", new Date(), uuid));

                    parent = new Module(name, "/");
                    parent.setEnabled(false);
                }
            } catch (IllegalArgumentException e) {
                throw new InvalidStructureException("functionalities.import.structure.invalid.uuid", e);
            }
        } else {
            parent = null;
        }
    
        importFunctionalities(parent, root.getChildren("functionality"), true, true);
    }
    
}
