
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import pt.utl.ist.berserk.logic.serviceManager.IService;


public class ReadPublicationScopes implements IService {

    public List<String> run(int publicationTypeId) {
        List<String> scopeList = new ArrayList<String>();

        scopeList.add(PublicationConstants.SCOPE_LOCAL);
        scopeList.add(PublicationConstants.SCOPE_NACIONAL);
        scopeList.add(PublicationConstants.SCOPE_INTERNACIONAL);

        return scopeList;
    }
}