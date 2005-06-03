/*
 * ITurnoAlunoPersistente.java
 *
 * Created on 21 de Outubro de 2002, 19:01
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.ShiftType;

public interface ITurnoAlunoPersistente extends IPersistentObject {

    public List readByShiftID(Integer id) throws ExcepcaoPersistencia;

    public List readByStudentAndExecutionPeriod(Integer studentOID, Integer executionPeriodOID)
            throws ExcepcaoPersistencia;

    public IShiftStudent readByTurnoAndAluno(Integer turnoOID, Integer alunoOID) throws ExcepcaoPersistencia;

    public List readByShift(Integer shiftOID) throws ExcepcaoPersistencia;

    public List readByStudentAndExecutionCourse(Integer studentOID, Integer executionCourseOID)
            throws ExcepcaoPersistencia;

    public List readByStudent(Integer studentOID) throws ExcepcaoPersistencia;

    public int readNumberOfStudentsByShift(Integer shiftOID) throws ExcepcaoPersistencia;

    public IShiftStudent readByStudentAndExecutionCourseAndLessonType(Integer studentOID,
            Integer executionCourseOID, ShiftType lessonType) throws ExcepcaoPersistencia;

    public List readStudentShiftByShift(Integer oldShiftOID) throws ExcepcaoPersistencia;
}