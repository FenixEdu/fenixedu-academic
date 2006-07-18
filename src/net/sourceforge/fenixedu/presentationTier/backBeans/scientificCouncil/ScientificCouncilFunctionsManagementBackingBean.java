/*
 * Created on April 21, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.backBeans.manager.personManagement.ManagerFunctionsManagementBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class ScientificCouncilFunctionsManagementBackingBean extends
        ManagerFunctionsManagementBackingBean {

    protected void getUnitsList(Unit parentUnit, StringBuilder buffer, YearMonthDay currentDate) {

        openLITag(buffer);

        List<Unit> subUnits = new ArrayList<Unit>(getSubUnits(parentUnit, currentDate));
        Collections.sort(subUnits, new BeanComparator("name"));

        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer);
        }

        buffer.append("<a href=\"").append(getContextPath()).append(
                "/scientificCouncil/functionsManagement/chooseFunction.faces?personID=")
                .append(personID).append("&unitID=").append(parentUnit.getIdInternal()).append("\">")
                .append(parentUnit.getName()).append("</a>").append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer);
        }

        for (Unit subUnit : subUnits) {
            getUnitsList(subUnit, buffer, currentDate);
        }

        if (!subUnits.isEmpty()) {
            closeULTag(buffer);
        }
    }
}
