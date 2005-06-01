/*
 * ITurnoPersistente.java Created on 17 de Outubro de 2002, 19:32
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.IShift;

public interface ITurnoPersistente extends IPersistentObject {

    public IShift readByNameAndExecutionCourse(String nome, Integer IDE) throws ExcepcaoPersistencia;

    public List readByExecutionCourseAndType(Integer executionCourseOID, Integer type)
            throws ExcepcaoPersistencia;

    public List readByExecutionCourse(Integer executionCourseOID) throws ExcepcaoPersistencia;

    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYear(Integer executionPeriodOID,
            Integer executionDegreeOID, Integer curricularYearOID) throws ExcepcaoPersistencia;

    public List readAvailableShiftsForClass(Integer schoolClassOID) throws ExcepcaoPersistencia;

    public IShift readByLesson(Integer lessonOID) throws ExcepcaoPersistencia;

    public List readShiftsThatContainsStudentAttendsOnExecutionPeriod(Integer studentOID,
            Integer executionPeriodOID) throws ExcepcaoPersistencia;

}