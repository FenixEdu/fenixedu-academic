/*
 * Created on Feb 6, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class UnitUtils {

    public static final String IST_UNIT_NAME = getLabel("ist.unit.name");

    public static final String IST_UNIT_ACRONYM = getLabel("ist.unit.acronym");

    public static final String EXTERNAL_INSTITUTION_UNIT_NAME = getLabel("external.instituions.name");

    public static List<Unit> readAllExternalInstitutionUnits() {
        List<Unit> allExternalUnits = new ArrayList<Unit>();
        for (Unit unit : Unit.readAllUnits()) {
            if (unit.getType() != null && unit.getType().equals(PartyTypeEnum.EXTERNAL_INSTITUTION)) {
                allExternalUnits.add(unit);
            }
        }
        return allExternalUnits;
    }

    public static Unit readExternalInstitutionUnitByName(String name) {
        for (Unit unit : Unit.readAllUnits()) {
            if (unit.getType() != null && unit.getType().equals(PartyTypeEnum.EXTERNAL_INSTITUTION)
                    && unit.getName().equals(name)) {
                return unit;
            }
        }
        return null;
    }

    public static Unit readUnitWithoutParentstByName(String name) {
        for (Unit unit : readAllUnitsWithoutParents()) {
            if (unit.getName().equals(name)) {
                return unit;
            }
        }
        return null;
    }

    public static List<Unit> readAllUnitsWithoutParents() {
        List<Unit> allUnitsWithoutParent = new ArrayList<Unit>();
        for (Unit unit : Unit.readAllUnits()) {
            if (unit.getParentUnits().isEmpty()) {
                allUnitsWithoutParent.add(unit);
            }
        }
        return allUnitsWithoutParent;
    }

    public static List<Unit> readAllDepartmentUnits() {
        final List<Unit> result = new ArrayList<Unit>();
        final YearMonthDay now = new YearMonthDay();
        for (final Unit unit : Unit.readAllUnits()) {
            if (unit.isActive(now) && unit.getType() == PartyTypeEnum.DEPARTMENT) {
                result.add(unit);
            }
        }
        return result;
    }

    private static String getLabel(String key) {
        final String language = PropertiesManager.getProperty("language");
        final String country = PropertiesManager.getProperty("location");
        final String variant = PropertiesManager.getProperty("variant");
        ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", new Locale(
                language, country, variant));
        return bundle.getString(key);
    }

    public static Unit readUnitWithoutParentstByAcronym(String acronym) {
        for (Unit topUnit : readAllUnitsWithoutParents()) {
            if (topUnit.getAcronym() != null && topUnit.getAcronym().equals(acronym)) {
                return topUnit;
            }
        }
        return null;
    }

    public static Unit readExternalInstitutionUnit() {
        return readUnitWithoutParentstByName(EXTERNAL_INSTITUTION_UNIT_NAME);
    }

    public static Unit readInstitutionUnit() {
        return readUnitWithoutParentstByName(IST_UNIT_NAME);
    }

    public static String getInstitutionTree(HttpServletRequest request, DomainObject domainObject, String paramName, String path)
            throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {

        StringBuilder buffer = new StringBuilder();
        Unit instituionUnit = UnitUtils.readInstitutionUnit();
        YearMonthDay currentDate = new YearMonthDay();

        buffer.append("<ul class='padding1 nobullet'>");
        getSubUnitsList(instituionUnit, buffer, currentDate, request, domainObject, paramName, path);
        buffer.append("</ul>");

        return buffer.toString();
    }

    private static void getSubUnitsList(Unit parentUnit, StringBuilder buffer, YearMonthDay currentDate,
            HttpServletRequest request, DomainObject domainObject, String paramName, String path) {

        buffer.append("<li>");

        List<Unit> subUnits = getUnitSubUnits(parentUnit, currentDate);
        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer, request);
        }

        buffer.append("<a href=\"").append(request.getContextPath()).append(path).append("&unitID=")
                .append(parentUnit.getIdInternal()).append("&").append(paramName).append("=").append(
                        domainObject.getIdInternal()).append("\">").append(parentUnit.getName())
                .append("</a>").append("</li>");

        if (!subUnits.isEmpty()) {
            buffer.append("<ul class='mvert0 nobullet' id=\"").append("aa").append(
                    parentUnit.getIdInternal()).append("\" ").append("style='display:none'>\r\n");

            Collections.sort(subUnits, new BeanComparator("name"));
        }

        for (Unit subUnit : subUnits) {
            getSubUnitsList(subUnit, buffer, currentDate, request, domainObject, paramName, path);
        }

        if (!subUnits.isEmpty()) {
            buffer.append("</ul>");
        }
    }

    private static List<Unit> getUnitSubUnits(Unit parentUnit, YearMonthDay currentDate) {
        List<AccountabilityTypeEnum> accountabilityEnums = new ArrayList<AccountabilityTypeEnum>();
        accountabilityEnums.add(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);
        accountabilityEnums.add(AccountabilityTypeEnum.ACADEMIC_STRUCTURE);
        return new ArrayList(parentUnit.getActiveSubUnits(currentDate, accountabilityEnums));
    }

    private static void putImage(Unit parentUnit, StringBuilder buffer, HttpServletRequest request) {
        buffer.append("<img ").append("src='").append(request.getContextPath()).append(
                "/images/toggle_plus10.gif' id=\"").append(parentUnit.getIdInternal()).append("\" ")
                .append("indexed='true' onClick=\"").append("check(document.getElementById('").append(
                        "aa").append(parentUnit.getIdInternal()).append("'),document.getElementById('")
                .append(parentUnit.getIdInternal()).append("'));return false;").append("\"> ");
    }
}
