/*
 * ITurnoOJB.java
 * 
 * Created on 17 de Outubro de 2002, 19:35
 */

package ServidorPersistente.OJB;

/**
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.ITurnoAula;
import Dominio.ShiftStudent;
import Dominio.TurmaTurno;
import Dominio.Turno;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
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

    public void lockWrite(ITurno shiftToWrite) throws ExcepcaoPersistencia, ExistingPersistentException
    {

        ITurno shiftFromDB = null;

        // If there is nothing to write, simply return.
        if (shiftToWrite == null)
            return;

        // Read shift from database.
        shiftFromDB =
            this.readByNameAndExecutionCourse(
                shiftToWrite.getNome(),
                shiftToWrite.getDisciplinaExecucao());

        // If shift is not in database, then write it.
        if (shiftFromDB == null)
            super.lockWrite(shiftToWrite);
        // else If the shift is mapped to the database, then write any existing
        // changes.
        else if (
            (shiftToWrite instanceof Turno)
                && ((Turno) shiftFromDB).getIdInternal().equals(((Turno) shiftToWrite).getIdInternal()))
        {
            super.lockWrite(shiftToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public void delete(ITurno turno) throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("chaveTurno", turno.getIdInternal());
        ITurnoAula turnoAula = null;
        TurnoAulaOJB turnoAulaOJB = new TurnoAulaOJB();

        List result = queryList(TurnoAula.class, crit);
        lockRead(result);
        Iterator iterador = result.iterator();
        while (iterador.hasNext())
        {
            turnoAula = (ITurnoAula) iterador.next();
            turnoAulaOJB.delete(turnoAula);
        }

        crit = new Criteria();
        crit.addEqualTo("chaveTurno", turno.getIdInternal());
        ITurmaTurno turmaTurno = null;
        TurmaTurnoOJB turmaTurnoOJB = new TurmaTurnoOJB();
        List result1 = queryList(TurmaTurno.class, crit);
        Iterator iterador1 = result1.iterator();
        while (iterador1.hasNext())
        {
            turmaTurno = (ITurmaTurno) iterador1.next();
            turmaTurnoOJB.delete(turmaTurno);
        }
        ITurnoAluno turnoAluno = null;
        TurnoAlunoOJB turnoAlunoOJB = new TurnoAlunoOJB();

        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.nome", turno.getNome());
        criteria.addEqualTo("shift.disciplinaExecucao.sigla", turno.getDisciplinaExecucao().getSigla());
        criteria.addEqualTo(
            "shift.disciplinaExecucao.executionPeriod.name",
            turno.getDisciplinaExecucao().getExecutionPeriod().getName());
        criteria.addEqualTo(
            "shift.disciplinaExecucao.executionPeriod.executionYear.year",
            turno.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
        List result2 = queryList(ShiftStudent.class, criteria);

        Iterator iterador2 = result2.iterator();
        while (iterador2.hasNext())
        {
            turnoAluno = (ITurnoAluno) iterador2.next();
            turnoAlunoOJB.delete(turnoAluno);
        }

        super.delete(turno);

    }

    public void deleteAll() throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + Turno.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            Iterator iterator = result.iterator();
            while (iterator.hasNext())
            {
                delete((ITurno) iterator.next());
            }
        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public Integer countAllShiftsOfAllClassesAssociatedWithShift(ITurno shift)
        throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery =
                "select all from "
                    + TurmaTurno.class.getName()
                    + " where turno.nome = $1 "
                    + " and turno.disciplinaExecucao.sigla = $2 "
                    + " and turno.disciplinaExecucao.executionPeriod.name = $3 "
                    + " and turno.disciplinaExecucao.executionPeriod.executionYear.year = $4"
                    + " and turno.tipo = $5";
            query.create(oqlQuery);

            query.bind(shift.getNome());
            query.bind(shift.getDisciplinaExecucao().getSigla());
            query.bind(shift.getDisciplinaExecucao().getExecutionPeriod().getName());
            query.bind(shift.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
            query.bind(shift.getTipo().getTipo());

            List result = (List) query.execute();
            lockRead(result);

            //return new Integer(result.size());
            List result2 = null;
            for (int i = 0; i != result.size(); i++)
            {
                try
                {
                    oqlQuery = "select all from " + TurmaTurno.class.getName();
                    //oqlQuery += ", " + Turma.class.getName() + ", " +
                    // TurmaTurno.class.getName() + ")";
                    oqlQuery += " where turno.tipo = $1 and turma.nome = $2";
                    query.create(oqlQuery);
                    query.bind(new Integer(TipoAula.PRATICA));
                    query.bind(((TurmaTurno) (result.get(i))).getTurma().getNome());
                    if (i == 0)
                    {
                        result2 = (List) query.execute();
                        lockRead(result2);
                    }
                    else
                    {
                        List result_tmp = (List) query.execute();
                        lockRead(result_tmp);
                        for (int j = 0; j != result_tmp.size(); j++)
                            if (!result2.contains(result_tmp.get(j)))
                                result2.add(result_tmp.get(j));
                    }
                }
                catch (QueryException ex)
                {
                    throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
                }
            }
            return new Integer(result2.size());

        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public ArrayList readByDisciplinaExecucao(String sigla, String anoLectivo, String siglaLicenciatura)
        throws ExcepcaoPersistencia
    {
        try
        {
            ArrayList turnos = new ArrayList();
            String oqlQuery = "select turnos from " + Turno.class.getName();
            oqlQuery += " where disciplinaExecucao.sigla = $1"
                + " and disciplinaExecucao.executionPeriod.executionYear.year = $2"
                + " and disciplinaExecucao.associatedCurricularCourses.degreeCurricularPlan.degree.sigla = $3";
            query.create(oqlQuery);
            query.bind(sigla);
            query.bind(anoLectivo);
            query.bind(siglaLicenciatura);
            List result = (List) query.execute();
            lockRead(result);

            for (int i = 0; i != result.size(); i++)
                turnos.add((result.get(i)));
            return turnos;
        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    public List readByExecutionCourseAndType(IExecutionCourse executionCourse, Integer type)
        throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select turnos from " + Turno.class.getName();
            oqlQuery += " where disciplinaExecucao.sigla = $1";
            oqlQuery += " and disciplinaExecucao.executionPeriod.name = $2";
            oqlQuery += " and disciplinaExecucao.executionPeriod.executionYear.year = $3";
            oqlQuery += " and tipo = $4";
            query.create(oqlQuery);
            query.bind(executionCourse.getSigla());
            query.bind(executionCourse.getExecutionPeriod().getName());
            query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
            query.bind(type);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    /**
	 * @see ServidorPersistente.ITurnoPersistente#readByExecutionCourse(Dominio.IDisciplinaExecucao)
	 */
    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select turnos from " + Turno.class.getName();
            oqlQuery += " where disciplinaExecucao.sigla = $1"
                + " and disciplinaExecucao.executionPeriod.name = $2"
                + " and disciplinaExecucao.executionPeriod.executionYear.year = $3";
            query.create(oqlQuery);
            query.bind(executionCourse.getSigla());
            query.bind(executionCourse.getExecutionPeriod().getName());
            query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

            List result = (List) query.execute();
            lockRead(result);
            return result;
        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
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

        List shifts = queryList(Turno.class, criteria);

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

}