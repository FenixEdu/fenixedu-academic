/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.basic;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.domain.cms.Cms;
import net.sourceforge.fenixedu.domain.cms.ICms;
import net.sourceforge.fenixedu.domain.cms.infrastructure.CmsConfiguration;
import net.sourceforge.fenixedu.domain.cms.infrastructure.ICmsConfiguration;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

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
	public ICms run(String name)
	{
		ICms cms = new Cms();
		ICmsConfiguration configuration = new CmsConfiguration(); // using the default configurations
		cms.setName(name);
		cms.setConfiguration(configuration);
		
		return cms;
	}
}
