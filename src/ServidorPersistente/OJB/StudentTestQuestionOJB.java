/*
 * Created on 27/Ago/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.StudentTestQuestion;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestionOJB extends ObjectFenixOJB implements IPersistentStudentTestQuestion
{

	public StudentTestQuestionOJB()
	{
	}

	public List readByStudentAndDistributedTest(IStudent student, IDistributedTest distributedTest)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.idInternal", student.getIdInternal());
		criteria.addEqualTo("distributedTest.idInternal", distributedTest.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public List readByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia
	{
		//Calendar start = Calendar.getInstance();
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
		criteria.addOrderBy("keyStudent", true);
		criteria.addOrderBy("testQuestionOrder", true);
		List result = queryList(StudentTestQuestion.class, criteria);
		//System.out.println("readByDistributedTest took [" + (Calendar.getInstance().getTimeInMillis() - start.getTimeInMillis()) +"] ms");
		return result;
	}

	public List readByStudent(IStudent student) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public List readByQuestion(IQuestion question) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyQuestion", question.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public List readByQuestionAndDistributedTest(IQuestion question, IDistributedTest distributedTest)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyQuestion", question.getIdInternal());
		criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public IStudentTestQuestion readByQuestionAndStudentAndDistributedTest(
		IQuestion question,
		IStudent student,
		IDistributedTest distributedTest)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyQuestion", question.getIdInternal());
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
		return (IStudentTestQuestion) queryObject(StudentTestQuestion.class, criteria);
	}

	public List readStudentsByDistributedTest(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia
	{

		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());

		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
		queryCriteria.addGroupBy("keyStudent");
		List result = (List) pb.getCollectionByQuery(queryCriteria);
		lockRead(result);

		List studentList = new ArrayList();

		Iterator iterator = result.iterator();
		while (iterator.hasNext())
		{
			IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) iterator.next();
			studentList.add(studentTestQuestion.getStudent());
		}
		return studentList;

	}

	public List readStudentTestQuestionsByDistributedTest(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
		criteria.addOrderBy("keyStudent", true);
		criteria.addOrderBy("testQuestionOrder", true);

		return readSpan(
			StudentTestQuestion.class,
			criteria,
			distributedTest.getNumberOfQuestions(),
			new Integer(0));
	}

	public int countByQuestionOrderAndOptionAndDistributedTest(
		Integer order,
		Integer option,
		IDistributedTest distributedTest)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("testQuestionOrder", order);
		criteria.addEqualTo("response", option);
		criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria);
	}

	public int countResponsedOrNotResponsed(
		Integer order,
		boolean responsed,
		IDistributedTest distributedTest)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("testQuestionOrder", order);
		if (responsed)
			criteria.addNotEqualTo("response", new Integer(0));
		else
			criteria.addEqualTo("response", new Integer(0));

		criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria);
	}

	public int countCorrectOrIncorrectAnswers(
		Integer order,
		Integer mark,
		boolean correct,
		IDistributedTest distributedTest)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("testQuestionOrder", order);
		if (correct)
			criteria.addEqualTo("testQuestionMark", mark);
		else
		{
			criteria.addNotEqualTo("testQuestionMark", mark);
			criteria.addNotEqualTo("testQuestionMark", new Integer(0));
		}

		criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria);
	}

	public int countNumberOfStudents(IDistributedTest distributedTest) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria) / distributedTest.getNumberOfQuestions().intValue();

	}

	public int countStudentTestByStudentAndExecutionCourse(
		IExecutionCourse executionCourse,
		IStudent student)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		criteria.addEqualTo("distributedTest.testScope.className", ExecutionCourse.class.getName());
		criteria.addEqualTo("distributedTest.testScope.keyClass", executionCourse.getIdInternal());
		criteria.addEqualTo("testQuestionOrder", new Integer(1));
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria);

	}

	public void deleteByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
		List studentTestQuestions = queryList(StudentTestQuestion.class, criteria);
		Iterator it = studentTestQuestions.iterator();
		while (it.hasNext())
		{
			delete((IStudentTestQuestion) it.next());
		}
	}

	public void delete(IStudentTestQuestion studentTestQuestion) throws ExcepcaoPersistencia
	{
		super.delete(studentTestQuestion);
	}

}
