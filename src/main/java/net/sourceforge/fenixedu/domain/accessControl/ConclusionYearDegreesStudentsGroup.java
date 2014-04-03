package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.ArrayUtils;
import org.fenixedu.bennu.core.domain.groups.NobodyGroup;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class ConclusionYearDegreesStudentsGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;
    private List<Degree> degrees;
    private ExecutionYear registrationEnd;

    public ConclusionYearDegreesStudentsGroup(List<Degree> degrees, ExecutionYear registrationEnd) {
        setDegrees(degrees);
        setRegistrationEnd(registrationEnd);
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();

        for (Degree degree : getDegrees()) {
            for (Registration registration : degree.getRegistrations()) {
                if (registration.hasConcluded()) {
                    LocalDate conclusionDate = getConclusionDate(degree, registration);
                    if (conclusionDate != null
                            && (conclusionDate.getYear() == getRegistrationEnd().getEndCivilYear() || conclusionDate.getYear() == getRegistrationEnd()
                                    .getBeginCivilYear())) {
                        elements.add(registration.getPerson());
                    }
                }
            }
        }
        return super.freezeSet(elements);
    }

    private LocalDate getConclusionDate(Degree degree, Registration registration) {
        for (StudentCurricularPlan scp : registration.getStudentCurricularPlansByDegree(degree)) {
            if (registration.isBolonha()) {
                if (scp.getLastConcludedCycleCurriculumGroup() != null) {
                    YearMonthDay conclusionDate =
                            registration.getConclusionDate(scp.getLastConcludedCycleCurriculumGroup().getCycleType());
                    if (conclusionDate != null) {
                        return conclusionDate.toLocalDate();
                    }
                }
                return null;
            } else {
                return registration.getConclusionDate() != null ? registration.getConclusionDate().toLocalDate() : null;
            }
        }
        return null;
    }

    @Override
    public boolean isMember(Person person) {
        if (person != null && person.hasStudent()) {
            for (final Registration registration : person.getStudent().getRegistrationsSet()) {
                if (registration.isConcluded() && getDegrees().contains(registration.getDegree())) {
                    LocalDate conclusionDate = getConclusionDate(registration.getDegree(), registration);
                    if (conclusionDate != null
                            && (conclusionDate.getYear() == getRegistrationEnd().getEndCivilYear() || conclusionDate.getYear() == getRegistrationEnd()
                                    .getBeginCivilYear())) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        List<OidOperator> degreeArguments = new ArrayList<OidOperator>();
        for (Degree degree : getDegrees()) {
            degreeArguments.add(new OidOperator(degree));
        }
        Argument[] returnArguments = new Argument[] { new OidOperator(getRegistrationEnd()) };
        return (Argument[]) ArrayUtils.addAll(returnArguments, degreeArguments.toArray());
    }

    @Override
    public String getName() {
        final StringBuilder executionYears = new StringBuilder(super.getName());

        final StringBuilder degreeNames = new StringBuilder();
        for (Iterator<Degree> iterator = getDegrees().iterator(); iterator.hasNext();) {
            Degree degree = iterator.next();
            degreeNames.append(degree.getPresentationName());
            if (iterator.hasNext()) {
                degreeNames.append(", ");
            }
        }
        return executionYears.append(degreeNames).toString();
    }

    @Override
    public boolean hasPresentationNameDynamic() {
        return true;
    }

    @Override
    public String getPresentationNameKey() {
        return "label.name.executionYear";
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getRegistrationEnd().getName() };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            List<Degree> degrees = new ArrayList<Degree>();
            ExecutionYear endRegistrationYear;
            try {
                endRegistrationYear = (ExecutionYear) arguments[0];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(0, ExecutionYear.class, arguments[0].getClass());
            }
            try {
                for (int iter = 1; iter < arguments.length; iter++) {
                    degrees.add((Degree) arguments[iter]);
                }
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(1, Degree.class, Degree.class);
            }
            return new ConclusionYearDegreesStudentsGroup(degrees, endRegistrationYear);
        }

        @Override
        public int getMinArguments() {
            return 2;
        }

        @Override
        public int getMaxArguments() {
            return 50;
        }
    }

    public List<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }

    public ExecutionYear getRegistrationEnd() {
        return registrationEnd;
    }

    public void setRegistrationEnd(ExecutionYear registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        org.fenixedu.bennu.core.domain.groups.Group group = NobodyGroup.getInstance();
        for (Degree degree : getDegrees()) {
            group = group.or(PersistentStudentsConcludedInExecutionYearGroup.getInstance(degree, getRegistrationEnd()));
        }
        return group;
    }
}