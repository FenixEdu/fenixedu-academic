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
 * Created on 22:35:25,16/Nov/2005
 * @version $Id$
 */
public class MailerNotAuthenticated extends CmsFilterException
{

	public MailerNotAuthenticated(String string)
	{
		super(string);
	}

}
