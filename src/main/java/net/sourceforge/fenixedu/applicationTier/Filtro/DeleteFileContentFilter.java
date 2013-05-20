package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class DeleteFileContentFilter {

    public static final DeleteFileContentFilter instance = new DeleteFileContentFilter();

    public void execute(FileContent fileContent) throws Exception {
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

}
