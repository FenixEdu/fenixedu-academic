/*
 * Created on Nov 8, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.facultyAdmOffice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.manager.personManagement.ManagerFunctionsManagementBackingBean;

public class FacultyAdmOfficeFunctionsManagement extends ManagerFunctionsManagementBackingBean {

    public String getUnits() throws FenixFilterException, FenixServiceException {
        StringBuffer buffer = new StringBuffer();

        final Object[] argsToRead = { Unit.class };

        List<IUnit> allUnits = new ArrayList<IUnit>();
        allUnits.addAll((List<IUnit>) ServiceUtils.executeService(null, "ReadAllDomainObject",
                argsToRead));

        Date currentDate = Calendar.getInstance().getTime();
        for (IUnit unit : allUnits) {
            if (unit.getParentUnits().isEmpty() && unit.isActive(currentDate)) {
                getUnitTree(buffer, unit);
            }
        }

        return buffer.toString();
    }

    public void getUnitTree(StringBuffer buffer, IUnit parentUnit) {
        buffer.append("<ul>");
        getUnitsList(parentUnit, 0, buffer);
        buffer.append("</ul>");
    }

    private void getUnitsList(IUnit parentUnit, int index, StringBuffer buffer) {

        buffer.append("<li>").append("<a href=\"").append(getContextPath()).append(
                "/facultyAdmOffice/functionsManagement/chooseFunction.faces?personID=").append(personID)
                .append("&unitID=").append(parentUnit.getIdInternal()).append("\">").append(
                        parentUnit.getName()).append("</a>").append("</li>").append("<ul>");

        Date currentDate = Calendar.getInstance().getTime();
        for (IUnit subUnit : parentUnit.getSubUnits()) {
            if (subUnit.isActive(currentDate)) {
                getUnitsList(subUnit, index + 1, buffer);
            }
        }

        buffer.append("</ul>");
    }
}
