/*
 * ITurnoAlunoPersistente.java
 *
 * Created on 21 de Outubro de 2002, 19:01
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import java.util.List;

import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Util.TipoAula;


public interface ITurnoAlunoPersistente extends IPersistentObject {
	
    public List readByShiftID(Integer id) throws ExcepcaoPersistencia;    
	public List readByStudentAndExecutionPeriod(IStudent student,IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
    public ITurnoAluno readByTurnoAndAluno(ITurno turno, IStudent aluno)
               throws ExcepcaoPersistencia;
   
    public void delete(ITurnoAluno turnoAluno) throws ExcepcaoPersistencia;
   
	public List readByShift(ITurno shift) throws ExcepcaoPersistencia;   
	public List readByStudentAndExecutionCourse(IStudent student,IExecutionCourse executionCourse) throws ExcepcaoPersistencia;
	
	// FIXME : Method is all messed up !! Query, name and parameters 
	public ITurno readByStudentIdAndShiftType(Integer id, TipoAula shiftType, String nameExecutionCourse)
				throws ExcepcaoPersistencia;

	public List readByStudent(IStudent student) throws ExcepcaoPersistencia;
    public ITurnoAluno readByStudentAndExecutionCourseAndLessonTypeAndGroup(IStudent student, IExecutionCourse executionCourse, TipoAula lessonType, ITurma group) throws ExcepcaoPersistencia;
    /**
     * @param shift
     * @return
     */
    public int readNumberOfStudentsByShift(ITurno shift);
    /**
     * 
     * @param student
     * @param executionCourse
     * @param lessonType
     * @return
     * @throws ExcepcaoPersistencia
     */
    public ITurnoAluno readByStudentAndExecutionCourseAndLessonType(
            IStudent student, IExecutionCourse executionCourse,
            TipoAula lessonType) throws ExcepcaoPersistencia;
}
