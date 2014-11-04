/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.phd.migration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;

public class SearchPhdMigrationProcessBean implements Serializable {

    static private final long serialVersionUID = -5653277152319382139L;

    public static enum SearchCriterion {
        PHD_STUDENT_NUMBER
    }

    public static enum FilterMigratedProcesses {
        FILTER_MIGRATED, FILTER_NOT_MIGRATED, NO_FILTER;

        public String getLocalizedName() {
            return getLocalizedName(I18N.getLocale());
        }

        public String getLocalizedName(final Locale locale) {
            return BundleUtil.getString(Bundle.PHD, locale, getQualifiedName());
        }

        public String getQualifiedName() {
            return getClass().getSimpleName() + "." + name();
        }
    }

    private SearchCriterion searchCriterion = SearchCriterion.PHD_STUDENT_NUMBER;

    private String searchValue;

    private ExecutionYear executionYear;

    private PhdMigrationProcessStateType processState;

    private Integer phdStudentNumber;

    private List<PhdProgram> phdPrograms;

    private Boolean filterPhdPrograms = Boolean.TRUE;

    private List<PhdMigrationIndividualProcessData> processes;

    private Boolean filterPhdProcesses = Boolean.TRUE;

    private FilterMigratedProcesses filterNotMigratedProcesses = FilterMigratedProcesses.NO_FILTER;

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public void setSearchCriterion(SearchCriterion searchCriterion) {
        this.searchCriterion = searchCriterion;
    }

    public SearchCriterion getSearchCriterion() {
        return searchCriterion;
    }

    public Boolean getFilterPhdPrograms() {
        return filterPhdPrograms;
    }

    public void setFilterPhdPrograms(Boolean filterPhdPrograms) {
        this.filterPhdPrograms = filterPhdPrograms;
    }

    public Boolean getFilterPhdProcesses() {
        return filterPhdProcesses;
    }

    public void setFilterPhdProcesses(Boolean filterPhdProcesses) {
        this.filterPhdProcesses = filterPhdProcesses;
    }

    public List<PhdProgram> getPhdPrograms() {
        final List<PhdProgram> result = new ArrayList<PhdProgram>();
        for (final PhdProgram each : this.phdPrograms) {
            result.add(each);
        }

        return result;
    }

    public void setPhdPrograms(List<PhdProgram> phdPrograms) {
        setPhdPrograms((Collection<PhdProgram>) phdPrograms);
    }

