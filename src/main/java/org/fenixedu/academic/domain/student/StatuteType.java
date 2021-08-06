/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.student;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
            boolean active, boolean explicitCreation, boolean visible, boolean specialSeasonGranted, boolean extraordinarySeasonGranted) {
        this();
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
        setExtraordinarySeasonGranted(extraordinarySeasonGranted);

        checkRules();
    }

    @Override
    public void setCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            super.setCode(null);
        } else {
            if (readAll().filter(statute -> code.equals(statute.getCode()) && statute != StatuteType.this).findAny().isPresent()) {
                throw new DomainException("error.StatuteType.code.alreadyUsed");
            }
            super.setCode(code);
        }
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

    public static Optional<StatuteType> findSpecialSeasonGrantedByRequestStatuteType() {
        return readAll(StatuteType::isSpecialSeasonGrantedByRequest).findFirst();
    }

    public static Optional<StatuteType> findExtraordinarySeasonGrantedStatuteType() {
        return readAll(StatuteType::isExtraordinarySeasonGranted).findFirst();
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

    public boolean isExtraordinarySeasonGranted() {
        return getExtraordinarySeasonGranted();
    }
}
