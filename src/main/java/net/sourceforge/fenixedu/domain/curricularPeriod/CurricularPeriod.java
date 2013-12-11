package net.sourceforge.fenixedu.domain.curricularPeriod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.util.CurricularPeriodLabelFormatter;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 * 
 */
public class CurricularPeriod extends CurricularPeriod_Base implements Comparable<CurricularPeriod> {

    static {
        getRelationCurricularPeriodParentChilds().addListener(new CurricularPeriodParentChildsListener());
    }

    public CurricularPeriod(AcademicPeriod academicPeriod) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setAcademicPeriod(academicPeriod);
    }

    public CurricularPeriod(AcademicPeriod academicPeriod, Integer order, CurricularPeriod parent) {
        this(academicPeriod);
        setChildOrder(order);
        setParent(parent);

    }

    public List<CurricularPeriod> getSortedChilds() {
        List<CurricularPeriod> sortedChilds = new ArrayList<CurricularPeriod>();
        sortedChilds.addAll(getChilds());
        Collections.sort(sortedChilds);
        return sortedChilds;
    }

    public CurricularPeriod getChildByOrder(Integer order) {

        for (CurricularPeriod curricularPeriod : getChilds()) {
            if (curricularPeriod.getChildOrder().equals(order)) {
                return curricularPeriod;
            }
        }

        return null;
    }

    private CurricularPeriod findChild(AcademicPeriod academicPeriod, Integer order) {

        for (CurricularPeriod curricularPeriod : getChilds()) {
            if (curricularPeriod.getChildOrder().equals(order) && curricularPeriod.getAcademicPeriod().equals(academicPeriod)) {
                return curricularPeriod;
            }
        }

        return null;
    }

    public CurricularPeriod getCurricularPeriod(CurricularPeriodInfoDTO... curricularPeriodsPaths) {

        validatePath(curricularPeriodsPaths);

        CurricularPeriod curricularPeriod = this;

        for (CurricularPeriodInfoDTO path : curricularPeriodsPaths) {
            curricularPeriod = curricularPeriod.findChild(path.getPeriodType(), path.getOrder());

            if (curricularPeriod == null) {
                return null;
            }
        }

        return curricularPeriod;
    }

    public CurricularPeriod addCurricularPeriod(CurricularPeriodInfoDTO... curricularPeriodsPaths) {

        validatePath(curricularPeriodsPaths);

        CurricularPeriod curricularPeriod = null;
        CurricularPeriod curricularPeriodParent = this;

        for (CurricularPeriodInfoDTO path : curricularPeriodsPaths) {
            curricularPeriod = curricularPeriodParent.findChild(path.getPeriodType(), path.getOrder());

            if (curricularPeriod == null) {
                curricularPeriod = new CurricularPeriod(path.getPeriodType(), path.getOrder(), curricularPeriodParent);
            }

            curricularPeriodParent = curricularPeriod;
        }

        return curricularPeriod;
    }

    public Integer getOrderByType(AcademicPeriod academicPeriod) {

        Integer resultOrder = null;

        if (this.getAcademicPeriod().equals(academicPeriod)) {
            resultOrder = this.getChildOrder();
        } else if (this.getParent() != null
                && this.getParent().getAcademicPeriod().getWeight() > this.getAcademicPeriod().getWeight()) {
            resultOrder = (this.getParent()).getOrderByType(academicPeriod);
        }

        return resultOrder;
    }

    private void validatePath(CurricularPeriodInfoDTO... curricularPeriodsPaths) {

        Arrays.sort(curricularPeriodsPaths, new Comparator<CurricularPeriodInfoDTO>() {
            @Override
            public int compare(CurricularPeriodInfoDTO c1, CurricularPeriodInfoDTO c2) {
                if (c1.getPeriodType().getWeight() > c2.getPeriodType().getWeight()) {
                    return -1;
                } else if (c1.getPeriodType().getWeight() < c2.getPeriodType().getWeight()) {
                    return 1;
                }
                throw new DomainException("error.pathShouldNotHaveSameTypePeriods");
            }

        });
    }

    public void delete() {

        getContexts().clear();
        setDegreeCurricularPlan(null);

        setParent(null);
        for (CurricularPeriod child : getChilds()) {
            child.delete();
        }

        setRootDomainObject(null);
        deleteDomainObject();

    }

    public String getLabel() {
        return CurricularPeriodLabelFormatter.getLabel(this, false);
    }

    public String getFullLabel() {
        return CurricularPeriodLabelFormatter.getFullLabel(this, false);
    }

    public String getFullLabel(final Locale locale) {
        return CurricularPeriodLabelFormatter.getFullLabelI18N(this, false, locale);
    }

    @Override
    public int compareTo(CurricularPeriod o) {
        return this.getFullWeight().compareTo(o.getFullWeight());
    }

    private Float getWeight() {
        float periodTypeWeight = (this.getAcademicPeriod() == null) ? 0 : this.getAcademicPeriod().getWeight();
        float periodOrder = (this.getChildOrder() == null) ? 0 : this.getChildOrder();
        return periodTypeWeight * periodOrder;
    }

    private Float getFullWeight() {
        return this.getWeight() + this.collectParentsWeight(this);
    }

    private Float collectParentsWeight(CurricularPeriod period) {
        Float result = Float.valueOf(0);

        if (period.hasParent()) {
            result = period.getParent().getWeight() + collectParentsWeight(period.getParent());
        }

        return result;
    }

    private static class CurricularPeriodParentChildsListener extends RelationAdapter<CurricularPeriod, CurricularPeriod> {
        @Override
        public void beforeAdd(CurricularPeriod parent, CurricularPeriod child) {
            if (parent == null) {
                return;
            }

            if (child.getAcademicPeriod().getWeight() >= parent.getAcademicPeriod().getWeight()) {
                throw new DomainException("error.childTypeGreaterThanParentType");
            }

            float childsWeight = child.getAcademicPeriod().getWeight();
            for (CurricularPeriod period : parent.getChilds()) {
                childsWeight += period.getAcademicPeriod().getWeight();
            }

            if (childsWeight > parent.getAcademicPeriod().getWeight()) {
                throw new DomainException("error.childWeightOutOfLimit");
            }

            // re-order childs
            Integer order = child.getChildOrder();
            if (order == null) {
                child.setChildOrder(parent.getChildsSet().size() + 1);
            } else {
                if (parent.getChildByOrder(order) != null) {
                    throw new DomainException("error.childAlreadyExists");
                }
            }
        }
    }

    public Integer getParentOrder() {
        if (this.getParent() != null) {
            return this.getParent().getChildOrder();
        }

        return null;
    }

    public CurricularPeriod getNext() {
        List<CurricularPeriod> brothers = this.getParent().getSortedChilds();

        for (Iterator<CurricularPeriod> iterator = brothers.iterator(); iterator.hasNext();) {
            CurricularPeriod brother = iterator.next();

            if (brother.getChildOrder().equals(this.getChildOrder()) && iterator.hasNext()) {
                return iterator.next();
            }
        }
        return null;
    }

    public CurricularPeriod contains(AcademicPeriod academicPeriod, Integer order) {
        if (this.getAcademicPeriod().equals(academicPeriod) && this.getChildOrder().equals(order)) {
            return this;
        }
        for (CurricularPeriod curricularPeriod : getChilds()) {
            CurricularPeriod period = curricularPeriod.contains(academicPeriod, order);
            if (period != null) {
                return period;
            }
        }
        return null;
    }

    public boolean hasCurricularPeriod(AcademicPeriod academicPeriod, Integer order) {
        if (this.getAcademicPeriod().equals(academicPeriod) && this.getChildOrder().equals(order)) {
            return true;
        }
        if (getParent() != null) {
            return getParent().hasCurricularPeriod(academicPeriod, order);
        } else {
            return false;
        }
    }

    @Deprecated
    public Integer getOrder() {
        return super.getChildOrder();
    }

    @Deprecated
    public void setOrder(Integer order) {
        super.setChildOrder(order);
    }

    public int getAbsoluteOrderOfChild() {
        if (getChildOrder() == null) {
            return 1;
        } else {
            final CurricularPeriod parentCurricularPeriod = getParent();
            final int absoluteOrderOfParent = parentCurricularPeriod.getAbsoluteOrderOfChild();
            final int numberOfBrothersAndSisters = parentCurricularPeriod.getChildsSet().size();
            return (absoluteOrderOfParent - 1) * numberOfBrothersAndSisters + getChildOrder().intValue();
        }
    }

    public List<Context> getContextsWithCurricularCourses() {
        return getContextsWithCurricularCourses(null);
    }

    public List<Context> getContextsWithCurricularCourses(final ExecutionSemester executionSemester) {
        return getChildContexts(CurricularCourse.class, executionSemester);
    }

    public List<Context> getChildContexts(final Class<? extends DegreeModule> clazz, final ExecutionSemester executionSemester) {
        final List<Context> result = new ArrayList<Context>();
        for (final Context context : super.getContextsSet()) {
            if ((clazz == null || clazz.isAssignableFrom(context.getChildDegreeModule().getClass()))
                    && (executionSemester == null || context.isValid(executionSemester))) {
                result.add(context);
            }
        }
        return result;
    }

    public boolean hasChildOrder() {
        return getChildOrder() != null;
    }

    public boolean hasChildOrderValue(final Integer order) {
        return hasChildOrder() && getChildOrder().equals(order);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.Context> getContexts() {
        return getContextsSet();
    }

    @Deprecated
    public boolean hasAnyContexts() {
        return !getContextsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod> getChilds() {
        return getChildsSet();
    }

    @Deprecated
    public boolean hasAnyChilds() {
        return !getChildsSet().isEmpty();
    }

    @Deprecated
    public boolean hasParent() {
        return getParent() != null;
    }

    @Deprecated
    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasAcademicPeriod() {
        return getAcademicPeriod() != null;
    }

}
