/*
 * Created on 25/Jul/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Question;

/**
 * @author Susana Fernandes
 */
public interface IPersistentQuestion extends IPersistentObject {
	public abstract Question readByFileNameAndMetadataId(
		String fileName,
		IMetadata metadata)
		throws ExcepcaoPersistencia;
	public abstract Question readExampleQuestionByMetadata(IMetadata metadata)
		throws ExcepcaoPersistencia;
	public abstract List readByMetadata(IMetadata metadata)
		throws ExcepcaoPersistencia;
	public abstract List readByMetadataAndVisibility(IMetadata metadata)
		throws ExcepcaoPersistencia;
	public abstract void deleteByMetadata(IMetadata metadata)
		throws ExcepcaoPersistencia;
	public abstract void delete(IQuestion question)
		throws ExcepcaoPersistencia;
}