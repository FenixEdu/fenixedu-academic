/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.cms;


import net.sourceforge.fenixedu.domain.cms.Cms;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 12:11:04,25/Out/2005
 * @version $Id$
 */
public abstract class CmsService implements IService
{
	protected Cms readFenixCMS() throws ExcepcaoPersistencia
	{
		return PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentCms().readFenixCMS();
	}	
}
