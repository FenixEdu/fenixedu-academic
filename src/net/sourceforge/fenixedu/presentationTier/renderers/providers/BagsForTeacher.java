package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class BagsForTeacher implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		IUserView userView = AccessControl.getUserView();
		Teacher teacher = userView.getPerson().getTeacher();
		List<NewTestModel> testModels = teacher.getTestModels();
		List<NewModelGroup> modelGroups = new ArrayList<NewModelGroup>();
		for (NewTestModel testModel : testModels) {
			modelGroups.add(testModel.getBag());
		}
		return modelGroups;
	}

	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
