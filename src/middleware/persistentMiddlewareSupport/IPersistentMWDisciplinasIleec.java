package middleware.persistentMiddlewareSupport;

import java.util.ArrayList;
import java.util.List;

import middleware.middlewareDomain.MWDisciplinaIleec;
import middleware.middlewareDomain.MWGruposIleec;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos
 * 3/Dez/2003
 */

public interface IPersistentMWDisciplinasIleec
{
	public List readAll() throws ExcepcaoPersistencia;
	public MWDisciplinaIleec readByCodigoDisciplina(String codigoDisciplina) throws ExcepcaoPersistencia;
	public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia;
	public Integer countAll();
	public ArrayList readByGroup(MWGruposIleec grupoILeec)throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
}