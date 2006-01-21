/**
* Aug 31, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.general;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDistrict;
import net.sourceforge.fenixedu.domain.District;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadAllDistricts extends Service {

    public List run() throws ExcepcaoPersistencia{
        List districts = (List) persistentObject.readAll(District.class);
        
        List infoDistricts = (List) CollectionUtils.collect(districts, new Transformer(){

            public Object transform(Object arg0) {
                District district = (District) arg0;                
                return InfoDistrict.newInfoFromDomain(district);
            }});
        
        return infoDistricts;
    }
}


