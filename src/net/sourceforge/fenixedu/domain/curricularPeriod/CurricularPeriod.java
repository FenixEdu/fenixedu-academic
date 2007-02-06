package net.sourceforge.fenixedu.domain.curricularPeriod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.CurricularPeriodLabelFormatter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 * 
 */
public class CurricularPeriod extends CurricularPeriod_Base implements Comparable<CurricularPeriod> {

    static {
        CurricularPeriodParentChilds.addListener(new CurricularPeriodParentChildsListener());
    }

    public CurricularPeriod(CurricularPeriodType curricularPeriodType) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setPeriodType(curricularPeriodType);
    }

    public CurricularPeriod(CurricularPeriodType curricularPeriodType, Integer order,
            CurricularPeriod parent) {
        this(curricularPeriodType);
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

    private CurricularPeriod findChild(CurricularPeriodType periodType, Integer order) {

        for (CurricularPeriod curricularPeriod : getChilds()) {
            if (curricularPeriod.getChildOrder().equals(order)
                    && curricularPeriod.getPeriodType() == periodType) {
                return curricularPeriod;
            }
        }

        return null;
    }

    public CurricularPeriod getCurricularPeriod(CurricularPeriodInfoDTO... curricularPeriodsPaths) {

        validatePath(curricularPeriodsPaths);

        CurricularPeriod curricularPeriod = this;

        for (CurricularPeriodInfoDTO path : curricularPeriodsPaths) {
            curricularPeriod = (CurricularPeriod) curricularPeriod.findChild(path.getPeriodType(), path
                    .getOrder());

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
            curricularPeriod = (CurricularPeriod) curricularPeriodParent.findChild(path.getPeriodType(),
                    path.getOrder());

            if (curricularPeriod == null) {
                curricularPeriod = new CurricularPeriod(path.getPeriodType(), path.getOrder(),
                        curricularPeriodParent);
            }

            curricularPeriodParent = curricularPeriod;
        }

        return curricularPeriod;
    }

    public Integer getOrderByType(CurricularPeriodType curricularPeriodType) {

        Integer resultOrder = null;

        if (this.getPeriodType() == curricularPeriodType) {
            resultOrder = this.getChildOrder();
        } else if (this.getParent() != null
                && this.getParent().getPeriodType().getWeight() > this.getPeriodType().getWeight()) {
            resultOrder = ((CurricularPeriod) this.getParent()).getOrderByType(curricularPeriodType);
        }

        return resultOrder;
    }

    private void validatePath(CurricularPeriodInfoDTO... curricularPeriodsPaths) {

        Arrays.sort(curricularPeriodsPaths, new Comparator<CurricularPeriodInfoDTO>() {
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
        removeDegreeCurricularPlan();

        for (CurricularPeriod child : getChilds()) {
            child.delete();
        }
        removeRootDomainObject();
        deleteDomainObject();

    }

    public String getLabel() {
        return CurricularPeriodLabelFormatter.getLabel(this, false);
    }

    public String getFullLabel() {
        return CurricularPeriodLabelFormatter.getFullLabel(this, false);
    }

    public int compareTo(CurricularPeriod o) {
        return this.getFullWeight().compareTo(o.getFullWeight());
    }

    private Float getWeight() {
        float periodTypeWeight = (this.getPeriodType() == null) ? 0 : this.getPeriodType().getWeight();
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

    private static class CurricularPeriodParentChildsListener extends
            dml.runtime.RelationAdapter<CurricularPeriod, CurricularPeriod> {
        @Override
        public void beforeAdd(CurricularPeriod parent, CurricularPeriod child) {

            if (child.getPeriodType().getWeight() >= parent.getPeriodType().getWeight()) {
                throw new DomainException("error.childTypeGreaterThanParentType");
            }

            float childsWeight = child.getPeriodType().getWeight();
            for (CurricularPeriod period : parent.getChilds()) {
                childsWeight += period.getPeriodType().getWeight();
            }

            if (childsWeight > parent.getPeriodType().getWeight()) {
                throw new DomainException("error.childWeightOutOfLimit");
            }

            // re-order childs
            Integer order = child.getChildOrder();
            if (order == null) {
                child.setChildOrder(parent.getChildsCount() + 1);
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

    public CurricularPeriod contains(CurricularPeriodType periodType, Integer order) {
        if (this.getPeriodType().equals(periodType) && this.getChildOrder().equals(order)) {
            return this;
        }
        for (CurricularPeriod curricularPeriod : getChilds()) {
            CurricularPeriod period = curricularPeriod.contains(periodType, order);
            if (period != null) {
                return period;
            }
        }
        return null;
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
            final int numberOfBrothersAndSisters = parentCurricularPeriod.getChildsCount();
            return (absoluteOrderOfParent - 1) * numberOfBrothersAndSisters + getChildOrder().intValue();
        }
    }
    
    public List<Context> getContextsWithCurricularCourses() {
	return getContextsWithCurricularCourses(null);
    }
    
    public List<Context> getContextsWithCurricularCourses(final ExecutionPeriod executionPeriod) {
	return getChildContexts(CurricularCourse.class, executionPeriod);
    }

    public List<Context> getChildContexts(final Class<? extends DegreeModule> clazz, final ExecutionPeriod executionPeriod) {
        final List<Context> result = new ArrayList<Context>();
        for (final Context context : super.getContextsSet()) {
            if ((clazz == null || clazz.isAssignableFrom(context.getChildDegreeModule().getClass())) 
        	    && (executionPeriod == null || context.isValid(executionPeriod))) {
        	result.add(context);
            }
        }
        return result;
    }
}
