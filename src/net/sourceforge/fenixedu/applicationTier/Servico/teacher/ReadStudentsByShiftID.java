/*
 * Created on 17/Set/2003, 12:47:09
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 17/Set/2003, 12:47:09
 * 
 */
public class ReadStudentsByShiftID implements IService {

    public List run(Integer executionCourseID, Integer shiftID) throws ExcepcaoPersistencia {

        List shiftStudentAssociations = null;
        List infoStudents = new LinkedList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        shiftStudentAssociations = sp.getITurnoAlunoPersistente().readByShiftID(shiftID);
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        for (Iterator associationsIterator = shiftStudentAssociations.iterator(); associationsIterator
                .hasNext();) {
            IShiftStudent element = (IShiftStudent) associationsIterator.next();
            IStudent student = (IStudent) persistentStudent.readByOID(Student.class, element
                    .getKeyStudent());
            infoStudents.add(Cloner.copyIStudent2InfoStudent(student));
        }

        return infoStudents;
    }

}
