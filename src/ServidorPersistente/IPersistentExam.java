/*
 * IPersistentExam.java Created on 2003/03/19
 */

package ServidorPersistente;

import java.util.Calendar;
import java.util.List;

import Dominio.IExam;
import Dominio.IExecutionPeriod;
import Dominio.IRoom;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public interface IPersistentExam extends IPersistentObject {

    public List readBy(Calendar day, Calendar beginning) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public void delete(IExam exam) throws ExcepcaoPersistencia;

    public List readBy(IRoom room, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public List readByRoomAndExecutionPeriod(IRoom room, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public List readBy(Calendar day, Calendar beginning, Calendar end) throws ExcepcaoPersistencia;

    public List readByRoomAndWeek(IRoom room, Calendar day) throws ExcepcaoPersistencia;

    public boolean isExamOfExecutionCourseTheStudentAttends(Integer examOID, String studentsUsername)
            throws ExcepcaoPersistencia;

}