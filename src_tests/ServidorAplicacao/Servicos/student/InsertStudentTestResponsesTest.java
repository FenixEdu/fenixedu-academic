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
public class InsertStudentTestResponsesTest extends ServiceNeedsAuthenticationTestCase {
    public InsertStudentTestResponsesTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/student/testInsertStudentTestResponsesDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertStudentTestResponses";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "L46730", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "D3673", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "D3673", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        String userName = new String("L46730");
        Integer distributedTestId = new Integer(236);
        ResponseNUM response1 = new ResponseNUM();
        response1.setResponse("3.141");
        ResponseNUM response2 = new ResponseNUM();
        response2.setResponse("14");
        ResponseSTR response3 = new ResponseSTR();
        response3.setResponse("Autumn");
        ResponseSTR response4 = new ResponseSTR();
        response4.setResponse("bbb");
        ResponseLID response5 = new ResponseLID();
        response5.setResponse(new String[] { "3", "4" });
        ResponseLID response6 = new ResponseLID();
        response6.setResponse(new String[] { "1", "3", "4" });
        ResponseLID response7 = new ResponseLID();
        response7.setResponse(new String[] { "2" });
        ResponseLID response8 = new ResponseLID();
        response8.setResponse(new String[] { "2", "3", "4" });
        ResponseLID response9 = new ResponseLID();
        response9.setResponse(new String[] { "1" });

