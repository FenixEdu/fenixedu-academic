/*
 * Created on 17/Set/2003, 12:47:09
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IStudent;
import Dominio.ShiftStudent;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 17/Set/2003, 12:47:09
 *  
 */
public class ReadStudentsByShiftID implements IServico {
    private static ReadStudentsByShiftID _servico = new ReadStudentsByShiftID();

    /**
     * The singleton access method of this class.
     */
    public static ReadStudentsByShiftID getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadStudentsByShiftID() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "teacher.ReadStudentsByShiftID";
    }

    public List run(Integer executionCourseID, Integer shiftID) {

        List shiftStudentAssociations = null;
        List infoStudents = new LinkedList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            shiftStudentAssociations = sp.getITurnoAlunoPersistente().readByShiftID(shiftID);
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            for (Iterator associationsIterator = shiftStudentAssociations.iterator(); associationsIterator
                    .hasNext();) {
                ShiftStudent element = (ShiftStudent) associationsIterator.next();
                IStudent student = (IStudent) persistentStudent.readByOID(Dominio.Student.class, element
                        .getKeyStudent());
                infoStudents.add(Cloner.copyIStudent2InfoStudent(student));
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoStudents;
    }

}