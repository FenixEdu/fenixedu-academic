/*
 * Created on Aug 26, 2003
 *
 */
package ServidorPersistente.customQueries;

import java.util.Calendar;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.accesslayer.QueryCustomizer;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 * @author Nuno Nunes & Luis Cruz
 *  
 */
public class AdvisoryFilter implements QueryCustomizer {

    public AdvisoryFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.QueryCustomizer#customizeQuery(java.lang.Object,
     *      org.apache.ojb.broker.PersistenceBroker,
     *      org.apache.ojb.broker.metadata.CollectionDescriptor,
     *      org.apache.ojb.broker.query.QueryByCriteria)
     */
    public Query customizeQuery(Object arg0, PersistenceBroker broker,
            CollectionDescriptor collectionDescriptor, QueryByCriteria query) {
        query.getCriteria().addGreaterOrEqualThan("expires", Calendar.getInstance().getTime());

        return query;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.metadata.AttributeContainer#addAttribute(java.lang.String,
     *      java.lang.String)
     */
    public void addAttribute(String arg0, String arg1) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.metadata.AttributeContainer#getAttribute(java.lang.String,
     *      java.lang.String)
     */
    public String getAttribute(String arg0, String arg1) {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.metadata.AttributeContainer#getAttribute(java.lang.String)
     */
    public String getAttribute(String arg0) {

        return null;
    }

}