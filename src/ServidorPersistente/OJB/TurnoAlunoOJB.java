/*
 * TurnoAlunoOJB.java
 *
 * Created on 21 de Outubro de 2002, 19:03
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.ShiftStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoAula;

public class TurnoAlunoOJB extends ObjectFenixOJB implements ITurnoAlunoPersistente
{

	public List readByStudentAndExecutionPeriod(IStudent student, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("keyStudent", student.getIdInternal());
		crit.addEqualTo("shift.disciplinaExecucao.keyExecutionPeriod", executionPeriod.getIdInternal());

		return queryList(ShiftStudent.class, crit);
	}

	public List readByStudentAndExecutionCourse(IStudent student, IExecutionCourse executionCourse) throws ExcepcaoPersistencia
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("keyStudent", student.getIdInternal());
		crit.addEqualTo("shift.chaveDisciplinaExecucao", executionCourse.getIdInternal());

		return queryList(ShiftStudent.class, crit);
	}

	public ITurnoAluno readByTurnoAndAluno(ITurno turno, IStudent aluno) throws ExcepcaoPersistencia
	{
	    Criteria criteria = new Criteria();
	    
	    criteria.addEqualTo("student.number", aluno.getNumber());
		criteria.addEqualTo("student.degreeType", aluno.getDegreeType());
		criteria.addEqualTo("turno.nome", turno.getNome());
		criteria.addEqualTo("turno.disciplinaExecucao.sigla", turno.getDisciplinaExecucao().getSigla());
		criteria.addEqualTo("turno.disciplinaExecucao.executionPeriod.name", turno.getDisciplinaExecucao().getExecutionPeriod().getName());
		criteria.addEqualTo("turno.disciplinaExecucao.executionPeriod.executionYear.year", turno.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
	    
	    return (ITurnoAluno) queryObject(ShiftStudent.class, criteria);
	    
//		try
//		{
//			ITurnoAluno turnoAluno = null;
//			String oqlQuery =
//				"select turnoaluno from "
//					+ ShiftStudent.class.getName()
//					+ " where student.number = $1"
//					+ " and student.degreeType = $2"
//					+ " and turno.nome = $3"
//					+ " and turno.disciplinaExecucao.sigla = $4"
//					+ " and turno.disciplinaExecucao.executionPeriod.name = $5"
//					+ " and turno.disciplinaExecucao.executionPeriod.executionYear.year = $6";
//
//			query.create(oqlQuery);
//			query.bind(aluno.getNumber());
//			query.bind(aluno.getDegreeType());
//			query.bind(turno.getNome());
//			query.bind(turno.getDisciplinaExecucao().getSigla());
//			query.bind(turno.getDisciplinaExecucao().getExecutionPeriod().getName());
//			query.bind(turno.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
//
//			List result = (List) query.execute();
//			lockRead(result);
//			if (result.size() != 0)
//				turnoAluno = (ITurnoAluno) result.get(0);
//			return turnoAluno;
//		} catch (QueryException ex)
//		{
//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//		}
	}

	public void lockWrite(ITurnoAluno shiftStudentToWrite) throws ExcepcaoPersistencia, ExistingPersistentException
	{

		ITurnoAluno shiftStudentFromDB = null;

		// If there is nothing to write, simply return.
		if (shiftStudentToWrite == null)
			return;

		// Read shiftStudent from database.
		shiftStudentFromDB = this.readByTurnoAndAluno(shiftStudentToWrite.getShift(), shiftStudentToWrite.getStudent());

		// If shiftStudent is not in database, then write it.
		if (shiftStudentFromDB == null)
			super.lockWrite(shiftStudentToWrite);
		// else If the shiftStudent is mapped to the database, then write any existing changes.
		else if (
			(shiftStudentToWrite instanceof ShiftStudent)
				&& ((ShiftStudent) shiftStudentFromDB).getIdInternal().equals(((ShiftStudent) shiftStudentToWrite).getIdInternal()))
		{
			super.lockWrite(shiftStudentToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	//Not sure if it works...
	public void simpleLockWrite(ITurnoAluno shiftStudentToWrite) throws ExcepcaoPersistencia
	{
		super.lockWrite(shiftStudentToWrite);
	}

	public void delete(ITurnoAluno turnoAluno) throws ExcepcaoPersistencia
	{
		super.delete(turnoAluno);
	}

	public void deleteAll() throws ExcepcaoPersistencia
	{
		String oqlQuery = "select all from " + ShiftStudent.class.getName();
		super.deleteAll(oqlQuery);
	}

	/**
	 * FIXME: wrong link from executionCourse to ExecutionDegree
	 */
	public ITurno readByStudentIdAndShiftType(Integer id, TipoAula shiftType, String nameExecutionCourse) throws ExcepcaoPersistencia
	{
		try
		{
			String oqlQuery = "select shift from " + ShiftStudent.class.getName();
			oqlQuery += " where shift.tipo = $1";
			oqlQuery += " and shift.disciplinaExecucao.licenciaturaExecucao.nome = $2";
			oqlQuery += " and student.number = $3";
			query.create(oqlQuery);
			query.bind(shiftType);
			query.bind(nameExecutionCourse);
			query.bind(id);
			List result = (List) query.execute();
			lockRead(result);
			if (result != null && !(result.isEmpty()))
				return ((ITurnoAluno) result.get(0)).getShift();
		} catch (QueryException ex)
		{
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
		return null;
	}
	/**
	 * @see ServidorPersistente.ITurnoAlunoPersistente#readByTurno(Dominio.ITurno)
	 */

	public List readByShift(ITurno shift) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("shift.nome", shift.getNome());
		criteria.addEqualTo("shift.disciplinaExecucao.sigla", shift.getDisciplinaExecucao().getSigla());
		criteria.addEqualTo("shift.disciplinaExecucao.executionPeriod.name", shift.getDisciplinaExecucao().getExecutionPeriod().getName());
		criteria.addEqualTo(
			"shift.disciplinaExecucao.executionPeriod.executionYear.year",
			shift.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());

		List result = queryList(ShiftStudent.class, criteria);
		
		List studentList = new ArrayList();

		Iterator iterator = result.iterator();
		queryList(ShiftStudent.class, criteria);
		while (iterator.hasNext())
		{
			ITurnoAluno shiftStudent = (ITurnoAluno) iterator.next();
			studentList.add(shiftStudent.getStudent());
		}
		lockRead(studentList);
		return studentList;
//		try
//		{
//			String oqlQuery =
//				"select shift from "
//					+ ShiftStudent.class.getName()
//					+ " where shift.nome = $1"
//					+ " and shift.disciplinaExecucao.sigla = $2"
//					+ " and shift.disciplinaExecucao.executionPeriod.name = $3"
//					+ " and shift.disciplinaExecucao.executionPeriod.executionYear.year = $4";
//			query.create(oqlQuery);
//			query.bind(shift.getNome());
//
//			IDisciplinaExecucao executionCourse = shift.getDisciplinaExecucao();
//			query.bind(executionCourse.getSigla());
//			query.bind(executionCourse.getExecutionPeriod().getName());
//			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
//
//			List result = (List) query.execute();
//
//			lockRead(result);
//			List studentList = new ArrayList();
//
//			Iterator iterator = result.iterator();
//			while (iterator.hasNext())
//			{
//				ITurnoAluno shiftStudent = (ITurnoAluno) iterator.next();
//				studentList.add(shiftStudent.getStudent());
//			}
//			return studentList;
//		} catch (QueryException ex)
//		{
//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//		}
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt, September the 16th, 2003    
	public List readByShiftID(Integer id) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("key_shift", id);
		return queryList(ShiftStudent.class, criteria);
	}

    /* (non-Javadoc)
     * @see ServidorPersistente.ITurnoAlunoPersistente#readByStudent(Dominio.IStudent)
     */
    public List readByStudent(IStudent student) throws ExcepcaoPersistencia
    {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyStudent", student.getIdInternal());
        return queryList(ShiftStudent.class, criteria);
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.ITurnoAlunoPersistente#readByStudentAndExecutionCourseAndLessonTypeAndGroup(Dominio.IStudent, Dominio.IDisciplinaExecucao, Util.TipoAula, Dominio.ITurma)
     */
    public ITurnoAluno readByStudentAndExecutionCourseAndLessonTypeAndGroup(IStudent student, IExecutionCourse executionCourse, TipoAula lessonType, ITurma group) throws ExcepcaoPersistencia
    {
		Criteria criteria = new Criteria();
	    
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		criteria.addEqualTo("shift.tipo", lessonType);
		criteria.addEqualTo("shift.chaveDisciplinaExecucao", executionCourse.getIdInternal());
		criteria.addEqualTo("shift.associatedClasses.idInternal", group.getIdInternal());
		return (ITurnoAluno) queryObject(ShiftStudent.class, criteria);
    }
}
