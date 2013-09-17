package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.UsernameUtils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class Login extends Login_Base {

    public Login(User user) {
        super();
        checkIfUserAlreadyHaveLogin(user);
        setUser(user);
        setIsPassInKerberos(Boolean.FALSE);
    }

    @Override
    public void delete() {
        for (; !getAlias().isEmpty(); getAlias().iterator().next().delete()) {
            ;
        }
        for (; !getLoginPeriods().isEmpty(); getLoginPeriods().iterator().next().delete()) {
            ;
        }
        super.delete();
    }

    public LoginPeriod readLoginPeriodByTimeInterval(YearMonthDay begin, YearMonthDay end) {
        for (LoginPeriod loginPeriod : getLoginPeriodsSet()) {
            if (loginPeriod.getBeginDate().equals(begin)
                    && ((loginPeriod.getEndDate() == null && end == null) || (loginPeriod.getEndDate().equals(end)))) {
                return loginPeriod;
            }
        }
        return null;
    }

    public boolean isOpened() {
        YearMonthDay currentDate = new YearMonthDay();
        for (LoginPeriod loginPeriod : getLoginPeriodsSet()) {
            if ((loginPeriod.getEndDate() == null || !loginPeriod.getEndDate().isBefore(currentDate))
                    && !loginPeriod.getBeginDate().isAfter(currentDate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLogin() {
        return true;
    }

    public String getUsername() {
        String userUId = getUserUId();
        if (userUId != null) {
            return userUId;
        } else {
            return getMostImportantAlias();
        }
    }

    public String getMostImportantAlias() {
        Collection<Role> personRoles = getUser().getPerson().getPersonRoles();
        Role mostImportantRole = UsernameUtils.getMostImportantRole(personRoles);
        if (mostImportantRole != null) {
            RoleType roleType = mostImportantRole.getRoleType();
            List<LoginAlias> loginAlias = getRoleLoginAlias(roleType);
            if (!loginAlias.isEmpty()) {
                return loginAlias.iterator().next().getAlias();
            }
        }
        return getAlias().isEmpty() ? null : getAlias().iterator().next().getAlias();
    }

    public String getUserUId() {
        LoginAlias loginAliasByType = getInstitutionalLoginAlias();
        return (loginAliasByType != null) ? loginAliasByType.getAlias() : null;
    }

    public void setUsername(RoleType roleType) {
        removeAliasWithoutCloseLogin(roleType);
        openLoginIfNecessary(roleType);
        updateAliases(roleType);
    }

    public void setUserUID() {
        final LoginAlias loginAlias = getInstitutionalLoginAlias();
        if (loginAlias == null) {
            final String userUId = UsernameUtils.updateIstUsername(getUser().getPerson());
            if (!StringUtils.isEmpty(userUId)) {
                LoginAlias.createNewInstitutionalLoginAlias(this, userUId);
                getUser().setUserUId(userUId);
            }
        }
    }

    public LoginAlias readLoginAliasByAlias(String alias) {
        if (alias != null) {
            for (LoginAlias loginAlias : getAlias()) {
                if (loginAlias.getAlias().equalsIgnoreCase(alias)) {
                    return loginAlias;
                }
            }
        }
        return null;
    }

    public LoginAlias getInstitutionalLoginAlias() {
        for (LoginAlias loginAlias : getAlias()) {
            if (loginAlias.getType().equals(LoginAliasType.INSTITUTION_ALIAS)) {
                return loginAlias;
            }
        }
        return null;
    }

    public List<LoginAlias> getRoleLoginAlias(RoleType roleType) {
        List<LoginAlias> result = new ArrayList<LoginAlias>();
        if (roleType != null) {
            for (LoginAlias loginAlias : getAlias()) {
                if (loginAlias.getType().equals(LoginAliasType.ROLE_TYPE_ALIAS) && loginAlias.getRoleType().equals(roleType)) {
                    result.add(loginAlias);
                }
            }
        }
        return result;
    }

    public List<LoginAlias> getAllRoleLoginAlias() {
        return readAllLoginAliasByType(LoginAliasType.ROLE_TYPE_ALIAS);
    }

    public List<LoginAlias> getAllCustomLoginAlias() {
        return readAllLoginAliasByType(LoginAliasType.CUSTOM_ALIAS);
    }

    public List<LoginAlias> readAllLoginAliasByType(LoginAliasType aliasType) {
        List<LoginAlias> result = new ArrayList<LoginAlias>();
        for (LoginAlias loginAlias : getAlias()) {
            if (loginAlias.getType().equals(aliasType)) {
                result.add(loginAlias);
            }
        }
        return result;
    }

    public Set<LoginAlias> getLoginAliasOrderByImportance() {
        Set<LoginAlias> result = new TreeSet<LoginAlias>(LoginAlias.COMPARATOR_BY_TYPE_AND_ROLE_TYPE_AND_ALIAS);
        result.addAll(getAlias());
        return result;
    }

    public void removeAlias(RoleType roleType) {
        removeAliasWithoutCloseLogin(roleType);
        closeLoginIfNecessary();
    }

    public boolean hasUsername(String username) {
        return readLoginAliasByAlias(username) != null;
    }

    public Set<LoginPeriod> getLoginPeriodsWithoutInvitationPeriods() {
        Person person = getUser().getPerson();
        Login login = person.getLoginIdentification();
        Set<LoginPeriod> loginPeriods = new TreeSet<LoginPeriod>(LoginPeriod.COMPARATOR_BY_BEGIN_DATE);
        loginPeriods.addAll(login.getLoginPeriods());
        for (Invitation invitation : person.getInvitationsOrderByDate()) {
            LoginPeriod period = login.readLoginPeriodByTimeInterval(invitation.getBeginDate(), invitation.getEndDate());
            if (period != null) {
                loginPeriods.remove(period);
            }
        }
        return loginPeriods;
    }

    public static Login readLoginByUsername(String username) {
        final LoginAlias loginAlias = LoginAlias.readLoginByUsername(username);
        return loginAlias == null ? null : loginAlias.getLogin();
    }

    public void closeLoginIfNecessary() {
        Person person = getUser().getPerson();

        if (!person.hasRole(RoleType.TEACHER) && !person.hasRole(RoleType.RESEARCHER) && !person.hasRole(RoleType.EMPLOYEE)
                && !person.hasRole(RoleType.STUDENT) && !person.hasRole(RoleType.ALUMNI) && !person.hasRole(RoleType.CANDIDATE)
                && !person.hasRole(RoleType.MANAGER) && !person.hasRole(RoleType.GRANT_OWNER)) {

            // minusDays(1) -> This is for person dont make login today
            YearMonthDay currentDate = new YearMonthDay().minusDays(1);
            for (LoginPeriod loginPeriod : getLoginPeriodsSet()) {
                if (loginPeriod.getEndDate() == null) {
                    loginPeriod.setEndDate(currentDate);
                }
            }
        }
    }

    public void openLoginIfNecessary(RoleType roleType) {
        switch (roleType) {
        case MANAGER:
        case TEACHER:
        case RESEARCHER:
        case EMPLOYEE:
        case STUDENT:
        case ALUMNI:
        case CANDIDATE:
        case GRANT_OWNER:
            for (LoginPeriod loginPeriod : getLoginPeriodsSet()) {
                if (loginPeriod.getEndDate() == null) {
                    return;
                }
            }
            // minusDays(2) -> This is for prevent errors in
            // closeLoginIfNecessary (setEndDate line).
            addLoginPeriods(new LoginPeriod(new YearMonthDay().minusDays(2), this));
            break;

        default:
            break;
        }
    }

    private void removeAliasWithoutCloseLogin(RoleType roleType) {
        if (roleType != null) {
            for (final Iterator<LoginAlias> iter = getAlias().iterator(); iter.hasNext();) {
                final LoginAlias loginAlias = iter.next();
                if (loginAlias.getRoleType() != null && loginAlias.getRoleType().equals(roleType)) {
                    iter.remove();
                    loginAlias.delete();
                }
            }
        }
    }

    private void checkIfUserAlreadyHaveLogin(User user) {
        if (user != null) {
            Login login = user.readUserLoginIdentification();
            if (login != null && !login.equals(this)) {
                throw new DomainException("error.user.login.already.exists");
            }
        }
    }

    public void updateAliases(final RoleType roleType) {
        final String newAlias = makeNewAlias(roleType);
        if (newAlias != null && !hasAlias(newAlias)) {
            LoginAlias.createNewCustomLoginAlias(this, newAlias);
        }
    }

    private boolean hasAlias(final String newAlias) {
        for (final LoginAlias loginAlias : getAliasSet()) {
            if (loginAlias.getAlias().equals(newAlias)) {
                return true;
            }
        }
        return false;
    }

    private String makeNewAlias(final RoleType roleType) {
        final Person person = getUser().getPerson();
        return roleType == RoleType.EMPLOYEE ? makeNewAliasEmployee(person) : roleType == RoleType.TEACHER ? makeNewAliasTeacher(person) : roleType == RoleType.GRANT_OWNER ? makeNewAliasGrantOwner(person) : null;
    }

    private String makeNewAliasEmployee(final Person person) {
        return person.hasEmployee() ? "F" + person.getEmployee().getEmployeeNumber() : null;
    }

    private String makeNewAliasTeacher(final Person person) {
        return person.hasEmployee() && person.hasTeacher() ? "D" + person.getEmployee().getEmployeeNumber() : null;
    }

    private String makeNewAliasGrantOwner(final Person person) {
        return person.hasEmployee() ? "F" + person.getEmployee().getEmployeeNumber() : null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.LoginAlias> getAlias() {
        return getAliasSet();
    }

    @Deprecated
    public boolean hasAnyAlias() {
        return !getAliasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.LoginPeriod> getLoginPeriods() {
        return getLoginPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyLoginPeriods() {
        return !getLoginPeriodsSet().isEmpty();
    }

    @Deprecated
    public boolean hasIsPassInKerberos() {
        return getIsPassInKerberos() != null;
    }

    @Deprecated
    public boolean hasPassword() {
        return getPassword() != null;
    }

}
