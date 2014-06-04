/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 28/Ago/2005
 *
 */
package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * @author Ricardo Rodrigues
 * 
 */
public enum StudentPersonalDataAuthorizationChoice {

    PROFESSIONAL_ENDS(false), /*
                              * only to professional ends (job propositions, scholarships, internship, etc)
                              */
    SEVERAL_ENDS(false), /*
                         * several non comercial ends (biographic, recreational, cultural, etc)
                         */
    ALL_ENDS(true), /* all ends, including comercial ones */

    NO_END(false), /* doesn't authorize the use of the data */

    STUDENTS_ASSOCIATION(true); /* allows for the student association */

    private boolean forStudentsAssociation;

    private StudentPersonalDataAuthorizationChoice(boolean forStudentsAssociation) {
        setForStudentsAssociation(forStudentsAssociation);
    }

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

    public static List<StudentPersonalDataAuthorizationChoice> getGeneralPersonalDataAuthorizationsTypes() {
        List<StudentPersonalDataAuthorizationChoice> authorizationsTypes =
                new ArrayList<StudentPersonalDataAuthorizationChoice>();
        StudentPersonalDataAuthorizationChoice[] values = StudentPersonalDataAuthorizationChoice.values();
        for (StudentPersonalDataAuthorizationChoice studentPersonalDataAuthorizationChoice : values) {
            if (!studentPersonalDataAuthorizationChoice.isForStudentsAssociation()) {
                authorizationsTypes.add(studentPersonalDataAuthorizationChoice);
            }
        }
        return authorizationsTypes;
    }

    public static StudentPersonalDataAuthorizationChoice getPersonalDataAuthorizationForStudentsAssociationType(
            boolean allowsForStudentAssociation) {
        if (allowsForStudentAssociation) {
            return STUDENTS_ASSOCIATION;
        } else {
            return NO_END;
        }
    }

    public void setForStudentsAssociation(boolean forStudentsAssociation) {
        this.forStudentsAssociation = forStudentsAssociation;
    }

    public boolean isForStudentsAssociation() {
        return forStudentsAssociation;
    }
}