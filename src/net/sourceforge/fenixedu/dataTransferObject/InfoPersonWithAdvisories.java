/*
 * Created on Jun 9, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IPerson;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

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