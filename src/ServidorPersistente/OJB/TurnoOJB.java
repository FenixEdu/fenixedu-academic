/*
 * ITurnoOJB.java
 * 
 * Created on 17 de Outubro de 2002, 19:35
 */

package ServidorPersistente.OJB;

/**
 * @author tfc130
 */
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IAula;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.TurmaTurno;
import Dominio.Turno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurnoPersistente;
import Util.TipoAula;

public class TurnoOJB extends ObjectFenixOJB implements ITurnoPersistente
{

    public ITurno readByNameAndExecutionCourse(String shiftName, IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        crit.addEqualTo("nome", shiftName);
        return (ITurno) queryObject(Turno.class, crit);
    }

    
    public void delete(ITurno turno) throws ExcepcaoPersistencia
    {

//        Criteria crit = new Criteria();
//        crit.addEqualTo("chaveTurno", turno.getIdInternal());
//        ITurnoAula turnoAula = null;
//        TurnoAulaOJB turnoAulaOJB = new TurnoAulaOJB();
//
//        List result = queryList(TurnoAula.class, crit);
//        lockRead(result);
//        Iterator iterador = result.iterator();
//        while (iterador.hasNext())
//        {
//            turnoAula = (ITurnoAula) iterador.next();
//            turnoAulaOJB.delete(turnoAula);
//        }

//        Criteria crit = new Criteria();
//        crit.addEqualTo("chaveTurno", turno.getIdInternal());
//        ITurmaTurno turmaTurno = null;
//        TurmaTurnoOJB turmaTurnoOJB = new TurmaTurnoOJB();
//        List result1 = queryList(TurmaTurno.class, crit);
//        Iterator iterador1 = result1.iterator();
//        while (iterador1.hasNext())
//        {
//            turmaTurno = (ITurmaTurno) iterador1.next();
//            turmaTurnoOJB.delete(turmaTurno);
//        }
//        ITurnoAluno turnoAluno = null;
//        TurnoAlunoOJB turnoAlunoOJB = new TurnoAlunoOJB();
//
//        Criteria criteria = new Criteria();
//        criteria.addEqualTo("shift.nome", turno.getNome());
//        criteria.addEqualTo("shift.disciplinaExecucao.sigla", turno.getDisciplinaExecucao().getSigla());
//        criteria.addEqualTo(
//            "shift.disciplinaExecucao.executionPeriod.name",
//            turno.getDisciplinaExecucao().getExecutionPeriod().getName());
//        criteria.addEqualTo(
//            "shift.disciplinaExecucao.executionPeriod.executionYear.year",
//            turno.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
//        List result2 = queryList(ShiftStudent.class, criteria);
//
//        Iterator iterador2 = result2.iterator();
//        while (iterador2.hasNext())
//        {
//            turnoAluno = (ITurnoAluno) iterador2.next();
//            turnoAlunoOJB.delete(turnoAluno);
//        }

        super.delete(turno);

    }

