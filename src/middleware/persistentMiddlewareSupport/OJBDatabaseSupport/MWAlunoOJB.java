
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWStudent;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;

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

	public List readAll() throws ExcepcaoPersistencia {
		
		Criteria criteria = new Criteria();
		
		return queryList(MWStudent.class, criteria);
	}

	public MWStudent readByNumber(Integer number) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		
		criteria.addEqualTo("number", number);
		
		return (MWStudent) queryObject(MWStudent.class, criteria);
	}

	/* (non-Javadoc)
	 * @see middleware.persistentMiddlewareSupport.IPersistentMWAluno#readAllBySpan(java.lang.Integer, java.lang.Integer)
	 */
	public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return readSpan(MWStudent.class, criteria, numberOfElementsInSpan, spanNumber);
	}
	
	/* (non-Javadoc)
	 * @see middleware.persistentMiddlewareSupport.IPersistentMWAluno#countAll()
	 */
	public Integer countAll()
	{
		return new Integer(count(MWStudent.class, new Criteria()));
	}

	public Iterator readAllBySpanIterator(Integer spanNumber, Integer numberOfElementsInSpan)
	{
		Criteria criteria = new Criteria();
		return readSpanIterator(MWStudent.class, criteria, numberOfElementsInSpan, spanNumber);
	}
	
}