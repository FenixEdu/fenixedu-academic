/*
 * Created on 27/Ago/2003
 *  
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

import Dominio.DistributedTest;
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
public class StudentTestQuestionOJB extends ObjectFenixOJB implements
		IPersistentStudentTestQuestion {

	public StudentTestQuestionOJB() {
	}

	public List readByStudentAndDistributedTest(IStudent student,
			IDistributedTest distributedTest) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.idInternal", student.getIdInternal());
		criteria.addEqualTo("distributedTest.idInternal", distributedTest
				.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public List readByDistributedTest(IDistributedTest distributedTest)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());

		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, false);

		queryCriteria.addOrderBy("student.number", true);
		queryCriteria.addOrderBy("testQuestionOrder", true);

		return (List) pb.getCollectionByQuery(queryCriteria);
	}

	public List readByStudent(IStudent student) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public List readByQuestion(IQuestion question) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyQuestion", question.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public List readByQuestionAndDistributedTest(IQuestion question,
			IDistributedTest distributedTest) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyQuestion", question.getIdInternal());
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());
		return queryList(StudentTestQuestion.class, criteria);
	}

	public IStudentTestQuestion readByQuestionAndStudentAndDistributedTest(
			IQuestion question, IStudent student,
			IDistributedTest distributedTest) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyQuestion", question.getIdInternal());
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());
		return (IStudentTestQuestion) queryObject(StudentTestQuestion.class,
				criteria);
	}

	public List readStudentsByDistributedTest(IDistributedTest distributedTest)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());
		criteria.addEqualTo("testQuestionOrder", new Integer(1));
		List result = queryList(StudentTestQuestion.class, criteria);
		List studentList = new ArrayList();
		for (int i = 0; i < result.size(); i++) {
			IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) result
					.get(i);
			if (!studentList.contains(studentTestQuestion.getStudent()))
				studentList.add(studentTestQuestion.getStudent());
		}
		return studentList;
	}

	public List readStudentsByDistributedTests(Collection distributedTestsIds)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addIn("keyDistributedTest", distributedTestsIds);
		criteria.addEqualTo("testQuestionOrder", new Integer(1));
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, false);
		queryCriteria.addGroupBy("student.number");
		List result = (List) pb.getCollectionByQuery(queryCriteria);
		lockRead(result);
		List studentList = new ArrayList();
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) iterator
					.next();
			if (!studentList.contains(studentTestQuestion.getStudent()))
				studentList.add(studentTestQuestion.getStudent());
		}
		return studentList;
	}

	public List readStudentTestQuestionsByDistributedTest(
			IDistributedTest distributedTest) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());

		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, false);

		queryCriteria.addOrderBy("keyStudent", true);
		queryCriteria.addOrderBy("testQuestionOrder", true);
		queryCriteria.setEndAtIndex(distributedTest.getNumberOfQuestions()
				.intValue());

		return (List) pb.getCollectionByQuery(queryCriteria);
	}

	public Double getMaximumDistributedTestMark(IDistributedTest distributedTest)
			throws ExcepcaoPersistencia {
		double result = 0;
		List studentTestQuestionList = readStudentTestQuestionsByDistributedTest(distributedTest);
		for (int i = 0; i < studentTestQuestionList.size(); i++)
			result = result
					+ ((IStudentTestQuestion) studentTestQuestionList.get(i))
							.getTestQuestionValue().doubleValue();
		return new Double(result);
	}

	public Double getMaximumDistributedTestMark(Integer distributedTestId)
			throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("idInternal", distributedTestId);
		IDistributedTest distributedTest = (IDistributedTest) queryObject(
				DistributedTest.class, criteria);
		return getMaximumDistributedTestMark(distributedTest);
	}

	public Double readStudentTestFinalMark(Integer distributedTestId,
			Integer studentId) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyDistributedTest", distributedTestId);
		criteria.addEqualTo("keyStudent", studentId);
		List studentTestQuestions = queryList(StudentTestQuestion.class,
				criteria);
		if (studentTestQuestions == null || studentTestQuestions.size() == 0)
			return null;
		Iterator it = studentTestQuestions.iterator();
		Double result = new Double(0);
		while (it.hasNext())
			result = new Double(result.doubleValue()
					+ ((IStudentTestQuestion) it.next()).getTestQuestionMark()
							.doubleValue());
		return result;
	}

	public List readDistributedTestsByTestQuestion(IQuestion question)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyQuestion", question.getIdInternal());
		List studentTestQuestions = queryList(StudentTestQuestion.class,
				criteria);
		Iterator it = studentTestQuestions.iterator();
		List result = new ArrayList();
		while (it.hasNext()) {
			IDistributedTest distributedTest = ((IStudentTestQuestion) it
					.next()).getDistributedTest();
			if (!result.contains(distributedTest))
				result.add(distributedTest);
		}
		return result;
	}

	public int countResponses(Integer order, String response,
			IDistributedTest distributedTest) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("testQuestionOrder", order);
		criteria.addLike("response", response);
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, false);
		int result = pb.getCount(queryCriteria);
		return result;
	}

	public List getResponses(Integer order, IDistributedTest distributedTest)
			throws ExcepcaoPersistencia {
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		Criteria criteria = new Criteria();
		criteria.addEqualTo("testQuestionOrder", order);
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());
		criteria.addNotNull("response");
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, true);
		queryCriteria.addGroupBy("response");
		List result = (List) pb.getCollectionByQuery(queryCriteria);

		List resultList = new ArrayList();
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			resultList.add(((IStudentTestQuestion) iterator.next())
					.getResponse());
		}
		return resultList;
	}

	public int countByResponse(String response, Integer order,
			IDistributedTest distributedTest) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("testQuestionOrder", order);
		criteria.addEqualTo("response", response);
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria);

	}

	public int countResponsedOrNotResponsed(Integer order, boolean responsed,
			IDistributedTest distributedTest) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		if (order != null)
			criteria.addEqualTo("testQuestionOrder", order);
		if (responsed)
			criteria.addNotNull("response");
		else
			criteria.addIsNull("response");
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria);
	}

	public int countCorrectOrIncorrectAnswers(Integer order, Double mark,
			boolean correct, IDistributedTest distributedTest)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("testQuestionOrder", order);
		//criteria.addNotEqualTo("response", null);
		criteria.addNotNull("response");
		if (correct)
			criteria.addGreaterOrEqualThan("testQuestionMark", mark);
		else
			criteria.addLessOrEqualThan("testQuestionMark", new Double(0));

		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria);
	}

	public int countPartiallyCorrectAnswers(Integer order, Double mark,
			IDistributedTest distributedTest) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("testQuestionOrder", order);
		criteria.addNotNull("response");
		criteria.addLessThan("testQuestionMark", mark);
		criteria.addGreaterThan("testQuestionMark", new Integer(0));
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria);
	}

	public int countNumberOfStudents(IDistributedTest distributedTest)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria)
				/ distributedTest.getNumberOfQuestions().intValue();
	}

	public int countStudentTestByStudentAndExecutionCourse(
			IExecutionCourse executionCourse, IStudent student)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		criteria.addEqualTo("distributedTest.testScope.className",
				ExecutionCourse.class.getName());
		criteria.addEqualTo("distributedTest.testScope.keyClass",
				executionCourse.getIdInternal());
		criteria.addEqualTo("testQuestionOrder", new Integer(1));
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria);
	}

	public int countByQuestion(IQuestion question) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyQuestion", question.getIdInternal());
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction())
				.getBroker();
		QueryByCriteria queryCriteria = new QueryByCriteria(
				StudentTestQuestion.class, criteria, false);
		return pb.getCount(queryCriteria);
	}

	public void deleteByDistributedTest(IDistributedTest distributedTest)
			throws ExcepcaoPersistencia {
//		Calendar start = Calendar.getInstance();
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyDistributedTest", distributedTest
				.getIdInternal());
		List studentTestQuestions = queryList(StudentTestQuestion.class,
				criteria);
		Iterator it = studentTestQuestions.iterator();
		while (it.hasNext()) {
			delete((IStudentTestQuestion) it.next());
		}
	}

	public void delete(IStudentTestQuestion studentTestQuestion)
			throws ExcepcaoPersistencia {
		super.delete(studentTestQuestion);
	}
}