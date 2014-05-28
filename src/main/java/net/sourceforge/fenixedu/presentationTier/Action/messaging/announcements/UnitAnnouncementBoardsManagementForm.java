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
/**
 * Author : Goncalo Luiz
 * Creation Date: Jun 26, 2006,6:23:35 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jun 26, 2006,6:23:35 PM
 * 
 */
public class UnitAnnouncementBoardsManagementForm extends ActionForm {
    private static final long serialVersionUID = 5861718840827152423L;

    private String name;
    private String keyUnit;
    private Boolean mandatory;
    private String unitBoardWritePermittedGroupType = UnitBoardPermittedGroupType.UB_PUBLIC.name();
    private String unitBoardReadPermittedGroupType = UnitBoardPermittedGroupType.UB_PUBLIC.name();
    private String unitBoardManagementPermittedGroupType = UnitBoardPermittedGroupType.UB_MANAGER.name();
    private String returnAction;
    private String returnMethod;
    private Boolean tabularVersion;

    public String getReturnMethod() {
        return returnMethod;
    }

    public void setReturnMethod(String returnMethod) {
        this.returnMethod = returnMethod;
    }

    public String getReturnAction() {
        return returnAction;
    }

    public void setReturnAction(String returnAction) {
        this.returnAction = returnAction;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.mandatory = false;
    }

    public String getKeyUnit() {
        return keyUnit;
    }

    public void setKeyUnit(String keyUnit) {
        this.keyUnit = keyUnit;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitBoardReadPermittedGroupType() {
        return unitBoardReadPermittedGroupType;
    }

    public void setUnitBoardReadPermittedGroupType(String unitBoardReadPermittedGroupType) {
        this.unitBoardReadPermittedGroupType = unitBoardReadPermittedGroupType;
    }

    public String getUnitBoardWritePermittedGroupType() {
        return unitBoardWritePermittedGroupType;
    }

    public void setUnitBoardWritePermittedGroupType(String unitBoardWritePermittedGroupType) {
        this.unitBoardWritePermittedGroupType = unitBoardWritePermittedGroupType;
    }

    public String getUnitBoardManagementPermittedGroupType() {
        return unitBoardManagementPermittedGroupType;
    }

    public void setUnitBoardManagementPermittedGroupType(String unitBoardManagementPermittedGroupType) {
        this.unitBoardManagementPermittedGroupType = unitBoardManagementPermittedGroupType;
    }

    public Boolean getTabularVersion() {
        return tabularVersion;
    }

    public void setTabularVersion(Boolean tabularVersion) {
        this.tabularVersion = tabularVersion;
    }
}
