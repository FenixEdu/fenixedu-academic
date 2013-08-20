package net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseLog;
import net.sourceforge.fenixedu.domain.ExecutionCourseLog.ExecutionCourseLogTypes;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.util.Month;
import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class SearchExecutionCourseLogBean implements Serializable {

    private ExecutionCourse executionCourse;
    private Boolean viewPhoto;

    private Collection<Month> months;
    private Collection<Professorship> professorships;
    private Collection<ExecutionCourseLogTypes> executionCourseLogsTypes;
    private Collection<ExecutionCourseLog> executionCourseLogs;

    public String getEnumerationResourcesString(String name) {
        return ResourceBundle.getBundle("resources/EnumerationResources", Language.getLocale()).getString(name);
    }

    public String getApplicationResourcesString(String name) {
        return ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale()).getString(name);
    }

    public SearchExecutionCourseLogBean(ExecutionCourse executionCourse) {
        setExecutionCourse(executionCourse);
        setViewPhoto(true);
        setProfessorships(getExecutionCourse().getProfessorships());
        setExecutionCourseLogTypes(ExecutionCourseLogTypes.valuesAsList());
        setMonths(executionCourse.getExecutionPeriod().getSemesterMonths());
        executionCourseLogs = new ArrayList<ExecutionCourseLog>();
    }

    public ExecutionCourse getExecutionCourse() {
        return this.executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public Boolean getViewPhoto() {
        return viewPhoto;
    }

    public void setViewPhoto(Boolean viewPhoto) {
        this.viewPhoto = viewPhoto;
    }

    public Collection<ExecutionCourseLogTypes> getExecutionCourseLogTypes() {
        return executionCourseLogsTypes;
    }

    public Collection<ExecutionCourseLogTypes> getExecutionCourseLogTypesAll() {
        return ExecutionCourseLogTypes.valuesAsList();
    }

    public void setExecutionCourseLogTypes(Collection<ExecutionCourseLogTypes> executionCourseLogsTypes) {
        this.executionCourseLogsTypes = executionCourseLogsTypes;
    }

    public Collection<Professorship> getProfessorships() {
        Collection<Professorship> pfs = new ArrayList<Professorship>();
        for (Professorship professorship : professorships) {
            pfs.add(professorship);
        }
        return pfs;
    }

    public void setProfessorships(Collection<Professorship> professorships) {
        Collection<Professorship> pfs = new ArrayList<Professorship>();
        for (Professorship pf : professorships) {
            pfs.add(pf);
        }
        this.professorships = pfs;
    }

    public Collection<ExecutionCourseLog> getExecutionCourseLogs() {
        Collection<ExecutionCourseLog> eclogs = new ArrayList<ExecutionCourseLog>();
        for (ExecutionCourseLog executionCourseLog : executionCourseLogs) {
            eclogs.add(executionCourseLog);
        }
        return eclogs;
    }

    public void setExecutionCourseLogs(Collection<ExecutionCourseLog> executionCourseLogs) {
        Collection<ExecutionCourseLog> eclogs = new ArrayList<ExecutionCourseLog>();
        for (ExecutionCourseLog eclog : executionCourseLogs) {
            eclogs.add(eclog);
        }
        this.executionCourseLogs = eclogs;
    }

    public Collection<Month> getMonths() {
        Collection<Month> mon = new ArrayList<Month>();
        for (Month month : months) {
            mon.add(month);
        }
        return mon;
    }

    public void setMonths(Collection<Month> months) {
        Collection<Month> mon = new ArrayList<Month>();
        for (Month mth : months) {
            mon.add(mth);
        }
        this.months = mon;
    }

    public Predicate<ExecutionCourseLog> getFilters() {

        Collection<Predicate<ExecutionCourseLog>> filters = new ArrayList<Predicate<ExecutionCourseLog>>();

        if (getExecutionCourseLogTypes().size() < ExecutionCourseLogTypes.values().length) {
            filters.add(new InlinePredicate<ExecutionCourseLog, Collection<ExecutionCourseLogTypes>>(getExecutionCourseLogTypes()) {

                @Override
                public boolean eval(ExecutionCourseLog executionCourseLog) {
                    return getValue().contains(executionCourseLog.getExecutionCourseLogType());
                }

            });
        }

        if (months.size() < getExecutionCourse().getExecutionPeriod().getSemesterMonths().size()) {
            filters.add(new InlinePredicate<ExecutionCourseLog, Collection<Month>>(getMonths()) {

                @Override
                public boolean eval(ExecutionCourseLog executionCourseLog) {
                    for (Month month : getValue()) {
                        if (month.getNumberOfMonth() == executionCourseLog.getWhenDateTime().getMonthOfYear()) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        if (professorships.size() < getExecutionCourse().getProfessorshipsCount()) {
            filters.add(new InlinePredicate<ExecutionCourseLog, Collection<Professorship>>(getProfessorships()) {

                @Override
                public boolean eval(ExecutionCourseLog executionCourseLog) {
                    for (Professorship pf : getValue()) {
                        if (pf.getPerson().getOid() == executionCourseLog.getPerson().getOid()) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        return new AndPredicate<ExecutionCourseLog>(filters);
    }

    public String getLabel() {

        String logTypeValues = "", professorshipNameValues = "", monthsValues = "";

        for (ExecutionCourseLogTypes logType : ExecutionCourseLogTypes.values()) {
            if (!logTypeValues.isEmpty()) {
                logTypeValues += ", ";
            }
            logTypeValues += getEnumerationResourcesString(logType.getQualifiedName());
        }

        for (Professorship prof : professorships) {
            if (!professorshipNameValues.isEmpty()) {
                professorshipNameValues += ", ";
            }
            professorshipNameValues += prof.getPerson().getPresentationName();
        }

        for (Month month : months) {
            if (!monthsValues.isEmpty()) {
                monthsValues += ", ";
            }
            monthsValues += getEnumerationResourcesString(month.getName());
        }

        return String.format("%s : %s \n%s : %s \n%s : %s", getApplicationResourcesString("log.label.selectLogType"),
                logTypeValues, getApplicationResourcesString("log.label.selectProfessorship"), professorshipNameValues,
                getApplicationResourcesString("log.label.selectMonth"), monthsValues);

    }

    public String getSearchElementsAsParameters() {
        String parameters = "";

        parameters += "&amp;executionCourse=" + getExecutionCourse().getExternalId();
        if (viewPhoto) {
            parameters += "&amp;viewPhoto=true";
        }
        if (getExecutionCourseLogTypes() != null && !getExecutionCourseLogTypes().isEmpty()) {
            parameters += "&amp;executionCourseLogTypes=";
            for (ExecutionCourseLogTypes logType : getExecutionCourseLogTypes()) {
                parameters += logType.toString() + ":";
            }
        }
        if (getProfessorships() != null && !getProfessorships().isEmpty()) {
            parameters += "&amp;professorships=";
            for (Professorship professorship : getProfessorships()) {
                parameters += professorship.getExternalId() + ":";
            }
        }
        if (getMonths() != null && !getMonths().isEmpty()) {
            parameters += "&amp;months=";
            for (Month month : getMonths()) {
                parameters += month.getNumberOfMonth() + ":";
            }
        }

        return parameters;
    }
}
