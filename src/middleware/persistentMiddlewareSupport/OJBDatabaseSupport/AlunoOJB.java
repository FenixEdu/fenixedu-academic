
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWAluno;
import middleware.persistentMiddlewareSupport.IPersistentAluno;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class AlunoOJB extends MiddlewareObjectOJB implements IPersistentAluno {
    
    public AlunoOJB() {
    }

	public List readAll() throws PersistentMiddlewareSupportException {
		
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(MWAluno.class,criteria);
		
		return (List) broker.getCollectionByQuery(query);
	}

	public MWAluno readByNumber(Integer number) throws PersistentMiddlewareSupportException {
		Criteria criteria = new Criteria();
		
		criteria.addEqualTo("number", number);
		Query query = new QueryByCriteria(MWAluno.class,criteria);
		
		return (MWAluno) broker.getObjectByQuery(query);
	}
    
}