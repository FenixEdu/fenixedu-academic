/*
 * Created on Nov 8, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.facultyAdmOffice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.backBeans.manager.personManagement.ManagerFunctionsManagementBackingBean;

import org.apache.commons.beanutils.BeanComparator;

public class FacultyAdmOfficeFunctionsManagement extends ManagerFunctionsManagementBackingBean {

    public String getUnits() throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {

        StringBuilder buffer = new StringBuilder();
        List<Unit> allUnits = UnitUtils.readAllUnitsWithoutParents();
        
        for (Unit unit : allUnits) {
            if (unit.getName().equals(UnitUtils.IST_UNIT_NAME) && unit.isActive(Calendar.getInstance().getTime())) {
                getUnitTree(buffer, unit);
            }
        }

        return buffer.toString();
    }

    public void getUnitTree(StringBuilder buffer, Unit parentUnit) {
        buffer.append("<ul class='padding1 nobullet'>");
        getUnitsList(parentUnit, buffer);
        buffer.append("</ul>");
    }

    private void getUnitsList(Unit parentUnit, StringBuilder buffer) {
        
        openLITag(buffer);

        if (parentUnit.hasAnySubUnits()) {
            putImage(parentUnit, buffer);
        }

        buffer.append("<a href=\"").append(getContextPath()).append(
                "/facultyAdmOffice/functionsManagement/chooseFunction.faces?personID=").append(
                personID).append("&unitID=").append(parentUnit.getIdInternal()).append("\">").append(
                parentUnit.getName()).append("</a>").append("</li>");

        if (parentUnit.hasAnySubUnits()) {
            openULTag(parentUnit, buffer);
        }

        List<Unit> subUnits = new ArrayList<Unit>();
        subUnits.addAll(parentUnit.getSubUnits());
        Collections.sort(subUnits, new BeanComparator("name"));
        
        for (Unit subUnit : subUnits) {
            if (subUnit.isActive(Calendar.getInstance().getTime())) {
                getUnitsList(subUnit, buffer);
            }
        }

        if (parentUnit.hasAnySubUnits()) {
            closeULTag(buffer);
        }              
    }
}
