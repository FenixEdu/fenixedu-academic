/*
 * Created on 3/Set/2003
 */

package ServidorAplicacao.Servico.student;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoQuestion;
import DataBeans.InfoSiteStudentTestFeedback;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.IOnlineTest;
import Dominio.IStudent;
import Dominio.IStudentTestLog;
import Dominio.IStudentTestQuestion;
import Dominio.Mark;
import Dominio.StudentTestLog;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestLog;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.CardinalityType;
import Util.tests.CorrectionFormula;
import Util.tests.QuestionType;
import Util.tests.RenderFIB;
import Util.tests.Response;
import Util.tests.ResponseCondition;
import Util.tests.ResponseLID;
import Util.tests.ResponseNUM;
import Util.tests.ResponseProcessing;
import Util.tests.ResponseSTR;
import Util.tests.TestType;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class InsertStudentTestResponses implements IService {

	public InsertStudentTestResponses() {
	}

	private String path = new String();

	public InfoSiteStudentTestFeedback run(String userName,
			Integer distributedTestId, Response[] response, String path)
			throws FenixServiceException {
		List infoStudentTestQuestionList = new ArrayList();
		InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = new InfoSiteStudentTestFeedback();
		this.path = path.replace('\\', '/');
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB
					.getInstance();
			IStudent student = persistentSuport.getIPersistentStudent()
					.readByUsername(userName);
			if (student == null)
				throw new FenixServiceException();
			IDistributedTest distributedTest = (IDistributedTest) persistentSuport
					.getIPersistentDistributedTest().readByOID(
							DistributedTest.class, distributedTestId);
			if (distributedTest == null)
				throw new FenixServiceException();
			String event = new String("Submeter Teste;");
			for (int questionNumber = 0; questionNumber < response.length; questionNumber++) {
				if (response[questionNumber] instanceof ResponseLID) {
					if (((ResponseLID) response[questionNumber]).getResponse() != null) {
						for (int responseNumber = 0; responseNumber < ((ResponseLID) response[questionNumber])
								.getResponse().length; responseNumber++) {
							if (responseNumber != 0)
								event = event.concat(",");
							event = event
									.concat(((ResponseLID) response[questionNumber])
											.getResponse()[responseNumber]);
						}
					}
				} else if (response[questionNumber] instanceof ResponseSTR) {
					event = event
							.concat(((ResponseSTR) response[questionNumber])
									.getResponse());
				} else if (response[questionNumber] instanceof ResponseNUM) {
					event = event
							.concat(((ResponseNUM) response[questionNumber])
									.getResponse());
				}
				event = event.concat(";");
			}

			double totalMark = 0;
			int responseNumber = 0;
			int notResponseNumber = 0;
			List errors = new ArrayList();

			if (compareDates(distributedTest.getEndDate(), distributedTest
					.getEndHour())) {
				List studentTestQuestionList = persistentSuport
						.getIPersistentStudentTestQuestion()
						.readByStudentAndDistributedTest(student,
								distributedTest);
				Iterator it = studentTestQuestionList.iterator();
				if (studentTestQuestionList.size() == 0)
					return null;
				IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
						.getIPersistentStudentTestQuestion();
				ParseQuestion parse = new ParseQuestion();

				while (it.hasNext()) {
					IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it
							.next();
					persistentStudentTestQuestion
							.lockWrite(studentTestQuestion);

					InfoStudentTestQuestion infoStudentTestQuestion = Cloner
							.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);
					infoStudentTestQuestion
							.setResponse(response[studentTestQuestion
									.getTestQuestionOrder().intValue() - 1]);

					if (infoStudentTestQuestion.getResponse().isResponsed()) {
						responseNumber++;
						if (studentTestQuestion.getResponse() != null
								&& distributedTest.getTestType().getType()
										.intValue() == TestType.EVALUATION) {
							//não pode aceitar nova resposta
						} else {
							try {
								infoStudentTestQuestion = parse
										.parseStudentTestQuestion(
												infoStudentTestQuestion,
												this.path);
								infoStudentTestQuestion
										.setQuestion(correctQuestionValues(
												infoStudentTestQuestion
														.getQuestion(),
												new Double(
														infoStudentTestQuestion
																.getTestQuestionValue()
																.doubleValue())));
							} catch (Exception e) {
								throw new FenixServiceException(e);
							}

							String error = validResponse(infoStudentTestQuestion);
							if (error == null) {
								if ((!distributedTest.getTestType().equals(
										new TestType(TestType.INQUIRY)))
										&& infoStudentTestQuestion
												.getQuestion()
												.getResponseProcessingInstructions()
												.size() != 0) {

									infoStudentTestQuestion = getMark(infoStudentTestQuestion);
								}
								totalMark += infoStudentTestQuestion
										.getTestQuestionMark().doubleValue();
								ByteArrayOutputStream out = new ByteArrayOutputStream();
								XMLEncoder encoder = new XMLEncoder(out);
								encoder.writeObject(infoStudentTestQuestion
										.getResponse());
								encoder.close();
								studentTestQuestion.setResponse(out.toString());
								studentTestQuestion
										.setTestQuestionMark(infoStudentTestQuestion
												.getTestQuestionMark());

							} else {
								notResponseNumber++;
								responseNumber--;
								errors.add(error);
							}
						}
					} else
						notResponseNumber++;
				}
				if (distributedTest.getTestType().equals(
						new TestType(TestType.EVALUATION))) {
					IOnlineTest onlineTest = (IOnlineTest) persistentSuport
							.getIPersistentOnlineTest().readByDistributedTest(
									distributedTest);
					IFrequenta attend = persistentSuport
							.getIFrequentaPersistente()
							.readByAlunoAndDisciplinaExecucao(
									student,
									(IExecutionCourse) distributedTest
											.getTestScope().getDomainObject());
					IMark mark = persistentSuport.getIPersistentMark().readBy(
							onlineTest, attend);

					if (mark == null) {
						mark = new Mark();
						mark.setAttend(attend);
						mark.setEvaluation(onlineTest);
					}
					mark.setMark(new java.text.DecimalFormat("#0.##")
							.format(totalMark));
					persistentSuport.getIPersistentMark().simpleLockWrite(mark);
				}

				IPersistentStudentTestLog persistentStudentTestLog = persistentSuport
						.getIPersistentStudentTestLog();
				IStudentTestLog studentTestLog = new StudentTestLog();
				studentTestLog.setDistributedTest(distributedTest);
				studentTestLog.setStudent(student);
				studentTestLog.setDate(Calendar.getInstance().getTime());
				studentTestLog.setEvent(event);
				persistentStudentTestLog.simpleLockWrite(studentTestLog);
			} else
				return null;
			infoSiteStudentTestFeedback.setResponseNumber(new Integer(
					responseNumber));
			infoSiteStudentTestFeedback.setNotResponseNumber(new Integer(
					notResponseNumber));
			infoSiteStudentTestFeedback.setErrors(errors);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoSiteStudentTestFeedback;
	}

	private boolean compareDates(Calendar date, Calendar hour) {
		Calendar calendar = Calendar.getInstance();
		CalendarDateComparator dateComparator = new CalendarDateComparator();
		CalendarHourComparator hourComparator = new CalendarHourComparator();
		if (dateComparator.compare(calendar, date) <= 0) {
			if (dateComparator.compare(calendar, date) == 0)
				if (hourComparator.compare(calendar, hour) <= 0)
					return true;
				else
					return false;
			return true;
		}
		return false;
	}

	private InfoStudentTestQuestion getMark(
			InfoStudentTestQuestion infoStudentTestQuestion) {
		if (infoStudentTestQuestion.getCorrectionFormula().getFormula()
				.intValue() == CorrectionFormula.IMS) {
			if ((infoStudentTestQuestion.getQuestion().getQuestionType()
					.getType().intValue() == QuestionType.LID)
					&& (infoStudentTestQuestion.getQuestion().getQuestionType()
							.getCardinalityType().getType().intValue() == CardinalityType.MULTIPLE)) {
				List questionCorrectionList = infoStudentTestQuestion
						.getQuestion().getResponseProcessingInstructions();
				Iterator questionCorrectionIt = questionCorrectionList
						.iterator();
				for (int i = 0; questionCorrectionIt.hasNext(); i++) {
					ResponseProcessing responseProcessing = (ResponseProcessing) questionCorrectionIt
							.next();
					if (responseProcessing
							.isAllCorrect(((ResponseLID) infoStudentTestQuestion
									.getResponse()).getResponse())) {
						infoStudentTestQuestion
								.setTestQuestionMark(responseProcessing
										.getResponseValue());
						ResponseLID r = (ResponseLID) infoStudentTestQuestion
								.getResponse();
						r.setResponseProcessingIndex(new Integer(i));
						infoStudentTestQuestion.setResponse(r);
						return infoStudentTestQuestion;
					}
				}
			} else if (infoStudentTestQuestion.getQuestion().getQuestionType()
					.getType().intValue() == QuestionType.LID
					&& infoStudentTestQuestion.getQuestion().getQuestionType()
							.getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
				List questionCorrectionList = infoStudentTestQuestion
						.getQuestion().getResponseProcessingInstructions();
				Iterator questionCorrectionIt = questionCorrectionList
						.iterator();
				for (int i = 0; questionCorrectionIt.hasNext(); i++) {
					ResponseProcessing responseProcessing = (ResponseProcessing) questionCorrectionIt
							.next();
					if (responseProcessing.isAllCorrect(new String(
							((ResponseLID) infoStudentTestQuestion
									.getResponse()).getResponse()[0]))) {
						infoStudentTestQuestion
								.setTestQuestionMark(responseProcessing
										.getResponseValue());
						ResponseLID r = (ResponseLID) infoStudentTestQuestion
								.getResponse();
						r.setResponseProcessingIndex(new Integer(i));
						infoStudentTestQuestion.setResponse(r);
						return infoStudentTestQuestion;
					}
				}
			} else if ((infoStudentTestQuestion.getQuestion().getQuestionType()
					.getType().intValue() == QuestionType.STR)
					|| (infoStudentTestQuestion.getQuestion().getQuestionType()
							.getType().intValue() == QuestionType.LID && (infoStudentTestQuestion
							.getQuestion().getQuestionType()
							.getCardinalityType().getType().intValue() == CardinalityType.SINGLE))) {
				List questionCorrectionList = infoStudentTestQuestion
						.getQuestion().getResponseProcessingInstructions();
				Iterator questionCorrectionIt = questionCorrectionList
						.iterator();
				for (int i = 0; questionCorrectionIt.hasNext(); i++) {
					ResponseProcessing responseProcessing = (ResponseProcessing) questionCorrectionIt
							.next();
					if (responseProcessing.isAllCorrect(new String(
							((ResponseSTR) infoStudentTestQuestion
									.getResponse()).getResponse()))) {
						infoStudentTestQuestion
								.setTestQuestionMark(responseProcessing
										.getResponseValue());
						ResponseSTR r = (ResponseSTR) infoStudentTestQuestion
								.getResponse();
						r.setResponseProcessingIndex(new Integer(i));
						infoStudentTestQuestion.setResponse(r);

						return infoStudentTestQuestion;
					}
				}
			} else if (infoStudentTestQuestion.getQuestion().getQuestionType()
					.getType().intValue() == QuestionType.NUM) {
				List questionCorrectionList = infoStudentTestQuestion
						.getQuestion().getResponseProcessingInstructions();
				Iterator questionCorrectionIt = questionCorrectionList
						.iterator();
				for (int i = 0; questionCorrectionIt.hasNext(); i++) {
					ResponseProcessing responseProcessing = (ResponseProcessing) questionCorrectionIt
							.next();
					if (responseProcessing.isAllCorrect(new Double(
							((ResponseNUM) infoStudentTestQuestion
									.getResponse()).getResponse()))) {
						infoStudentTestQuestion
								.setTestQuestionMark(responseProcessing
										.getResponseValue());
						ResponseNUM r = (ResponseNUM) infoStudentTestQuestion
								.getResponse();
						r.setResponseProcessingIndex(new Integer(i));
						infoStudentTestQuestion.setResponse(r);

						return infoStudentTestQuestion;
					}
				}
			}
		}

		else if (infoStudentTestQuestion.getCorrectionFormula().getFormula()
				.intValue() == CorrectionFormula.FENIX) {
			Integer fenixCorrectResponseIndex = getFenixCorrectResponseIndex(infoStudentTestQuestion
					.getQuestion().getResponseProcessingInstructions());

			if (infoStudentTestQuestion.getQuestion().getQuestionType()
					.getType().intValue() == QuestionType.LID) {
				List correctResponseList = getFenixCorrectResponse4LID(
						infoStudentTestQuestion.getQuestion()
								.getResponseProcessingInstructions(),
						fenixCorrectResponseIndex);
				if (infoStudentTestQuestion.getQuestion().getQuestionType()
						.getCardinalityType().getType().intValue() == CardinalityType.MULTIPLE) {
					//2*(correctOptionsChosen+wrongOptionNotChosen)/allOptionNumber-1

					int allOptionNumber = infoStudentTestQuestion.getQuestion()
							.getOptionNumber().intValue();
					int correctOptionNumber = correctResponseList.size();
					int wrongOptionsNumber = allOptionNumber
							- correctOptionNumber;
					int correctOptionsChosen = 0;
					int wrongOptionChosen = 0;
					Boolean[] isCorrect = new Boolean[((ResponseLID) infoStudentTestQuestion
							.getResponse()).getResponse().length];
					for (int i = 0; i < ((ResponseLID) infoStudentTestQuestion
							.getResponse()).getResponse().length; i++) {
						Iterator responseConditionIt = correctResponseList
								.iterator();
						boolean correct = false;
						while (responseConditionIt.hasNext()) {
							if (((ResponseCondition) responseConditionIt.next())
									.isCorrect(new String(
											((ResponseLID) infoStudentTestQuestion
													.getResponse())
													.getResponse()[i]))) {
								correct = true;
								break;
							}
						}
						isCorrect[i] = new Boolean(correct);
						if (correct)
							correctOptionsChosen++;
						else
							wrongOptionChosen++;
					}
					int wrongOptionNotChosen = wrongOptionsNumber
							- wrongOptionChosen;

					if (allOptionNumber > 1) {
						infoStudentTestQuestion
								.setTestQuestionMark(new Double(
										infoStudentTestQuestion
												.getTestQuestionValue()
												.doubleValue()
												* (2
														* (correctOptionsChosen + wrongOptionNotChosen)
														* (java.lang.Math
																.pow(
																		allOptionNumber,
																		-1)) - 1)));
					} else {
						infoStudentTestQuestion.setTestQuestionMark(new Double(
								infoStudentTestQuestion.getTestQuestionValue()
										.doubleValue()
										* correctOptionsChosen));
					}
					ResponseLID r = (ResponseLID) infoStudentTestQuestion
							.getResponse();
					r.setIsCorrect(isCorrect);
					infoStudentTestQuestion.setResponse(r);
					return infoStudentTestQuestion;

				} else if (infoStudentTestQuestion.getQuestion()
						.getQuestionType().getCardinalityType().getType()
						.intValue() == CardinalityType.SINGLE) {
					//(1/num_op)-1
					if (isCorrect(correctResponseList, new String(
							((ResponseLID) infoStudentTestQuestion
									.getResponse()).getResponse()[0]))) {

						infoStudentTestQuestion.setTestQuestionMark(new Double(
								infoStudentTestQuestion.getTestQuestionValue()
										.doubleValue()));
						ResponseLID r = (ResponseLID) infoStudentTestQuestion
								.getResponse();
						r.setIsCorrect(new Boolean[] { new Boolean(true) });
						infoStudentTestQuestion.setResponse(r);
					} else {
						if (infoStudentTestQuestion.getQuestion()
								.getOptionNumber().intValue() > 1) {
							infoStudentTestQuestion
									.setTestQuestionMark(new Double(
											-(infoStudentTestQuestion
													.getTestQuestionValue()
													.intValue() * (java.lang.Math
													.pow(
															infoStudentTestQuestion
																	.getQuestion()
																	.getOptionNumber()
																	.intValue() - 1,
															-1)))));
						} else
							infoStudentTestQuestion
									.setTestQuestionMark(new Double(0));
						ResponseLID r = (ResponseLID) infoStudentTestQuestion
								.getResponse();
						r.setIsCorrect(new Boolean[] { new Boolean(false) });
						infoStudentTestQuestion.setResponse(r);
					}
					return infoStudentTestQuestion;
				}
			} else if (infoStudentTestQuestion.getQuestion().getQuestionType()
					.getType().intValue() == QuestionType.STR) {
				List correctResponseList = ((ResponseProcessing) infoStudentTestQuestion
						.getQuestion().getResponseProcessingInstructions().get(
								fenixCorrectResponseIndex.intValue()))
						.getResponseConditions();

				if (isCorrect(correctResponseList, new String(
						((ResponseSTR) infoStudentTestQuestion.getResponse())
								.getResponse()))) {
					infoStudentTestQuestion.setTestQuestionMark(new Double(
							infoStudentTestQuestion.getTestQuestionValue()
									.doubleValue()));
					ResponseSTR r = (ResponseSTR) infoStudentTestQuestion
							.getResponse();
					r.setIsCorrect(new Boolean(true));
					infoStudentTestQuestion.setResponse(r);
				} else {
					infoStudentTestQuestion.setTestQuestionMark(new Double(0));
					ResponseSTR r = (ResponseSTR) infoStudentTestQuestion
							.getResponse();
					r.setIsCorrect(new Boolean(false));
					infoStudentTestQuestion.setResponse(r);
				}
				return infoStudentTestQuestion;
			} else if (infoStudentTestQuestion.getQuestion().getQuestionType()
					.getType().intValue() == QuestionType.NUM) {
				List correctResponseList = ((ResponseProcessing) infoStudentTestQuestion
						.getQuestion().getResponseProcessingInstructions().get(
								fenixCorrectResponseIndex.intValue()))
						.getResponseConditions();
				if (isCorrect(correctResponseList, new Double(
						((ResponseNUM) infoStudentTestQuestion.getResponse())
								.getResponse()))) {
					infoStudentTestQuestion.setTestQuestionMark(new Double(
							infoStudentTestQuestion.getTestQuestionValue()
									.doubleValue()));
					ResponseNUM r = (ResponseNUM) infoStudentTestQuestion
							.getResponse();
					r.setIsCorrect(new Boolean(true));
					infoStudentTestQuestion.setResponse(r);
				} else {
					infoStudentTestQuestion.setTestQuestionMark(new Double(0));
					ResponseNUM r = (ResponseNUM) infoStudentTestQuestion
							.getResponse();
					r.setIsCorrect(new Boolean(false));
					infoStudentTestQuestion.setResponse(r);
				}
				return infoStudentTestQuestion;
			}

		}
		infoStudentTestQuestion.setTestQuestionMark(new Double(0));
		return infoStudentTestQuestion;
	}

	private String validResponse(InfoStudentTestQuestion infoStudentTestQuestion) {

		if (infoStudentTestQuestion.getQuestion().getQuestionType().getRender() instanceof RenderFIB) {
			RenderFIB renderFIB = (RenderFIB) infoStudentTestQuestion
					.getQuestion().getQuestionType().getRender();
			try {
				if (renderFIB.getFibtype().intValue() == RenderFIB.INTEGER_CODE)
					new Integer(((ResponseNUM) infoStudentTestQuestion
							.getResponse()).getResponse());
				else if (renderFIB.getFibtype().intValue() == RenderFIB.DECIMAL_CODE) {
					new Double(((ResponseNUM) infoStudentTestQuestion
							.getResponse()).getResponse());
				}
			} catch (NumberFormatException ex) {
				return new String("Pergunta "
						+ infoStudentTestQuestion.getTestQuestionOrder()
								.toString() + ": Formato de resposta inválido");
			}
		}
		return null;
	}

	private InfoQuestion correctQuestionValues(InfoQuestion infoQuestion,
			Double questionValue) {
		Double maxValue = new Double(0);

		Iterator it = infoQuestion.getResponseProcessingInstructions()
				.iterator();
		while (it.hasNext()) {
			ResponseProcessing responseProcessing = (ResponseProcessing) it
					.next();
			if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
					|| responseProcessing.getAction().intValue() == ResponseProcessing.ADD)
				if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0)
					maxValue = responseProcessing.getResponseValue();
		}
		if (maxValue.compareTo(questionValue) != 0) {
			it = infoQuestion.getResponseProcessingInstructions().iterator();
			double difValue = questionValue.doubleValue()
					* Math.pow(maxValue.doubleValue(), -1);

			while (it.hasNext()) {
				ResponseProcessing responseProcessing = (ResponseProcessing) it
						.next();

				responseProcessing.setResponseValue(new Double(
						responseProcessing.getResponseValue().doubleValue()
								* difValue));
			}
		}

		return infoQuestion;
	}

	private boolean isCorrect(List correctResponse, String studentResponse) {
		Iterator it = correctResponse.iterator();
		boolean match = false;
		while (it.hasNext()) {
			ResponseCondition responseCondition = (ResponseCondition) it.next();
			if (responseCondition.isCorrect(studentResponse))
				match = true;
			else
				return false;
		}
		if (!match)
			return false;
		return true;
	}

	private boolean isCorrect(List correctResponse, Double studentResponse) {
		Iterator it = correctResponse.iterator();
		boolean match = false;
		while (it.hasNext()) {
			ResponseCondition responseCondition = (ResponseCondition) it.next();
			if (responseCondition.isCorrect(studentResponse))
				match = true;
			else
				return false;
		}
		if (!match)
			return false;
		return true;
	}

	public List getFenixCorrectResponse4LID(List responseProcessingList,
			Integer index) {

		List oneResponse = new ArrayList();
		ResponseProcessing responseProcessing = (ResponseProcessing) responseProcessingList
				.get(index.intValue());

		if (responseProcessing.isFenixCorrectResponse()) {
			oneResponse = new ArrayList();
			Iterator itResponseCondition = responseProcessing
					.getResponseConditions().iterator();
			while (itResponseCondition.hasNext()) {
				ResponseCondition rc = (ResponseCondition) itResponseCondition
						.next();

				if (rc != null
						&& rc.getCondition().intValue() == ResponseCondition.VAREQUAL)
					oneResponse.add(rc);

			}
		}

		return oneResponse;
	}

	public Integer getFenixCorrectResponseIndex(List responseProcessingList) {
		Iterator itResponseProcessing = responseProcessingList.iterator();
		List oneResponse = new ArrayList();
		for (int i = 0; itResponseProcessing.hasNext(); i++) {
			if (((ResponseProcessing) itResponseProcessing.next())
					.isFenixCorrectResponse()) {
				return new Integer(i);
			}
		}
		return null;
	}
}