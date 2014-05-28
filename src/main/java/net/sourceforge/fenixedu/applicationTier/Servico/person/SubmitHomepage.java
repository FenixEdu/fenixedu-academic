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
package net.sourceforge.fenixedu.applicationTier.Servico.person;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SubmitHomepage {

    @Atomic
    public static void run(final Person person, final Boolean activated, final Boolean showUnit, final Boolean showCategory,
            final Boolean showPhoto, final Boolean showResearchUnitHomepage, final Boolean showCurrentExecutionCourses,
            final Boolean showActiveStudentCurricularPlans, final Boolean showAlumniDegrees, final String researchUnitHomepage,
            final MultiLanguageString researchUnit, final Boolean showCurrentAttendingExecutionCourses) {
        check(RolePredicates.PERSON_PREDICATE);

        Homepage homepage = person.initializeSite();

        homepage.setActivated(activated);
        homepage.setShowUnit(showUnit);
        homepage.setShowCategory(showCategory);
        homepage.setShowPhoto(showPhoto);
        homepage.setShowResearchUnitHomepage(showResearchUnitHomepage);
        homepage.setShowCurrentExecutionCourses(showCurrentExecutionCourses);
        homepage.setShowActiveStudentCurricularPlans(showActiveStudentCurricularPlans);
        homepage.setShowAlumniDegrees(showAlumniDegrees);
        homepage.setResearchUnitHomepage(researchUnitHomepage);
        homepage.setResearchUnit(researchUnit);
        homepage.setShowCurrentAttendingExecutionCourses(showCurrentAttendingExecutionCourses);
    }

}