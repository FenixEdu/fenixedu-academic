/*
 * IAulaPersistente.java
 *
 * Created on 17 de Outubro de 2002, 20:55
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import java.util.Calendar;
import java.util.List;

import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.ISala;
import Util.DiaSemana;
import Util.TipoAula;

public interface IAulaPersistente extends IPersistentObject {
    public IAula readByDiaSemanaAndInicioAndFimAndSala(DiaSemana diaSemana,
                    Calendar inicio, Calendar fim, ISala sala)
               throws ExcepcaoPersistencia;
    public void lockWrite(IAula aula) throws ExcepcaoPersistencia;
    public void delete(IAula aula) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
    public List readByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
    public List readByDisciplinaExecucaoETipo(String sigla, TipoAula tipoAula) throws ExcepcaoPersistencia;
    public List readBySalaEmSemestre(String nomeSala, Integer semestre) throws ExcepcaoPersistencia;
	/**
	 * Method readByDisciplinaExecucaoETipo.
	 * @param executionCourse
	 * @param tipoAula
	 * @return List
	 */
	List readByExecutionCourseAndLessonType(
		IDisciplinaExecucao executionCourse,
		TipoAula lessonType)throws ExcepcaoPersistencia;
		
	/**
	 * 
	 * @param lesson
	 * @return List
	 * @throws ExcepcaoPersistencia when query fails.
	 */
	public List readLessonsInPeriod(IAula lesson) throws ExcepcaoPersistencia;		
}
