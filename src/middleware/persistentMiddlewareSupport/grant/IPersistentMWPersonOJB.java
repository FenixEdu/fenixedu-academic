/*
 * Created on Feb 19, 2004
 */
package middleware.persistentMiddlewareSupport.grant;

import java.util.List;

import middleware.middlewareDomain.grant.MWPerson;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author pica
 * @author barbosa
 */
public interface IPersistentMWPersonOJB
{
	public List readAll() throws ExcepcaoPersistencia;
	public MWPerson readByIDNumberAndIDType(String idNumber,Integer idType) throws ExcepcaoPersistencia;
    public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia;
    public Integer countAll();
}