/*
 * Created on 25/Jul/2003
 *
 */
package ServidorPersistente;

import Dominio.IMetadata;
import Dominio.Question;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public interface IPersistentQuestion  extends IPersistentObject{
	public abstract Question readByFileNameAndMetadataId(
		String fileName,
		IMetadata metadata)
		throws ExcepcaoPersistencia;
}