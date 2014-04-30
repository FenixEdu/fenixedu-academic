package net.sourceforge.fenixedu.domain.cms;

import org.fenixedu.bennu.portal.servlet.PortalBackend;
import org.fenixedu.bennu.portal.servlet.SemanticURLHandler;

public class OldCmsPortalBackend implements PortalBackend {

    public static final String BACKEND_KEY = "old-cms";
    private final SemanticURLHandler handler = new OldCmsSemanticURLHandler();

    @Override
    public SemanticURLHandler getSemanticURLHandler() {
        return handler;
    }

    @Override
    public boolean requiresServerSideLayout() {
        return false;
    }

    @Override
    public String getBackendKey() {
        return BACKEND_KEY;
    }

}
