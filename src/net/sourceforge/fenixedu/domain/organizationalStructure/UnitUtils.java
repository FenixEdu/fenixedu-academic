/*
 * Created on Feb 6, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu._development.PropertiesManager;

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

    public static Unit readUnitWithoutParentstByName(String name)  {
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
    	ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", new Locale(language, country, variant));   
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
}
