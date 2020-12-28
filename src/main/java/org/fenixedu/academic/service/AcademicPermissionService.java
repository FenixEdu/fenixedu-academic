package org.fenixedu.academic.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.DomainObject;

public abstract class AcademicPermissionService {

    public static boolean hasAccess(final String permission) {
        return PermissionService.hasAccess(permission);
    }

    public static boolean hasAccess(final String permission, User user) {
        return PermissionService.hasAccess(permission, user);
    }

    public static boolean hasAccess(final String permission, final Degree degree) {
        return PermissionService.hasAccess(permission, (DomainObject) degree)
                || PermissionService.hasAccess(permission, (DomainObject) degree.getDegreeType());
    }

    public static boolean hasAccess(final String permission, final Degree degree, User user) {
        return PermissionService.hasAccess(permission, (DomainObject) degree, user)
                || PermissionService.hasAccess(permission, (DomainObject) degree.getDegreeType(), user);
    }

    public static Set<Degree> getDegrees(final String permission) {
        Set<Degree> result = new HashSet<>();
        result.addAll(PermissionService.getObjects(permission, Degree.class, Authenticate.getUser()));
        result.addAll(PermissionService.getObjects(permission, DegreeType.class, Authenticate.getUser()).stream()
                .flatMap(degreeType -> degreeType.getDegreeSet().stream()).collect(Collectors.toSet()));
        return result;
    }

    public static Set<Degree> getDegrees(final String permission, User user) {
        Set<Degree> result = new HashSet<>();
        result.addAll(PermissionService.getObjects(permission, Degree.class, user));
        result.addAll(PermissionService.getObjects(permission, DegreeType.class, user).stream()
                .flatMap(degreeType -> degreeType.getDegreeSet().stream()).collect(Collectors.toSet()));
        return result;
    }

    public static Set<DegreeType> getDegreeTypes(final String permission) {
        return PermissionService.getObjects(permission, DegreeType.class, Authenticate.getUser());
    }

    public static Set<DegreeType> getDegreeTypes(final String permission, User user) {
        return PermissionService.getObjects(permission, DegreeType.class, user);
    }
}
