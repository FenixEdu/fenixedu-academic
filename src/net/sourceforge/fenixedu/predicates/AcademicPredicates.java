package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class AcademicPredicates {
    public static final AccessControlPredicate<Object> MANAGE_AUTHORIZATIONS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_AUTHORIZATIONS)
                    .isMember(AccessControl.getPerson());
        };
    };

    // AVMC: Required to filter which degrees can be selected 
    public static final AccessControlPredicate<Object> MANAGE_EQUIVALENCES = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_EQUIVALENCES).isMember(AccessControl.getPerson());
        };
    };

    public static final AccessControlPredicate<Object> CREATE_REGISTRATION = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return new AcademicAuthorizationGroup(AcademicOperationType.CREATE_REGISTRATION).isMember(AccessControl.getPerson());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_MARKSHEETS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_MARKSHEETS).isMember(AccessControl.getPerson());
        };
    };

    public static final AccessControlPredicate<Object> DISSERTATION_MARKSHEETS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return new AcademicAuthorizationGroup(AcademicOperationType.DISSERTATION_MARKSHEETS).isMember(AccessControl
                    .getPerson());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_PAYMENTS = new AccessControlPredicate<Object>() {

        @Override
        public boolean evaluate(Object c) {
            return AccessControl.getUserView().hasRoleType(RoleType.MANAGER);
            // return new
            // AcademicAuthorizationGroup(AcademicOperationType.MANAGE_PAYMENTS).isMember(AccessControl.getPerson());
        }

    };

    public static final AccessControlPredicate<Object> DEPOSIT_AMOUNT_ON_PAYMENT_EVENT = new AccessControlPredicate<Object>() {

        @Override
        public boolean evaluate(Object c) {
            return AccessControl.getUserView().hasRoleType(RoleType.MANAGER);
            // return new
            // AcademicAuthorizationGroup(AcademicOperationType.DEPOSIT_AMOUNT_ON_PAYMENT_EVENT).isMember(AccessControl
            // .getPerson()) || MANAGE_PAYMENTS.evaluate(c);
        }
    };

    public static final AccessControlPredicate<Object> CREATE_PAYMENT_EVENT = new AccessControlPredicate<Object>() {

        @Override
        public boolean evaluate(Object c) {
            return AccessControl.getUserView().hasRoleType(RoleType.MANAGER);
            // return new
            // AcademicAuthorizationGroup(AcademicOperationType.CREATE_PAYMENT_EVENT).isMember(AccessControl.getPerson())
            // || MANAGE_PAYMENTS.evaluate(c);
        }

    };

    public static final AccessControlPredicate<Object> SERVICE_REQUESTS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return new AcademicAuthorizationGroup(AcademicOperationType.SERVICE_REQUESTS).isMember(AccessControl.getPerson());
        };
    };

    public static final AccessControlPredicate<AcademicServiceRequest> SERVICE_REQUESTS_REVERT_TO_PROCESSING_STATE =
            new AccessControlPredicate<AcademicServiceRequest>() {
                @Override
                public boolean evaluate(final AcademicServiceRequest request) {
                    return AcademicAuthorizationGroup.isAuthorized(AccessControl.getPerson(), request,
                            AcademicOperationType.REPEAT_CONCLUSION_PROCESS);
                };
            };

    public static final AccessControlPredicate<Object> EDIT_STUDENT_PERSONAL_DATA = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object request) {
            return new AcademicAuthorizationGroup(AcademicOperationType.EDIT_STUDENT_PERSONAL_DATA).isMember(AccessControl
                    .getPerson());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_ACCOUNTING_EVENTS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object request) {
            return new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_ACCOUNTING_EVENTS).isMember(AccessControl
                    .getPerson());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_STUDENT_PAYMENTS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object request) {
            return new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_STUDENT_PAYMENTS).isMember(AccessControl
                    .getPerson());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_PHD_PROCESSES = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object request) {
            return new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_PHD_PROCESSES).isMember(AccessControl.getPerson());
        };
    };

    public static final AccessControlPredicate<Object> VIEW_FULL_STUDENT_CURRICULUM = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object request) {
            return new AcademicAuthorizationGroup(AcademicOperationType.VIEW_FULL_STUDENT_CURRICULUM).isMember(AccessControl
                    .getPerson());
        };
    };
}
