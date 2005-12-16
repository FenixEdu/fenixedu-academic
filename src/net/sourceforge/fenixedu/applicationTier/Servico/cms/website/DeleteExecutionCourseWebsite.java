/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.website;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.domain.cms.website.IExecutionCourseWebsite;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 19:55:05,9/Dez/2005
 * @version $Id$
 */
public class DeleteExecutionCourseWebsite extends CmsService
{
	public void run(IExecutionCourseWebsite website)
	{
		website.delete();
	}
}
