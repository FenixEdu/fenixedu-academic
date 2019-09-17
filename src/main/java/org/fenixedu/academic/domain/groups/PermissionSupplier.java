package org.fenixedu.academic.domain.groups;

import java.util.Set;
import java.util.function.BiFunction;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.bennu.core.domain.User;

import com.qubit.terra.qubAccessControl.domain.AccessControlPermission;

public class PermissionSupplier {

    
    public static BiFunction<AccessControlPermission, User, Set<DegreeType>> getDegreeTypesAccessibleByUser;
    
    public static BiFunction<AccessControlPermission, User, Set<Degree>> getDegreesAccessibleByUser;
    

    public static BiFunction<AccessControlPermission, User, Boolean> isMember;
    
    public static Set<Degree> getDegreesForUser(AccessControlPermission permission, User user){
        Set<Degree> degrees =  getDegreesAccessibleByUser.apply(permission, user);
        getDegreeTypesAccessibleByUser.apply(permission, user).forEach(degreeType -> degrees.addAll(degreeType.getDegreeSet()));
        return degrees;
    }
    
}
