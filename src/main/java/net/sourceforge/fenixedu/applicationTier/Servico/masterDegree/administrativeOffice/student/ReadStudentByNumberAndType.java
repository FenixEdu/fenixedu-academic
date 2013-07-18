/*
 * LerAula.java
 *
 * Created on December 16th, 2002, 1:58
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student;


import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentByNumberAndType {

    // FIXME: We have to read the student by type also !!

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static Object run(Integer number, DegreeType degreeType) {

        InfoStudent infoStudent = null;

        // //////////////////////////////////////////////////////////////////////
        // //////////////////////////////////
        // Isto não é para ficar assim. Está assim temporariamente até se
        // saber como é feita de facto a distinção
        // dos aluno, referente ao tipo, a partir da página de login.
        // //////////////////////////////////////////////////////////////////////
        // //////////////////////////////////
        Registration registration = Registration.readStudentByNumberAndDegreeType(number, degreeType);

        if (registration != null) {
            infoStudent = InfoStudent.newInfoFromDomain(registration);
        }

        return infoStudent;
    }

}