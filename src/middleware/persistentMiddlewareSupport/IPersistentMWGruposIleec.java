/*
 * Created on 4/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport;

import java.util.List;

import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import Dominio.CurricularCourseGroup;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentMWGruposIleec
{
	public List readAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	public List readByFenixGroup(CurricularCourseGroup ccg)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	public List readByAreaName(String areaName)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
}
