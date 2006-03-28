/*
 * Created on Apr 27, 2005
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;


/**
 * @author Ricardo Rodrigues
 *
 */

public class NonAffiliatedTeacher extends NonAffiliatedTeacher_Base {

	public NonAffiliatedTeacher() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public static Set<NonAffiliatedTeacher> findNonAffiliatedTeacherByName(final String name) {
		final Set<NonAffiliatedTeacher> nonAffiliatedTeachers = new HashSet<NonAffiliatedTeacher>();
		for (final NonAffiliatedTeacher nonAffiliatedTeacher : RootDomainObject.getInstance().getNonAffiliatedTeachersSet()) {
			if (nonAffiliatedTeacher.getName().equalsIgnoreCase(name)) {
				nonAffiliatedTeachers.add(nonAffiliatedTeacher);
			}
		}
		return nonAffiliatedTeachers;
	}

}
