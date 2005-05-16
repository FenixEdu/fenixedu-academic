/*
 * Created on May 9, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadNonAffiliatedTeachersByName implements IService {

    public ReadNonAffiliatedTeachersByName() {
    }
    
    public List run(String nameToSearch) throws ExcepcaoPersistencia{
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentNonAffiliatedTeacher persistentNonAffiliatedTeacher = sp
                .getIPersistentNonAffiliatedTeacher();
        
        String names[] = nameToSearch.split(" ");
        StringBuffer nonAffiliatedTeacherName = new StringBuffer("%");

        for (int i = 0; i <= names.length - 1; i++) {
            nonAffiliatedTeacherName.append(names[i]);
            nonAffiliatedTeacherName.append("%");
        }
        
        List nonAffiliatedTeachers = (List) persistentNonAffiliatedTeacher.readByName(nonAffiliatedTeacherName.toString());
        
        List infoNonAffiliatedTeachers = new ArrayList(nonAffiliatedTeachers.size());

        for (Iterator iter = nonAffiliatedTeachers.iterator(); iter.hasNext();) {
            INonAffiliatedTeacher nonAffiliatedTeacher = (INonAffiliatedTeacher) iter.next();
            InfoNonAffiliatedTeacher infoNonAffiliatedTeacher = new InfoNonAffiliatedTeacher();
            infoNonAffiliatedTeacher.copyFromDomain(nonAffiliatedTeacher);
            infoNonAffiliatedTeachers.add(infoNonAffiliatedTeacher);
        }

        return infoNonAffiliatedTeachers;       
    }

}
