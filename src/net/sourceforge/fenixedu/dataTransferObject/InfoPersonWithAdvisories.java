/*
 * Created on Jun 9, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IPerson;

/**
 * @author João Mota
 *  
 */
public class InfoPersonWithAdvisories extends InfoPerson {

    public void copyFromDomain(IPerson person) {
        super.copyFromDomain(person);
        if (person != null && person.getAdvisories() != null) {

            Date currentDate = Calendar.getInstance().getTime();
            List<InfoAdvisory> list = new ArrayList<InfoAdvisory>();                       
            for (IAdvisory advisory : person.getAdvisories()) {
                if(advisory.getExpires().after(currentDate)){
                    list.add(InfoAdvisory.newInfoFromDomain(advisory));
                }
            }                       
            setInfoAdvisories(list);
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