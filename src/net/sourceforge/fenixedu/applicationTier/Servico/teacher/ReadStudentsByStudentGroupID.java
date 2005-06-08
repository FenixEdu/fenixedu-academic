/*
 * Created on 18/Set/2003, 18:17:51
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 18/Set/2003, 18:17:51
 * 
 */
public class ReadStudentsByStudentGroupID implements IService {

    public List run(Integer executionCourseId, Integer groupId) throws FenixServiceException,
            ExcepcaoPersistencia {

        List infoStudents = new LinkedList();
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();

        IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
                StudentGroup.class, groupId);
        List studentGroupAttendacies = studentGroup.getStudentGroupAttends();

        for (Iterator iter = studentGroupAttendacies.iterator(); iter.hasNext();) {
            IStudentGroupAttend studentGroupAttend = (IStudentGroupAttend) iter.next();
            Integer studentID = studentGroupAttend.getAttend().getAluno().getIdInternal();
            IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentID);
            infoStudents.add(Cloner.copyIStudent2InfoStudent(student));
        }
        return infoStudents;
    }
}