/*
 * Created on 13/Jan/2004
 *
 */
package ServidorPersistente.customQueries;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.accesslayer.QueryCustomizer;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 * 
 * @author Susana Fernandes
 *
 */
public class QuestionFilter implements QueryCustomizer
{

	/* (non-Javadoc)
	 * @see org.apache.ojb.broker.accesslayer.QueryCustomizer#customizeQuery(java.lang.Object, org.apache.ojb.broker.PersistenceBroker, org.apache.ojb.broker.metadata.CollectionDescriptor, org.apache.ojb.broker.query.QueryByCriteria)
	 */
	public Query customizeQuery(
		Object arg0,
		PersistenceBroker arg1,
		CollectionDescriptor arg2,
		QueryByCriteria query)
	{
		query.getCriteria().addEqualTo("visibility", new Boolean(true));
		return query;
	}

	/* (non-Javadoc)
	 * @see org.apache.ojb.broker.metadata.AttributeContainer#addAttribute(java.lang.String, java.lang.String)
	 */
	public void addAttribute(String arg0, String arg1)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.ojb.broker.metadata.AttributeContainer#getAttribute(java.lang.String, java.lang.String)
	 */
	public String getAttribute(String arg0, String arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.ojb.broker.metadata.AttributeContainer#getAttribute(java.lang.String)
	 */
	public String getAttribute(String arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
