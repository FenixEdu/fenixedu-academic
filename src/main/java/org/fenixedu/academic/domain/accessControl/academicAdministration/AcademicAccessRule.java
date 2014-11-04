package net.sourceforge.fenixedu.domain.accessControl.academicAdministration;

import java.text.Collator;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.rules.AccessRuleSystem;
import net.sourceforge.fenixedu.domain.accessControl.rules.AccessTarget;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;
import org.joda.time.DateTime;

public class AcademicAccessRule extends AcademicAccessRule_Base {
    public static abstract class AcademicAccessTarget implements AccessTarget {
        public abstract void write(AcademicAccessRule academicAccessRule, AcademicOperationType operation);
    }

    public static class AcademicProgramAccessTarget extends AcademicAccessTarget {
        private final AcademicProgram program;

        public AcademicProgramAccessTarget(AcademicProgram program) {
            this.program = program;
        }

        public AcademicProgram getProgram() {
            return program;
        }

        @Override
        public void write(AcademicAccessRule academicAccessRule, AcademicOperationType operation) {
            if (!operation.isProgramAllowedAsTarget()) {
                throw new DomainException("error.persistent.authorization.group.does.not.allow.offices");
            }
            academicAccessRule.addProgram(program);
        }
    }

    public static class AdministrativeOfficeAccessTarget extends AcademicAccessTarget {
        private final AdministrativeOffice office;

        public AdministrativeOfficeAccessTarget(AdministrativeOffice office) {
            this.office = office;
        }

        public AdministrativeOffice getOffice() {
            return office;
        }

        @Override
        public void write(AcademicAccessRule academicAccessRule, AcademicOperationType operation) {
            if (!operation.isOfficeAllowedAsTarget()) {
                throw new DomainException("error.persistent.authorization.group.does.not.allow.offices");
            }
            academicAccessRule.addOffice(office);
        }
    }

    public static final Comparator<AcademicAccessRule> COMPARATOR_BY_OPERATION = new Comparator<AcademicAccessRule>() {
        @Override
        public int compare(AcademicAccessRule o1, AcademicAccessRule o2) {
            String operationName1 = o1.getOperation().getLocalizedName();
            String operationName2 = o2.getOperation().getLocalizedName();
            return Collator.getInstance().compare(operationName1, operationName2);
        }
    };

    public AcademicAccessRule(AcademicOperationType operation, Group whoCanAccess, Set<AcademicAccessTarget> whatCanAffect) {
        super();
        setOperation(operation);
        setPersistentGroup(whoCanAccess.toPersistentGroup());
        for (AcademicAccessTarget target : whatCanAffect) {
            target.write(this, operation);
        }
    }

    @Override
    public AcademicOperationType getOperation() {
        return (AcademicOperationType) super.getOperation();
    }

    @Override
    public Set<AdministrativeOffice> getOfficeSet() {
        // TODO remove when framework supports read-only slots
        return super.getOfficeSet();
    }

    @Override
    public Set<AcademicProgram> getProgramSet() {
        // TODO remove when framework supports read-only slots
        return super.getProgramSet();
    }

    @Override
    public <T extends AccessTarget> Set<T> getWhatCanAffect() {
        return (Set<T>) Stream.concat(getProgramSet().stream().map(AcademicProgramAccessTarget::new),
                getOfficeSet().stream().map(AdministrativeOfficeAccessTarget::new)).collect(Collectors.toSet());
    }

    public AcademicAccessRule changeProgramsAndOffices(Set<AcademicProgram> programs, Set<AdministrativeOffice> offices) {
        Set<AccessTarget> targets =
                Stream.concat(programs.stream().map(AcademicProgramAccessTarget::new),
                        offices.stream().map(AdministrativeOfficeAccessTarget::new)).collect(Collectors.toSet());
        return (AcademicAccessRule) changeWhatCanAffect(targets).get();
    }

    public Stream<AcademicProgram> getFullProgramSet() {
        Stream<AcademicProgram> managed = getOfficeSet().stream().flatMap(o -> o.getManagedAcademicProgramSet().stream());
        return Stream.concat(Stream.concat(getProgramSet().stream(), managed), Stream.of(Degree.readEmptyDegree()));
    }

    public static Stream<AcademicAccessRule> accessRules() {
        return AccessRuleSystem.accessRules().filter(r -> r instanceof AcademicAccessRule).map(r -> (AcademicAccessRule) r);
    }

    public static Stream<AcademicAccessRule> accessRules(DateTime when) {
        return AccessRuleSystem.accessRules(when).filter(r -> r instanceof AcademicAccessRule).map(r -> (AcademicAccessRule) r);
    }

