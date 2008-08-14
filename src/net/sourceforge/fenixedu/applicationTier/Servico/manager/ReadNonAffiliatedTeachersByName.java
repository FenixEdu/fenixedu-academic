/*
 * Created on May 9, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadNonAffiliatedTeachersByName extends Service {

    public List run(String nameToSearch) {
	String names[] = nameToSearch.split(" ");
	StringBuilder nonAffiliatedTeacherName = new StringBuilder(".*");

	for (int i = 0; i <= names.length - 1; i++) {
	    nonAffiliatedTeacherName.append(names[i]);
	    nonAffiliatedTeacherName.append(".*");
	}

	Set<NonAffiliatedTeacher> nonAffiliatedTeachers = NonAffiliatedTeacher
		.findNonAffiliatedTeacherByName(nonAffiliatedTeacherName.toString());

	List infoNonAffiliatedTeachers = new ArrayList(nonAffiliatedTeachers.size());

	for (NonAffiliatedTeacher nonAffiliatedTeacher : nonAffiliatedTeachers) {
	    InfoNonAffiliatedTeacher infoNonAffiliatedTeacher = new InfoNonAffiliatedTeacher();
	    infoNonAffiliatedTeacher.copyFromDomain(nonAffiliatedTeacher);
	    infoNonAffiliatedTeachers.add(infoNonAffiliatedTeacher);
	}

	return infoNonAffiliatedTeachers;
    }

}
