/*
 * Created on May 9, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadNonAffiliatedTeachersByName extends Service {

    public ReadNonAffiliatedTeachersByName() {
    }
    
    public List run(String nameToSearch) throws ExcepcaoPersistencia{
        IPersistentNonAffiliatedTeacher persistentNonAffiliatedTeacher = persistentSupport
                .getIPersistentNonAffiliatedTeacher();
        
        String names[] = nameToSearch.split(" ");
        StringBuilder nonAffiliatedTeacherName = new StringBuilder("%");

        for (int i = 0; i <= names.length - 1; i++) {
            nonAffiliatedTeacherName.append(names[i]);
            nonAffiliatedTeacherName.append("%");
        }
        
        List nonAffiliatedTeachers = persistentNonAffiliatedTeacher.readByName(nonAffiliatedTeacherName.toString());
        
        List infoNonAffiliatedTeachers = new ArrayList(nonAffiliatedTeachers.size());

        for (Iterator iter = nonAffiliatedTeachers.iterator(); iter.hasNext();) {
            NonAffiliatedTeacher nonAffiliatedTeacher = (NonAffiliatedTeacher) iter.next();
            InfoNonAffiliatedTeacher infoNonAffiliatedTeacher = new InfoNonAffiliatedTeacher();
            infoNonAffiliatedTeacher.copyFromDomain(nonAffiliatedTeacher);
            infoNonAffiliatedTeachers.add(infoNonAffiliatedTeacher);
        }

        return infoNonAffiliatedTeachers;       
    }

}
