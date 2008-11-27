/*
 * LerAula.java
 *
 * Created on December 16th, 2002, 1:58
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

/**
 * Serviço LerAluno.
 * 
 * @author tfc130
 */

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadStudentByNumberAndType extends FenixService {

    // FIXME: We have to read the student by type also !!

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static Object run(Integer number, DegreeType degreeType) {

	InfoStudent infoStudent = null;

	////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////
	// Isto não é para ficar assim. Está assim temporariamente até se
	// saber como é feita de facto a distinção
	// dos aluno, referente ao tipo, a partir da página de login.
	////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////
	Registration registration = Registration.readStudentByNumberAndDegreeType(number, degreeType);

	if (registration != null) {
	    infoStudent = InfoStudent.newInfoFromDomain(registration);
	}

	return infoStudent;
    }

}