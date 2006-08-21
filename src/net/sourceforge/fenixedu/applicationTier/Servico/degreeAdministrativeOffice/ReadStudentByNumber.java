package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentByNumber extends Service {

    public Object run(Integer number, DegreeType degreeType) throws ExcepcaoPersistencia {

        InfoStudent infoStudent = null;
        Registration registration = Registration.readStudentByNumberAndDegreeType(number, degreeType);

        if (registration != null) {
            infoStudent = InfoStudent.newInfoFromDomain(registration);
        }

        return infoStudent;
    }

}