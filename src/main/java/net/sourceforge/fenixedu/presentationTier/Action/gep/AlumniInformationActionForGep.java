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
package net.sourceforge.fenixedu.presentationTier.Action.gep;

import net.sourceforge.fenixedu.presentationTier.Action.gep.GepApplication.GepAlumniApp;
import net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice.AlumniInformationAction;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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