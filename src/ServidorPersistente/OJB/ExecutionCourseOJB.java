/**
 * DisciplinaExecucaoOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryBySQL;
import org.apache.ojb.odmg.HasBroker;

import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISite;
import Dominio.Professorship;
import Dominio.Site;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import Util.TipoCurso;

public class ExecutionCourseOJB extends ObjectFenixOJB implements IPersistentExecutionCourse
{

	public ExecutionCourseOJB()
	{
	}

	

	public List readAll() throws ExcepcaoPersistencia
	{
		return queryList(ExecutionCourse.class, new Criteria());
	}

	public IExecutionCourse readBySiglaAndAnoLectivoAndSiglaLicenciatura(
		String sigla,
		String anoLectivo,
		String siglaLicenciatura)
		throws ExcepcaoPersistencia
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("sigla", sigla);
		crit.addEqualTo("executionPeriod.executionYear.year", anoLectivo);
		crit.addEqualTo(
			"associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
			siglaLicenciatura);
		return (IExecutionCourse) queryObject(ExecutionCourse.class, crit);

	}

	/**
	 * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByCurricularYearAndExecutionPeriodAndExecutionDegree(java.lang.Integer,
	 *      Dominio.IExecutionPeriod, java.lang.String)
	 */
	public List readByCurricularYearAndExecutionPeriodAndExecutionDegree(
		Integer curricularYear,
		IExecutionPeriod executionPeriod,
		ICursoExecucao executionDegree)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();

		criteria.addEqualTo(
			"associatedCurricularCourses.scopes.curricularSemester.curricularYear.year",
			curricularYear);
		criteria.addEqualTo(
			"associatedCurricularCourses.scopes.curricularSemester.semester",
			executionPeriod.getSemester());
		criteria.addEqualTo(
			"associatedCurricularCourses.degreeCurricularPlan.name",
			executionDegree.getCurricularPlan().getName());
		criteria.addEqualTo(
			"associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
			executionDegree.getCurricularPlan().getDegree().getSigla());
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());

		List executionCourseList = queryList(ExecutionCourse.class, criteria, true);
		return executionCourseList;
	}

	/**
	 * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionPeriodAndExecutionDegree(java.lang.Integer,
	 *      Dominio.IExecutionPeriod, java.lang.String)
	 */
	public List readByExecutionPeriodAndExecutionDegree(
			IExecutionPeriod executionPeriod,
			ICursoExecucao executionDegree)
	throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();

		criteria.addEqualTo(
				"associatedCurricularCourses.scopes.curricularSemester.semester",
				executionPeriod.getSemester());
		criteria.addEqualTo(
				"associatedCurricularCourses.degreeCurricularPlan.name",
				executionDegree.getCurricularPlan().getName());
		criteria.addEqualTo(
				"associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
				executionDegree.getCurricularPlan().getDegree().getSigla());
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());

		List executionCourseList = queryList(ExecutionCourse.class, criteria, true);
		return executionCourseList;
	}
	
	/**
	 * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionCourseInitials(java.lang.String)
	 */
	public IExecutionCourse readByExecutionCourseInitialsAndExecutionPeriod(
		String courseInitials,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia
	{

		Criteria criteria = new Criteria();

		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo(
			"executionPeriod.executionYear.year",
			executionPeriod.getExecutionYear().getYear());
		criteria.addEqualTo("sigla", courseInitials);
		return (IExecutionCourse) queryObject(ExecutionCourse.class, criteria);
	}

	public void deleteExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
	{

		Criteria crit1 = new Criteria();
		crit1.addEqualTo("executionPeriod.name", executionCourse.getExecutionPeriod().getName());
		crit1.addEqualTo(
			"executionPeriod.executionYear.year",
			executionCourse.getExecutionPeriod().getExecutionYear().getYear());
		crit1.addEqualTo("sigla", executionCourse.getSigla());
		List result = queryList(ExecutionCourse.class, crit1);

		if (result != null && !result.isEmpty())
		{
			IExecutionCourse executionCourseTemp = (IExecutionCourse) result.get(0);
			super.delete(executionCourseTemp);
		}

	}

	public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
		return queryList(ExecutionCourse.class, criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionPeriod(Dominio.IExecutionPeriod,
	 *      Util.TipoCurso)
	 */
	public List readByExecutionPeriod(IExecutionPeriod executionPeriod, TipoCurso curso)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
		criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.tipoCurso", curso);
		return queryList(ExecutionCourse.class, criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(Dominio.IExecutionPeriod,
	 *      Dominio.ICursoExecucao, Dominio.ICurricularYear, java.lang.String)
	 */
	public List readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(
		IExecutionPeriod executionPeriod,
		ICursoExecucao executionDegree,
		ICurricularYear curricularYear,
		String executionCourseName)
		throws ExcepcaoPersistencia
	{

		Criteria criteria = new Criteria();
		if (executionCourseName != null && !executionCourseName.equals(""))
		{
			criteria.addLike("nome", executionCourseName);
		}
		if (curricularYear != null)
		{
			criteria.addEqualTo(
				"associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
				curricularYear.getIdInternal());
		}

		if (executionDegree != null)
		{
			criteria.addEqualTo(
				"associatedCurricularCourses.scopes.curricularCourse.degreeCurricularPlan.idInternal",
				executionDegree.getCurricularPlan().getIdInternal());
		}
		criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());

		List temp = queryList(ExecutionCourse.class, criteria, true);

		return temp;
		//		return queryList(DisciplinaExecucao.class, criteria, true);
	}

	//	returns a list of teachers in charge ids
	public List readExecutionCourseTeachers(Integer executionCourseId) throws ExcepcaoPersistencia
	{

		Criteria criteria = new Criteria();

		criteria.addEqualTo("keyExecutionCourse", executionCourseId);

		return queryList(Professorship.class, criteria);
	}

	

	public Boolean readSite(Integer executionCourseId) throws ExcepcaoPersistencia
	{
		Boolean result = null;

		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionCourse", executionCourseId);

		ISite site = (ISite) queryObject(Site.class, criteria);
		if (site == null)
			result = new Boolean(false);
		else
			result = new Boolean(true);
		return result;
	}

	public IExecutionCourse readbyCurricularCourseAndExecutionPeriod(
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia
	{

		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
		criteria.addEqualTo("associatedCurricularCourses.idInternal", curricularCourse.getIdInternal());

		return (IExecutionCourse) queryObject(ExecutionCourse.class, criteria);

	}
	public List readListbyCurricularCourseAndExecutionPeriod(
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia
	{

		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
		criteria.addEqualTo("associatedCurricularCourses.idInternal", curricularCourse.getIdInternal());

		return queryList(ExecutionCourse.class, criteria);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentExecutionCourse#readByExecutionDegree(Dominio.CursoExecucao)
	 */
	public List readByDegreeCurricularPlanAndExecutionYear(
		IDegreeCurricularPlan degreeCurricularPlan,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"associatedCurricularCourses.degreeCurricularPlanKey",
			degreeCurricularPlan.getIdInternal());
		criteria.addEqualTo("executionPeriod.keyExecutionYear", executionYear.getIdInternal());
		return queryList(ExecutionCourse.class, criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentExecutionCourse#readByExecutionDegreeAndExecutionPeriod(Dominio.ICursoExecucao,
	 *      Dominio.IExecutionPeriod)
	 */
	public List readByExecutionDegreeAndExecutionPeriod(
		ICursoExecucao executionDegree,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"associatedCurricularCourses.degreeCurricularPlan.idInternal",
			executionDegree.getCurricularPlan().getIdInternal());
		criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
		return queryList(ExecutionCourse.class, criteria, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentExecutionCourse#readByExecutionCourseIds(java.util.List)
	 */
	public List readByExecutionCourseIds(List executionCourseIds) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addIn("idInternal", executionCourseIds);
		return queryList(ExecutionCourse.class, criteria);
	}

	/*
	 * @author Fernanda Quiterio @author jpvl
	 *  
	 */
	public List readByExecutionPeriodWithNoCurricularCourses(IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia
	{

		DescriptorRepository descriptorRepository = MetadataManager.getInstance().getRepository();
		ClassDescriptor classDescriptor =
			descriptorRepository.getDescriptorFor(ExecutionCourse.class.getName());
		CollectionDescriptor collectionDescriptor =
			classDescriptor.getCollectionDescriptorByName("associatedCurricularCourses");

		StringBuffer sqlStatement = new StringBuffer();
		sqlStatement
			.append("select ec.* from ")
			.append(classDescriptor.getFullTableName())
			.append(" as ec ")
			.append(" left join ")
			.append(collectionDescriptor.getIndirectionTable())
			.append(" as ccec")
			.append(" on ec.")
			.append(classDescriptor.getFieldDescriptorByName("idInternal").getColumnName())
			.append(" = ccec.")
			.append(collectionDescriptor.getFksToThisClass()[0])
			.append(" where ccec.")
			.append(collectionDescriptor.getFksToThisClass()[0])
			.append(" is null and ec.")
			.append(classDescriptor.getFieldDescriptorByName("keyExecutionPeriod").getColumnName())
			.append(" = ")
			.append(executionPeriod.getIdInternal());

		Query query = new QueryBySQL(ExecutionCourse.class, sqlStatement.toString());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
		List collection = (List) pb.getCollectionByQuery(query);

		lockRead(collection);
		return collection;
	}
}