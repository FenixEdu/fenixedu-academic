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
        buffer.append("<ul>");
        getUnitsList(parentUnit, 0, buffer);
        buffer.append("</ul>");
    }

    private void getUnitsList(Unit parentUnit, int index, StringBuilder buffer) {

        buffer.append("<li>").append("<a href=\"").append(getContextPath()).append(
                "/facultyAdmOffice/functionsManagement/chooseFunction.faces?personID=").append(personID)
                .append("&unitID=").append(parentUnit.getIdInternal()).append("\">").append(
                        parentUnit.getName()).append("</a>").append("</li>").append("<ul>");

        for (Unit subUnit : parentUnit.getSubUnits()) {
            if (subUnit.isActive(Calendar.getInstance().getTime())) {
                getUnitsList(subUnit, index + 1, buffer);
            }
        }

        buffer.append("</ul>");
    }
}
