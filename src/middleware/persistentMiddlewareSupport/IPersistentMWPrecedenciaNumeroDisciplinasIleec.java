/*
 * Created on 18/Dez/2002
 *
 */
package middleware.persistentMiddlewareSupport;

import java.util.List;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */
public interface IPersistentMWPrecedenciaNumeroDisciplinasIleec
{
	public List readAll() throws ExcepcaoPersistencia;
}
