/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto.resourceAllocationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.OccupationPeriodReference;
import org.fenixedu.academic.domain.OccupationPeriodType;
import org.fenixedu.bennu.core.domain.Bennu;

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

    private final String availablePeriodTypes;

    private ExecutionYear executionYear;

    private List<OccupationPeriodBean> periods = new ArrayList<OccupationPeriodBean>();

    private List<ExecutionDegree> degrees;

    private OccupationPeriodType newPeriodType;

    // Actual private elements

    private int idCounter = 0;

    // Typical bean methods

    public PeriodsManagementBean() {
        setExecutionYear(ExecutionYear.readCurrentExecutionYear());
        this.availablePeriodTypes = computeAvailablePeriodTypes();
    }

    private static String computeAvailablePeriodTypes() {
        EnumSet<OccupationPeriodType> types = EnumSet.of(OccupationPeriodType.LESSONS);

        if (EvaluationSeason.readNormalSeason() != null && EvaluationSeason.readImprovementSeason() != null) {
            types.add(OccupationPeriodType.EXAMS);
            types.add(OccupationPeriodType.GRADE_SUBMISSION);
        }

        if (EvaluationSeason.readSpecialSeason() != null) {
            types.add(OccupationPeriodType.EXAMS_SPECIAL_SEASON);
            types.add(OccupationPeriodType.GRADE_SUBMISSION_SPECIAL_SEASON);
        }

        return types.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    public String getAvailablePeriodTypes() {
        return availablePeriodTypes;
    }

    public Collection<ExecutionYear> getYears() {
        List<ExecutionYear> years = new ArrayList<ExecutionYear>(Bennu.getInstance().getExecutionYearsSet());
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

        setDegrees(new ArrayList<ExecutionDegree>(executionYear.getExecutionDegreesSet()));

        Collections.sort(degrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);

        for (ExecutionDegree degree : degrees) {

            Collection<OccupationPeriodReference> references = degree.getOccupationPeriodReferencesSet();

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
