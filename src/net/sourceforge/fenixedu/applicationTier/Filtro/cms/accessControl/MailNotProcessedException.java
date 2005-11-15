/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 8:36:12,8/Nov/2005
 * @version $Id$
 */
public class MailNotProcessedException extends FenixServiceException
{
	public static final int MESSAGE_TOO_LARGE=0;
	public static final int SENDER_NOT_ALLOWED=1;
	
	public MailNotProcessedException(int cause)
	{
		super();
	}
}
