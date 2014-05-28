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
package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;

public class SearchPhdIndividualProgramProcessBean implements Serializable {

    static private final long serialVersionUID = -5653277152319382139L;

    public static enum SearchCriterion {
        PROCESS_NUMBER, STUDENT_NUMBER, NAME, PHD_STUDENT_NUMBER
    }

    public static enum OnlineApplicationFilter {
        ONLY_ONLINE, EXCLUDE_ONLINE
    }

    private SearchCriterion searchCriterion = SearchCriterion.PROCESS_NUMBER;

    private String searchValue;

    private ExecutionYear executionYear;

    private PhdIndividualProgramProcessState processState;

    private String processNumber;

    private Integer studentNumber;

    private Integer phdStudentNumber;

    private List<PhdProgram> phdPrograms;

    private Boolean filterPhdPrograms = Boolean.TRUE;

    private List<PhdIndividualProgramProcess> processes;

    private Boolean filterPhdProcesses = Boolean.TRUE;

    private String name;

    private PhdProgramCandidacyProcessState candidacyProcessState;

    private PhdThesisProcessStateType thesisProcessState;

    private PhdProgram phdProgram;

    private OnlineApplicationFilter onlineApplicationFilter;

    private PhdCandidacyPeriod phdCandidacyPeriod;

    private PhdIndividualProgramCollaborationType phdCollaborationType;

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

    public PhdProgramCandidacyProcessState getCandidacyProcessState() {
        return candidacyProcessState;
    }

