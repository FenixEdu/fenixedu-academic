/**
* Aug 31, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.general;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDistrict;
import net.sourceforge.fenixedu.domain.District;
import net.sourceforge.fenixedu.domain.IDistrict;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDistrict;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadAllDistricts implements IService {

    public List run() throws ExcepcaoPersistencia{
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentDistrict persistentDistrict = sp.getIPersistentDistrict();
        
        List districts = (List) persistentDistrict.readAll(District.class);
        
        List infoDistricts = (List) CollectionUtils.collect(districts, new Transformer(){

            public Object transform(Object arg0) {
                IDistrict district = (IDistrict) arg0;                
                return InfoDistrict.newInfoFromDomain(district);
            }});
        
        return infoDistricts;
    }
}


