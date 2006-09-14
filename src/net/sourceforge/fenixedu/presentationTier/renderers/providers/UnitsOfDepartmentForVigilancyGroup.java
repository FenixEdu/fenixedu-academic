package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilancyCourseGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class UnitsOfDepartmentForVigilancyGroup implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) source;
        Department department = bean.getSelectedDepartment();

        List<Unit> unitsOfDepartment;

        if (department != null) {
            unitsOfDepartment = department.getDepartmentUnit().getScientificAreaUnits();
            unitsOfDepartment.add(department.getDepartmentUnit());
        } else {
            unitsOfDepartment = new ArrayList<Unit>();
        }

        Collections.sort(unitsOfDepartment, new BeanComparator("name"));

        return unitsOfDepartment;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
