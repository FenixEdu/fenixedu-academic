package middleware.persistentMiddlewareSupport;

import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos
 * 3/Dez/2003
 */

public interface IPersistentMWDisciplinasIleec
{
	public List readAll() throws ExcepcaoPersistencia;
	public List readByCodigoDisciplina(String codigoDisciplina) throws ExcepcaoPersistencia;
	public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia;
	public Integer countAll();
}