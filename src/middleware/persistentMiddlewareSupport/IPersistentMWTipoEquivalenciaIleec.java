package middleware.persistentMiddlewareSupport;

import java.util.List;

import middleware.middlewareDomain.MWTipoEquivalenciaIleec;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos
 * 3/Dez/2003
 */

public interface IPersistentMWTipoEquivalenciaIleec
{
	public List readAll() throws ExcepcaoPersistencia;
	public MWTipoEquivalenciaIleec readByTipoEquivalencia(Integer tipoEquivalencia) throws ExcepcaoPersistencia;
	public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia;
	public Integer countAll();
}