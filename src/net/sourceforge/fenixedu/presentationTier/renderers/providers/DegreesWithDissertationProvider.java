package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreesWithDissertationProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        
        for (Degree degree : RootDomainObject.getInstance().getDegrees()) {
            switch (degree.getDegreeType()) {
            case DEGREE:
            case MASTER_DEGREE:
            case BOLONHA_INTEGRATED_MASTER_DEGREE:
            case BOLONHA_MASTER_DEGREE:
                break;
            default:
                continue;
            }
            
            planLoop: 
            for (DegreeCurricularPlan plan : degree.getDegreeCurricularPlans()) {
                if (! plan.isActive()) {
                    continue;
                }
                
                for (CurricularCourse course : plan.getCurricularCourses()) {
                    if (course.isDissertation()) {
                        degrees.add(degree);
                        break planLoop;
                    }
                }
            }
        }

        return degrees;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
