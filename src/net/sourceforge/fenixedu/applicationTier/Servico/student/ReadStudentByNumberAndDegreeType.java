package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentByNumberAndDegreeType extends Service {

    public Object run(Integer number, DegreeType degreeType) throws ExcepcaoPersistencia {
        
        final Student student = Student.readStudentByNumberAndDegreeType(number, degreeType);
        
        if (student != null) {
            return InfoStudentWithInfoPerson.newInfoFromDomain(student);
        }

        return null;
    }

}