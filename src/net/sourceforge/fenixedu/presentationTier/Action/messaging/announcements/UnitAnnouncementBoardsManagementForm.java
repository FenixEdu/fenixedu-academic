/**
 * Author : Goncalo Luiz
 * Creation Date: Jun 26, 2006,6:23:35 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br><br>
 * Created on Jun 26, 2006,6:23:35 PM
 *
 */
public class UnitAnnouncementBoardsManagementForm extends ValidatorForm {
    private static final long serialVersionUID = 5861718840827152423L;
    
    private String name;
    private Integer keyUnit;
    private Boolean mandatory;
    private String unitBoardWritePermittedGroupType=UnitBoardPermittedGroupType.UB_PUBLIC.name();
    private String unitBoardReadPermittedGroupType=UnitBoardPermittedGroupType.UB_PUBLIC.name();
    private String unitBoardManagementPermittedGroupType=UnitBoardPermittedGroupType.UB_MANAGER.name();
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
        this.mandatory=false;
    }

    public Integer getKeyUnit() {
        return keyUnit;
    }

    public void setKeyUnit(Integer keyUnit) {
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
