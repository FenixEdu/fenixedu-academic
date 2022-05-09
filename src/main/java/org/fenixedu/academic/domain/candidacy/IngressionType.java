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
package org.fenixedu.academic.domain.candidacy;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class IngressionType extends IngressionType_Base {

	private IngressionType() {
		super();
		setRootDomainObject(Bennu.getInstance());
	}

	@Atomic(mode = TxMode.WRITE)
	public static IngressionType createIngressionType(String code, LocalizedString description) {

		IngressionType ingressionType = new IngressionType();
		ingressionType.setCode(code);
		ingressionType.setDescription(description);

		return ingressionType;
	}

	public boolean hasEntryPhase() {
		return getHasEntryPhase();
	}

	public String getLocalizedName() {
		return getLocalizedName(Locale.getDefault());
	}

	public String getLocalizedName(Locale locale) {
		return getCode() + " - " + getDescription().getContent(locale);
	}

	public static Collection<IngressionType> findAll() {
        return Bennu.getInstance().getIngressionTypesSet();
    }
	
	public static Optional<IngressionType> findIngressionTypeByCode(String name) {
		Predicate<? super IngressionType> matchesName = ingressionType -> name.equals(ingressionType.getCode());
		return Bennu.getInstance().getIngressionTypesSet().stream().filter(matchesName).findAny();
	}
	
	public static Stream<IngressionType> findAllByPredicate(Predicate<IngressionType> predicate) {
		return Bennu.getInstance().getIngressionTypesSet().stream().filter(predicate);
	}

	public static Optional<IngressionType> findByPredicate(Predicate<IngressionType> predicate) {
		return Bennu.getInstance().getIngressionTypesSet().stream().filter(predicate).findAny();
	}

	// Checks if there is any other objects, besides the one being changed, for
	// which the tested predicate returns true
	private void checkUniqueWithPredicate(Predicate<IngressionType> predicate) {
		Optional<IngressionType> findAny = Bennu.getInstance().getIngressionTypesSet().stream()
				.filter(predicate.and(ingressionType -> ingressionType != this)).findAny();
		if (findAny.isPresent()) {
			throw new DomainException("label.error.ingressionTypePredicateIsNotUnique");
		}
	}

	// These methods are the only business logic which was being used through the
	// code.
	// It must be evaluated if these methods should be kept
	@Deprecated
	public boolean isExternalDegreeChange() {
		return getIsExternalDegreeChange();
	}

	@Deprecated
	public boolean isInternalDegreeChange() {
		return getIsInternalDegreeChange();
	}

	@Deprecated
	public boolean isTransfer() {
		return getIsTransfer();
	}

	@Deprecated
	public boolean isMiddleAndSuperiorCourses() {
		return getIsMiddleAndSuperiorCourses();
	}

	@Deprecated
	public boolean isOver23() {
		return getIsOver23();
	}

	@Deprecated
	public boolean isInternal2ndCycleAccess() {
		return getIsInternal2ndCycleAccess();
	}

	@Deprecated
	public boolean isIsolatedCurricularUnits() {
		return getIsIsolatedCurricularUnits();
	}

	@Deprecated
	public boolean isInternal3rdCycleAccess() {
		return getIsInternal3rdCycleAccess();
	}

	@Deprecated
	public boolean isDirectAccessFrom1stCycle() {
		return getIsDirectAccessFrom1stCycle();
	}

	@Deprecated
	public boolean isHandicappedContingent() {
		return getIsHandicappedContingent();
	}

	@Deprecated
	public boolean isFirstCycleAttribution() {
		return getIsFirstCycleAttribution();
	}

	@Deprecated
	public boolean isReIngression() {
		return getIsReIngression();
	}

	// The booleans which map to some of the Enum values must be protected to avoid
	// having more than one IngressionType instance with the same boolean as true

	@Override
	@Deprecated
	protected void setIsExternalDegreeChange(boolean isExternalDegreeChange) {
		if (isExternalDegreeChange) {
			checkUniqueWithPredicate(IngressionType::isExternalDegreeChange);
		}

		super.setIsExternalDegreeChange(isExternalDegreeChange);
	}

	@Override
	@Deprecated
	protected void setIsInternalDegreeChange(boolean isInternalDegreeChange) {
		if (isInternalDegreeChange) {
			checkUniqueWithPredicate(IngressionType::isInternalDegreeChange);
		}
		super.setIsInternalDegreeChange(isInternalDegreeChange);
	}

	@Override
	@Deprecated
	protected void setIsTransfer(boolean isTransfer) {
		if (isTransfer) {
			checkUniqueWithPredicate(IngressionType::isTransfer);
		}
		super.setIsTransfer(isTransfer);
	}

	@Override
	@Deprecated
	protected void setIsMiddleAndSuperiorCourses(boolean isMiddleAndSuperiorCourses) {
		if (isMiddleAndSuperiorCourses) {
			checkUniqueWithPredicate(IngressionType::isMiddleAndSuperiorCourses);
		}
		super.setIsMiddleAndSuperiorCourses(isMiddleAndSuperiorCourses);
	}

	@Override
	@Deprecated
	protected void setIsOver23(boolean isOver23) {
		if (isOver23) {
			checkUniqueWithPredicate(IngressionType::isOver23);
		}
		super.setIsOver23(isOver23);
	}

	@Override
	@Deprecated
	protected void setIsInternal2ndCycleAccess(boolean isInternal2ndCycleAccess) {
		if (isInternal2ndCycleAccess) {
			checkUniqueWithPredicate(IngressionType::isInternal2ndCycleAccess);
		}
		super.setIsInternal2ndCycleAccess(isInternal2ndCycleAccess);
	}

	@Override
	public void setIsIsolatedCurricularUnits(boolean isIsolatedCurricularUnits) {
		if (isIsolatedCurricularUnits) {
			checkUniqueWithPredicate(IngressionType::isIsolatedCurricularUnits);
		}
		super.setIsIsolatedCurricularUnits(isIsolatedCurricularUnits);
	}

	@Override
	@Deprecated
	protected void setIsInternal3rdCycleAccess(boolean isInternal3rdCycleAccess) {
		if (isInternal3rdCycleAccess) {
			checkUniqueWithPredicate(IngressionType::isInternal3rdCycleAccess);
		}
		super.setIsInternal3rdCycleAccess(isInternal3rdCycleAccess);
	}

	@Override
	@Deprecated
	protected void setIsDirectAccessFrom1stCycle(boolean isDirectAccessFrom1stCycle) {
		if (isDirectAccessFrom1stCycle) {
			checkUniqueWithPredicate(IngressionType::isDirectAccessFrom1stCycle);
		}
		super.setIsDirectAccessFrom1stCycle(isDirectAccessFrom1stCycle);
	}

	@Override
	@Deprecated
	protected void setIsHandicappedContingent(boolean isHandicappedContingent) {
		if (isHandicappedContingent) {
			checkUniqueWithPredicate(IngressionType::isHandicappedContingent);
		}
		super.setIsHandicappedContingent(isHandicappedContingent);
	}

	@Override
	@Deprecated
	protected void setIsFirstCycleAttribution(boolean isFirstCycleAttribution) {
		if (isFirstCycleAttribution) {
			checkUniqueWithPredicate(IngressionType::isFirstCycleAttribution);
		}
		super.setIsFirstCycleAttribution(isFirstCycleAttribution);
	}

	@Override
	@Deprecated
	protected void setIsReIngression(boolean isReIngression) {
		if (isReIngression) {
			checkUniqueWithPredicate(IngressionType::isReIngression);
		}
		super.setIsReIngression(isReIngression);
	}

	public void delete() {
		if (!getRegistrationSet().isEmpty()) {
			throw new DomainException("label.error.ingressionType.delete.hasRegistrationsAssociated");
		}
		if (!getStudentCandidacySet().isEmpty()) {
			throw new DomainException("label.error.ingressionType.delete.hasStudentCandidaciesAssociated");
		}
		setRootDomainObject(null);
		deleteDomainObject();
	}

	public void editState(boolean hasEntryPhase, boolean isDirectAccessFrom1stCycle, boolean isExternalDegreeChange,
			boolean isFirstCycleAttribution, boolean isHandicappedContingent, boolean isInternal2ndCycleAccess,
			boolean isInternal3rdCycleAccess, boolean isInternalDegreeChange, boolean isIsolatedCurricularUnits,
			boolean isMiddleAndSuperiorCourses, boolean isOver23, boolean isReIngression, boolean isTransfer) {
		setHasEntryPhase(hasEntryPhase);
		setIsDirectAccessFrom1stCycle(isDirectAccessFrom1stCycle);
		setIsExternalDegreeChange(isExternalDegreeChange);
		setIsFirstCycleAttribution(isFirstCycleAttribution);
		setIsHandicappedContingent(isHandicappedContingent);
		setIsInternal2ndCycleAccess(isInternal2ndCycleAccess);
		setIsInternal3rdCycleAccess(isInternal3rdCycleAccess);
		setIsInternalDegreeChange(isInternalDegreeChange);
		setIsIsolatedCurricularUnits(isIsolatedCurricularUnits);
		setIsMiddleAndSuperiorCourses(isMiddleAndSuperiorCourses);
		setIsOver23(isOver23);
		setIsReIngression(isReIngression);
		setIsTransfer(isTransfer);
	}
	
	@Override
	public void setCode(String code) {
		
		if (Strings.isNullOrEmpty(code)) {
            throw new DomainException("error.ingressionType.code.cannot.be.null");
        }

        if (findAll().stream().anyMatch(it -> it != this && Objects.equals(it.getCode(), code))) {
            throw new DomainException("error.ingressionType.duplicated.code", code);
        }
		
		super.setCode(code);
	}
}
