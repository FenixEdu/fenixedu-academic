/*
 * Created on 25/Jul/2003
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IMetadata;
import Dominio.Question;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQuestion;

/**
 * @author Susana Fernandes
 */
public class QuestionOJB extends ObjectFenixOJB implements IPersistentQuestion {

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
}
