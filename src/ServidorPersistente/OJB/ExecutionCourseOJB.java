/**
 * DisciplinaExecucaoOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

import java.util.Iterator;
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
import org.odmg.QueryException;

import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.ISite;
import Dominio.ITurno;
import Dominio.Professorship;
import Dominio.Site;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public class ExecutionCourseOJB extends ObjectFenixOJB implements IPersistentExecutionCourse
{

	public ExecutionCourseOJB()
	{
	}

	public boolean apagarTodasAsDisciplinasExecucao()
	{
		try
		{
			String oqlQuery = "select all from " + ExecutionCourse.class.getName();
			super.deleteAll(oqlQuery);
			return true;
		}
		catch (ExcepcaoPersistencia ex)
		{
			return false;
		}
	}

	public void escreverDisciplinaExecucao(IExecutionCourse executionCourseToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException
	{

		IExecutionCourse executionCourseFromDB = null;

		// If there is nothing to write, simply return.
		if (executionCourseToWrite == null)
			return;

		// Read execution course from database.
		executionCourseFromDB =
			this.readByExecutionCourseInitialsAndExecutionPeriod(
				executionCourseToWrite.getSigla(),
				executionCourseToWrite.getExecutionPeriod());

		// If execution course is not in database, then write it.
		if (executionCourseFromDB == null)
			super.lockWrite(executionCourseToWrite);
		// else If the execution course is mapped to the database, then write any existing changes.
		else if (
			(executionCourseToWrite instanceof ExecutionCourse)
				&& ((ExecutionCourse) executionCourseFromDB).getIdInternal().equals(
					((ExecutionCourse) executionCourseToWrite).getIdInternal()))
		{
			super.lockWrite(executionCourseToWrite);
			// else Throw an already existing exception
		}
		else
			throw new ExistingPersistentException();
	}

	// TODO : Write test for this method
	public List readAll() throws ExcepcaoPersistencia
	{
		try
		{
			String oqlQuery = "select all from " + ExecutionCourse.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		}
		catch (QueryException ex)
		{
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public IExecutionCourse readBySiglaAndAnoLectivoAndSiglaLicenciatura(
		String sigla,
		String anoLectivo,
		String siglaLicenciatura)
		throws ExcepcaoPersistencia
	{
		try
		{
			IExecutionCourse disciplinaExecucao = null;
			String oqlQuery = "select disciplinaExecucao from " + ExecutionCourse.class.getName();
			oqlQuery += " where sigla = $1";
			oqlQuery += " and executionPeriod.executionYear.year = $2";
			oqlQuery += " and associatedCurricularCourses.degreeCurricularPlan.degree.sigla = $3";
			query.create(oqlQuery);
			query.bind(sigla);
			query.bind(anoLectivo);
			query.bind(siglaLicenciatura);
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				disciplinaExecucao = (IExecutionCourse) result.get(0);
			return disciplinaExecucao;
		}
		catch (QueryException ex)
		{
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/**
	 * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByAnoCurricularAndAnoLectivoAndSiglaLicenciatura(java.lang.Integer,
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
		try
		{
			String oqlQuery = "select all from " + ExecutionCourse.class.getName();
			oqlQuery += " where executionPeriod.name = $1 "
				+ " and executionPeriod.executionYear.year = $2 "
				+ " and sigla = $3 ";
			query.create(oqlQuery);

			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
			query.bind(executionCourse.getSigla());

			List result = (List) query.execute();
			lockRead(result);

			if (!result.isEmpty())
			{
				IExecutionCourse executionCourseTemp = (IExecutionCourse) result.get(0);
				// Delete All Attends

				List attendsTemp =
					SuportePersistenteOJB
						.getInstance()
						.getIFrequentaPersistente()
						.readByExecutionCourse(
						executionCourseTemp);
				Iterator iterator = attendsTemp.iterator();
				while (iterator.hasNext())
				{
					SuportePersistenteOJB.getInstance().getIFrequentaPersistente().delete(
						(IFrequenta) iterator.next());
				}

				// Delete All Shifts
				List shiftsTemp =
					SuportePersistenteOJB.getInstance().getITurnoPersistente().readByExecutionCourse(
						executionCourseTemp);
				iterator = shiftsTemp.iterator();
				while (iterator.hasNext())
				{
					SuportePersistenteOJB.getInstance().getITurnoPersistente().delete(
						(ITurno) iterator.next());
				}
				super.delete(executionCourseTemp);
			}

		}
		catch (QueryException ex)
		{
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
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

	public void lockWrite(IExecutionCourse executionCourseToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException
	{

		IExecutionCourse executionCourseFromDB = null;

		// If there is nothing to write, simply return.
		if (executionCourseToWrite == null)
		{
			return;
		}

		// Read ExecutionCourse from database.
		executionCourseFromDB =
			this.readByExecutionCourseInitialsAndExecutionPeriod(
				executionCourseToWrite.getSigla(),
				executionCourseToWrite.getExecutionPeriod());

		// If ExecutionCourse is not in database, then write it.
		if (executionCourseFromDB == null)
		{
			super.lockWrite(executionCourseToWrite);
			// else If the ExecutionCourse is mapped to the database, then write any existing changes.
		}
		else if (
			(executionCourseToWrite instanceof ExecutionCourse)
				&& ((ExecutionCourse) executionCourseFromDB).getIdInternal().equals(
					((ExecutionCourse) executionCourseToWrite).getIdInternal()))
		{
			super.lockWrite(executionCourseToWrite);
			// else Throw an already existing exception
		}
		else
			throw new ExistingPersistentException();
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
	 * @author Fernanda Quiterio
	 * @author jpvl
	 * 
	 */
	public List readByExecutionPeriodWithNoCurricularCourses(IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia
	{
		//		select ec.* from execution_course as ec
		//		left join curricular_course_execution_course as ccec
		//		on ec.id_internal=ccec.key_execution_course
		//		where ccec.key_execution_course is null
		//		and ec.key_execution_period=80;

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
		
//		List collection = (List) getCurrentPersistenceBroker().getCollectionByQuery(query);
		lockRead(collection);
		return collection;
	}
}