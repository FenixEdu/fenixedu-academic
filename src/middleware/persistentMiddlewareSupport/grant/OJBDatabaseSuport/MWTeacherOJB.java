/*
 * Created on Feb 19, 2004
 */
package middleware.persistentMiddlewareSupport.grant.OJBDatabaseSuport;

import java.util.List;

import middleware.middlewareDomain.grant.MWTeacher;
import middleware.persistentMiddlewareSupport.grant.IPersistentMWTeacherOJB;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author pica
 * @author barbosa
 */
public class MWTeacherOJB extends ObjectFenixOJB implements IPersistentMWTeacherOJB
{
	public MWTeacherOJB()
	{
	}

	public List readAll() throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return queryList(MWTeacher.class, criteria);
	}

	public MWTeacher readByNumber(Integer number) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("number", number);
		return (MWTeacher) queryObject(MWTeacher.class, criteria);
	}
	
	public MWTeacher readByChavePessoa(Integer chave) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("chavePessoa", chave);
		return (MWTeacher) queryObject(MWTeacher.class, criteria);
	}
}