    public void setCandidacyProcessState(PhdProgramCandidacyProcessState candidacyProcessState) {
        this.candidacyProcessState = candidacyProcessState;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public PhdIndividualProgramProcessState getProcessState() {
        return processState;
    }

    public void setProcessState(PhdIndividualProgramProcessState processState) {
        this.processState = processState;
    }

    public String getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(String processNumber) {
        this.processNumber = processNumber;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public PhdThesisProcessStateType getThesisProcessState() {
        return thesisProcessState;
    }

    public void setThesisProcessState(PhdThesisProcessStateType thesisProcessStateType) {
        this.thesisProcessState = thesisProcessStateType;
    }

    public PhdCandidacyPeriod getPhdCandidacyPeriod() {
        return phdCandidacyPeriod;
    }

    public void setPhdCandidacyPeriod(PhdCandidacyPeriod phdCandidacyPeriod) {
        this.phdCandidacyPeriod = phdCandidacyPeriod;
    }

    public List<PhdIndividualProgramProcess> getProcesses() {
        final List<PhdIndividualProgramProcess> result = new ArrayList<PhdIndividualProgramProcess>();
        for (final PhdIndividualProgramProcess each : this.processes) {
            result.add(each);
        }

        return result;
    }

    public void setProcesses(List<PhdIndividualProgramProcess> processes) {
        final List<PhdIndividualProgramProcess> result = new ArrayList<PhdIndividualProgramProcess>();
        for (final PhdIndividualProgramProcess each : processes) {
            result.add(each);
        }

        this.processes = result;
    }

    public AndPredicate<PhdIndividualProgramProcess> getPredicates() {
        if (getSearchValue() != null && !getSearchValue().isEmpty()) {
            String searchValue = getSearchValue().trim();
            setProcessNumber((getSearchCriterion() == SearchCriterion.PROCESS_NUMBER) ? searchValue : null);
            setStudentNumber((getSearchCriterion() == SearchCriterion.STUDENT_NUMBER) ? Integer.valueOf(searchValue) : null);
            setPhdStudentNumber((getSearchCriterion() == SearchCriterion.PHD_STUDENT_NUMBER) ? Integer.valueOf(searchValue) : null);
            setName((getSearchCriterion() == SearchCriterion.NAME) ? searchValue : null);
        }

        final AndPredicate<PhdIndividualProgramProcess> result = new AndPredicate<PhdIndividualProgramProcess>();

        result.add(getManagedPhdProgramsAndProcessesPredicate());

        if (getStudentNumber() != null) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, Integer>(getStudentNumber()) {
                @Override
                public boolean eval(PhdIndividualProgramProcess toEval) {
                    return toEval.getStudent() != null && toEval.getStudent().getNumber().compareTo(getValue()) == 0;
                }
            });
            return result;
        }

        if (!StringUtils.isEmpty(getProcessNumber())) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, String>(getProcessNumber()) {
                @Override
                public boolean eval(PhdIndividualProgramProcess toEval) {
                    return toEval.getProcessNumber().equals(getValue());
                }
            });
            return result;
        }

        if (getPhdStudentNumber() != null) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, Integer>(getPhdStudentNumber()) {
                @Override
                public boolean eval(PhdIndividualProgramProcess toEval) {
                    return toEval.getPhdStudentNumber() != null && toEval.getPhdStudentNumber().compareTo(getValue()) == 0;
                }
            });
            return result;
        }

        if (!StringUtils.isEmpty(getName())) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, String>(getName()) {
                @Override
                public boolean eval(PhdIndividualProgramProcess toEval) {
                    return Person.findPerson(getValue()).contains(toEval.getPerson());
                }
            });
            return result;
        }

        result.add(getAndPredicate());

        return result;
    }

    public AndPredicate<PhdIndividualProgramProcess> getAndPredicate() {
        final AndPredicate<PhdIndividualProgramProcess> result = new AndPredicate<PhdIndividualProgramProcess>();

        result.add(getManagedPhdProgramsAndProcessesPredicate());

        if (getExecutionYear() != null) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, ExecutionYear>(getExecutionYear()) {
                @Override
                public boolean eval(PhdIndividualProgramProcess toEval) {
                    return toEval.getExecutionYear() == getValue();
                }
            });
        }

        if (getPhdProgram() != null) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, PhdProgram>(getPhdProgram()) {

                @Override
                public boolean eval(PhdIndividualProgramProcess process) {
                    return process.getPhdProgram() == getValue();
                }

            });
        }

        if (getProcessState() != null) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, PhdIndividualProgramProcessState>(getProcessState()) {
                @Override
                public boolean eval(PhdIndividualProgramProcess toEval) {
                    return toEval.getActiveState() == getValue();
                }
            });
        }

        if (PhdIndividualProgramProcessState.CANDIDACY.equals(getProcessState()) && getCandidacyProcessState() != null) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, PhdProgramCandidacyProcessState>(
                    getCandidacyProcessState()) {
                @Override
                public boolean eval(PhdIndividualProgramProcess process) {
                    return getValue().equals(process.getCandidacyProcess().getActiveState());
                }
            });
        }

        if (PhdIndividualProgramProcessState.THESIS_DISCUSSION.equals(getProcessState()) && getThesisProcessState() != null) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, PhdThesisProcessStateType>(getThesisProcessState()) {
                @Override
                public boolean eval(PhdIndividualProgramProcess process) {
                    return process.getThesisProcess() != null && getValue().equals(process.getThesisProcess().getActiveState());
                }
            });
        }

        if (OnlineApplicationFilter.EXCLUDE_ONLINE.equals(getOnlineApplicationFilter())) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, String>(null) {

                @Override
                public boolean eval(PhdIndividualProgramProcess process) {
                    return !process.getCandidacyProcess().isPublicCandidacy();
                }

            });
        }

        if (OnlineApplicationFilter.ONLY_ONLINE.equals(getOnlineApplicationFilter())) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, String>(null) {

                @Override
                public boolean eval(PhdIndividualProgramProcess process) {
                    return process.getCandidacyProcess().isPublicCandidacy();
                }

            });

            if (getPhdCandidacyPeriod() != null) {
                result.add(new InlinePredicate<PhdIndividualProgramProcess, PhdCandidacyPeriod>(getPhdCandidacyPeriod()) {
                    @Override
                    public boolean eval(PhdIndividualProgramProcess process) {
                        return process.getCandidacyProcess().getPublicPhdCandidacyPeriod() == getValue();
                    }
                });
            }
        }

        if (getPhdCollaborationType() != null) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, PhdIndividualProgramCollaborationType>(
                    getPhdCollaborationType()) {

                @Override
                public boolean eval(PhdIndividualProgramProcess process) {
                    return process.getCollaborationType() == getValue();
                }

            });
        }

        return result;
    }

    private AndPredicate<PhdIndividualProgramProcess> getManagedPhdProgramsAndProcessesPredicate() {
        final AndPredicate<PhdIndividualProgramProcess> result = new AndPredicate<PhdIndividualProgramProcess>();
        if (getFilterPhdPrograms() != null && getFilterPhdPrograms().booleanValue()) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, List<PhdProgram>>(getPhdPrograms()) {

                @Override
                public boolean eval(PhdIndividualProgramProcess toEval) {
                    if (toEval.hasPhdProgram()) {
                        return getValue().contains(toEval.getPhdProgram());
                    } else if (toEval.hasPhdProgramFocusArea()) {
                        return !CollectionUtils.intersection(getValue(), toEval.getPhdProgramFocusArea().getPhdPrograms())
                                .isEmpty();
                    } else {
                        return false;
                    }
                }
            });
        }

        if (getFilterPhdProcesses() != null && getFilterPhdProcesses().booleanValue()) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, List<PhdIndividualProgramProcess>>(getProcesses()) {

                @Override
                public boolean eval(PhdIndividualProgramProcess toEval) {
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

    public PhdProgram getPhdProgram() {
        return phdProgram;
    }

    public void setPhdProgram(PhdProgram phdProgram) {
        this.phdProgram = phdProgram;
    }

    public OnlineApplicationFilter getOnlineApplicationFilter() {
        return onlineApplicationFilter;
    }

    public void setOnlineApplicationFilter(OnlineApplicationFilter onlineApplicationFilter) {
        this.onlineApplicationFilter = onlineApplicationFilter;
    }

    public PhdIndividualProgramCollaborationType getPhdCollaborationType() {
        return phdCollaborationType;
    }

    public void setPhdCollaborationType(PhdIndividualProgramCollaborationType phdCollaborationType) {
        this.phdCollaborationType = phdCollaborationType;
    }
}