    public void setPhdPrograms(Collection<PhdProgram> phdPrograms) {
        final List<PhdProgram> result = new ArrayList<PhdProgram>();
        for (final PhdProgram each : phdPrograms) {
            result.add(each);
        }

        this.phdPrograms = result;
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public PhdMigrationProcessStateType getProcessState() {
        return processState;
    }

    public void setProcessState(PhdMigrationProcessStateType processState) {
        this.processState = processState;
    }

    public List<PhdMigrationIndividualProcessData> getProcesses() {
        final List<PhdMigrationIndividualProcessData> result = new ArrayList<PhdMigrationIndividualProcessData>();
        for (final PhdMigrationIndividualProcessData each : this.processes) {
            result.add(each);
        }

        return result;
    }

    public void setProcesses(List<PhdMigrationIndividualProcessData> processes) {
        final List<PhdMigrationIndividualProcessData> result = new ArrayList<PhdMigrationIndividualProcessData>();
        for (final PhdMigrationIndividualProcessData each : processes) {
            result.add(each);
        }

        this.processes = result;
    }

    public AndPredicate<PhdMigrationIndividualProcessData> getPredicates() {
        if (getSearchValue() != null && !getSearchValue().isEmpty()) {
            String searchValue = getSearchValue().trim();
            setPhdStudentNumber((getSearchCriterion() == SearchCriterion.PHD_STUDENT_NUMBER) ? Integer.valueOf(searchValue) : null);
        }

        final AndPredicate<PhdMigrationIndividualProcessData> result = new AndPredicate<PhdMigrationIndividualProcessData>();

        result.add(getManagedPhdProgramsAndProcessesPredicate());

        if (getPhdStudentNumber() != null) {
            result.add(new InlinePredicate<PhdMigrationIndividualProcessData, Integer>(getPhdStudentNumber()) {
                @Override
                public boolean eval(PhdMigrationIndividualProcessData toEval) {
                    return toEval.getNumber() != null && toEval.getNumber().compareTo(getValue()) == 0;
                }
            });
            return result;
        }

        return getAndPredicate();
    }

    public AndPredicate<PhdMigrationIndividualProcessData> getAndPredicate() {
        final AndPredicate<PhdMigrationIndividualProcessData> result = new AndPredicate<PhdMigrationIndividualProcessData>();

        result.add(getManagedPhdProgramsAndProcessesPredicate());

        if (getExecutionYear() != null) {
            result.add(new InlinePredicate<PhdMigrationIndividualProcessData, ExecutionYear>(getExecutionYear()) {
                @Override
                public boolean eval(PhdMigrationIndividualProcessData toEval) {
                    return toEval.getExecutionYear() == getValue();
                }
            });
        }

        if (getProcessState() != null) {
            result.add(new InlinePredicate<PhdMigrationIndividualProcessData, PhdMigrationProcessStateType>(getProcessState()) {
                @Override
                public boolean eval(PhdMigrationIndividualProcessData toEval) {
                    return toEval.getMigrationStatus() == getValue();
                }
            });
        }

        return result;
    }

    private AndPredicate<PhdMigrationIndividualProcessData> getManagedPhdProgramsAndProcessesPredicate() {
        final AndPredicate<PhdMigrationIndividualProcessData> result = new AndPredicate<PhdMigrationIndividualProcessData>();
        if (getFilterPhdPrograms() != null && getFilterPhdPrograms().booleanValue()) {
            result.add(new InlinePredicate<PhdMigrationIndividualProcessData, List<PhdProgram>>(getPhdPrograms()) {

                @Override
                public boolean eval(PhdMigrationIndividualProcessData toEval) {
                    if (toEval.getProcessBean().hasPhdProgram()) {
                        return getValue().contains(toEval.getProcessBean().getPhdProgram());
                    } else {
                        return false;
                    }
                }
            });
        }

        if (getFilterNotMigratedProcesses() != null && !getFilterNotMigratedProcesses().equals(FilterMigratedProcesses.NO_FILTER)) {
            result.add(new InlinePredicate<PhdMigrationIndividualProcessData, FilterMigratedProcesses>(
                    getFilterNotMigratedProcesses()) {

                @Override
                public boolean eval(PhdMigrationIndividualProcessData toEval) {
                    if (getValue().equals(FilterMigratedProcesses.FILTER_MIGRATED)) {
                        return toEval.isMigratedToIndividualProgramProcess();
                    } else {
                        return !toEval.isMigratedToIndividualProgramProcess();
                    }
                }

            });
        }

        if (getFilterPhdProcesses() != null && getFilterPhdProcesses().booleanValue()) {
            result.add(new InlinePredicate<PhdMigrationIndividualProcessData, List<PhdMigrationIndividualProcessData>>(
                    getProcesses()) {

                @Override
                public boolean eval(PhdMigrationIndividualProcessData toEval) {
                    return getValue().contains(toEval);
                }
            });
        }
        return result;
    }

    public Integer getPhdStudentNumber() {
        return phdStudentNumber;
    }

    public void setPhdStudentNumber(Integer phdStudentNumber) {
        this.phdStudentNumber = phdStudentNumber;
    }

    public FilterMigratedProcesses getFilterNotMigratedProcesses() {
        return filterNotMigratedProcesses;
    }

    public void setFilterNotMigratedProcesses(FilterMigratedProcesses filterNotMigratedProcesses) {
        this.filterNotMigratedProcesses = filterNotMigratedProcesses;
    }
}
