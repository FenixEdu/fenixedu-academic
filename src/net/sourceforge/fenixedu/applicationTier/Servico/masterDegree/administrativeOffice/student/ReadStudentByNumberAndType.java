/*
 * LerAula.java
 *
 * Created on December 16th, 2002, 1:58
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student;

/**
 * Serviço LerAluno.
 * 
 * @author tfc130
 */

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentByNumberAndType extends Service {

    // FIXME: We have to read the student by type also !!

    public Object run(Integer number, DegreeType degreeType) throws ExcepcaoPersistencia {

        InfoStudent infoStudent = null;

        // ////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Isto não é para ficar assim. Está assim temporariamente até se
        // saber como é feita de facto a distinção
        // dos aluno, referente ao tipo, a partir da página de login.
        // ////////////////////////////////////////////////////////////////////////////////////////////////////////
        Registration registration = Registration.readStudentByNumberAndDegreeType(number, degreeType);

        if (registration != null) {
            infoStudent = InfoStudent.newInfoFromDomain(registration);
        }

        return infoStudent;
    }

}