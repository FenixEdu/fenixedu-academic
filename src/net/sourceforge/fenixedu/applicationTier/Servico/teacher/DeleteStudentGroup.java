/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentGroup;

/**
 * @author asnr and scpo
 * 
 */
public class DeleteStudentGroup extends FenixService {

	public Boolean run(Integer executionCourseCode, Integer studentGroupCode) throws FenixServiceException {
		StudentGroup deletedStudentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);

		if (deletedStudentGroup == null) {
			throw new ExistingServiceException();
		}

		deletedStudentGroup.delete();

		return Boolean.TRUE;
	}

}
