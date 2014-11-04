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
 * Created on 22/Dez/2003
 *
 */
package org.fenixedu.academic.ui.struts.action.personnelSection;

import org.fenixedu.academic.ui.struts.action.manager.personManagement.FindPersonAction;

import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

/**
 * @author T�nia Pous�o
 * 
 */
@StrutsFunctionality(app = PersonnelSectionApplication.class, path = "find-person", titleKey = "link.manage.people.search")
@Mapping(module = "personnelSection", path = "/findPerson")
@Forwards(value = { @Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp"),
        @Forward(name = "displayPerson", path = "/manager/personManagement/displayPerson.jsp"),
        @Forward(name = "findPerson", path = "/manager/personManagement/findPerson.jsp") })
public class PersonManagementAction extends FindPersonAction {

    @Override
    protected String getModulePrefix() {
        return "personnelSection";
    }
}