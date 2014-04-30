package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Site;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;

public class DeleteFileContentFilter {

    public static final DeleteFileContentFilter instance = new DeleteFileContentFilter();

    public void execute(FileContent fileContent) throws NotAuthorizedException {
        Site site = fileContent.getSite();

        if (site != null) {
            Group owner = site.getOwner();

            if (owner == null) {
                throw new NotAuthorizedException();
            }

            if (!owner.isMember(Authenticate.getUser())) {
                throw new NotAuthorizedException();
            }
        }

    }

}
