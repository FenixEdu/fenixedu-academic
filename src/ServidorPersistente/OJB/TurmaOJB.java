/*
 * TurmaOJB.java
 * 
 * Created on 17 de Outubro de 2002, 18:44
 */

package ServidorPersistente.OJB;

/**
 * @author tfc130
 */
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurmaPersistente;
import Util.TipoCurso;

public class TurmaOJB extends ObjectFenixOJB implements ITurmaPersistente
{

   

    public void delete(ITurma turma) throws ExcepcaoPersistencia
    {
        /*Criteria crit = new Criteria();
        crit.addEqualTo("turma.nome", turma.getNome());
        crit.addEqualTo("turma.executionPeriod.name", turma.getExecutionPeriod().getName());
        crit.addEqualTo(
            "turma.executionPeriod.executionYear.year",
            turma.getExecutionPeriod().getExecutionYear().getYear());
        crit.addEqualTo(
            "turma.executionDegree.executionYear.year",
            turma.getExecutionDegree().getExecutionYear().getYear());
        crit.addEqualTo(
            "turma.executionDegree.curricularPlan.name",
            turma.getExecutionDegree().getCurricularPlan().getName());
        crit.addEqualTo(
            "turma.executionDegree.curricularPlan.degree.sigla",
            turma.getExecutionDegree().getCurricularPlan().getDegree().getSigla());

        ITurmaTurno turmaTurno = null;
        TurmaTurnoOJB turmaTurnoOJB = new TurmaTurnoOJB();

        List result = queryList(TurmaTurno.class, crit);
        Iterator iterador = result.iterator();
        while (iterador.hasNext())
        {
            turmaTurno = (ITurmaTurno) iterador.next();
            turmaTurnoOJB.delete(turmaTurno);
        }*/
        super.delete(turma);

    }

    public List readAll() throws ExcepcaoPersistencia
    {
        return queryList(Turma.class, new Criteria());
    }

    /**
	 * @see ServidorPersistente.ITurmaPersistente#readByExecutionPeriodAndCurricularYearAndExecutionDegree(Dominio.IExecutionPeriod,
	 *      java.lang.Integer, Dominio.ICursoExecucao)
	 */
    public List readByExecutionPeriodAndCurricularYearAndExecutionDegree(
        IExecutionPeriod executionPeriod,
        Integer curricularYear,
        ICursoExecucao executionDegree)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo(
            "executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());
        crit.addEqualTo("executionPeriod.name", executionPeriod.getName());
        crit.addEqualTo("anoCurricular", curricularYear);
        crit.addEqualTo(
            "executionDegree.executionYear.year",
            executionDegree.getExecutionYear().getYear());
        crit.addEqualTo(
            "executionDegree.curricularPlan.name",
            executionDegree.getCurricularPlan().getName());
        crit.addEqualTo(
            "executionDegree.curricularPlan.degree.sigla",
            executionDegree.getCurricularPlan().getDegree().getSigla());
        return queryList(Turma.class, crit);

    }
    /**
	 * @see ServidorPersistente.ITurmaPersistente#readByNameAndExecutionDegreeAndExecutionPeriod(java.lang.String,
	 *      Dominio.ICursoExecucao, Dominio.IExecutionPeriod)
	 */
    public ITurma readByNameAndExecutionDegreeAndExecutionPeriod(
        String className,
        ICursoExecucao executionDegree,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("nome", className);
        criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo(
            "executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());
        criteria.addEqualTo(
            "executionDegree.executionYear.year",
            executionDegree.getExecutionYear().getYear());
        criteria.addEqualTo(
            "executionDegree.curricularPlan.name",
            executionDegree.getCurricularPlan().getName());
        criteria.addEqualTo(
            "executionDegree.curricularPlan.degree.sigla",
            executionDegree.getCurricularPlan().getDegree().getSigla());

        return (ITurma) queryObject(Turma.class, criteria);

    }

    public List readByDegreeNameAndDegreeCode(String name, String code) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionDegree.curricularPlan.degree.nome", name);
        crit.addEqualTo("executionDegree.curricularPlan.degree.sigla", code);
        return queryList(Turma.class, crit);

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ITurmaPersistente#readByExecutionPeriod(Dominio.IExecutionPeriod)
	 */
    public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo(
            "executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());
        crit.addEqualTo("executionPeriod.name", executionPeriod.getName());
        return queryList(Turma.class, crit);

    }

    public List readByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo(
            "executionDegree.executionYear.year",
            executionDegree.getExecutionYear().getYear());
        crit.addEqualTo(
            "executionDegree.curricularPlan.name",
            executionDegree.getCurricularPlan().getName());
        crit.addEqualTo(
            "executionDegree.curricularPlan.degree.sigla",
            executionDegree.getCurricularPlan().getDegree().getSigla());
        return queryList(Turma.class, crit);

    }

    public List readByExecutionPeriodAndDegreeType(
        IExecutionPeriod executionPeriod,
        TipoCurso tipoCurso)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo(
            "executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());
        criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("executionDegree.curricularPlan.degree.tipoCurso", tipoCurso);
        //Query queryPB = new QueryByCriteria(Turma.class, criteria);
        return queryList(Turma.class, criteria);
    }

    public List readByExecutionDegreeAndExecutionPeriod(
        ICursoExecucao execucao,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
            "executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());
        criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("executionDegree.executionYear.year", execucao.getExecutionYear().getYear());
        criteria.addEqualTo(
            "executionDegree.curricularPlan.name",
            execucao.getCurricularPlan().getName());
        criteria.addEqualTo(
            "executionDegree.curricularPlan.degree.sigla",
            execucao.getCurricularPlan().getDegree().getSigla());
        //Query queryPB = new QueryByCriteria(Turma.class, criteria);
        return queryList(Turma.class, criteria);
    }
	public List readByExecutionDegreeAndDegreeAndExecutionPeriod(
		   ICursoExecucao execucao,
		   ICurso degree,
		   IExecutionPeriod executionPeriod)
		   throws ExcepcaoPersistencia
	   {
		    Criteria criteria = new Criteria();
			criteria.addEqualTo(
			"executionDegree.idInternal",
			execucao.getIdInternal());
			criteria.addEqualTo(
			"executionDegree.curricularPlan.degree.idInternal",
			degree.getIdInternal());
			criteria.addEqualTo(
			"executionPeriod.idInternal",
			executionPeriod.getIdInternal());
		   return queryList(Turma.class, criteria);
	   }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ITurmaPersistente#readByExecutionCourse(Dominio.IDisciplinaExecucao)
	 */
    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
            "associatedShifts.disciplinaExecucao.idInternal",
            executionCourse.getIdInternal());
        return queryList(Turma.class, criteria, true);
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.ITurmaPersistente#readClassesThatContainsStudentAttendsOnExecutionPeriod(Dominio.IStudent, Dominio.IExecutionPeriod)
     */
    public List readClassesThatContainsStudentAttendsOnExecutionPeriod(IStudent student, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
    {
        Criteria criteria =new Criteria();
        
        criteria.addEqualTo("associatedShifts.disciplinaExecucao.attendingStudents.idInternal", student.getIdInternal());
        criteria.addEqualTo("associatedShifts.disciplinaExecucao.executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(Turma.class, criteria, true);
    }
}
