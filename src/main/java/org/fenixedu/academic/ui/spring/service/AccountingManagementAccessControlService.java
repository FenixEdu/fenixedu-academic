package org.fenixedu.academic.ui.spring.service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.accounting.AcademicEvent;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.springframework.stereotype.Service;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@Service
public class AccountingManagementAccessControlService {


    public void checkEventOwnerOrPaymentManager(Event event, User user) {
        try {
            checkEventOwner(event, user);
        } catch (UnsupportedOperationException e) {
            checkPaymentManager(event, user);
        }
    }

    private void checkPermission(Event event, User user, BiPredicate<Event, User> permission) {
        if (user == null) {
            throw new UnsupportedOperationException("Unauthorized");
        }

        if (permission.test(event, user)){
            return;
        }

        throw new UnsupportedOperationException("Unauthorized");
    }

    public void checkPaymentManager(Event event, User user) {
        checkPermission(event, user, this::isPaymentManager);
    }

    public void checkAdvancedPaymentManager(Event event, User user) {
        checkPermission(event, user, this::isAdvancedPaymentManager);
    }

    public void checkEventOwner(final Event event, final User user) {
        checkPermission(event, user, this::isEventOwner);
    }

    public boolean isPaymentManager(final User user) {
        return hasOperationType(user, Collections.emptySet(), AcademicOperationType.MANAGE_STUDENT_PAYMENTS) || isAdvancedPaymentManager(user);
    }

    private boolean isAdvancedPaymentManager(User user) {
        return hasOperationType(user, Collections.emptySet(), AcademicOperationType.MANAGE_STUDENT_PAYMENTS_ADV);
    }

    public boolean isPaymentManager(final Event event, final User user) {
        return hasOperationType(user, getAdminOffices(event), AcademicOperationType.MANAGE_STUDENT_PAYMENTS) || isAdvancedPaymentManager(event, user);
    }

    public boolean isAdvancedPaymentManager(final Event event, final User user) {
        return hasOperationType(user, getAdminOffices(event), AcademicOperationType.MANAGE_STUDENT_PAYMENTS_ADV);
    }

    public boolean isEventOwner(final Event event, final User user) {
        return user != null && event.getPerson().getUser() == user;
    }

    private boolean hasOperationType(User user, Set<AdministrativeOffice> adminOffices, AcademicOperationType operationType) {
        return Optional.ofNullable(user).map(getAcademicAuthorizationGroup(adminOffices, operationType)::isMember).orElse(false);
    }

    private AcademicAuthorizationGroup getAcademicAuthorizationGroup(Set<AdministrativeOffice> adminOffices, AcademicOperationType operationType) {
        return AcademicAuthorizationGroup.get(operationType, Collections.emptySet(), adminOffices, null);
    }

    private Set<AdministrativeOffice> getAdminOffices(Event event) {
        if (event instanceof AcademicEvent) {
            final AdministrativeOffice administrativeOffice = ((AcademicEvent) event).getAdministrativeOffice();
            if (administrativeOffice != null) {
                return Collections.singleton(administrativeOffice);
            }
        }
        return Collections.emptySet();
    }
}
