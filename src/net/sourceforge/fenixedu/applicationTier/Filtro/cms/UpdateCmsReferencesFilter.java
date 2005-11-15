/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.cms;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterParameters;
import pt.utl.ist.berserk.logic.filterManager.IFilter;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 16:00:57,25/Out/2005
 * @version $Id$
 */
public abstract class UpdateCmsReferencesFilter implements IFilter
{

	final public void execute(ServiceRequest arg0, ServiceResponse arg1, FilterParameters arg2) throws FilterException, Exception
	{
		this.execute(arg0,arg1);
	}
	
	abstract public void execute(ServiceRequest request, ServiceResponse response) throws FilterException, Exception; 

}
