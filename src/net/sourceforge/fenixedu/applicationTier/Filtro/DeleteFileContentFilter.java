package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class DeleteFileContentFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	FileContent fileContent = getFileContent(request, response);
	Site site = fileContent.getSite();

	if (site != null) {
	    IGroup owner = site.getOwner();

	    if (owner == null) {
		throw new NotAuthorizedFilterException();
	    }

	    if (!owner.allows(getRemoteUser(request))) {
		throw new NotAuthorizedFilterException();
	    }
	}

    }

    protected FileContent getFileContent(ServiceRequest request, ServiceResponse response) {
	return (FileContent) request.getServiceParameters().getParameter(0);
    }
}