        Response[] responses = { response1, response2, response3, response4, response5, response6,
                response7, response8, response9 };
        String path = new String("e:\\eclipse\\workspace\\fenix-patches\\build\\standalone\\ciapl\\");
        Object[] args = { userName, distributedTestId, responses, path };
        return args;
    }

    protected String[] getAuthenticatedAndAuthorizedUserForFenixFormula() {
        String[] args = { "L46730", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndAuthorizedUserForIMSFormula() {
        String[] args = { "L48178", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndAuthorizedUserForInquiry() {
        String[] args = { "L48184", "pass", getApplication() };
        return args;
    }

    protected Object[] getArgsForFenixFormula() {
        String userName = new String("L46730");
        Integer distributedTestId = new Integer(236);
        ResponseNUM response1 = new ResponseNUM();
        response1.setResponse("3.141");
        ResponseNUM response2 = new ResponseNUM();
        response2.setResponse("14");
        ResponseSTR response3 = new ResponseSTR();
        response3.setResponse("Autumn");
        ResponseSTR response4 = new ResponseSTR();
        response4.setResponse("bbb");
        ResponseLID response5 = new ResponseLID();
        response5.setResponse(new String[] { "3", "4" });
        ResponseLID response6 = new ResponseLID();
        response6.setResponse(new String[] { "1", "3", "4" });
        ResponseLID response7 = new ResponseLID();
        response7.setResponse(new String[] { "2" });
        ResponseLID response8 = new ResponseLID();
        response8.setResponse(new String[] { "2", "3", "4" });
        ResponseLID response9 = new ResponseLID();
        response9.setResponse(new String[] { "1" });
        Response[] responses = { response1, response2, response3, response4, response5, response6,
                response7, response8, response9 };
        String path = new String("e:\\eclipse\\workspace\\fenix-patches\\build\\standalone\\ciapl\\");
        Object[] args = { userName, distributedTestId, responses, path };
        return args;
    }

    protected Object[] getArgsForIMSFormula() {
        String userName = new String("L48178");
        Integer distributedTestId = new Integer(237);
        ResponseNUM response1 = new ResponseNUM();
        response1.setResponse("3.14");
        ResponseNUM response2 = new ResponseNUM();
        response2.setResponse("14");
        ResponseSTR response3 = new ResponseSTR();
        response3.setResponse("Aut");
        ResponseSTR response4 = new ResponseSTR();
        response4.setResponse("bbb");
        ResponseLID response5 = new ResponseLID();
        response5.setResponse(new String[] { "2", "3" });
        ResponseLID response6 = new ResponseLID();
        response6.setResponse(new String[] { "1", "2" });
        ResponseLID response7 = new ResponseLID();
        response7.setResponse(new String[] { "5" });
        ResponseLID response8 = new ResponseLID();
        response8.setResponse(new String[] { "3", "4" });
        ResponseLID response9 = new ResponseLID();
        response9.setResponse(new String[] { "1" });
        Response[] responses = { response1, response2, response3, response4, response5, response6,
                response7, response8, response9 };
        String path = new String("e:\\eclipse\\workspace\\fenix-patches\\build\\standalone\\ciapl\\");
        Object[] args = { userName, distributedTestId, responses, path };
        return args;
    }

    protected Object[] getArgsForInquiry() {
        String userName = new String("L48184");
        Integer distributedTestId = new Integer(238);
        ResponseNUM response1 = new ResponseNUM();
        response1.setResponse("3.14");
        ResponseNUM response2 = new ResponseNUM();
        response2.setResponse("14");
        ResponseSTR response3 = new ResponseSTR();
        response3.setResponse("Aut");
        ResponseSTR response4 = new ResponseSTR();
        response4.setResponse("bbb");
        ResponseLID response5 = new ResponseLID();
        response5.setResponse(new String[] { "2", "3" });
        ResponseLID response6 = new ResponseLID();
        response6.setResponse(new String[] { "1", "2" });
        ResponseLID response7 = new ResponseLID();
        response7.setResponse(new String[] { "5" });
        ResponseLID response8 = new ResponseLID();
        response8.setResponse(new String[] { "3", "4" });
        ResponseLID response9 = new ResponseLID();
        response9.setResponse(new String[] { "1" });
        Response[] responses = { response1, response2, response3, response4, response5, response6,
                response7, response8, response9 };
        String path = new String("e:\\eclipse\\workspace\\fenix-patches\\build\\standalone\\ciapl\\");
        Object[] args = { userName, distributedTestId, responses, path };
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
            IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUserForFenixFormula());
            Object[] args1 = getArgsForFenixFormula();
            System.out.println("Test for Fenix Formula.....");

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args1);

            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            //get Student
            Criteria criteria = new Criteria();
            criteria.addEqualTo("person.username", args1[0]);
            QueryByCriteria queryCriteria = new QueryByCriteria(Student.class, criteria);
            IStudent student = ((IStudent) broker.getObjectByQuery(queryCriteria));
            //get StudentTestQuestions
            criteria = new Criteria();
            criteria.addEqualTo("keyDistributedTest", args1[1]);
            criteria.addEqualTo("keyStudent", student.getIdInternal());
            queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
            List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();
            Iterator it = studentTestQuestionList.iterator();
            while (it.hasNext()) {
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
                if (studentTestQuestion.getResponse() == null)
                    fail("InsertStudentTestResponsesTest: response is null");
                XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(studentTestQuestion
                        .getResponse().getBytes()));
                Response r = (Response) decoder.readObject();
                decoder.close();
                double order = studentTestQuestion.getTestQuestionOrder().intValue();
                if (r == null)
                    fail("response = null");
                if (r.isResponsed() == false)
                    fail("response is not responsed");

                if (order == 1) {
                    if (((ResponseNUM) r).getIsCorrect().booleanValue() == false)
                        fail("1 - response incorrect but should be correct");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 4)
                        fail("1 - value incorrect");
                } else if (order == 2) {
                    if (((ResponseNUM) r).getIsCorrect().booleanValue() == true)
                        fail("2 - response is not incorrect but should be");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                        fail("2 - value incorrect");
                } else if (order == 3) {
                    if (((ResponseSTR) r).getIsCorrect().booleanValue() == false)
                        fail("3 - response is incorrect but should be correct");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 4)
                        fail("3 - value incorrect");
                } else if (order == 4) {
                    if (r == null)
                        fail("4 - response = null");
                    if (r.isResponsed() == false)
                        fail("4 - response is not responsed");
                    if (((ResponseSTR) r).getIsCorrect() != null)
                        fail("4 - response is correct, but does´t have correct response");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                        fail("4 - value incorrect");

                } else if (order == 5) {
                    if (((ResponseLID) r).getResponse().length != 2)
                        fail("5 -incorrectResponseNumber");
                    if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == true)
                        fail("5 - response[0] is correct but should be incorrect");
                    if (((ResponseLID) r).getIsCorrect()[1].booleanValue() == false)
                        fail("5 - response[1] is incorrect but should be correct");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                        fail("5 - value incorrect");
                } else if (order == 6) {
                    if (((ResponseLID) r).getResponse().length != 3)
                        fail("6 - incorrectResponseNumber");
                    if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == true)
                        fail("6 - response[0] is correct but should be incorrect");
                    if (((ResponseLID) r).getIsCorrect()[1].booleanValue() == false)
                        fail("6 - response[1] is incorrect but should be correct");
                    if (((ResponseLID) r).getIsCorrect()[2].booleanValue() == false)
                        fail("6 - response[2] is incorrect but should be correct");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 2)
                        fail("6 - value incorrect");
                } else if (order == 7) {
                    if (((ResponseLID) r).getResponse().length != 1)
                        fail("7 - incorrectResponseNumber");
                    if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == false)
                        fail("7 - response[0] is incorrect but should be correct");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 2)
                        fail("7 - value incorrect");
                } else if (order == 8) {
                    if (((ResponseLID) r).getResponse().length != 3)
                        fail("8 - incorrectResponseNumber");
                    if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == true)
                        fail("8 - response[0] is correct but should be incorrect");
                    if (((ResponseLID) r).getIsCorrect()[1].booleanValue() == true)
                        fail("8 - response[1] is correct but should be incorrect");
                    if (((ResponseLID) r).getIsCorrect()[2].booleanValue() == true)
                        fail("8 - response[2] is correct but should be incorrect");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != -2)
                        fail("8 - value incorrect");
                } else if (order == 9) {
                    if (((ResponseLID) r).getResponse().length != 1)
                        fail("9 - incorrectResponseNumber");
                    if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == true)
                        fail("9 - response[0] is correct but should be incorrect");
                    if (!(studentTestQuestion.getTestQuestionMark().doubleValue() < -1 && studentTestQuestion
                            .getTestQuestionMark().doubleValue() > -2))
                        fail("9 - value incorrect");
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
            IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUserForIMSFormula());
            System.out.println("Test for Ims Formula.....");
            Object[] args = getArgsForIMSFormula();
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            //get Student
            Criteria criteria = new Criteria();
            criteria.addEqualTo("person.username", args[0]);
            QueryByCriteria queryCriteria = new QueryByCriteria(Student.class, criteria);
            IStudent student = ((IStudent) broker.getObjectByQuery(queryCriteria));
            //get StudentTestQuestions
            criteria = new Criteria();
            criteria.addEqualTo("keyDistributedTest", args[1]);
            criteria.addEqualTo("keyStudent", student.getIdInternal());
            queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
            List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();
            Iterator it = studentTestQuestionList.iterator();
            while (it.hasNext()) {
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
                if (studentTestQuestion.getResponse() == null)
                    fail("InsertStudentTestResponsesTest: response is null");
                XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(studentTestQuestion
                        .getResponse().getBytes()));
                Response r = (Response) decoder.readObject();
                decoder.close();
                double order = studentTestQuestion.getTestQuestionOrder().intValue();
                if (r == null)
                    fail("response = null");
                if (r.isResponsed() == false)
                    fail("response is not responsed");
                if (order == 1) {
                    if (((ResponseNUM) r).getIsCorrect().booleanValue() == true)
                        fail("1 - response correct but should be incorrect");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != -4)
                        fail("1 - value incorrect");
                } else if (order == 2) {
                    if (((ResponseNUM) r).getIsCorrect().booleanValue() == true)
                        fail("2 - response is not incorrect but should be");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != -4)
                        fail("2 - value incorrect");
                } else if (order == 3) {
                    if (((ResponseSTR) r).getIsCorrect().booleanValue() == true)
                        fail("3 - response is correct but should be incorrect");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                        fail("3 - value incorrect");
                } else if (order == 4) {
                    if (r == null)
                        fail("4 - response = null");
                    if (r.isResponsed() == false)
                        fail("4 - response is not responsed");
                    if (((ResponseSTR) r).getIsCorrect() != null)
                        fail("4 - response is correct, but does´t have correct response");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                        fail("4 - value incorrect");

                } else if (order == 5) {
                    if (((ResponseLID) r).getResponse().length != 2)
                        fail("5 -incorrectResponseNumber");
                    if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == true)
                        fail("5 - response[0] is correct but should be incorrect");
                    if (((ResponseLID) r).getIsCorrect()[1].booleanValue() == false)
                        fail("5 - response[1] is incorrect but should be correct");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                        fail("5 - value incorrect");
                } else if (order == 6) {
                    if (((ResponseLID) r).getResponse().length != 2)
                        fail("6 - incorrectResponseNumber");
                    if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == false)
                        fail("6 - response[0] is incorrect but should be correct");
                    if (((ResponseLID) r).getIsCorrect()[1].booleanValue() == false)
                        fail("6 - response[1] is incorrect but should be correct");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 4)
                        fail("6 - value incorrect");
                } else if (order == 7) {
                    if (((ResponseLID) r).getResponse().length != 1)
                        fail("7 - incorrectResponseNumber");
                    if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == true)
                        fail("7 - response[0] is correct but should be incorrect");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                        fail("7 - value incorrect");
                } else if (order == 8) {
                    if (((ResponseLID) r).getResponse().length != 2)
                        fail("8 - incorrectResponseNumber");
                    if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == true)
                        fail("8 - response[0] is correct but should be incorrect");
                    if (((ResponseLID) r).getIsCorrect()[1].booleanValue() == true)
                        fail("8 - response[1] is correct but should be incorrect");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                        fail("8 - value incorrect");
                } else if (order == 9) {
                    if (((ResponseLID) r).getResponse().length != 1)
                        fail("9 - incorrectResponseNumber");
                    if (((ResponseLID) r).getIsCorrect()[0].booleanValue() == true)
                        fail("9 - response[0] is correct but should be incorrect");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                        fail("9 - value incorrect");
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
            IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUserForInquiry());
            System.out.println("Test for Inquiry Formula.....");
            Object[] args = getArgsForInquiry();
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            //get Student
            Criteria criteria = new Criteria();
            criteria.addEqualTo("person.username", args[0]);
            QueryByCriteria queryCriteria = new QueryByCriteria(Student.class, criteria);
            IStudent student = ((IStudent) broker.getObjectByQuery(queryCriteria));
            //get StudentTestQuestions
            criteria = new Criteria();
            criteria.addEqualTo("keyDistributedTest", args[1]);
            criteria.addEqualTo("keyStudent", student.getIdInternal());
            queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
            List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();
            Iterator it = studentTestQuestionList.iterator();
            while (it.hasNext()) {
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
                if (studentTestQuestion.getResponse() == null)
                    fail("InsertStudentTestResponsesTest: response is null");
                XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(studentTestQuestion
                        .getResponse().getBytes()));
                Response r = (Response) decoder.readObject();
                decoder.close();
                if (r == null)
                    fail("response = null");
                if (r.isResponsed() == false)
                    fail("response is not responsed");
                double order = studentTestQuestion.getTestQuestionOrder().intValue();
                if (order == 1 || order == 2) {
                    if (((ResponseNUM) r).getIsCorrect() != null)
                        fail("isCorrect isn't null");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                        fail("value != 0");
                }
                if (order == 5 || order == 6 || order == 7 || order == 8 || order == 9) {
                    if (((ResponseLID) r).getIsCorrect() != null)
                        fail("isCorrect isn't null");
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                        fail("value != 0");
                }
                if (order == 3 || order == 4) {
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