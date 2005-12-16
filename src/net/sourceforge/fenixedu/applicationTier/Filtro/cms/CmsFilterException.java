/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.cms;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 16:18:42,14/Nov/2005
 * @version $Id$
 */ 
public class CmsFilterException extends FenixFilterException
{
	public CmsFilterException()
	{
		super();
	}
	public CmsFilterException(String msg)
	{
		super(msg);
	}
}
