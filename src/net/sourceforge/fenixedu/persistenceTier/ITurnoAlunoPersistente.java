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

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITurnoAluno;
import net.sourceforge.fenixedu.util.TipoAula;

public interface ITurnoAlunoPersistente extends IPersistentObject {

    public List readByShiftID(Integer id) throws ExcepcaoPersistencia;

    public List readByStudentAndExecutionPeriod(IStudent student, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public ITurnoAluno readByTurnoAndAluno(IShift turno, IStudent aluno) throws ExcepcaoPersistencia;

    public void delete(ITurnoAluno turnoAluno) throws ExcepcaoPersistencia;

    public List readByShift(IShift shift) throws ExcepcaoPersistencia;

    public List readByStudentAndExecutionCourse(IStudent student, IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia;

    // FIXME : Method is all messed up !! Query, name and parameters
    public IShift readByStudentIdAndShiftType(Integer id, TipoAula shiftType, String nameExecutionCourse)
            throws ExcepcaoPersistencia;

    public List readByStudent(IStudent student) throws ExcepcaoPersistencia;

    public ITurnoAluno readByStudentAndExecutionCourseAndLessonTypeAndGroup(IStudent student,
            IExecutionCourse executionCourse, TipoAula lessonType, ISchoolClass group)
            throws ExcepcaoPersistencia;

    /**
     * @param shift
     * @return
     */
    public int readNumberOfStudentsByShift(IShift shift);

    /**
     * 
     * @param student
     * @param executionCourse
     * @param lessonType
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public ITurnoAluno readByStudentAndExecutionCourseAndLessonType(IStudent student,
            IExecutionCourse executionCourse, TipoAula lessonType) throws ExcepcaoPersistencia;

    /**
     * @param oldShift
     * @return
     */
    public List readStudentShiftByShift(IShift oldShift) throws ExcepcaoPersistencia;
}