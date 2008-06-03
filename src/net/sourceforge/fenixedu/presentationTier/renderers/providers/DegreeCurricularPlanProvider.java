package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlanProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
    	final List<DegreeCurricularPlan> degreeCurricularPlans = new ArrayList<DegreeCurricularPlan>(RootDomainObject.getInstance().getDegreeCurricularPlansSet());
    	Collections.sort(degreeCurricularPlans, DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME);
        return degreeCurricularPlans; 
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
