package org.fenixedu.academic.domain.groups;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import com.qubit.terra.qubAccessControl.domain.AccessControlPermission;

public class PermissionService {

    private static BiFunction<AccessControlPermission, User, Set<DegreeType>> degreeTypeProvider;
    private static BiFunction<AccessControlPermission, User, Set<Degree>> degreeProvider;
    private static BiFunction<AccessControlPermission, User, Set<Unit>> unitProvider;
    private static BiFunction<AccessControlPermission, User, Boolean> memberProvider;

    public static Set<DegreeType> getDegreeTypes(AccessControlPermission permission, User user) {
        return degreeTypeProvider.apply(permission, user);
    }

    public static Set<DegreeType> getDegreeTypes(String permission, User user) {
        AccessControlPermission accessControlPermission = AccessControlPermission.findByCode(permission);
        if (accessControlPermission == null) {
            return new HashSet<>();
        }
        return getDegreeTypes(accessControlPermission, user);
    }

    public static void registerDegreeTypeProvider(BiFunction<AccessControlPermission, User, Set<DegreeType>> degreeTypeProvider) {
        PermissionService.degreeTypeProvider = degreeTypeProvider;
    }

    public static Set<Degree> getDegrees(AccessControlPermission permission, User user) {
        Set<Degree> degrees = new HashSet<>();
        degrees.addAll(degreeProvider.apply(permission, user));
        getDegreeTypes(permission, user).forEach(degreeType -> degrees.addAll(degreeType.getDegreeSet()));
        return degrees;
    }

    public static Set<Degree> getDegrees(String permission, User user) {
        AccessControlPermission accessControlPermission = AccessControlPermission.findByCode(permission);
        if (accessControlPermission == null) {
            return new HashSet<>();
        }
        return getDegrees(accessControlPermission, user);
    }

    public static void registerDegreeProvider(BiFunction<AccessControlPermission, User, Set<Degree>> degreeProvider) {
        PermissionService.degreeProvider = degreeProvider;
    }

    public static Set<Unit> getUnits(AccessControlPermission permission, User user) {
        Set<Unit> units = new HashSet<>();
        units.addAll(unitProvider.apply(permission, user));
        return units;
    }

    public static Set<Unit> getUnits(String permission, User user) {
        AccessControlPermission accessControlPermission = AccessControlPermission.findByCode(permission);
        if (accessControlPermission == null) {
            return new HashSet<>();
        }
        return getUnits(accessControlPermission, user);
    }

    public static void registerUnitProvider(BiFunction<AccessControlPermission, User, Set<Unit>> unitProvider) {
        PermissionService.unitProvider = unitProvider;
    }

    public static boolean hasAccess(AccessControlPermission permission, User user) {
        return memberProvider.apply(permission, user);
    }

    public static boolean hasAccess(AccessControlPermission permission) {
        return hasAccess(permission, Authenticate.getUser());
    }

    public static boolean hasAccess(AccessControlPermission permission, Degree degree, User user) {
        return getDegrees(permission, user).contains(degree);
    }

    public static boolean hasAccess(AccessControlPermission permission, Degree degree) {
        return hasAccess(permission, degree, Authenticate.getUser());
    }

    public static boolean hasAccess(AccessControlPermission permission, Unit unit, User user) {
        return getUnits(permission, user).contains(unit);
    }

    public static boolean hasAccess(AccessControlPermission permission, Unit unit) {
        return hasAccess(permission, unit, Authenticate.getUser());
    }

    public static boolean hasAccess(String permission, User user) {
        AccessControlPermission accessControlPermission = AccessControlPermission.findByCode(permission);
        if (accessControlPermission == null) {
            return false;
        }
        return hasAccess(accessControlPermission, user);
    }

    public static boolean hasAccess(String permission) {
        return hasAccess(permission, Authenticate.getUser());
    }

    public static boolean hasAccess(String permission, Degree degree, User user) {
        AccessControlPermission accessControlPermission = AccessControlPermission.findByCode(permission);
        if (accessControlPermission == null) {
            return false;
        }
        return hasAccess(accessControlPermission, degree, user);
    }

    public static boolean hasAccess(String permission, Degree degree) {
        return hasAccess(permission, degree, Authenticate.getUser());
    }

    public static boolean hasAccess(String permission, Unit unit, User user) {
        AccessControlPermission accessControlPermission = AccessControlPermission.findByCode(permission);
        if (accessControlPermission == null) {
            return false;
        }
        return hasAccess(accessControlPermission, unit, user);
    }

    public static boolean hasAccess(String permission, Unit unit) {
        return hasAccess(permission, unit, Authenticate.getUser());
    }

    public static void registerMemberProvider(BiFunction<AccessControlPermission, User, Boolean> isMemberProvider) {
        PermissionService.memberProvider = isMemberProvider;
    }
}
