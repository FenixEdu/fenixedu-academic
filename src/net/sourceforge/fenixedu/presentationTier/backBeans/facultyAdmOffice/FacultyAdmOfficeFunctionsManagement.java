/*
 * Created on Nov 8, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.facultyAdmOffice;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.backBeans.manager.personManagement.ManagerFunctionsManagementBackingBean;

public class FacultyAdmOfficeFunctionsManagement extends ManagerFunctionsManagementBackingBean {

    public String getUnits() throws FenixFilterException, FenixServiceException {

        StringBuilder buffer = new StringBuilder();
        List<Unit> allUnits = readAllDomainObjects(Unit.class);
        
        for (Unit unit : allUnits) {
            if (unit.getParentUnits().isEmpty() && unit.isActive(Calendar.getInstance().getTime())) {
                getUnitTree(buffer, unit);
            }
        }

        return buffer.toString();
    }

    public void getUnitTree(StringBuilder buffer, Unit parentUnit) {
        buffer.append("<ul class='padding nobullet'>");
        getUnitsList(parentUnit, buffer);
        buffer.append("</ul>");
    }

    private void getUnitsList(Unit parentUnit, StringBuilder buffer) {
        
        buffer.append("<li>");

        if (parentUnit.hasAnySubUnits()) {
            buffer.append("<img ").append("src='").append(getContextPath()).append(
                    "/images/toggle_plus10.gif' id=\"").append(parentUnit.getIdInternal()).append("\" ")
                    .append("indexed='true' onClick=\"").append("check(document.getElementById('")
                    .append("aa").append(parentUnit.getIdInternal()).append(
                            "'),document.getElementById('").append(parentUnit.getIdInternal()).append(
                            "'));return false;").append("\"> ");
        }

        buffer.append("<a href=\"").append(getContextPath()).append(
                "/facultyAdmOffice/functionsManagement/chooseFunction.faces?personID=").append(
                personID).append("&unitID=").append(parentUnit.getIdInternal()).append("\">").append(
                parentUnit.getName()).append("</a>").append("</li>");

        if (parentUnit.hasAnySubUnits()) {
            buffer.append("<ul class='mvert0' id=\"").append("aa").append(parentUnit.getIdInternal())
                    .append("\" ").append("style='display:none'>\r\n");
        }

        for (Unit subUnit : parentUnit.getSubUnits()) {
            if (subUnit.isActive(Calendar.getInstance().getTime())) {
                getUnitsList(subUnit, buffer);
            }
        }

        if (parentUnit.hasAnySubUnits()) {
            buffer.append("</ul>");
        }              
    }
}
