/*
 * LerAula.java
 *
 * Created on December 16th, 2002, 1:58
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadStudent extends FenixService {

    // FIXME: We have to read the student by type also !!

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static Object run(Integer number) {

	InfoStudent infoStudent = null;

	// //////////////////////////////////////////////////////////////////////
	// //////////////////////////////////
	// Isto não é para ficar assim. Está assim temporariamente até se
	// saber como é feita de facto a distinção
	// dos aluno, referente ao tipo, a partir da página de login.
	// //////////////////////////////////////////////////////////////////////
	// //////////////////////////////////
	Registration registration = Registration.readStudentByNumberAndDegreeType(number, DegreeType.DEGREE);

	if (registration != null) {
	    infoStudent = new InfoStudent(registration);
	    infoStudent.setIdInternal(registration.getIdInternal());
	}

	return infoStudent;
    }

}