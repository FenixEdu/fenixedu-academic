package org.fenixedu.academic.domain.student;

import java.util.Collection;
import java.util.Comparator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class StatuteType extends StatuteType_Base {

    public static Comparator<StatuteType> COMPARATOR_BY_NAME = new Comparator<StatuteType>() {

        @Override
        public int compare(StatuteType o1, StatuteType o2) {
            int result = o1.getName().compareTo(o2.getName());
            return result == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : result;
        }
    };

    private StatuteType() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public StatuteType(final String code, final LocalizedString name, final boolean appliedOnRegistration,
            final boolean grantsWorkingStudentStatute, boolean associativeLeaderStatute, boolean specialSeasonGrantedByRequest,
            boolean grantOwnerStatute, boolean seniorStatute, boolean handicappedStatute, final boolean active,
            boolean explicitCreation, boolean visible) {
        this();
        if (isCodeUsed(code)) {
            throw new DomainException("error.StatuteType.code.alreadyUsed");
        }
        setCode(code);
        setName(name);
        setAppliedOnRegistration(appliedOnRegistration);
        setGrantsWorkingStudentStatute(grantsWorkingStudentStatute);
        setAssociativeLeaderStatute(associativeLeaderStatute);
        setSpecialSeasonGrantedByRequest(specialSeasonGrantedByRequest);
        setGrantOwnerStatute(grantOwnerStatute);
        setSeniorStatute(seniorStatute);
        setHandicappedStatute(handicappedStatute);
        setActive(active);

        setExplicitCreation(explicitCreation);
        setVisible(visible);

        checkRules();
    }

    private boolean isCodeUsed(String code) {
        for (StatuteType statuteType : Bennu.getInstance().getStatuteTypesSet()) {
            if (code.equals(statuteType.getCode())) {
                return true;
            }
        }
        return false;
    }

    protected void checkRules() {
        check(getName(), "error.StatuteType.name.required");
        check(getAppliedOnRegistration(), "error.StatuteType.appliedOnRegistration.required");
        check(getGrantsWorkingStudentStatute(), "error.StatuteType.grantsWorkingStudentStatute.required");
        check(getActive(), "error.StatuteType.active.required");
    }

    private static void check(final Object obj, final String message, final String... args) {
        if (obj == null) {
            throw new DomainException(message, args);
        }
    }

    public boolean isAppliedOnRegistration() {
        return getAppliedOnRegistration() != null && getAppliedOnRegistration();
    }

    public boolean isGrantsWorkingStudentStatute() {
        return getGrantsWorkingStudentStatute() != null && getGrantsWorkingStudentStatute();
    }

    public boolean isAssociativeLeaderStatute() {
        return getAssociativeLeaderStatute() != null && getAssociativeLeaderStatute();
    }

    public boolean isSpecialSeasonGrantedByRequest() {
        return getSpecialSeasonGrantedByRequest() != null && getSpecialSeasonGrantedByRequest();
    }

    public boolean isGrantOwnerStatute() {
        return getGrantOwnerStatute() != null && getGrantOwnerStatute();
    }

    public boolean isSeniorStatute() {
        return getSeniorStatute() != null && getSeniorStatute();
    }

    public boolean isHandicappedStatute() {
        return getHandicappedStatute() != null && getHandicappedStatute();
    }

    public boolean isActive() {
        return getActive() != null && getActive();
    }

    public boolean isDeletable() {
        // return getStudentStatutesCount() == 0;
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new DomainException("error.StatuteType.deletion.not.possible");
        }

        setRootDomainObject(null);
        deleteDomainObject();
    }

    public static Collection<StatuteType> readAll() {
        return Bennu.getInstance().getStatuteTypesSet();
    }

    public static Collection<StatuteType> readActiveStatuteTypes() {
        return CollectionUtils.select(StatuteType.readAll(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((StatuteType) arg0).isActive();
            }
        });
    }

    public static StatuteType find(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (StatuteType statuteType : readActiveStatuteTypes()) {
            if (StringUtils.equals(code, statuteType.getCode())) {
                return statuteType;
            }
        }

        return null;
    }

    public static StatuteType findSpecialSeasonGrantedByRequestStatuteType() {
        for (StatuteType type : readAll()) {
            if (type.isSpecialSeasonGrantedByRequest()) {
                return type;
            }
        }
        return null;
    }

    public static StatuteType findSeniorStatuteType() {
        for (StatuteType type : readAll()) {
            if (type.isSeniorStatute()) {
                return type;
            }
        }
        return null;
    }

    public static StatuteType findHandicappedStatuteType() {
        for (StatuteType type : readAll()) {
            if (type.isHandicappedStatute()) {
                return type;
            }
        }
        return null;
    }

    public boolean isExplicitCreation() {
        return getExplicitCreation();
    }

    public boolean isVisible() {
        return getVisible();
    }
}
