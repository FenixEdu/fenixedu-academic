package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class LibraryCardUnitNamesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        LibraryCardDTO libraryCardDTO = (LibraryCardDTO) source;
        List<String> unitNameList = new ArrayList<String>();
        if (!StringUtils.isEmpty(libraryCardDTO.getUnit().getAcronym())) {
            unitNameList.add(libraryCardDTO.getUnit().getAcronym());
        }
        for (Unit unit : libraryCardDTO.getUnit().getParentUnits()) {
            if (unit.getName().length() > 35 && !StringUtils.isEmpty(unit.getAcronym())) {
                unitNameList.add(unit.getAcronym());
            } else {
                unitNameList.add(unit.getName());
            }
        }
        return unitNameList;
    }

    public Converter getConverter() {
        return null;
    }

}
