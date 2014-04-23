package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

public class DegreeStudentsCycleGroup extends DegreeStudentsGroup {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;
    private CycleType cycleType;

    public DegreeStudentsCycleGroup(Degree object, CycleType cycleType) {
        super(object);
        setCycleType(cycleType);
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();
        for (DegreeCurricularPlan degreeCurricularPlan : getDegree().getActiveDegreeCurricularPlans()) {
            if (matchCycleTypes(getCycleType(), degreeCurricularPlan.getDegreeType())) {
                for (StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlans()) {
                    if (studentCurricularPlan.isActive()) {
                        final CycleCurriculumGroup cycleCurriculumGroup = studentCurricularPlan.getCycle(getCycleType());
                        if (cycleCurriculumGroup != null && !cycleCurriculumGroup.isConcluded()) {
                            elements.add(studentCurricularPlan.getRegistration().getPerson());
                        }
                    }
                }
            }
        }

        return super.freezeSet(elements);
    }

    private boolean matchCycleTypes(final CycleType cycleType, final DegreeType degreeType) {
        for (final CycleType ct : degreeType.getCycleTypes()) {
            if (cycleType == ct) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(Person person) {
        if (person != null && person.hasStudent()) {
            for (final Registration registration : person.getStudent().getRegistrationsSet()) {
                StudentCurricularPlan activeStudentCurricularPlan = registration.getActiveStudentCurricularPlan();
                if (registration.isActive() && activeStudentCurricularPlan.getDegree() == getDegree()
                        && getCycleType().equals(activeStudentCurricularPlan.getDegreeType())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()), new StaticArgument(getCycleType().name()) };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getObject().getPresentationName(), getCycleType().getDescription() };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                final String cycleTypeName = (String) arguments[1];
                final CycleType cycleType = CycleType.valueOf(cycleTypeName);
                return new DegreeStudentsCycleGroup((Degree) arguments[0], cycleType);
            } catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.degreeGroup.notDegree",
                        arguments[0].toString());
            }
        }

        @Override
        public int getMinArguments() {
            return 2;
        }

        @Override
        public int getMaxArguments() {
            return 2;
        }

    }

    public CycleType getCycleType() {
        return cycleType;
    }

    public void setCycleType(CycleType cycleType) {
        this.cycleType = cycleType;
    }

    @Override
    public PersistentStudentGroup convert() {
        return PersistentStudentGroup.getInstance(getDegree(), getCycleType());
    }
}
