package org.fenixedu.academic.ui.spring.service;

import java.util.Collections;
import java.util.Set;

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

    public void checkPaymentManager(Event event, User user) {
        if (user == null) {
            throw new UnsupportedOperationException("Unauthorized");
        }

        if (isPaymentManager(user, getAdminOffices(event))) {
            return;
        }

        throw new UnsupportedOperationException("Unauthorized");
    }

    public boolean isPaymentManager(User user, Set<AdministrativeOffice> adminOffices) {
        if (user == null) {
            return false;
        }

        Group group = AcademicAuthorizationGroup
                .get(AcademicOperationType.MANAGE_STUDENT_PAYMENTS, Collections.emptySet(), adminOffices,
                        null).or(AcademicAuthorizationGroup
                        .get(AcademicOperationType.MANAGE_STUDENT_PAYMENTS_ADV, Collections.emptySet(), adminOffices,
                                null));

        return group.isMember(user);
    }

    public boolean isEventOwner(final Event event, final User user) {
        return event.getPerson().getUser() == user;
    }

    public void checkEventOwner(final Event event, final User user) {
        if (!isEventOwner(event, user)) {
            throw new UnsupportedOperationException("Unauthorized");
        }
    }

    private Set<AdministrativeOffice> getAdminOffices(Event event) {
        if (event instanceof AcademicEvent) {
            return Collections.singleton(((AcademicEvent) event).getAdministrativeOffice());
        }
        return Collections.emptySet();
    }
}
