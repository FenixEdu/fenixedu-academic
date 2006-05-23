/*
 * Created on Jun 9, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Person;

/**
 * @author João Mota
 *  
 */
public class InfoPersonWithAdvisories extends InfoPerson {

    public void copyFromDomain(Person person) {
        super.copyFromDomain(person);
        if (person != null && person.getAdvisories() != null) {

            Date currentDate = Calendar.getInstance().getTime();
            List<InfoAdvisory> list = new ArrayList<InfoAdvisory>();                       
            for (Advisory advisory : person.getAdvisories()) {
                if(advisory.getExpires() == null || advisory.getExpires().after(currentDate)){
                    list.add(InfoAdvisory.newInfoFromDomain(advisory));
                }
            }                       
            setInfoAdvisories(list);
        }
    }

    public static InfoPerson newInfoFromDomain(Person person) {
        InfoPersonWithAdvisories infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPersonWithAdvisories();
            infoPerson.copyFromDomain(person);
        }
        return infoPerson;
    }

}