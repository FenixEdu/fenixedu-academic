/*
 * Created on April 21, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.backBeans.manager.personManagement.ManagerFunctionsManagementBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class ScientificCouncilFunctionsManagementBackingBean extends ManagerFunctionsManagementBackingBean {

    public String getUnits() throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {

        StringBuilder buffer = new StringBuilder();
        List<Unit> allUnits = UnitUtils.readAllUnitsWithoutParents();
        
        for (Unit unit : allUnits) {
            if (unit.getName().equals(UnitUtils.IST_UNIT_NAME) && unit.isActive(new YearMonthDay())) {
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
                "/scientificCouncil/functionsManagement/chooseFunction.faces?personID=").append(
                personID).append("&unitID=").append(parentUnit.getIdInternal()).append("\">").append(
                parentUnit.getName()).append("</a>").append("</li>");

        if (parentUnit.hasAnySubUnits()) {
            openULTag(parentUnit, buffer);
        }

        List<Unit> subUnits = new ArrayList<Unit>();
        subUnits.addAll(parentUnit.getSubUnits());
        Collections.sort(subUnits, new BeanComparator("name"));
        
        for (Unit subUnit : subUnits) {
            if (subUnit.isActive(new YearMonthDay())) {
                getUnitsList(subUnit, buffer);
            }
        }

        if (parentUnit.hasAnySubUnits()) {
            closeULTag(buffer);
        }              
    }
}
