package net.sourceforge.fenixedu.applicationTier.Servico.student;


import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentByNumberAndDegreeType {

    @Service
    public static Object run(Integer number, DegreeType degreeType) {

        final Registration registration = Registration.readStudentByNumberAndDegreeType(number, degreeType);

        if (registration != null) {
            return InfoStudent.newInfoFromDomain(registration);
        }

        return null;
    }

}