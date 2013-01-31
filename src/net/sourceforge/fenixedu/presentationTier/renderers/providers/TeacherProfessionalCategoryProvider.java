package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TeacherProfessionalCategoryProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object arg0, Object arg1) {
		List<ProfessionalCategory> result = new ArrayList<ProfessionalCategory>();
		for (ProfessionalCategory professionalCategory : RootDomainObject.getInstance().getProfessionalCategories()) {
			if (professionalCategory.getCategoryType().equals(CategoryType.TEACHER)) {
				result.add(professionalCategory);
			}
		}
		return result;
	}

}
