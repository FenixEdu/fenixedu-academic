/*
 * Created on Nov 8, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.facultyAdmOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.backBeans.manager.personManagement.ManagerFunctionsManagementBackingBean;

import org.joda.time.YearMonthDay;

public class FacultyAdmOfficeFunctionsManagement extends ManagerFunctionsManagementBackingBean {

    public String getUnits() throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {
        StringBuilder buffer = new StringBuilder();
        getUnitTree(buffer, UnitUtils.readInstitutionUnit());
        return buffer.toString();
    }

    public void getUnitTree(StringBuilder buffer, Unit parentUnit) {
        buffer.append("<ul class='padding1 nobullet'>");
        getUnitsList(parentUnit, null, buffer);
        buffer.append("</ul>");
    }

    private void getUnitsList(Unit parentUnit, Unit parentUnitParent, StringBuilder buffer) {
        
        openLITag(buffer);

        if (parentUnit.hasAnySubUnits()) {
            putImage(parentUnit, buffer, parentUnitParent);
        }

        buffer.append("<a href=\"").append(getContextPath()).append(
                "/facultyAdmOffice/functionsManagement/chooseFunction.faces?personID=").append(
                personID).append("&unitID=").append(parentUnit.getIdInternal()).append("\">").append(
                parentUnit.getName()).append("</a>").append("</li>");

        if (parentUnit.hasAnySubUnits()) {
            openULTag(parentUnit, buffer, parentUnitParent);
        }

        List<Unit> subUnits = new ArrayList<Unit>();
        subUnits.addAll(parentUnit.getSubUnits());
        Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
        
        for (Unit subUnit : subUnits) {
            if (subUnit.isActive(new YearMonthDay())) {
                getUnitsList(subUnit, parentUnitParent, buffer);
            }
        }

        if (parentUnit.hasAnySubUnits()) {
            closeULTag(buffer);
        }              
    }
}
