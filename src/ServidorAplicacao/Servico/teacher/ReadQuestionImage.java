/*
 * Created on 4/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import Dominio.IQuestion;
import Dominio.Question;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadQuestionImage implements IServico {
	private static ReadQuestionImage service = new ReadQuestionImage();
	private String path = new String();

	public static ReadQuestionImage getService() {
		return service;
	}

	public String getNome() {
		return "ReadQuestionImage";
	}

	public String run(Integer exerciseId, Integer imageId, String path)
		throws FenixServiceException {
		this.path = path.replace('\\', '/');
		ISuportePersistente persistentSuport;
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();

			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();

			IQuestion question = new Question(exerciseId);

			question =
				(IQuestion) persistentQuestion.readByOId(question, false);
			ParseQuestion parse = new ParseQuestion();
			String image;
			try {
				image =
					parse.parseQuestionImage(
						question.getXmlFile(),
						imageId.intValue(),
						this.path);
			} catch (Exception e) {
				throw new FenixServiceException(e);
			}
			return image;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
