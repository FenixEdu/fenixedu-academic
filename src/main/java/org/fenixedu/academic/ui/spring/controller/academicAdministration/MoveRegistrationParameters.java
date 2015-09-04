package org.fenixedu.academic.ui.spring.controller.academicAdministration;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;

public class MoveRegistrationParameters {
    private User source;
    private User target;
    private Set<User> similars;
    private User searched;

    public MoveRegistrationParameters() {
    }

    public MoveRegistrationParameters(User target) {
        setTarget(target);
    }

    public User getSource() {
        return source;
    }

    public void setSource(User source) {
        this.source = source;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
        findSimilarAccounts();
    }

    public Set<User> getSimilars() {
        return similars;
    }

    public User getSearched() {
        return searched;
    }

    public void setSearched(User searched) {
        this.searched = searched;
    }

    private void findSimilarAccounts() {
        String[] parts = target.getProfile().getFullName().split(" ");
        String query = parts.length > 1 ? parts[0] + " " + parts[parts.length - 1] : target.getProfile().getFullName();
        similars =
                UserProfile.searchByName(query, Integer.MAX_VALUE).map(UserProfile::getUser).filter(Objects::nonNull)
                        .filter(u -> !u.equals(target)).filter(u -> barelyCompatibleStudent(u, target))
                        .collect(Collectors.toSet());
    }

    private static boolean barelyCompatibleStudent(User user, User target) {
        return user.getPerson().getStudent() != null && !user.getPerson().getStudent().getRegistrationsSet().isEmpty()
                && user.getPerson().getDateOfBirthYearMonthDay() != null
                && user.getPerson().getDateOfBirthYearMonthDay().equals(target.getPerson().getDateOfBirthYearMonthDay());
    }
}
