/*
 * Created on 18/Set/2003, 18:17:51
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 18/Set/2003, 18:17:51
 *  
 */
public class ReadStudentsByStudentGroupID implements IServico {
    private static ReadStudentsByStudentGroupID _servico = new ReadStudentsByStudentGroupID();

    /**
     * The singleton access method of this class.
     */
    public static ReadStudentsByStudentGroupID getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadStudentsByStudentGroupID() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "teacher.ReadStudentsByStudentGroupID";
    }

    public List run(Integer executionCourseId, Integer groupId) throws FenixServiceException {

        List infoStudents = new LinkedList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
            IPersistentStudentGroupAttend persistentStudentGroupAttend = sp
                    .getIPersistentStudentGroupAttend();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();

            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
                    StudentGroup.class, groupId);
            List studentGroupAttendacies = persistentStudentGroupAttend
                    .readAllByStudentGroup(studentGroup);
            for (Iterator iter = studentGroupAttendacies.iterator(); iter.hasNext();) {
                IStudentGroupAttend studentGroupAttend = (IStudentGroupAttend) iter.next();
                Integer studentID = studentGroupAttend.getAttend().getAluno().getIdInternal();
                IStudent student = (IStudent) persistentStudent.readByOID(Student.class,
                        studentID);
                infoStudents.add(Cloner.copyIStudent2InfoStudent(student));
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException();
        }
        return infoStudents;
    }

}