    public Integer countAllShiftsOfAllClassesAssociatedWithShift(ITurno shift)
        throws ExcepcaoPersistencia
    {
			Criteria criteria = new Criteria();
			criteria.addEqualTo("turno.nome", shift.getNome());
			criteria.addEqualTo("turno.disciplinaExecucao.sigla", shift.getDisciplinaExecucao().getSigla());
			criteria.addEqualTo("turno.disciplinaExecucao.executionPeriod.name", shift.getDisciplinaExecucao().getExecutionPeriod().getName());
			criteria.addEqualTo("turno.disciplinaExecucao.executionPeriod.executionYear.year", shift.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
			criteria.addEqualTo("turno.tipo", shift.getTipo().getTipo());

            List result = queryList(TurmaTurno.class, criteria);

            //return new Integer(result.size());
            List result2 = null;
            for (int i = 0; i != result.size(); i++)
            {
                	criteria = new Criteria();
                	criteria.addEqualTo("turno.tipo", new Integer(TipoAula.PRATICA));
					criteria.addEqualTo("turma.nome", ((TurmaTurno) (result.get(i))).getTurma().getNome());
                    if (i == 0)
                    {
                        result2 = queryList(TurmaTurno.class, criteria);
                    }
                    else
                    {
                        List result_tmp = queryList(TurmaTurno.class, criteria);
                        for (int j = 0; j != result_tmp.size(); j++)
                            if (!result2.contains(result_tmp.get(j)))
                                result2.add(result_tmp.get(j));
                    }
            }
            return new Integer(result2.size());
    }

    public List readByDisciplinaExecucao(String sigla, String anoLectivo, String siglaLicenciatura)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.sigla", sigla);
        crit.addEqualTo("disciplinaExecucao.executionPeriod.executionYear.year", anoLectivo);
        crit.addEqualTo(
            "disciplinaExecucao.associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
            siglaLicenciatura);
        return queryList(Turno.class, crit);

    }
    public List readByExecutionCourseAndType(IExecutionCourse executionCourse, Integer type)
        throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        crit.addEqualTo("tipo", type);
        return queryList(Turno.class, crit);

    }
    /**
	 * @see ServidorPersistente.ITurnoPersistente#readByExecutionCourse(Dominio.IDisciplinaExecucao)
	 */
    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        return queryList(Turno.class, crit);

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ITurnoPersistente#readByExecutionDegreeAndCurricularYear(Dominio.ICursoExecucao,
	 *      Dominio.ICurricularYear)
	 */
    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYear(
        IExecutionPeriod executionPeriod,
        ICursoExecucao executionDegree,
        ICurricularYear curricularYear)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo(
            "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
            curricularYear.getIdInternal());
        criteria.addEqualTo(
            "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.semester",
            executionPeriod.getSemester());
        criteria.addEqualTo(
            "disciplinaExecucao.associatedCurricularCourses.degreeCurricularPlan.idInternal",
            executionDegree.getCurricularPlan().getIdInternal());
        criteria.addEqualTo(
            "disciplinaExecucao.executionPeriod.idInternal",
            executionPeriod.getIdInternal());

        List shifts = queryList(Turno.class, criteria, true);
        return shifts;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ITurnoPersistente#readAvailableShiftsForClass(Dominio.ITurma)
	 */
    public List readAvailableShiftsForClass(ITurma schoolClass) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();

        criteria.addEqualTo(
            "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
            schoolClass.getAnoCurricular());
        criteria.addEqualTo(
            "disciplinaExecucao.associatedCurricularCourses.degreeCurricularPlan.idInternal",
            schoolClass.getExecutionDegree().getCurricularPlan().getIdInternal());
        criteria.addEqualTo(
            "disciplinaExecucao.executionPeriod.idInternal",
            schoolClass.getExecutionPeriod().getIdInternal());

        List shifts = queryList(Turno.class, criteria, true);

        List classShifts =
            SuportePersistenteOJB.getInstance().getITurmaTurnoPersistente().readByClass(schoolClass);

        shifts.removeAll(classShifts);

        return shifts;
    }

    //by gedl AT rnl DOT ist DOT utl DOT pt, September the 17th, 2003
    public List readByExecutionCourseID(Integer id) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("chaveDisciplinaExecucao", id);
        return queryList(Turno.class, criteria);
    }

	/* (non-Javadoc)
	 * @see ServidorPersistente.ITurnoPersistente#readByLesson()
	 */
	public List readByLesson(IAula lesson) throws ExcepcaoPersistencia
	{
		if (lesson != null)
		{
			Criteria criteria = new Criteria();
			criteria.addEqualTo("associatedLessons.idInternal", lesson.getIdInternal());
			return queryList(Turno.class, criteria);
		}
		
			return null;
		
	}
	
	/* (non-Javadoc)
	 * @see ServidorPersistente.ITurnoPersistente#readByLesson()
	 */
	public ITurno readShiftByLesson(IAula lesson) throws ExcepcaoPersistencia
	{
		if (lesson != null)
		{
			Criteria criteria = new Criteria();
			criteria.addEqualTo("associatedLessons.idInternal", lesson.getIdInternal());
			return (ITurno) queryObject(Turno.class, criteria);
		}
		else
		{
			return null;
		}
	}

	
	/* (non-Javadoc)
	 * @see ServidorPersistente.ITurmaPersistente#readShiftsThatContainsStudentAttendsOnExecutionPeriod(Dominio.IStudent, Dominio.IExecutionPeriod)
	 */
	public List readShiftsThatContainsStudentAttendsOnExecutionPeriod(IStudent student, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
	{
		Criteria criteria =new Criteria();
		
		criteria.addEqualTo("disciplinaExecucao.attendingStudents.idInternal", student.getIdInternal());
		criteria.addEqualTo("disciplinaExecucao.executionPeriod.idInternal", executionPeriod.getIdInternal());
		return queryList(Turno.class, criteria, true);
	}

}