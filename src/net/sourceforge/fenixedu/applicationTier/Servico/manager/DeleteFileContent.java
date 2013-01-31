package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.file.FileManagerException;

/**
 * @author naat
 */
public class DeleteFileContent extends FenixService {

	public void run(FileContent fileContent) throws FenixServiceException, DomainException, FileManagerException {
		fileContent.delete();
	}
}
