package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class DeleteFileContentFilter extends Filtro {

    @Override
    public void execute(Object[] parameters) throws Exception {
        FileContent fileContent = getFileContent(parameters);
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

    protected FileContent getFileContent(Object[] parameters) {
        return (FileContent) parameters[0];
    }
}
