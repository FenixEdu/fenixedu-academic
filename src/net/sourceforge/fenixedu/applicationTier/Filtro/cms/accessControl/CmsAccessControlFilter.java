/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl;

import net.sourceforge.fenixedu.applicationTier.Filtro.Filter;
import net.sourceforge.fenixedu.domain.NotImplementedException;
import net.sourceforge.fenixedu.domain.cms.Cms;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 16:21:38,14/Nov/2005
 * @version $Id$
 */
public abstract class CmsAccessControlFilter extends Filter {

	protected Cms readFenixCMS() throws ExcepcaoPersistencia {
		//return persistentSupport.getIPersistentCms().readFenixCMS();
        throw new NotImplementedException();
	}

}
