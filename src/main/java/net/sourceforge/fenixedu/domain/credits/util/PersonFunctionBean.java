package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunctionShared;
import net.sourceforge.fenixedu.domain.organizationalStructure.SharedFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixframework.Atomic;

public class PersonFunctionBean implements Serializable {
    private ExecutionSemester executionSemester;
    private Teacher teacher;
    private Unit unit;
    private Function function;
    private BigDecimal percentage;
    private BigDecimal credits;
    private PersonFunction personFunction;

    public PersonFunctionBean(Teacher teacher, ExecutionSemester executionSemester) {
        super();
        setExecutionSemester(executionSemester);
        setTeacher(teacher);
    }

    public PersonFunctionBean(PersonFunction personFunction) {
        setExecutionSemester((ExecutionSemester) personFunction.getExecutionInterval());
        setTeacher(((Person) personFunction.getChildParty()).getTeacher());
        setUnit((Unit) personFunction.getParentParty());
        setFunction(personFunction);
        setPersonFunction(personFunction);
    }

    public PersonFunctionBean(PersonFunction personFunction, ExecutionSemester executionSemester) {
        setExecutionSemester(executionSemester);
        setTeacher(((Person) personFunction.getChildParty()).getTeacher());
        setUnit((Unit) personFunction.getParentParty());
        setFunction(personFunction);
        setPersonFunction(personFunction);
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setUnitName(UnitName unitName) {
        setUnit(unitName.getUnit());
    }

    public UnitName getUnitName() {
        return getUnit() == null ? null : getUnit().getUnitName();
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(PersonFunction personFunction) {
        this.function = personFunction.getFunction();
        if (personFunction instanceof PersonFunctionShared) {
            setPercentage(((PersonFunctionShared) personFunction).getPercentage());
        } else {
            setCredits(new BigDecimal(personFunction.getCredits()));
        }
    }

    public void setFunction(Function function) {
        this.function = function;
        PersonFunction personFunction = getPersonFunction();
        if (personFunction != null) {
            if (personFunction instanceof PersonFunctionShared) {
                setPercentage(((PersonFunctionShared) personFunction).getPercentage());
            } else {
                setCredits(new BigDecimal(personFunction.getCredits()));
            }
        }
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    @Atomic
    public void createOrEditPersonFunction() {
        PersonFunction thisPersonFunction = getPersonFunction();
        final StringBuilder log = new StringBuilder();
        if (getFunction() instanceof SharedFunction) {
            BigDecimal availablePercentage = new BigDecimal(100);
            PersonFunctionShared thisPersonFunctionShared = (PersonFunctionShared) thisPersonFunction;
            for (PersonFunctionShared personFunctionShared : getPersonFunctionsShared()) {
                if (personFunctionShared.getExecutionInterval().equals(getExecutionSemester())
                        && (thisPersonFunctionShared == null || !thisPersonFunctionShared.equals(personFunctionShared))) {
                    availablePercentage = availablePercentage.subtract(personFunctionShared.getPercentage());
                }
            }
            if (getPercentage().compareTo(availablePercentage) > 0) {
                throw new DomainException("message.exceeded.percentage");
            }
            if (thisPersonFunctionShared == null) {
                new PersonFunctionShared(getUnit(), getTeacher().getPerson(), (SharedFunction) getFunction(),
                        getExecutionSemester(), getPercentage());
            } else {
                thisPersonFunctionShared.setPercentage(getPercentage());
            }
            log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                    "label.teacher.personFunction.createOrEdit", getFunction().getName(), getUnit().getPresentationName(),
                    getTeacher().getPerson().getNickname(), getPercentage().toString()));
        } else {
            if (thisPersonFunction == null) {
                new PersonFunction(getUnit(), getTeacher().getPerson(), getFunction(), getExecutionSemester(), getCredits()
                        .doubleValue());
            } else {
                thisPersonFunction.setCredits(getCredits().doubleValue());
            }
            log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                    "label.teacher.personFunction.createOrEdit", getFunction().getName(), getUnit().getPresentationName(),
                    getTeacher().getPerson().getNickname(), getCredits().toString()));

        }
        TeacherService teacherService = getTeacher().getTeacherServiceByExecutionPeriod(getExecutionSemester());
        if (teacherService == null) {
            teacherService = new TeacherService(getTeacher(), getExecutionSemester());
        }
        new TeacherServiceLog(teacherService, log.toString());
    }

    @Atomic
    public void deletePersonFunction() {
        if (getExecutionSemester() == null) {
            List<ExecutionSemester> executionSemesters =
                    ExecutionSemester.readExecutionPeriod(getPersonFunction().getBeginDate(), getPersonFunction().getEndDate());
            ExecutionSemester actualExecutionSemester = ExecutionSemester.readActualExecutionSemester();
            for (ExecutionSemester executionSemester : executionSemesters) {
                if (executionSemester.isBeforeOrEquals(actualExecutionSemester)) {
                    createLogForDeletePersonFunction(executionSemester);
                }
            }
        } else {
            createLogForDeletePersonFunction(getExecutionSemester());
        }
        getPersonFunction().delete();
    }

    protected void createLogForDeletePersonFunction(ExecutionSemester executionSemester) {
        TeacherService teacherService = getTeacher().getTeacherServiceByExecutionPeriod(executionSemester);
        if (teacherService == null) {
            teacherService = new TeacherService(getTeacher(), getExecutionSemester());
        }
        if (getFunction() instanceof SharedFunction) {
            new TeacherServiceLog(teacherService, BundleUtil.getStringFromResourceBundle(
                    "resources.TeacherCreditsSheetResources", "label.teacher.personFunction.delete", getFunction().getName(),
                    getUnit().getPresentationName(), getTeacher().getPerson().getNickname(), getPercentage().toString()));
        } else {
            new TeacherServiceLog(teacherService, BundleUtil.getStringFromResourceBundle(
                    "resources.TeacherCreditsSheetResources", "label.teacher.personFunction.delete", getFunction().getName(),
                    getUnit().getPresentationName(), getTeacher().getPerson().getNickname(), getCredits().toString()));
        }
    }

    public List<PersonFunctionShared> getPersonFunctionsShared() {
        List<PersonFunctionShared> functions = new ArrayList<PersonFunctionShared>();
        for (PersonFunction personFunction : getFunction().getPersonFunctions()) {
            if (personFunction instanceof PersonFunctionShared) {
                PersonFunctionShared personFunctionShared = (PersonFunctionShared) personFunction;
                if (personFunctionShared.getExecutionInterval().equals(getExecutionSemester())) {
                    functions.add(personFunctionShared);
                }
            }
        }
        return functions;
    }

    public PersonFunction getPersonFunction() {
        if (personFunction != null) {
            return personFunction;
        }
        if (getFunction() != null) {
            for (PersonFunction personFunction : getTeacher().getPerson().getPersonFunctions(getFunction())) {
                if (intersectsSemester(personFunction)) {
                    return personFunction;
                }
            }
        }
        return null;
    }

    private boolean intersectsSemester(PersonFunction personFunction) {
        if (personFunction.hasExecutionInterval()) {
            return personFunction.getExecutionInterval().equals(getExecutionSemester());
        }
        return personFunction.belongsToPeriod(getExecutionSemester().getBeginDateYearMonthDay(), getExecutionSemester()
                .getEndDateYearMonthDay());
    }

    public List<Function> getAvailableFunctions() {
        List<Function> result = new ArrayList<Function>();
        if (getUnit() != null) {
            for (Function function : getUnit().getFunctions(true)) {
                if (function.isActive() && !function.isVirtual()) {
                    result.add(function);
                }
            }
        }
        return result;
    }

    public List<Function> getAvailableSharedFunctions() {
        List<Function> result = new ArrayList<Function>();
        if (getUnit() != null) {
            for (Function function : getUnit().getFunctions(true)) {
                if (function instanceof SharedFunction && function.isActive() && !function.isVirtual()) {
                    result.add(function);
                }
            }
        }
        return result;
    }

    public void setPersonFunction(PersonFunction personFunction) {
        this.personFunction = personFunction;
    }

}
