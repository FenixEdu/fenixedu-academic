package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.exceptions.InvalidStructureException;
import net.sourceforge.fenixedu.domain.functionalities.Module;

import org.jdom.Element;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ImportStartupFunctionalities extends ImportFunctionalities {

    /**
     * Imports functionalities that don't yet exist in the current model from
     * the structure read from the given input stream. The <tt>uuid</tt> present
     * in the document are used to verify if a functionality was already
     * imported. If the functionality is already present in the system then it
     * is not imported but we try to import it's children, if any.
     * 
     * @param stream
     *            the input stream for the XMl document describing the
     *            functionalities to import
     * @throws IOException
     *             when it's not possible to read from the stream
     */
    protected void run(InputStream stream) throws IOException {
        Element root = getRootElement(stream);

        Module parent;

        String uuidString = root.getAttributeValue("parent");
        if (uuidString != null) {
            try {
                UUID uuid = UUID.fromString(root.getAttributeValue("parent"));
                parent = Module.getRootModule().findModule(uuid);

                if (parent == null) {
                    // the specified parent does not exist, create an artificial
                    // one
                    MultiLanguageString name = new MultiLanguageString();
                    name.setContent(String.format("[%1$tF %1$tT] %2$s", new Date(), uuid));

                    parent = new Module(name, "/");
                }
            } catch (IllegalArgumentException e) {
                throw new InvalidStructureException("functionalities.import.structure.invalid.uuid", e);
            }
        } else {
            parent = null;
        }

        importFunctionalities(parent, root.getChildren("functionality"), true, true);
    }

    protected void run(FileInputStream stream) throws IOException {
        run((InputStream) stream);
    }

    protected void run(ByteArrayInputStream stream) throws IOException {
        run((InputStream) stream);
    }

    // Service Invokers migrated from Berserk

    private static final ImportStartupFunctionalities serviceInstance = new ImportStartupFunctionalities();

    @Atomic
    public static void runImportStartupFunctionalities(InputStream stream) throws IOException, NotAuthorizedException {
        ManagerAuthorizationFilter.instance.execute();
        serviceInstance.run(stream);
    }

}