    protected static Stream<AcademicAccessRule> filter(AcademicOperationType function) {
        return accessRules().filter(r -> r.getOperation().equals(function));
    }

    protected static Stream<AcademicAccessRule> filter(AcademicOperationType function, DateTime when) {
        return accessRules(when).filter(r -> r.getOperation().equals(function));
    }

    protected static Stream<AcademicAccessRule> filter(AcademicOperationType function, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices) {
        return filter(function).filter(r -> r.getProgramSet().containsAll(programs)).filter(
                r -> r.getOfficeSet().containsAll(offices));
    }

    protected static Stream<AcademicAccessRule> filter(AcademicOperationType function, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, DateTime when) {
        return filter(function, when).filter(r -> r.getProgramSet().containsAll(programs)).filter(
                r -> r.getOfficeSet().containsAll(offices));
    }

    public static Set<User> getMembers(Predicate<? super AcademicAccessRule> filter) {
        return AcademicAccessRule.accessRules().filter(filter).map(r -> r.getWhoCanAccess())
                .reduce((result, group) -> result.or(group)).orElseGet(NobodyGroup::get).getMembers();
    }

    public static Set<User> getMembers(AcademicOperationType function, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices) {
        return filter(function, programs, offices).map(r -> r.getWhoCanAccess()).reduce((result, group) -> result.or(group))
                .orElseGet(NobodyGroup::get).getMembers();
    }

    public static Set<User> getMembers(Predicate<? super AcademicAccessRule> filter, DateTime when) {
        return AcademicAccessRule.accessRules(when).filter(filter).map(r -> r.getWhoCanAccess())
                .reduce((result, group) -> result.or(group)).orElseGet(NobodyGroup::get).getMembers(when);
    }

    public static Set<User> getMembers(AcademicOperationType function, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, DateTime when) {
        return filter(function, programs, offices, when).map(r -> r.getWhoCanAccess())
                .reduce((result, group) -> result.or(group)).orElseGet(NobodyGroup::get).getMembers(when);
    }

    public static boolean isMember(User user, Predicate<? super AcademicAccessRule> filter) {
        return AcademicAccessRule.accessRules().filter(filter).map(r -> r.getWhoCanAccess())
                .reduce((result, group) -> result.or(group)).orElseGet(NobodyGroup::get).isMember(user);
    }

    public static boolean isMember(User user, AcademicOperationType function, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices) {
        return filter(function, programs, offices).map(r -> r.getWhoCanAccess()).reduce((result, group) -> result.or(group))
                .orElseGet(NobodyGroup::get).isMember(user);
    }

    public static boolean isMember(User user, Predicate<? super AcademicAccessRule> filter, DateTime when) {
        return AcademicAccessRule.accessRules(when).filter(filter).map(r -> r.getWhoCanAccess())
                .reduce((result, group) -> result.or(group)).orElseGet(NobodyGroup::get).isMember(user);
    }

    public static boolean isMember(User user, AcademicOperationType function, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, DateTime when) {
        return filter(function, programs, offices, when).map(r -> r.getWhoCanAccess())
                .reduce((result, group) -> result.or(group)).orElseGet(NobodyGroup::get).isMember(user, when);
    }

    public static Stream<AcademicProgram> getProgramsAccessibleToFunction(AcademicOperationType function, User user) {
        return filter(function).filter(r -> r.getWhoCanAccess().isMember(user)).flatMap(r -> r.getFullProgramSet());
    }

    public static boolean isProgramAccessibleToFunction(AcademicOperationType function, AcademicProgram program, User user) {
        return filter(function).filter(r -> r.getWhoCanAccess().isMember(user)).flatMap(r -> r.getFullProgramSet())
                .anyMatch(p -> p.equals(program));
    }

    public static Stream<Degree> getDegreesAccessibleToFunction(AcademicOperationType function, User user) {
        return getProgramsAccessibleToFunction(function, user).filter(p -> p instanceof Degree).map(p -> (Degree) p);
    }

    public static Stream<PhdProgram> getPhdProgramsAccessibleToFunction(AcademicOperationType function, User user) {
        return getProgramsAccessibleToFunction(function, user).filter(p -> p instanceof PhdProgram).map(p -> (PhdProgram) p);
    }

    public static Stream<DegreeType> getDegreeTypesAccessibleToFunction(AcademicOperationType function, User user) {
        return getProgramsAccessibleToFunction(function, user).map(p -> p.getDegreeType()).filter(java.util.Objects::nonNull);
    }

    public static Stream<AdministrativeOffice> getOfficesAccessibleToFunction(AcademicOperationType function, User user) {
        return filter(function).filter(r -> r.getWhoCanAccess().isMember(user)).flatMap(r -> r.getOfficeSet().stream());
    }
}
