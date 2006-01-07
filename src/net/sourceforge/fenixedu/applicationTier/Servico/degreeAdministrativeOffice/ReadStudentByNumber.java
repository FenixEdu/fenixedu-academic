package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentByNumber implements IService {

    public Object run(Integer number, DegreeType degreeType) throws ExcepcaoPersistencia {

        InfoStudent infoStudent = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Student student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                degreeType);

        if (student != null) {
            infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
        }

        return infoStudent;
    }

}