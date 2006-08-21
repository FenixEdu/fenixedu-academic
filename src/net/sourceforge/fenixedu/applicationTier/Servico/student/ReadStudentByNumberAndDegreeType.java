package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentByNumberAndDegreeType extends Service {

    public Object run(Integer number, DegreeType degreeType) throws ExcepcaoPersistencia {
        
        final Registration registration = Registration.readStudentByNumberAndDegreeType(number, degreeType);
        
        if (registration != null) {
            return InfoStudent.newInfoFromDomain(registration);
        }

        return null;
    }

}