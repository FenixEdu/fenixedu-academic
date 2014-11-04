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
package org.fenixedu.academic.ui.struts.action.gep;

import org.fenixedu.academic.ui.struts.action.gep.GepApplication.GepAlumniApp;
import org.fenixedu.academic.ui.struts.action.publicRelationsOffice.AlumniInformationAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = GepAlumniApp.class, path = "search", titleKey = "label.alumni.search")
@Mapping(module = "gep", path = "/alumni")
@Forwards({ @Forward(name = "removeRecipients", path = "/gep/alumni/removeRecipients.jsp"),
        @Forward(name = "manageRecipients", path = "/gep/alumni/manageRecipients.jsp"),
        @Forward(name = "alumni.showAlumniStatistics", path = "/gep/alumni/alumniStatistics.jsp"),
        @Forward(name = "addRecipients", path = "/gep/alumni/addRecipients.jsp"),
        @Forward(name = "alumni.showAlumniDetails", path = "/gep/alumni/alumniDetails.jsp") })
public class AlumniInformationActionForGep extends AlumniInformationAction {

    @StrutsFunctionality(app = GepAlumniApp.class, path = "statistics", titleKey = "label.alumni.statistics")
    @Mapping(path = "/alumniStatistics", module = "gep")
    public static class AlumniStatisticsActionForGep extends AlumniStatisticsAction {

    }
}