package net.sourceforge.fenixedu.presentationTier.backBeans.manager.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans.DegreeManagementBackingBean;

public class ManagerDegreeManagementBackingBean extends DegreeManagementBackingBean {

    public List<Degree> getFilteredPreBolonhaDegrees() {
        final List<Degree> orderedResult = Degree.readOldDegrees();

        final Iterator<Degree> degrees = orderedResult.iterator();
        while (degrees.hasNext()) {
            final Degree degree = degrees.next();
            if (degree.getDegreeType() != DegreeType.DEGREE) {
        	degrees.remove();
            }
        }
        
        Collections.sort(orderedResult, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        return orderedResult;
    }

    @Override
    public List<Degree> getFilteredBolonhaDegrees() {
        final List<Degree> orderedResult = new ArrayList<Degree>(Degree.readBolonhaDegrees());
        Collections.sort(orderedResult, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        return orderedResult;
    }

}
