package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class BagsForTeacher implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        IUserView userView = AccessControl.getUserView();
        Teacher teacher = userView.getPerson().getTeacher();
        Collection<NewTestModel> testModels = teacher.getTestModels();
        List<NewModelGroup> modelGroups = new ArrayList<NewModelGroup>();
        for (NewTestModel testModel : testModels) {
            modelGroups.add(testModel.getBag());
        }
        return modelGroups;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
