package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Fernanda Quitério 1/Mar/2004
 * 
 */
public class ReadStudentByNumberAndAllDegreeTypes extends Service {

    public Object run(Integer number) throws ExcepcaoPersistencia {
        InfoStudent infoStudent = null;

        Registration student = Registration.readStudentByNumberAndDegreeType(number, DegreeType.DEGREE);

        if (student == null) {
            student = Registration.readStudentByNumberAndDegreeType(number, DegreeType.MASTER_DEGREE);
        }
        if (student != null) {
            infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
        }

        return infoStudent;
    }

}