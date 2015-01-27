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
package org.fenixedu.academic.domain.accessControl.academicAdministration;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.accessControl.rules.AccessRule;
import org.fenixedu.academic.domain.accessControl.rules.AccessRuleSystem;
import org.fenixedu.academic.domain.accessControl.rules.AccessTarget;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.joda.time.DateTime;

public class AcademicAccessRule extends AcademicAccessRule_Base implements Comparable<AcademicAccessRule> {
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
        Set<AccessTarget> targets = new HashSet<>();
        if (programs != null) {
            programs.stream().forEach(p -> targets.add(new AcademicProgramAccessTarget(p)));
        }
        if (offices != null) {
            offices.stream().forEach(p -> targets.add(new AdministrativeOfficeAccessTarget(p)));
        }
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
        Stream<AcademicAccessRule> stream = filter(function);
        if (programs != null && !programs.isEmpty()) {
            stream = stream.filter(r -> r.getFullProgramSet().collect(Collectors.toSet()).containsAll(programs));
        }
        if (offices != null && !offices.isEmpty()) {
            stream = stream.filter(r -> r.getOfficeSet().containsAll(offices));
        }
        return stream;
    }

    protected static Stream<AcademicAccessRule> filter(AcademicOperationType function, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, DateTime when) {
        Stream<AcademicAccessRule> stream = filter(function, when);
        if (programs != null && !programs.isEmpty()) {
            stream = stream.filter(r -> r.getFullProgramSet().collect(Collectors.toSet()).containsAll(programs));
        }
        if (offices != null && !offices.isEmpty()) {
            stream = stream.filter(r -> r.getOfficeSet().containsAll(offices));
        }
        return stream;
    }

    public static Set<User> getMembers(Predicate<? super AcademicAccessRule> filter) {
        return AcademicAccessRule.accessRules().filter(filter).map(AccessRule::getWhoCanAccess)
                .flatMap(group -> group.getMembers().stream()).collect(Collectors.toSet());
    }

    public static Set<User> getMembers(AcademicOperationType function, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices) {
        return filter(function, programs, offices).map(AccessRule::getWhoCanAccess).flatMap(group -> group.getMembers().stream())
                .collect(Collectors.toSet());
    }

    public static Set<User> getMembers(Predicate<? super AcademicAccessRule> filter, DateTime when) {
        return AcademicAccessRule.accessRules(when).filter(filter).map(AccessRule::getWhoCanAccess)
                .flatMap(group -> group.getMembers(when).stream()).collect(Collectors.toSet());
    }

    public static Set<User> getMembers(AcademicOperationType function, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, DateTime when) {
        return filter(function, programs, offices, when).map(AccessRule::getWhoCanAccess)
                .flatMap(group -> group.getMembers(when).stream()).collect(Collectors.toSet());
    }

    public static boolean isMember(User user, Predicate<? super AcademicAccessRule> filter) {
        return AcademicAccessRule.accessRules().filter(filter).anyMatch(group -> group.isMember(user));
    }

    public static boolean isMember(User user, AcademicOperationType function, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices) {
        return filter(function, programs, offices).anyMatch(group -> group.isMember(user));
    }

    public static boolean isMember(User user, Predicate<? super AcademicAccessRule> filter, DateTime when) {
        return AcademicAccessRule.accessRules(when).filter(filter).anyMatch(group -> group.isMember(user, when));
    }

    public static boolean isMember(User user, AcademicOperationType function, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, DateTime when) {
        return filter(function, programs, offices, when).anyMatch(group -> group.isMember(user, when));
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

    @Override
    public int compareTo(AcademicAccessRule o) {
        int op = getOperation().compareTo(o.getOperation());
        if (op != 0) {
            return op;
        }
        int group = getWhoCanAccess().compareTo(o.getWhoCanAccess());
        if (group != 0) {
            return group;
        }
        return getExternalId().compareTo(o.getExternalId());
    }
}
