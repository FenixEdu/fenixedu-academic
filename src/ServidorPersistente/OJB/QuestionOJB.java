/*
 * Created on 25/Jul/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Question;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQuestion;

/**
 * @author Susana Fernandes
 */
public class QuestionOJB
	extends ObjectFenixOJB
	implements IPersistentQuestion {

	public QuestionOJB() {
	}

	public Question readByFileNameAndMetadataId(
		String fileName,
		IMetadata metadata)
		throws ExcepcaoPersistencia {
		Question result = null;
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
		criteria.addEqualTo("xmlFileName", fileName);
		return (Question) queryObject(Question.class, criteria);
	}

	public Question readExampleQuestionByMetadata(IMetadata metadata)
		throws ExcepcaoPersistencia {
		Question result = null;
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyMetadata", metadata.getIdInternal());

		List questions = queryList(Question.class, criteria);
		if (questions != null && questions.size() != 0)
			result = (Question) questions.get(0);

		return result;
	}

	public List readByMetadata(IMetadata metadata)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
		return queryList(Question.class, criteria);
	}

	public void delete(IQuestion question) throws ExcepcaoPersistencia {
		super.delete(question);
	}
}
