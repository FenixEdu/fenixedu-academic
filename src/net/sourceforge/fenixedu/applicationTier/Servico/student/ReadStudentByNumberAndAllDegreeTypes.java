package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * 
 * @author Fernanda Quitério 1/Mar/2004
 * 
 */
public class ReadStudentByNumberAndAllDegreeTypes extends Service {

    public InfoStudent run (Integer number) {
        return InfoStudent.newInfoFromDomain(Registration.readByNumber(number));
    }

}
