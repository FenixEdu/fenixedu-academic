/*
 * Created on Feb 19, 2004
 */

package middleware.persistentMiddlewareSupport.grant;

import java.util.List;

import middleware.middlewareDomain.grant.MWTeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
/**
 * @author pica
 * @author barbosa
 */
public interface IPersistentMWTeacherOJB extends IPersistentObject
{

	public List readAll() throws ExcepcaoPersistencia;
	public MWTeacher readByNumber(Integer number) throws ExcepcaoPersistencia;
	public MWTeacher readByChavePessoa(Integer chave) throws ExcepcaoPersistencia;
}