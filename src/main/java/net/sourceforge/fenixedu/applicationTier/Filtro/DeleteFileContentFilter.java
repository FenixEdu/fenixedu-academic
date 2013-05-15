package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.berserk.ServiceRequest;

public class DeleteFileContentFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request) throws Exception {
        FileContent fileContent = getFileContent(request);
        Site site = fileContent.getSite();

        if (site != null) {
            IGroup owner = site.getOwner();

            if (owner == null) {
                throw new NotAuthorizedFilterException();
            }

            if (!owner.allows(AccessControl.getUserView())) {
                throw new NotAuthorizedFilterException();
            }
        }

    }

    protected FileContent getFileContent(ServiceRequest request) {
        return (FileContent) request.getServiceParameters().getParameter(0);
    }
}
