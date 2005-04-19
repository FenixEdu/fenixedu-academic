package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author David Santos in Mar 5, 2004
 */

public class ReadStudentsByPerson implements IService {

    public List run(InfoPerson infoPerson) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPerson person = new Person();
        person.setIdInternal(infoPerson.getIdInternal());
        final List students = persistentSuport.getIPersistentStudent().readbyPerson(person);
        final List infoStudents = new ArrayList(students.size());
        for (final Iterator iterator = students.iterator(); iterator.hasNext(); ) {
            final IStudent student = (IStudent) iterator.next();
            final InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(student);
            infoStudents.add(infoStudent);
        }
        return infoStudents;
    }
}