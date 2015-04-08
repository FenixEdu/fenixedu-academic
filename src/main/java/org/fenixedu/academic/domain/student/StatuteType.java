package org.fenixedu.academic.domain.student;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class StatuteType extends StatuteType_Base {

    public static Comparator<StatuteType> COMPARATOR_BY_NAME = Comparator.comparing(StatuteType::getName).thenComparing(
            DomainObjectUtil.COMPARATOR_BY_ID);

    private StatuteType() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public StatuteType(String code, LocalizedString name, boolean workingStudentStatute, boolean associativeLeaderStatute,
            boolean specialSeasonGrantedByRequest, boolean grantOwnerStatute, boolean seniorStatute, boolean handicappedStatute,
            boolean active, boolean explicitCreation, boolean visible, boolean specialSeasonGranted) {
        this();
        if (isCodeUsed(code)) {
            throw new DomainException("error.StatuteType.code.alreadyUsed");
        }
        setCode(code);
        setName(name);
        setWorkingStudentStatute(workingStudentStatute);
        setAssociativeLeaderStatute(associativeLeaderStatute);
        setSpecialSeasonGrantedByRequest(specialSeasonGrantedByRequest);
        setGrantOwnerStatute(grantOwnerStatute);
        setSeniorStatute(seniorStatute);
        setHandicappedStatute(handicappedStatute);
        setActive(active);

        setExplicitCreation(explicitCreation);
        setVisible(visible);
        setSpecialSeasonGranted(specialSeasonGranted);

        checkRules();
    }

    @Override
    public void setCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return;
        }

        super.setCode("");
        if (isCodeUsed(code)) {
            throw new DomainException("error.StatuteType.code.alreadyUsed");
        }
        super.setCode(code);
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
    }

    private static void check(final Object obj, final String message, final String... args) {
        if (obj == null) {
            throw new DomainException(message, args);
        }
    }

    public boolean isWorkingStudentStatute() {
        return getWorkingStudentStatute();
    }

    @Deprecated
    public boolean isAssociativeLeaderStatute() {
        return getAssociativeLeaderStatute();
    }

    @Deprecated
    public boolean isSpecialSeasonGrantedByRequest() {
        return getSpecialSeasonGrantedByRequest();
    }

    public boolean isGrantOwnerStatute() {
        return getGrantOwnerStatute();
    }

    public boolean isSeniorStatute() {
        return getSeniorStatute();
    }

    public boolean isHandicappedStatute() {
        return getHandicappedStatute();
    }

    @Deprecated
    public boolean isActive() {
        return getActive();
    }

    public boolean isDeletable() {
        return getStudentStatutesSet().isEmpty();
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new DomainException("error.StatuteType.deletion.not.possible");
        }

        setRootDomainObject(null);
        deleteDomainObject();
    }

    @SafeVarargs
    public static Stream<StatuteType> readAll(Predicate<StatuteType>... predicates) {
        Stream<StatuteType> statuteTypes = Bennu.getInstance().getStatuteTypesSet().stream();
        for (Predicate<StatuteType> predicate : predicates) {
            statuteTypes = statuteTypes.filter(predicate);
        }
        return statuteTypes;
    }

    public static Optional<StatuteType> find(String code) {
        Predicate<StatuteType> matchesCode = statute -> StringUtils.equals(code, statute.getCode());
        return readAll(StatuteType::isActive, matchesCode).findFirst();
    }

    public static Optional<StatuteType> findSpecialSeasonGrantedByRequestStatuteType() {
        return readAll(StatuteType::isSpecialSeasonGrantedByRequest).findFirst();
    }

    public static Optional<StatuteType> findSeniorStatuteType() {
        return readAll(StatuteType::isSeniorStatute).findFirst();
    }

    public static Optional<StatuteType> findHandicappedStatuteType() {
        return readAll(StatuteType::isHandicappedStatute).findFirst();
    }

    @Deprecated
    public boolean isExplicitCreation() {
        return getExplicitCreation();
    }

    public boolean isVisible() {
        return getVisible();
    }

    public boolean isSpecialSeasonGranted() {
        return getSpecialSeasonGranted();
    }
}
