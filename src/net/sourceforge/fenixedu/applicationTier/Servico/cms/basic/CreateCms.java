/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.basic;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.domain.cms.Cms;
import net.sourceforge.fenixedu.domain.cms.infrastructure.CmsConfiguration;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 11:25:32,18/Nov/2005
 * @version $Id$
 */
public class CreateCms extends CmsService
{
	public Cms run(String name)
	{
		Cms cms = new Cms();
		CmsConfiguration configuration = new CmsConfiguration(); // using the default configurations
		cms.setName(name);
		cms.setConfiguration(configuration);
		
		return cms;
	}
}
