package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OccupationPeriodReference;
import net.sourceforge.fenixedu.domain.OccupationPeriodType;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class PeriodsManagementBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6463983445360741454L;

    // Bean elements

    private ExecutionYear executionYear;

    private List<OccupationPeriodBean> periods = new ArrayList<OccupationPeriodBean>();

    private List<ExecutionDegree> degrees;

    private OccupationPeriodType newPeriodType;

    // Actual private elements

    private int idCounter = 0;

    // Typical bean methods

    public PeriodsManagementBean() {
        setExecutionYear(ExecutionYear.readCurrentExecutionYear());
    }

    public Collection<ExecutionYear> getYears() {
        List<ExecutionYear> years = new ArrayList<ExecutionYear>(RootDomainObject.getInstance().getExecutionYears());
        Collections.sort(years);
        return years;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
        populatePeriodsForExecutionYear();
    }

    public List<OccupationPeriodBean> getPeriods() {
        return periods;
    }

    public void setPeriods(List<OccupationPeriodBean> periods) {
        this.periods = periods;
    }

    public List<ExecutionDegree> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<ExecutionDegree> degrees) {
        this.degrees = degrees;
    }

    public OccupationPeriodType getNewPeriodType() {
        return newPeriodType;
    }

    public void setNewPeriodType(OccupationPeriodType newPeriodType) {
        this.newPeriodType = newPeriodType;
    }

    // Utility methods

    public OccupationPeriodBean getBeanById(String idStr) {
        final int id = Integer.parseInt(idStr);
        return Iterables.find(getPeriods(), new Predicate<OccupationPeriodBean>() {
            @Override
            public boolean apply(OccupationPeriodBean bean) {
                return bean.getId() == id;
            }
        });
    }

    public void removePeriod(String parameter) {
        OccupationPeriodBean bean = getBeanById(parameter);
        periods.remove(bean);
        bean.deletePeriod();
    }

    public void addNewBean() {
        periods.add(0, new OccupationPeriodBean(idCounter++));
    }

    // Method that creates the period clusters

    private void populatePeriodsForExecutionYear() {

        periods.clear();

        Multimap<OccupationPeriodType, OccupationPeriodBean> map = HashMultimap.create();

        setDegrees(new ArrayList<ExecutionDegree>(executionYear.getExecutionDegrees()));

        Collections.sort(degrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);

        for (ExecutionDegree degree : degrees) {

            Collection<OccupationPeriodReference> references = degree.getOccupationPeriodReferences();

            for (OccupationPeriodReference reference : references) {

                OccupationPeriodBean bean = null;

                for (OccupationPeriodBean periodBean : map.get(reference.getPeriodType())) {
                    if (periodBean.getOccupationPeriod().isEqualTo(reference.getOccupationPeriod())) {
                        bean = periodBean;
                        break;
                    }
                }

                if (bean == null) {
                    bean = new OccupationPeriodBean(reference, idCounter++);
                    map.put(reference.getPeriodType(), bean);
                }

                bean.addReference(reference);
            }

        }

        periods.addAll(map.values());

        Collections.sort(periods);

    }

    public void duplicatePeriod(String oldId) {
        OccupationPeriodBean bean = getBeanById(oldId);
        periods.add(bean.duplicate(idCounter++, newPeriodType));

        Collections.sort(periods);
    }

    public void removeNewBean() {
        if (periods.iterator().next().getNewObject()) {
            periods.remove(0);
        }
    }

    public boolean getHasNewObject() {
        return !periods.isEmpty() && periods.iterator().next().getNewObject();
    }

}
