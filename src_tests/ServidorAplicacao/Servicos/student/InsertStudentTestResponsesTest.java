/*
 * Created on 9/Set/2003
 *  
 */
package ServidorAplicacao.Servicos.student;
import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.Student;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import Util.tests.Response;
import Util.tests.ResponseLID;
import Util.tests.ResponseNUM;
import Util.tests.ResponseSTR;
import framework.factory.ServiceManagerServiceFactory;
/**
 * @author Susana Fernandes
 */
public class InsertStudentTestResponsesTest
		extends
			ServiceNeedsAuthenticationTestCase {
	public InsertStudentTestResponsesTest(String testName) {
		super(testName);
	}
	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/student/testInsertStudentTestResponsesDataSet.xml";
	}
	protected String getNameOfServiceToBeTested() {
		return "InsertStudentTestResponses";
	}
	protected String[] getAuthenticatedAndAuthorizedUser() {
		String[] args = {"L46730", "pass", getApplication()};
		return args;
	}
	protected String[] getAuthenticatedAndUnauthorizedUser() {
		String[] args = {"D3673", "pass", getApplication()};
		return args;
	}
	protected String[] getNotAuthenticatedUser() {
		String[] args = {"D3673", "pass", getApplication()};
		return args;
	}
	protected Object[] getAuthorizeArguments() {
		String userName = new String("L46730");
		Integer distributedTestId = new Integer(254);
		ResponseNUM response1 = new ResponseNUM();
		response1.setResponse("3.141");
		ResponseNUM response2 = new ResponseNUM();
		response2.setResponse("3.142");
		ResponseLID response3 = new ResponseLID();
		response3.setResponse(new String[]{"1"});
		ResponseLID response4 = new ResponseLID();
		response4.setResponse(new String[]{"3"});
		ResponseNUM response5 = new ResponseNUM();
		response5.setResponse("170");
		ResponseNUM response6 = new ResponseNUM();
		response6.setResponse("169");
		ResponseLID response7 = new ResponseLID();
		response7.setResponse(new String[]{"3", "4"});
		ResponseLID response8 = new ResponseLID();
		response8.setResponse(new String[]{"1", "5"});
		ResponseSTR response9 = new ResponseSTR();
		response9.setResponse("169");
		ResponseSTR response10 = new ResponseSTR();
		response10.setResponse("170");
		Response[] responses = {response1, response2, response3, response4,
				response5, response6, response7, response8, response9,
				response10};
		String path = new String(
				"s:\\eclipse-m8\\workspace\\fenix\\build\\standalone\\ciapl\\");
		Object[] args = {userName, distributedTestId, responses, path};
		return args;
	}
	protected Object[] getArgsForFenixFormula() {
		String userName = new String("L46730");
		Integer distributedTestId = new Integer(254);
		ResponseNUM response1 = new ResponseNUM();
		response1.setResponse("3.141");
		ResponseNUM response2 = new ResponseNUM();
		response2.setResponse("3.142");
		ResponseLID response3 = new ResponseLID();
		response3.setResponse(new String[]{"1"});
		ResponseLID response4 = new ResponseLID();
		response4.setResponse(new String[]{"3"});
		ResponseNUM response5 = new ResponseNUM();
		response5.setResponse("170");
		ResponseNUM response6 = new ResponseNUM();
		response6.setResponse("169");
		ResponseLID response7 = new ResponseLID();
		response7.setResponse(new String[]{"3", "4"});
		ResponseLID response8 = new ResponseLID();
		response8.setResponse(new String[]{"1", "5"});
		ResponseSTR response9 = new ResponseSTR();
		response9.setResponse("169");
		ResponseSTR response10 = new ResponseSTR();
		response10.setResponse("170");
		Response[] responses = {response1, response2, response3, response4,
				response5, response6, response7, response8, response9,
				response10};
		String path = new String(
				"s:\\eclipse-m8\\workspace\\fenix\\build\\standalone\\ciapl\\");
		Object[] args = {userName, distributedTestId, responses, path};
		return args;
	}
	protected Object[] getArgsForIMSFormula() {
		String userName = new String("L46730");
		Integer distributedTestId = new Integer(255);
		ResponseNUM response1 = new ResponseNUM();
		response1.setResponse("3.141");
		ResponseNUM response2 = new ResponseNUM();
		response2.setResponse("3.142");
		ResponseLID response3 = new ResponseLID();
		response3.setResponse(new String[]{"1"});
		ResponseLID response4 = new ResponseLID();
		response4.setResponse(new String[]{"3"});
		ResponseNUM response5 = new ResponseNUM();
		response5.setResponse("170");
		ResponseNUM response6 = new ResponseNUM();
		response6.setResponse("169");
		ResponseLID response7 = new ResponseLID();
		response7.setResponse(new String[]{"3", "4"});
		ResponseLID response8 = new ResponseLID();
		response8.setResponse(new String[]{"1", "5"});
		ResponseSTR response9 = new ResponseSTR();
		response9.setResponse("169");
		ResponseSTR response10 = new ResponseSTR();
		response10.setResponse("170");
		Response[] responses = {response1, response2, response3, response4,
				response5, response6, response7, response8, response9,
				response10};
		String path = new String(
				"s:\\eclipse-m8\\workspace\\fenix\\build\\standalone\\ciapl\\");
		Object[] args = {userName, distributedTestId, responses, path};
		return args;
	}
	protected Object[] getArgsForInquiry() {
		String userName = new String("L46730");
		Integer distributedTestId = new Integer(256);
		ResponseNUM response1 = new ResponseNUM();
		response1.setResponse("3.141");
		ResponseNUM response2 = new ResponseNUM();
		response2.setResponse("3.142");
		ResponseLID response3 = new ResponseLID();
		response3.setResponse(new String[]{"1"});
		ResponseLID response4 = new ResponseLID();
		response4.setResponse(new String[]{"3"});
		ResponseNUM response5 = new ResponseNUM();
		response5.setResponse("170");
		ResponseNUM response6 = new ResponseNUM();
		response6.setResponse("169");
		ResponseLID response7 = new ResponseLID();
		response7.setResponse(new String[]{"3", "4"});
		ResponseLID response8 = new ResponseLID();
		response8.setResponse(new String[]{"1", "5"});
		ResponseSTR response9 = new ResponseSTR();
		response9.setResponse("169");
		ResponseSTR response10 = new ResponseSTR();
		response10.setResponse("170");
		Response[] responses = {response1, response2, response3, response4,
				response5, response6, response7, response8, response9,
				response10};
		String path = new String(
				"s:\\eclipse-m8\\workspace\\fenix\\build\\standalone\\ciapl\\");
		Object[] args = {userName, distributedTestId, responses, path};
		return args;
	}
	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}
	public void testSuccessfull() {
		try {
			testFenixFormula();
			testImsFormula();
			testInquiry();
		} catch (Exception e) {
			fail("InsertStudentTestResponsesTest: " + e);
		}
	}
	private boolean testFenixFormula() throws Exception {
		try {
			IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
			System.out.println("Test for Fenix Formula.....");
			Object[] args1 = getArgsForFenixFormula();
			ServiceManagerServiceFactory.executeService(userView,
					getNameOfServiceToBeTested(), args1);
			PersistenceBroker broker = PersistenceBrokerFactory
					.defaultPersistenceBroker();
			//get Student
			Criteria criteria = new Criteria();
			criteria.addEqualTo("person.username", args1[0]);
			QueryByCriteria queryCriteria = new QueryByCriteria(Student.class,
					criteria);
			IStudent student = ((IStudent) broker
					.getObjectByQuery(queryCriteria));
			//get StudentTestQuestions
			criteria = new Criteria();
			criteria.addEqualTo("keyDistributedTest", args1[1]);
			criteria.addEqualTo("keyStudent", student.getIdInternal());
			queryCriteria = new QueryByCriteria(StudentTestQuestion.class,
					criteria);
			List studentTestQuestionList = (List) broker
					.getCollectionByQuery(queryCriteria);
			broker.close();
			Iterator it = studentTestQuestionList.iterator();
			while (it.hasNext()) {
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it
						.next();
				if (studentTestQuestion.getResponse() == null)
					fail("InsertStudentTestResponsesTest: response is null");
				XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(
						studentTestQuestion.getResponse().getBytes()));
				Response r = (Response) decoder.readObject();
				decoder.close();
				double order = studentTestQuestion.getTestQuestionOrder()
						.intValue();
				if (r == null)
					fail("response = null");
				if (r.isResponsed() == false)
					fail("response is not responsed");
				if (order == 1) {
					if (((ResponseNUM) r).getIsCorrect().booleanValue() == false)
						fail("response is not correct but should be");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() != 2)
						fail("value incorrect1");
				}
				if (order == 3) {
					if (((ResponseLID) r).getResponse().length != 1)
						fail("incorrectResponseNumber");
					if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == true)
						fail("response is correct but should be incorrect");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() == 2)
						fail("value incorrect3");
				}
				if (order == 5) {
					if (((ResponseNUM) r).getIsCorrect().booleanValue() == true)
						fail("response is correct but should be incorrect");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() == 2)
						fail("value incorrect5");
				}
				if (order == 7) {
					if (((ResponseLID) r).getResponse().length != 2)
						fail("incorrectResponseNumber");
					if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == false)
						fail("response is incorrect but should be correct");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() != 5)
						fail("value incorrect7");
				}
				if (order == 9) {
					if (((ResponseSTR) r).getIsCorrect().booleanValue() == true)
						fail("response is correct but should be incorrect");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() == 2)
						fail("value incorrect9");
				}
				if (order == 2 || order == 6) {
					if (r == null)
						fail("response = null");
					if (r.isResponsed() == false)
						fail("response is not responsed");
					if (((ResponseNUM) r).getIsCorrect() != null)
						fail("response is correct, but does´t have correct response");
				} else if (order == 4 || order == 8) {
					if (r == null)
						fail("response = null");
					if (r.isResponsed() == false)
						fail("response is not responsed");
					if (((ResponseLID) r).getIsCorrect() != null)
						fail("response is correct, but does´t have correct response");
				} else if (order == 10) {
					if (r == null)
						fail("response = null");
					if (r.isResponsed() == false)
						fail("response is not responsed");
					if (((ResponseSTR) r).getIsCorrect() != null)
						fail("response is correct, but does´t have correct response");
				}
			}
		} catch (FenixServiceException ex) {
			fail("InsertStudentTestResponsesTest -> testFenixFormula:  " + ex);
		} catch (Exception ex) {
			fail("InsertStudentTestResponsesTest -> testFenixFormula:  " + ex);
		}
		return true;
	}
	private boolean testImsFormula() throws Exception {
		try {
			IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
			System.out.println("Test for Ims Formula.....");
			Object[] args = getArgsForIMSFormula();
			ServiceManagerServiceFactory.executeService(userView,
					getNameOfServiceToBeTested(), args);
			PersistenceBroker broker = PersistenceBrokerFactory
					.defaultPersistenceBroker();
			//get Student
			Criteria criteria = new Criteria();
			criteria.addEqualTo("person.username", args[0]);
			QueryByCriteria queryCriteria = new QueryByCriteria(Student.class,
					criteria);
			IStudent student = ((IStudent) broker
					.getObjectByQuery(queryCriteria));
			//get StudentTestQuestions
			criteria = new Criteria();
			criteria.addEqualTo("keyDistributedTest", args[1]);
			criteria.addEqualTo("keyStudent", student.getIdInternal());
			queryCriteria = new QueryByCriteria(StudentTestQuestion.class,
					criteria);
			List studentTestQuestionList = (List) broker
					.getCollectionByQuery(queryCriteria);
			broker.close();
			Iterator it = studentTestQuestionList.iterator();
			while (it.hasNext()) {
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it
						.next();
				if (studentTestQuestion.getResponse() == null)
					fail("InsertStudentTestResponsesTest: response is null");
				XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(
						studentTestQuestion.getResponse().getBytes()));
				Response r = (Response) decoder.readObject();
				decoder.close();
				double order = studentTestQuestion.getTestQuestionOrder()
						.intValue();
				if (r == null)
					fail("response = null");
				if (r.isResponsed() == false)
					fail("response is not responsed");
				if (order == 1) {
					if (((ResponseNUM) r).getIsCorrect() != null)
						fail("isCorrect isn't null");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() != 2)
						fail("value incorrect1");
				}
				if (order == 3) {
					if (((ResponseLID) r).getResponse().length != 1)
						fail("incorrectResponseNumber");
					if (((ResponseLID) r).getIsCorrect() != null)
						fail("isCorrect isn't null");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
						fail("value incorrect3");
				}
				if (order == 5) {
					if (((ResponseNUM) r).getIsCorrect() != null)
						fail("isCorrect isn't null");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() != -2)
						fail("value incorrect5");
				}
				if (order == 7) {
					if (((ResponseLID) r).getResponse().length != 2)
						fail("incorrectResponseNumber");
					if (((ResponseLID) r).getIsCorrect() != null)
						fail("isCorrect isn't null");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
						fail("value incorrect7");
				}
				if (order == 9) {
					if (((ResponseSTR) r).getIsCorrect() != null)
						fail("isCorrect isn't null");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
						fail("value incorrect9");
				}
				if (order == 2 || order == 6) {
					if (r == null)
						fail("response = null");
					if (r.isResponsed() == false)
						fail("response is not responsed");
					if (((ResponseNUM) r).getIsCorrect() != null)
						fail("response is correct, but does´t have correct response");
				} else if (order == 4 || order == 8) {
					if (r == null)
						fail("response = null");
					if (r.isResponsed() == false)
						fail("response is not responsed");
					if (((ResponseLID) r).getIsCorrect() != null)
						fail("response is correct, but does´t have correct response");
				} else if (order == 10) {
					if (r == null)
						fail("response = null");
					if (r.isResponsed() == false)
						fail("response is not responsed");
					if (((ResponseSTR) r).getIsCorrect() != null)
						fail("response is correct, but does´t have correct response");
				}
			}
		} catch (FenixServiceException ex) {
			fail("InsertStudentTestResponsesTest -> testImsFormula:  " + ex);
		} catch (Exception ex) {
			fail("InsertStudentTestResponsesTest -> testImsFormula:  " + ex);
		}
		return true;
	}
	private boolean testInquiry() throws Exception {
		try {
			IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
			System.out.println("Test for Inquiry Formula.....");
			Object[] args = getArgsForInquiry();
			ServiceManagerServiceFactory.executeService(userView,
					getNameOfServiceToBeTested(), args);
			PersistenceBroker broker = PersistenceBrokerFactory
					.defaultPersistenceBroker();
			//get Student
			Criteria criteria = new Criteria();
			criteria.addEqualTo("person.username", args[0]);
			QueryByCriteria queryCriteria = new QueryByCriteria(Student.class,
					criteria);
			IStudent student = ((IStudent) broker
					.getObjectByQuery(queryCriteria));
			//get StudentTestQuestions
			criteria = new Criteria();
			criteria.addEqualTo("keyDistributedTest", args[1]);
			criteria.addEqualTo("keyStudent", student.getIdInternal());
			queryCriteria = new QueryByCriteria(StudentTestQuestion.class,
					criteria);
			List studentTestQuestionList = (List) broker
					.getCollectionByQuery(queryCriteria);
			broker.close();
			Iterator it = studentTestQuestionList.iterator();
			while (it.hasNext()) {
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it
						.next();
				if (studentTestQuestion.getResponse() == null)
					fail("InsertStudentTestResponsesTest: response is null");
				XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(
						studentTestQuestion.getResponse().getBytes()));
				Response r = (Response) decoder.readObject();
				decoder.close();
				if (r == null)
					fail("response = null");
				if (r.isResponsed() == false)
					fail("response is not responsed");
				double order = studentTestQuestion.getTestQuestionOrder()
						.intValue();
				if (order == 1 || order == 2 || order == 5 || order == 6) {
					if (((ResponseNUM) r).getIsCorrect() != null)
						fail("isCorrect isn't null");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
						fail("value != 0");
				}
				if (order == 3 || order == 4 || order == 7 || order == 8) {
					if (((ResponseLID) r).getIsCorrect() != null)
						fail("isCorrect isn't null");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
						fail("value != 0");
				}
				if (order == 9 || order == 10) {
					if (((ResponseSTR) r).getIsCorrect() != null)
						fail("isCorrect isn't null");
					if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
						fail("value != 0");
				}
			}
		} catch (FenixServiceException ex) {
			fail("InsertStudentTestResponsesTest -> testInquiry:  " + ex);
		} catch (Exception ex) {
			fail("InsertStudentTestResponsesTest -> testInquiry:  " + ex);
		}
		return true;
	}
}