package org.fenixedu.academic.domain.candidacy;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class IngressionType extends IngressionType_Base {

    private IngressionType() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    @Atomic(mode = TxMode.WRITE)
    public static IngressionType createIngressionType(String code, LocalizedString fullDescription) {
        IngressionType ingressionType = new IngressionType();
        ingressionType.setCode(code);
        ingressionType.setFullDescription(fullDescription);
        return ingressionType;
    }

    @Deprecated
    public static IngressionType getIngressionTypeForIngression(Ingression ingression) {
        return findIngressionTypeByCode(ingression.getName());
    }

    @Deprecated
    public static Ingression getIngressionForIngressionType(IngressionType ingressionType) {
        return Ingression.valueOf(ingressionType.getCode());
    }

    public String getDescription() {
        return getDescription(I18N.getLocale());
    }

    public String getDescription(Locale locale) {
        return (getFullDescription().getContent(locale).length() > 50 ? getFullDescription().getContent(locale).substring(0, 49)
                + " ..." : getFullDescription().getContent(locale));
    }

    public boolean hasEntryPhase() {
        return getHasEntryPhase();
    }

    @Override
    public void setHasEntryPhase(boolean hasEntryPhase) {
        super.setHasEntryPhase(hasEntryPhase);
    }

    public String getLocalizedName() {
        return getLocalizedName(Locale.getDefault());
    }

    public String getLocalizedName(Locale locale) {
        return getCode() + " - " + getDescription(locale);
    }

    public static IngressionType findIngressionTypeByCode(String name) {
        Predicate<? super IngressionType> matchesName = ingressionType -> name.equals(ingressionType.getCode());
        return Bennu.getInstance().getIngressionTypesSet().stream().filter(matchesName).findAny().orElse(null);
    }

    public static IngressionType findByPredicate(Predicate<IngressionType> predicate) {
        return Bennu.getInstance().getIngressionTypesSet().stream().filter(predicate).findAny().orElse(null);
    }

    //Checks if there is any other objects, besides the one being changed, for which the tested predicate returns true
    private void checkUniqueWithPredicate(Predicate<IngressionType> predicate) {
        Optional<IngressionType> findAny =
                Bennu.getInstance().getIngressionTypesSet().stream()
                        .filter(predicate.and(ingressionType -> ingressionType != this)).findAny();
        if (findAny.isPresent()) {
            throw new DomainException("label.error.ingressionTypePredicateIsNotUnique");
        }
    }

    // These methods are the only business logic which was being used through the code.
    // It must be evaluated if these methods should be kept
    public boolean isExternalCourseChange() {
        return getIsExternalCourseChange();
    }

    public boolean isInternalCourseChange() {
        return getIsInternalCourseChange();
    }

    public boolean isTransfer() {
        return getIsTransfer();
    }

    public boolean isMiddleAndSuperiorCourses() {
        return getIsMiddleAndSuperiorCourses();
    }

    public boolean isOver23() {
        return getIsOver23();
    }

    public boolean isInternal2ndCycleAccess() {
        return getIsInternal2ndCycleAccess();
    }

    public boolean isIsolatedCurricularUnits() {
        return getIsIsolatedCurricularUnits();
    }

    public boolean isInternal3rdCycleAccess() {
        return getIsInternal3rdCycleAccess();
    }

    public boolean isDirectAccessFrom1stCycle() {
        return getIsDirectAccessFrom1stCycle();
    }

    public boolean isHandicappedContingent() {
        return getIsHandicappedContingent();
    }

    public boolean isFirstCycleAttribution() {
        return getIsFirstCycleAttribution();
    }

    public boolean isReIngression() {
        return getIsReIngression();
    }

    //The booleans which map to some of the Enum values must be protected to avoid having more than one IngressionType instance with the same boolean as true

    @Override
    public void setIsExternalCourseChange(boolean isExternalCourseChange) {
        if (isExternalCourseChange) {
            checkUniqueWithPredicate(IngressionType::isExternalCourseChange);
        }

        super.setIsExternalCourseChange(isExternalCourseChange);
    }

    @Override
    public void setIsInternalCourseChange(boolean isInternalCourseChange) {
        if (isInternalCourseChange) {
            checkUniqueWithPredicate(IngressionType::isInternalCourseChange);
        }
        super.setIsInternalCourseChange(isInternalCourseChange);
    }

    @Override
    public void setIsTransfer(boolean isTransfer) {
        if (isTransfer) {
            checkUniqueWithPredicate(IngressionType::isTransfer);
        }
        super.setIsTransfer(isTransfer);
    }

    @Override
    public void setIsMiddleAndSuperiorCourses(boolean isMiddleAndSuperiorCourses) {
        if (isMiddleAndSuperiorCourses) {
            checkUniqueWithPredicate(IngressionType::isMiddleAndSuperiorCourses);
        }
        super.setIsMiddleAndSuperiorCourses(isMiddleAndSuperiorCourses);
    }

    @Override
    public void setIsOver23(boolean isOver23) {
        if (isOver23) {
            checkUniqueWithPredicate(IngressionType::isOver23);
        }
        super.setIsOver23(isOver23);
    }

    @Override
    public void setIsInternal2ndCycleAccess(boolean isInternal2ndCycleAccess) {
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
    public void setIsInternal3rdCycleAccess(boolean isInternal3rdCycleAccess) {
        if (isInternal3rdCycleAccess) {
            checkUniqueWithPredicate(IngressionType::isInternal3rdCycleAccess);
        }
        super.setIsInternal3rdCycleAccess(isInternal3rdCycleAccess);
    }

    @Override
    public void setIsDirectAccessFrom1stCycle(boolean isDirectAccessFrom1stCycle) {
        if (isDirectAccessFrom1stCycle) {
            checkUniqueWithPredicate(IngressionType::isDirectAccessFrom1stCycle);
        }
        super.setIsDirectAccessFrom1stCycle(isDirectAccessFrom1stCycle);
    }

    @Override
    public void setIsHandicappedContingent(boolean isHandicappedContingent) {
        if (isHandicappedContingent) {
            checkUniqueWithPredicate(IngressionType::isHandicappedContingent);
        }
        super.setIsHandicappedContingent(isHandicappedContingent);
    }

    @Override
    public void setIsFirstCycleAttribution(boolean isFirstCycleAttribution) {
        if (isFirstCycleAttribution) {
            checkUniqueWithPredicate(IngressionType::isFirstCycleAttribution);
        }
        super.setIsFirstCycleAttribution(isFirstCycleAttribution);
    }

    @Override
    public void setIsReIngression(boolean isReIngression) {
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
}
