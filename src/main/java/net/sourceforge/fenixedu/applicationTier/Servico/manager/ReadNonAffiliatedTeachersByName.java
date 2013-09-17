/*
 * Created on May 9, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import pt.ist.fenixframework.Atomic;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadNonAffiliatedTeachersByName {

    protected List run(String nameToSearch) {
        String names[] = nameToSearch.split(" ");
        StringBuilder nonAffiliatedTeacherName = new StringBuilder(".*");

        for (int i = 0; i <= names.length - 1; i++) {
            nonAffiliatedTeacherName.append(names[i]);
            nonAffiliatedTeacherName.append(".*");
        }

        Set<NonAffiliatedTeacher> nonAffiliatedTeachers =
                NonAffiliatedTeacher.findNonAffiliatedTeacherByName(nonAffiliatedTeacherName.toString());

        List infoNonAffiliatedTeachers = new ArrayList(nonAffiliatedTeachers.size());

        for (NonAffiliatedTeacher nonAffiliatedTeacher : nonAffiliatedTeachers) {
            InfoNonAffiliatedTeacher infoNonAffiliatedTeacher = new InfoNonAffiliatedTeacher();
            infoNonAffiliatedTeacher.copyFromDomain(nonAffiliatedTeacher);
            infoNonAffiliatedTeachers.add(infoNonAffiliatedTeacher);
        }

        return infoNonAffiliatedTeachers;
    }

    // Service Invokers migrated from Berserk

    private static final ReadNonAffiliatedTeachersByName serviceInstance = new ReadNonAffiliatedTeachersByName();

    @Atomic
    public static List runReadNonAffiliatedTeachersByName(String nameToSearch) throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(nameToSearch);
        } catch (NotAuthorizedException ex1) {
            try {
                GEPAuthorizationFilter.instance.execute();
                return serviceInstance.run(nameToSearch);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}