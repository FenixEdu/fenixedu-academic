package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author David Santos in Mar 5, 2004
 */

public class ReadStudentsByPerson implements IService {
    public ReadStudentsByPerson() {
    }

    public List run(InfoPerson infoPerson) throws FenixServiceException {
        List infoStudents = new ArrayList();
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPerson person = Cloner.copyInfoPerson2IPerson(infoPerson);
            List students = persistentSuport.getIPersistentStudent().readbyPerson(person);
            Iterator iterator = students.iterator();
            while (iterator.hasNext()) {
                IStudent student = (IStudent) iterator.next();
                InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(student);
                infoStudents.add(infoStudent);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return infoStudents;
    }
}