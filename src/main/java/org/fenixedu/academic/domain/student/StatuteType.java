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

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;

public class StatuteType extends StatuteType_Base {

    public static Comparator<StatuteType> COMPARATOR_BY_NAME =
            Comparator.comparing(StatuteType::getName).thenComparing(DomainObjectUtil.COMPARATOR_BY_ID);

    private StatuteType() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    @Deprecated
    public StatuteType(String code, LocalizedString name, boolean workingStudentStatute, boolean associativeLeaderStatute,
            boolean specialSeasonGrantedByRequest, boolean grantOwnerStatute, boolean seniorStatute, boolean handicappedStatute,
            boolean active, boolean explicitCreation, boolean visible, boolean specialSeasonGranted,
            final boolean appliedOnRegistration) {

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
        setAppliedOnRegistration(appliedOnRegistration);

        checkRules();
    }

    @Override
    public void setCode(String code) {
    	
    	if (Strings.isNullOrEmpty(code)) {
            throw new DomainException("error.StatuteType.code.cannot.be.null");
        }

        if (findAll().stream().anyMatch(it -> it != this && Objects.equals(it.getCode(), code))) {
            throw new DomainException("error.StatuteType.code.alreadyUsed", code);
        }
    	
		super.setCode(code);
        
    }

    protected void checkRules() {
        check(getCode(), "error.StatuteType.code.required");
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

    public boolean isAppliedOnRegistration() {
        return getAppliedOnRegistration();
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

    public static StatuteType create(String code, LocalizedString name) {
        final StatuteType result = new StatuteType();
        result.setCode(code);
        result.setName(name);
        result.setActive(true);
        result.setVisible(true);
        result.setExplicitCreation(true);
        result.setAppliedOnRegistration(true);

        result.checkRules();

        return result;
    }

    public static Collection<StatuteType> findActive() {
        return readAll(s -> s.getActive()).collect(Collectors.toSet());
    }

    public static Optional<StatuteType> findByCode(String code) {
        return readAll(s -> Objects.equals(code, s.getCode())).findFirst();
    }

    public static Collection<StatuteType> findAll() {
        return readAll().collect(Collectors.toSet());
    }

    //TODO: change to private
    @SafeVarargs
    public static Stream<StatuteType> readAll(Predicate<StatuteType>... predicates) {
        Stream<StatuteType> statuteTypes = Bennu.getInstance().getStatuteTypesSet().stream();
        for (Predicate<StatuteType> predicate : predicates) {
            statuteTypes = statuteTypes.filter(predicate);
        }
        return statuteTypes;
    }
}
