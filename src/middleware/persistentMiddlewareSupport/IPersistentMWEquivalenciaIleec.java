package middleware.persistentMiddlewareSupport;

import java.util.List;

import middleware.middlewareDomain.MWEquivalenciaIleec;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos
 * 3/Dez/2003
 */

public interface IPersistentMWEquivalenciaIleec
{
	public List readAll() throws ExcepcaoPersistencia;
	public MWEquivalenciaIleec readByTipoEquivalenciaAndCodigoDisciplinaCurriculoAntigo(Integer tipoEquivalencia, String codigoDisciplinaCurriculoAntigo) throws ExcepcaoPersistencia;
	public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia;
	public Integer countAll();
}