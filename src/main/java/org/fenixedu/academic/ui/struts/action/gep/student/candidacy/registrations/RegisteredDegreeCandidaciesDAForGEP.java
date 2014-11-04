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
package org.fenixedu.academic.ui.struts.action.gep.student.candidacy.registrations;

import java.util.Set;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.candidacy.registrations.RegisteredDegreeCandidaciesDA;
import org.fenixedu.academic.ui.struts.action.gep.GepApplication.GepRegisteredDegreeCandidaciesApp;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = GepRegisteredDegreeCandidaciesApp.class, path = "list",
        titleKey = "label.registeredDegreeCandidacies.first.time.list")
@Mapping(path = "/registeredDegreeCandidacies", module = "gep")
@Forwards(@Forward(name = "viewRegisteredDegreeCandidacies",
        path = "/gep/student/candidacies/registration/viewRegisteredDegreeCandidacies.jsp"))
public class RegisteredDegreeCandidaciesDAForGEP extends RegisteredDegreeCandidaciesDA {

    @Override
    protected Set<Degree> getDegreesToSearch() {
        return Bennu.getInstance().getDegreesSet();
    }

}
