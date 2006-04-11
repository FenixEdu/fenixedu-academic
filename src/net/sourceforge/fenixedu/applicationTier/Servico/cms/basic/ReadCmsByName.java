/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.basic;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.domain.NotImplementedException;
import net.sourceforge.fenixedu.domain.cms.Cms;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 9:59:27,15/Nov/2005
 * @version $Id$
 */
public class ReadCmsByName extends CmsService {
    
    public Cms run(String name) throws ExcepcaoPersistencia {
        //return persistentSupport.getIPersistentCms().readCmsByName(name);
        throw new NotImplementedException();
    }
}
