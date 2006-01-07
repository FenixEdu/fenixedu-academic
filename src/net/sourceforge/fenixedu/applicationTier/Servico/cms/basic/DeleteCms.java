/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.basic;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.domain.cms.Cms;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 16:56:39,17/Nov/2005
 * @version $Id$
 */
public class DeleteCms extends CmsService
{
	public void run(String name) throws ExcepcaoPersistencia
	{
		Cms cms  = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentCms().readCmsByName(name);
		cms.delete();
	}
}
