/*
 * IPersistentExam.java Created on 2003/03/19
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.Calendar;
import java.util.List;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public interface IPersistentExam extends IPersistentObject {

    public List readBy(Calendar day, Calendar beginning) throws ExcepcaoPersistencia;

    public List readByRoomAndExecutionPeriod(String roomName, String executionPeriodName, String year)
            throws ExcepcaoPersistencia;

    public List readBy(Calendar day, Calendar beginning, Calendar end) throws ExcepcaoPersistencia;
    
    public List readByRoomAndWeek(String roomName, Calendar day) throws ExcepcaoPersistencia;

    public boolean isExamOfExecutionCourseTheStudentAttends(Integer examOID, String studentsUsername)
            throws ExcepcaoPersistencia;

}