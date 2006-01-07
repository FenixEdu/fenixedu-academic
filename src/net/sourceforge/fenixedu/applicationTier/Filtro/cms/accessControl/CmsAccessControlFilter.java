/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl;

import net.sourceforge.fenixedu.domain.cms.Cms;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.filterManager.IFilter;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 16:21:38,14/Nov/2005
 * @version $Id$
 */
public abstract class CmsAccessControlFilter implements IFilter
{
	protected Cms readFenixCMS() throws ExcepcaoPersistencia
	{
		return PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentCms().readFenixCMS();
	}
}
