/*
 * Created on 29/Jan/2004
 */
package ServidorAplicacao;
import java.util.Iterator;
import java.util.List;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;
/**
 * @author Susana Fernandes
 */
public class UpdateTestsMarks {
	public static void main(String[] args) throws Exception {
		ISuportePersistente persistentSuport = SuportePersistenteOJB
				.getInstance();
		Integer[] distributedTestIds = {new Integer(215), new Integer(217),
				new Integer(219), new Integer(221), new Integer(223),
				new Integer(225), new Integer(227), new Integer(229),
				new Integer(231), new Integer(233), new Integer(235)};
		for (int i = 0; i < distributedTestIds.length; i++) {
			persistentSuport.iniciarTransaccao();
			//String path = new String(
			//	"E:\\eclipse-M7\\workspace\\fenix\\build\\standalone\\ciapl\\");
			String path = new String("/opt/jakarta/tomcat/webapps/fenix/");
			System.out.println("distributedTestId: " + distributedTestIds[i]);
			IDistributedTest distributedTest = (IDistributedTest) persistentSuport
					.getIPersistentDistributedTest().readByOId(
							new DistributedTest(distributedTestIds[i]), false);
			if (distributedTest == null)
				throw new InvalidArgumentsServiceException();
			persistentSuport.confirmarTransaccao();
			persistentSuport.iniciarTransaccao();
			IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
					.getIPersistentStudentTestQuestion();
			List studentTestQuestionList = persistentStudentTestQuestion
					.readByDistributedTest(distributedTest);
			Iterator it = studentTestQuestionList.iterator();
			while (it.hasNext()) {
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it
						.next();
				if (studentTestQuestion.getResponse().intValue() != 0) {
					persistentStudentTestQuestion
							.simpleLockWrite(studentTestQuestion);
					Double questionMark = new Double(0);
					InfoStudentTestQuestion infoStudentTestQuestion = Cloner
							.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);
					try {
						ParseQuestion parse = new ParseQuestion();
						infoStudentTestQuestion.setQuestion(parse
								.parseQuestion(infoStudentTestQuestion
										.getQuestion().getXmlFile(),
										infoStudentTestQuestion.getQuestion(),
										path));
						infoStudentTestQuestion = parse.getOptionsShuffle(
								infoStudentTestQuestion, path);
					} catch (Exception e) {
						throw new FenixServiceException(e);
					}
					if (infoStudentTestQuestion.getQuestion()
							.getCorrectResponse().contains(
									studentTestQuestion.getResponse()))
						questionMark = new Double(studentTestQuestion
								.getTestQuestionValue().doubleValue());
					else
						questionMark = new Double(
								-(infoStudentTestQuestion
										.getTestQuestionValue().intValue() * (java.lang.Math
										.pow(
												infoStudentTestQuestion
														.getQuestion()
														.getOptionNumber()
														.intValue() - 1, -1))));
					studentTestQuestion.setTestQuestionMark(questionMark);
				}
			}
			persistentSuport.confirmarTransaccao();
			System.out.println("Acabou para o distributedTestId: "
					+ distributedTestIds[i]);
		}
		System.out.println("Acabou");
	}
}