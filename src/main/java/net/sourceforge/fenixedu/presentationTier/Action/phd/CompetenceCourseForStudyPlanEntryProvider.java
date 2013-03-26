package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.phd.PhdStudyPlanEntryBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CompetenceCourseForStudyPlanEntryProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object current) {
        final PhdStudyPlanEntryBean bean = (PhdStudyPlanEntryBean) source;

        return bean.getDegree() != null ? bean.getDegree().getLastActiveDegreeCurricularPlan().getCompetenceCourses() : Collections.EMPTY_LIST;

    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }
}
