package middleware.persistentMiddlewareSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWCurricularCourse;
import middleware.middlewareDomain.MWCurricularCourseOutsideStudentDegree;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWStudent;
import middleware.middlewareDomain.MWUniversity;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.Employee;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ExecutionCourse;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.Frequenta;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDomainObject;
import Dominio.IEmployee;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IStudentGroupAttend;
import Dominio.IStudentKind;
import Dominio.ITeacher;
import Dominio.IUniversity;
import Dominio.Mark;
import Dominio.Pessoa;
import Dominio.Role;
import Dominio.ShiftStudent;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import Dominio.StudentGroupAttend;
import Dominio.StudentKind;
import Dominio.Teacher;
import Dominio.University;
import Util.DegreeCurricularPlanState;
import Util.EnrolmentEvaluationType;
import Util.PeriodState;
import Util.RoleType;
import Util.StudentCurricularPlanState;
import Util.StudentType;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;


/**
 * @author David Santos in Jan 29, 2004
 */

public class PersistanceBrokerForMigrationProcess
{
	PersistenceBroker broker = null;
	
	public PersistanceBrokerForMigrationProcess()
	{
		this.broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}

	/* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
	/* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
	/* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
	/* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

	private List queryList(Class classToQuery, Criteria criteria)
	{
		return queryList(classToQuery, criteria, false);
	}

	private List queryList(Class classToQuery, Criteria criteria, boolean distinct)
	{
		Query queryCriteria = new QueryByCriteria(classToQuery, criteria, distinct);
		List list = (List) this.broker.getCollectionByQuery(queryCriteria);
		if (list == null)
		{
			list = new ArrayList();
		}
		return list;
	}

	private Object queryObject(Class classToQuery, Criteria criteria)
	{
		Query queryCriteria = new QueryByCriteria(classToQuery, criteria);
		Object object = this.broker.getObjectByQuery(queryCriteria);

		return object;
	}

	private int count(PersistenceBroker pb, Query query)
	{
		return pb.getCount(query);
	}

	public int count(Class classToQuery, Criteria criteria)
	{
		Query query = new QueryByCriteria(classToQuery, criteria);
		return count(this.broker, query);
	}

	public void delete(Object obj)
	{
		this.broker.delete(obj);
		this.broker.removeFromCache(obj);
	}

	public void deleteByOID(Class classToQuery, Integer oid)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("idInternal", oid);
		List objectsToDelete = queryList(classToQuery, criteria);
		for (int i = 0; i < objectsToDelete.size(); i++)
		{
			delete(objectsToDelete.get(i));
		}
	}

	public IDomainObject readByOID(Class classToQuery, Integer oid)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("idInternal", oid);
		return (IDomainObject) queryObject(classToQuery, criteria);
	}

	public List readSpan(Class classToQuery, Criteria criteria, Integer numberOfElementsInSpan, Integer spanNumber)
	{
		if (numberOfElementsInSpan == null || numberOfElementsInSpan.intValue() == 0)
		{
			throw new IllegalArgumentException("Invalid numberOfElementsInSpan!");
		}

		Query query = new QueryByCriteria(classToQuery, criteria);

		int startIndex = 1;
		if (spanNumber.intValue() != 0)
		{
			startIndex = spanNumber.intValue() * numberOfElementsInSpan.intValue();
		}

		query.setStartAtIndex(startIndex);

		int endIndex = startIndex + numberOfElementsInSpan.intValue() - 1;
		if (endIndex == 0)
		{
			endIndex = 1;
		}

		query.setEndAtIndex(endIndex);
		List list = (List) this.broker.getCollectionByQuery(query);
		return list;
	}

	public Iterator readSpanIterator(Class classToQuery, Criteria criteria, Integer numberOfElementsInSpan, Integer spanNumber)
	{
		if (numberOfElementsInSpan == null || numberOfElementsInSpan.intValue() == 0)
		{
			throw new IllegalArgumentException("Invalid numberOfElementsInSpan!");
		}

		Query query = new QueryByCriteria(classToQuery, criteria);

		int startIndex = 1;
		if (spanNumber.intValue() != 0)
		{
			startIndex = spanNumber.intValue() * numberOfElementsInSpan.intValue();
		}

		query.setStartAtIndex(startIndex);

		int endIndex = startIndex + numberOfElementsInSpan.intValue() - 1;
		if (endIndex == 0)
		{
			endIndex = 1;
		}

		query.setEndAtIndex(endIndex);
		Iterator iterator = this.broker.getIteratorByQuery(query);
		return iterator;
	}

	public Iterator readIteratorByCriteria(Class classToQuery, Criteria criteria)
	{
		Query query = new QueryByCriteria(classToQuery, criteria);
		Iterator iterator = this.broker.getIteratorByQuery(query);
		return iterator;
	}

	public List readByCriteria(Class classToQuery, Criteria criteria)
	{
		return queryList(classToQuery, criteria);
	}

	public void beginTransaction()
	{
		this.broker.beginTransaction();
	}

	public void abortTransaction()
	{
		this.broker.abortTransaction();
	}

	public void commitTransaction()
	{
		this.broker.commitTransaction();
	}

	public void clearCache()
	{
		this.broker.clearCache();
	}

	public boolean close()
	{
		return this.broker.close();
	}

	public void store(Object object)
	{
		this.broker.store(object);
	}

	/* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
	/* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
	/* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
	/* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

	public ITeacher readTeacherByNumber(Integer teacherNumber)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("teacherNumber", teacherNumber);
		return (ITeacher) queryObject(Teacher.class, criteria);
	}

	public IEmployee readEmployeeByNumber(Integer number)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("employeeNumber", number);
		return (Employee) queryObject(Employee.class, criteria);
	}

	public IExecutionYear readExecutionYearByName(String year)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("year", year);
		return (IExecutionYear) queryObject(ExecutionYear.class, criteria);
	}

	public IExecutionPeriod readExecutionPeriodByNameAndExecutionYear(String executionPeriodName, IExecutionYear executionYear)
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("name", executionPeriodName);
		crit.addEqualTo("executionYear.year", executionYear.getYear());
		return (IExecutionPeriod) queryObject(ExecutionPeriod.class, crit);

	}

	public ICursoExecucao readExecutionDegreeByDegreeNameAndExecutionYearAndDegreeType(
		String name,
		IExecutionYear executionYear,
		TipoCurso degreeType)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("academicYear", executionYear.getIdInternal());
		criteria.addLike("curricularPlan.degree.nome", name);
		criteria.addEqualTo("curricularPlan.degree.tipoCurso", degreeType);
		return (ICursoExecucao) queryObject(CursoExecucao.class, criteria);
	}

	public MWDegreeTranslation readMWDegreeTranslationByDegreeCode(Integer degreeCode)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degreeCode", degreeCode);
		return (MWDegreeTranslation) queryObject(MWDegreeTranslation.class, criteria);
	}

	public List readCurricularCoursesByCourseCodeAndDegreeCurricularPlan(
		String curricularCourseCode,
		IDegreeCurricularPlan degreeCurricularPlan)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", curricularCourseCode);
		criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
		return queryList(CurricularCourse.class, criteria);
	}

	public List readCurricularCoursesByCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
		String courseCode,
		TipoCurso degreeType,
		DegreeCurricularPlanState degreeCurricularPlanState)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", courseCode);
		criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", degreeType);
		criteria.addEqualTo("degreeCurricularPlan.state", degreeCurricularPlanState);
		return queryList(CurricularCourse.class, criteria);
	}

	public MWCurricularCourseOutsideStudentDegree readMWCurricularCourseOutsideStudentDegreeByCourseCodeAndDegreeCode(
		String courseCode,
		Integer degreeCode)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("courseCode", courseCode);
		criteria.addEqualTo("degreeCode", degreeCode);
		return (MWCurricularCourseOutsideStudentDegree) queryObject(MWCurricularCourseOutsideStudentDegree.class, criteria);
	}

	public List readAttendsByStudentNumberInCurrentExecutionPeriod(Integer number)
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("aluno.number", number);
		crit.addEqualTo("disciplinaExecucao.executionPeriod.state", PeriodState.CURRENT);
		return queryList(Frequenta.class, crit);
	}

	public IDegreeCurricularPlan readDegreeCurricularPlanByNameAndDegree(String name, ICurso degree)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeKey", degree.getIdInternal());
		return (IDegreeCurricularPlan) queryObject(DegreeCurricularPlan.class, criteria);
	}

	public List readStudentCurricularPlansByStudentAntState(IStudent student, StudentCurricularPlanState state)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentKey", student.getIdInternal());
		criteria.addEqualTo("currentState", state);
		return queryList(StudentCurricularPlan.class, criteria);
	}

	public MWBranch readMWBranchByDegreeCodeAndBranchCode(Integer degreeCode, Integer branchCode)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("branchcode", branchCode);
		criteria.addEqualTo("degreecode", degreeCode);
		return (MWBranch) queryObject(MWBranch.class, criteria);
	}
	
	public IBranch readBranchByDegreeCurricularPlanAndCode(IDegreeCurricularPlan degreeCurricularPlan, String code)
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
		crit.addEqualTo("code", code);
		return (IBranch) queryObject(Branch.class, crit);
	}

	public MWCurricularCourse readMWCurricularCourseByCode(String code)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coursecode", code);
		return (MWCurricularCourse) queryObject(MWCurricularCourse.class, criteria);
	}
	
	public MWUniversity readMWUniversityByCode(String code)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("universityCode", code);
		return (MWUniversity) queryObject(MWUniversity.class, criteria);
	}

	public IUniversity readUniversityByNameAndCode(String name, String code)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("code", code);
		return (IUniversity) queryObject(University.class, criteria);
	}
	
	public Iterator readMWStudentsBySpanIterator(Integer spanNumber, Integer numberOfElementsInSpan)
	{
		Criteria criteria = new Criteria();
		return readSpanIterator(MWStudent.class, criteria, numberOfElementsInSpan, spanNumber);
	}

	public List readMWEnrolmentsByStudentNumber(Integer number)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("number", number);
		return queryList(MWEnrolment.class, criteria);
	}

	public IStudent readStudentByNumberAndDegreeType(Integer number, TipoCurso degreeType)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("number", number);
		criteria.addEqualTo("degreeType", degreeType);
		return (IStudent) queryObject(Student.class, criteria);
	}

	public IStudentCurricularPlan readActiveStudentCurricularPlanByStudentNumberAndDegreeType(Integer number, TipoCurso degreeType)
	{
		Criteria criteria = new Criteria();
		List studentPlanState = new ArrayList();
		studentPlanState.add(StudentCurricularPlanState.ACTIVE_OBJ);
		studentPlanState.add(StudentCurricularPlanState.SCHOOLPARTCONCLUDED_OBJ);
		
		criteria.addIn("currentState", studentPlanState);
		criteria.addEqualTo("student.number", number);
		criteria.addEqualTo("student.degreeType", degreeType);

		IStudentCurricularPlan storedStudentCurricularPlan =
			(IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, criteria);
		return storedStudentCurricularPlan;
	}

	public IExecutionPeriod readCurrentExecutionPeriod()
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("state", PeriodState.CURRENT);
		return (IExecutionPeriod) queryObject(ExecutionPeriod.class, criteria);
	}

	public IExecutionCourse readExecutionCourseByCurricularCourseAndExecutionPeriod(
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
		criteria.addEqualTo("associatedCurricularCourses.idInternal", curricularCourse.getIdInternal());
		return (IExecutionCourse) queryObject(ExecutionCourse.class, criteria);
	}

	public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
		criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
		criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
		return (IEnrolment) queryObject(Enrolment.class, criteria);
	}

	public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
		IEnrolment enrolment,
		EnrolmentEvaluationType evaluationType,
		String grade)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
		criteria.addEqualTo("enrolmentEvaluationType", evaluationType);
		if (grade == null)
		{
			criteria.addIsNull("grade");
		} else
		{
			criteria.addEqualTo("grade", grade);
		}

		IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) queryObject(EnrolmentEvaluation.class, criteria);

		return enrolmentEvaluation;
	}

	public IFrequenta readAttendByStudentAndExecutionCourse(IStudent student, IExecutionCourse executionCourse)
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("aluno.idInternal", student.getIdInternal());
		crit.addEqualTo("chaveDisciplinaExecucao", executionCourse.getIdInternal());
		return (IFrequenta) queryObject(Frequenta.class, crit);
	}

	public Integer countAllMWStudents()
	{
		return new Integer(count(MWStudent.class, new Criteria()));
	}

	public List readAllMWStudentsBySpan(Integer spanNumber, Integer numberOfElementsInSpan)
	{
		Criteria criteria = new Criteria();
		return readSpan(MWStudent.class, criteria, numberOfElementsInSpan, spanNumber);
	}
	
	public Iterator readAllMWStudentsBySpanIterator(Integer spanNumber, Integer numberOfElementsInSpan)
	{
		Criteria criteria = new Criteria();
		return readSpanIterator(MWStudent.class, criteria, numberOfElementsInSpan, spanNumber);
	}
	
	public IPessoa readPersonByUsername(String username)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("username", username);
		IPessoa person = (IPessoa) queryObject(Pessoa.class, criteria);
		return person;
	}

	public List readMarksByAttend(IFrequenta attend)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("attend.idInternal", attend.getIdInternal());
		return queryList(Mark.class, criteria);
	}

	public IStudentGroupAttend readStudentGroupAttendByAttend(IFrequenta attend)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyAttend", attend.getIdInternal());
		return (IStudentGroupAttend) queryObject(StudentGroupAttend.class, criteria);
	}

	public List readShiftStudentByStudent(IStudent student)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		return queryList(ShiftStudent.class, criteria);
	}

	public IPessoa readPersonByDocumentIDNumberAndDocumentIDType(String documentIDNumber, TipoDocumentoIdentificacao documentIDType)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("numeroDocumentoIdentificacao", documentIDNumber);
		criteria.addEqualTo("tipoDocumentoIdentificacao", documentIDType);
		return (IPessoa) queryObject(Pessoa.class, criteria);
	}

	public IRole readRoleByType(RoleType roleType)
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("roleType",new Integer(roleType.getValue()));
		return (IRole) queryObject(Role.class,crit);
	}

	public IStudent readStudentByPersonAndDegreeType(IPessoa person, TipoCurso degreeType)
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("person.idInternal", person.getIdInternal());
		crit.addEqualTo("degreeType", degreeType);
		return (IStudent) queryObject(Student.class, crit);
	}

	public IStudentKind readStudentKindByStudentType(StudentType studentType)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentType", studentType);
		return (IStudentKind) queryObject(StudentKind.class, criteria);
	}

	public IStudentCurricularPlan readStudentCurricularPlanByStudentAndDegreeCurricularPlanAndState(
		IStudent student,
		IDegreeCurricularPlan degreeCurricularPlan,
		StudentCurricularPlanState state)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
		criteria.addEqualTo("student.idInternal", student.getIdInternal());
		criteria.addEqualTo("currentState", state);
		return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, criteria);
	}

	public IBranch readBranchByDegreeCurricularPlanAndBranchName(IDegreeCurricularPlan degreeCurricularPlan, String branchName)
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
		crit.addEqualTo("name", branchName);
		return (IBranch) queryObject(Branch.class, crit);
	}

}