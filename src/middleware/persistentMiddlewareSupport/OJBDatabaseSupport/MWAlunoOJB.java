
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class MWAlunoOJB extends ObjectFenixOJB implements IPersistentMWAluno {
    
    public MWAlunoOJB() {
    }

	public List readAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {
		
		Criteria criteria = new Criteria();
		
		return queryList(MWAluno.class, criteria);
	}

	public MWAluno readByNumber(Integer number) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		
		criteria.addEqualTo("number", number);
		
		return (MWAluno) queryObject(MWAluno.class, criteria);
	}
    
}