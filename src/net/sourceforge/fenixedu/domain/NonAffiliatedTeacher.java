/*
 * Created on Apr 27, 2005
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern pattern = Pattern.compile(name.toLowerCase());
        final Set<NonAffiliatedTeacher> nonAffiliatedTeachers = new HashSet<NonAffiliatedTeacher>();
        for (final NonAffiliatedTeacher nonAffiliatedTeacher : RootDomainObject.getInstance().getNonAffiliatedTeachersSet()) {
            Matcher matcher = pattern.matcher(nonAffiliatedTeacher.getName().toLowerCase());
            if (matcher.find()) {
                nonAffiliatedTeachers.add(nonAffiliatedTeacher);
            }
        }
        return nonAffiliatedTeachers;
    }

}
