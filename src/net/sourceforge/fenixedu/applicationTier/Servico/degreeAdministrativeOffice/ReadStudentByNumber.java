package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentByNumber extends Service {

    public Object run(Integer number, DegreeType degreeType) throws ExcepcaoPersistencia {

        InfoStudent infoStudent = null;
        Registration student = Registration.readStudentByNumberAndDegreeType(number, degreeType);

        if (student != null) {
            infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
        }

        return infoStudent;
    }

}