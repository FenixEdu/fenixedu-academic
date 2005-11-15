/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl;

import net.sourceforge.fenixedu.applicationTier.Filtro.cms.CmsFilterException;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 21:07:27,14/Nov/2005
 * @version $Id$
 */
public class RequesterNotAllowedToDelete extends CmsFilterException
{
	public RequesterNotAllowedToDelete(String msg)
	{
		super(msg);
	}
}
