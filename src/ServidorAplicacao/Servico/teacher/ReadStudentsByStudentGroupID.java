/*
 * Created on 18/Set/2003, 18:17:51
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
                    Dominio.StudentGroup.class, groupId);
            List studentGroupAttendacies = persistentStudentGroupAttend
                    .readAllByStudentGroup(studentGroup);
            for (Iterator iter = studentGroupAttendacies.iterator(); iter.hasNext();) {
                IStudentGroupAttend studentGroupAttend = (IStudentGroupAttend) iter.next();
                Integer studentID = studentGroupAttend.getAttend().getAluno().getIdInternal();
                IStudent student = (IStudent) persistentStudent.readByOID(Dominio.Student.class,
                        studentID);
                infoStudents.add(Cloner.copyIStudent2InfoStudent(student));
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException();
        }
        return infoStudents;
    }

}