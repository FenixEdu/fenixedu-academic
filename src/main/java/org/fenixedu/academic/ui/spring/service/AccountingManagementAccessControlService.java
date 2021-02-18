package org.fenixedu.academic.ui.spring.service;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.accounting.AcademicEvent;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.AuthorizationException;
import org.fenixedu.bennu.core.security.Authenticate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@Service
public class AccountingManagementAccessControlService {

    public static Supplier<Person> LOGGED_PERSON = () -> {
        final User user = Authenticate.getUser();
        return user == null ? null : user.getPerson();
    };

    public void checkEventOwnerOrPaymentManager(final Event event) {
        final Person person = LOGGED_PERSON.get();
        try {
            checkEventOwner(event, person);
        } catch (final AuthorizationException e) {
            checkPaymentManager(event, person == null ? null : person.getUser());
        }
    }

    private void checkPermission(final Event event, final Person person, final BiPredicate<Event, User> permission) {
        final User user = person == null ? null : person.getUser();
        if (user == null) {
            throw getUnauthorizedException();
        }

        if (permission.test(event, user)){
            return;
        }

        throw getUnauthorizedException();
    }

    private AuthorizationException getUnauthorizedException() {
        return AuthorizationException.unauthorized();
    }

    public void checkPaymentManager(final Event event, final User user) {
        checkPermission(event, user == null ? null : user.getPerson(), this::isPaymentManager);
    }

    public void checkAdvancedPaymentManager(final Event event, final User user) {
        checkPermission(event, user == null ? null : user.getPerson(), this::isAdvancedPaymentManager);
    }

    public void checkEventOwner(final Event event) {
        checkEventOwner(event, LOGGED_PERSON.get());
    }

    public void checkEventOwner(final Event event, final Person person) {
        if (!isEventOwner(event, person)) {
            throw getUnauthorizedException();
        }
    }

    public boolean isPaymentManager(final User user) {
        return hasOperationType(user, Collections.emptySet(), AcademicOperationType.MANAGE_STUDENT_PAYMENTS) || isAdvancedPaymentManager(user);
    }

    private boolean isAdvancedPaymentManager(final User user) {
        return hasOperationType(user, Collections.emptySet(), AcademicOperationType.MANAGE_STUDENT_PAYMENTS_ADV);
    }

    public boolean isPaymentManager(final Event event, final User user) {
        return hasOperationType(user, getAdminOffices(event), AcademicOperationType.MANAGE_STUDENT_PAYMENTS) || isAdvancedPaymentManager(event, user);
    }

    public boolean isAdvancedPaymentManager(final Event event, final User user) {
        return hasOperationType(user, getAdminOffices(event), AcademicOperationType.MANAGE_STUDENT_PAYMENTS_ADV);
    }

    public boolean isEventOwner(final Event event) {
        return isEventOwner(event, LOGGED_PERSON.get());
    }

    public boolean isEventOwner(final Event event, final Person person) {
        return person != null && event.getPerson() == person;
    }

    private boolean hasOperationType(final User user, Set<AdministrativeOffice> adminOffices, AcademicOperationType operationType) {
        return Optional.ofNullable(user).map(getAcademicAuthorizationGroup(adminOffices, operationType)::isMember).orElse(false);
    }

    private AcademicAuthorizationGroup getAcademicAuthorizationGroup(final Set<AdministrativeOffice> adminOffices,
                                                                     final AcademicOperationType operationType) {
        return AcademicAuthorizationGroup.get(operationType, Collections.emptySet(), adminOffices, null);
    }

    private Set<AdministrativeOffice> getAdminOffices(final Event event) {
        if (event instanceof AcademicEvent) {
            final AdministrativeOffice administrativeOffice = ((AcademicEvent) event).getAdministrativeOffice();
            if (administrativeOffice != null) {
                return Collections.singleton(administrativeOffice);
            }
        }
        return Collections.emptySet();
    }
}
