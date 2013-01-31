package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class UnitsOfDepartmentForVigilantGroup implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		VigilantGroupBean bean = (VigilantGroupBean) source;
		Department department = bean.getSelectedDepartment();

		List<Unit> unitsOfDepartment = new ArrayList<Unit>();

		if (department != null) {
			for (Unit unit : department.getDepartmentUnit().getScientificAreaUnits()) {
				unitsOfDepartment.add(unit);
			}
			unitsOfDepartment.add(department.getDepartmentUnit());
		}

		Collections.sort(unitsOfDepartment, new BeanComparator("name"));
		return unitsOfDepartment;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
