package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.file.FileManagerException;

/**
 * Changes the group of people that is allowed to access the file.
 * 
 * @author naat
 */
public class EditFilePermissions extends FileContentService {

	public void run(Site site, FileContent fileContent, Group group) throws FenixServiceException, DomainException,
			FileManagerException {
		fileContent.setPermittedGroup(group);
	}

}