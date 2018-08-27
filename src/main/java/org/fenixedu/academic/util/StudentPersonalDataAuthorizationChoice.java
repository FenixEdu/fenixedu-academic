/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 28/Ago/2005
 *
 */
package org.fenixedu.academic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * @author Ricardo Rodrigues
 * 
 */
public enum StudentPersonalDataAuthorizationChoice {

    PROFESSIONAL_ENDS, /*
                              * only to professional ends (job propositions, scholarships, internship, etc)
                              */
    SEVERAL_ENDS, /*
                         * several non comercial ends (biographic, recreational, cultural, etc)
                         */
    ALL_ENDS, /* all ends, including comercial ones */

    NO_END, /* doesn't authorize the use of the data */

    JOB_PLATFORM,

    JOB_PLATFORM_AND_OTHERS,

    STUDENTS_ASSOCIATION; /* allows for the student association */

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return StudentPersonalDataAuthorizationChoice.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return StudentPersonalDataAuthorizationChoice.class.getName() + "." + name();
    }

    public String getDescription() {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }

    public static String activeValues() {
        return FenixEduAcademicConfiguration.getConfiguration().activeStudentPersonalDataAuthorizationChoices();
    }

    public static List<StudentPersonalDataAuthorizationChoice> active() {
        return Splitter.on(",").splitToList(FenixEduAcademicConfiguration.getConfiguration().activeStudentPersonalDataAuthorizationChoices()).stream().map(String::trim).map(StudentPersonalDataAuthorizationChoice::valueOf).collect(Collectors.toList());
    }

    public static StudentPersonalDataAuthorizationChoice getPersonalDataAuthorizationForStudentsAssociationType(
            boolean allowsForStudentAssociation) {
        if (allowsForStudentAssociation) {
            return STUDENTS_ASSOCIATION;
        } else {
            return NO_END;
        }
    }
}