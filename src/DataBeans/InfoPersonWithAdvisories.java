/*
 * Created on Jun 9, 2004
 *  
 */
package DataBeans;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.IAdvisory;
import Dominio.IPerson;

/**
 * @author João Mota
 *  
 */
public class InfoPersonWithAdvisories extends InfoPerson {

    public void copyFromDomain(IPerson person) {
        super.copyFromDomain(person);
        if (person != null && person.getAdvisories() != null) {

            setInfoAdvisories((List) CollectionUtils.collect(person.getAdvisories(), new Transformer() {

                public Object transform(Object arg0) {
                    return InfoAdvisory.newInfoFromDomain((IAdvisory) arg0);
                }
            }));

        }
    }

    public static InfoPerson newInfoFromDomain(IPerson person) {
        InfoPersonWithAdvisories infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPersonWithAdvisories();
            infoPerson.copyFromDomain(person);
        }
        return infoPerson;
    }

}