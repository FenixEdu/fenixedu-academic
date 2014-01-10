package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.fenixedu.bennu.core.security.Authenticate;

public class DeleteFileContentFilter {

    public static final DeleteFileContentFilter instance = new DeleteFileContentFilter();

    public void execute(FileContent fileContent) throws NotAuthorizedException {
        Site site = fileContent.getSite();

        if (site != null) {
            IGroup owner = site.getOwner();

            if (owner == null) {
                throw new NotAuthorizedException();
            }

            if (!owner.allows(Authenticate.getUser())) {
                throw new NotAuthorizedException();
            }
        }

    }

}
