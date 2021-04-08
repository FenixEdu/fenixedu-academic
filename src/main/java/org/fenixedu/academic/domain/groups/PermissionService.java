package org.fenixedu.academic.domain.groups;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import com.qubit.terra.qubAccessControl.domain.AccessControlPermission;
import com.qubit.terra.qubAccessControl.domain.AccessControlProfile;
import com.qubit.terra.qubAccessControl.domain.ObjectProfilesCache;

import pt.ist.fenixframework.DomainObject;

public class PermissionService {

    private static BiFunction<AccessControlPermission, User, Set<AccessControlProfile>> profileProvider;
    private static BiFunction<AccessControlPermission, User, Set<? extends DomainObject>> objectsProvider;
    private static BiFunction<AccessControlPermission, User, Boolean> memberProvider;
    private static BiFunction<AccessControlProfile, User, Boolean> profileMembershipProvider;

    public static void registerProfileMembershipProvider(
            BiFunction<AccessControlProfile, User, Boolean> profileMembershipProvider) {
        PermissionService.profileMembershipProvider = profileMembershipProvider;
    }

    public static void registerProfileProvider(
            BiFunction<AccessControlPermission, User, Set<AccessControlProfile>> profileProvider) {
        PermissionService.profileProvider = profileProvider;
    }

    public static Set<? extends DomainObject> getObjects(AccessControlPermission permission, User user) {
        return objectsProvider.apply(permission, user);
    }

    public static Set<? extends DomainObject> getObjects(String permission, User user) {
        AccessControlPermission accessControlPermission = AccessControlPermission.findByCode(permission);
        if (accessControlPermission == null) {
            return new HashSet<>();
        }
        return getObjects(accessControlPermission, user);
    }

    public static <T extends DomainObject> Set<T> getObjects(AccessControlPermission permission, Class<T> clazz, User user) {
        return (Set<T>) permission.getProfileSet().stream().flatMap(p -> getAllParents(p).stream()).filter(
                p -> clazz != null && clazz.getName().equals(p.getObjectsClass()) && profileMembershipProvider.apply(p, user))
                .flatMap(p -> p.provideObjects().stream()).collect(Collectors.toSet());
    }

    private static Set<AccessControlProfile> getAllParents(AccessControlProfile profile) {
        HashSet<AccessControlProfile> parents = new HashSet<AccessControlProfile>();
        parents.add(profile);
        profile.getParentSet().forEach(parent -> parents.addAll(getAllParents(parent)));
        return parents;
    }

    public static <T extends DomainObject> Set<T> getObjects(String permission, Class<T> clazz, User user) {
        AccessControlPermission accessControlPermission = AccessControlPermission.findByCode(permission);
        if (accessControlPermission == null) {
            return new HashSet<>();
        }
        return getObjects(accessControlPermission, clazz, user);
    }

    public static void registerObjectsProvider(
            BiFunction<AccessControlPermission, User, Set<? extends DomainObject>> objectsProvider) {
        PermissionService.objectsProvider = objectsProvider;
    }

    public static boolean hasAccess(AccessControlPermission permission, User user) {
        return memberProvider.apply(permission, user);
    }

    public static boolean hasAccess(AccessControlPermission permission) {
        return hasAccess(permission, Authenticate.getUser());
    }

    public static <T extends DomainObject> boolean hasAccess(AccessControlPermission permission, T object, User user) {
        return ObjectProfilesCache.hasAccess(permission, object).stream().anyMatch(p -> profileMembershipProvider.apply(p, user));
    }

    public static <T extends DomainObject> boolean hasAccess(AccessControlPermission permission, T object) {
        return hasAccess(permission, object, Authenticate.getUser());
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

    public static <T extends DomainObject> boolean hasAccess(String permission, T object, User user) {
        AccessControlPermission accessControlPermission = AccessControlPermission.findByCode(permission);
        if (accessControlPermission == null) {
            return false;
        }
        return hasAccess(accessControlPermission, object, user);
    }

    public static <T extends DomainObject> boolean hasAccess(String permission, T object) {
        return hasAccess(permission, object, Authenticate.getUser());
    }

    public static void registerMemberProvider(BiFunction<AccessControlPermission, User, Boolean> isMemberProvider) {
        PermissionService.memberProvider = isMemberProvider;
    }

    public static <T extends DomainObject> Collection<T> filter(String permission, Collection<T> objects) {
        User user = Authenticate.getUser();
        Collection<T> result = new ArrayList<>();
        result.addAll(objects);
        // Obtaining the class of the objects in the collection
        // with objects.stream().findFirst().get().getClass()
        // is a problem if the collection has more than one type
        // of objects.
        //
        // Daniel Pires - 13 May 2020
        //
        result.retainAll(getObjects(permission, objects.stream().findFirst().get().getClass(), user));
        return result;
    }

    public static <T extends DomainObject> Stream<T> filter(String permission, Stream<T> objects) {
        return filter(permission, objects.collect(Collectors.toSet())).stream();
    }
}
