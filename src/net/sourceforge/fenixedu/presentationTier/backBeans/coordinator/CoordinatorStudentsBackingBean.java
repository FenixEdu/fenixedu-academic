package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;

public class CoordinatorStudentsBackingBean extends FenixBackingBean {

    private Integer degreeCurricularPlanID = null;

    private String sortBy = null;

    public Integer getDegreeCurricularPlanID() {
        return (degreeCurricularPlanID == null) ? degreeCurricularPlanID = getAndHoldIntegerParameter("degreeCurricularPlanID") : degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public String getSortBy() {
        return (sortBy == null) ? sortBy = getAndHoldStringParameter("sortBy") : sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public IDegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException, FenixServiceException {
        final Integer degreeCurricularPlanID = getDegreeCurricularPlanID();
        return (IDegreeCurricularPlan) readDomainObject(DegreeCurricularPlan.class, degreeCurricularPlanID);
    }

    public Collection<IStudentCurricularPlan> getStudentCurricularPlans() throws FenixFilterException, FenixServiceException {
        final IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final String sortBy = (getSortBy() != null) ? getSortBy() : "student.number";

        final List<IStudentCurricularPlan> sortedStudentCurricularPlans =
                new ArrayList<IStudentCurricularPlan>(degreeCurricularPlan.getStudentCurricularPlans());
        Collections.sort(sortedStudentCurricularPlans, new BeanComparator(sortBy));
        return sortedStudentCurricularPlans.subList(0, Math.min(100, sortedStudentCurricularPlans.size()));
    }

}
