package middleware.persistentMiddlewareSupport;

import java.util.ArrayList;
import java.util.List;

import middleware.middlewareDomain.MWDisciplinaIleec;
import middleware.middlewareDomain.MWGrupoIleec;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author David Santos
 * 3/Dez/2003
 */

public interface IPersistentMWDisciplinaIleec extends IPersistentObject
{
	public List readAll() throws ExcepcaoPersistencia;
	public MWDisciplinaIleec readByCodigoDisciplina(String codigoDisciplina) throws ExcepcaoPersistencia;
	public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia;
	public Integer countAll();
	public ArrayList readByGroup(MWGrupoIleec grupoILeec)throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	public MWDisciplinaIleec readByIdInternal(Integer idInternal) throws ExcepcaoPersistencia;
}