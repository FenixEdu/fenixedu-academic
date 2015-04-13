package org.fenixedu.academic.ui.spring.controller.candidacy.manageIngressionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.spring.AcademicBaseController;
import org.fenixedu.academic.ui.spring.AcademicController;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.Atomic;

@SpringFunctionality(app = AcademicController.class, title = "label.title.candidacy.manageIngressionType", accessGroup = "anyone")
@RequestMapping("/academic/candidacy/manageingressiontype/ingressiontype")
public class IngressionTypeController extends AcademicBaseController {

    @RequestMapping
    public String home(Model model) {
        return "redirect:/academic/candidacy/manageingressiontype/ingressiontype/";
    }

    private IngressionType getIngressionType(Model m) {
        return (IngressionType) m.asMap().get("ingressionType");
    }

    private void setIngressionType(IngressionType ingressionType, Model m) {
        m.addAttribute("ingressionType", ingressionType);
    }

    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") IngressionType ingressionType, Model model) {

        setIngressionType(ingressionType, model);
        try {
            deleteIngressionType(ingressionType);
        } catch (DomainException e) {
            addErrorMessage(e.getLocalizedMessage(), model);
            return "redirect:/academic/candidacy/manageingressiontype/ingressiontype/";
        }
        addInfoMessage(BundleUtil.getString(Bundle.APPLICATION, "label.success.ingressionType.delete"), model);
        return "redirect:/academic/candidacy/manageingressiontype/ingressiontype/";
    }

    @Atomic
    public void deleteIngressionType(IngressionType ingressionType) {
        ingressionType.delete();
    }

    @RequestMapping(value = "/")
    public String search(Model model) {
        List<IngressionType> searchingressiontypeResultsDataSet = filterSearchIngressionType();

        //add the results dataSet to the model
        model.addAttribute("searchingressiontypeResultsDataSet", searchingressiontypeResultsDataSet);
        return "academic/candidacy/manageingressiontype/ingressiontype/search";
    }

    private List<IngressionType> getSearchUniverseSearchIngressionTypeDataSet() {
        return new ArrayList<IngressionType>(Bennu.getInstance().getIngressionTypesSet());
    }

    private List<IngressionType> filterSearchIngressionType() {

        return getSearchUniverseSearchIngressionTypeDataSet().stream().collect(Collectors.toList());
    }

    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") IngressionType ingressionType, Model model) {
        setIngressionType(ingressionType, model);
        return "academic/candidacy/manageingressiontype/ingressiontype/read";
    }

    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") IngressionType ingressionType, Model model) {
        setIngressionType(ingressionType, model);
        return "academic/candidacy/manageingressiontype/ingressiontype/update";
    }

    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") IngressionType ingressionType,
            @RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "fulldescription",
                    required = false) org.fenixedu.commons.i18n.LocalizedString fullDescription, @RequestParam(
                    value = "hasentryphase", required = false) boolean hasEntryPhase, @RequestParam(
                    value = "isdirectaccessfrom1stcycle", required = false) boolean isDirectAccessFrom1stCycle, @RequestParam(
                    value = "isexternalcoursechange", required = false) boolean isExternalCourseChange, @RequestParam(
                    value = "isfirstcycleattribution", required = false) boolean isFirstCycleAttribution, @RequestParam(
                    value = "ishandicappedcontingent", required = false) boolean isHandicappedContingent, @RequestParam(
                    value = "isinternal2ndcycleaccess", required = false) boolean isInternal2ndCycleAccess, @RequestParam(
                    value = "isinternal3rdcycleaccess", required = false) boolean isInternal3rdCycleAccess, @RequestParam(
                    value = "isinternalcoursechange", required = false) boolean isInternalCourseChange, @RequestParam(
                    value = "isisolatedcurricularunits", required = false) boolean isIsolatedCurricularUnits, @RequestParam(
                    value = "ismiddleandsuperiorcourses", required = false) boolean isMiddleAndSuperiorCourses, @RequestParam(
                    value = "isover23", required = false) boolean isOver23, @RequestParam(value = "isreingression",
                    required = false) boolean isReIngression,
            @RequestParam(value = "istransfer", required = false) boolean isTransfer, Model model) {

        setIngressionType(ingressionType, model);

        try {
            updateIngressionType(code, fullDescription, hasEntryPhase, isDirectAccessFrom1stCycle, isExternalCourseChange,
                    isFirstCycleAttribution, isHandicappedContingent, isInternal2ndCycleAccess, isInternal3rdCycleAccess,
                    isInternalCourseChange, isIsolatedCurricularUnits, isMiddleAndSuperiorCourses, isOver23, isReIngression,
                    isTransfer, model);
        } catch (DomainException e) {
            addErrorMessage(e.getLocalizedMessage(), model);
            return "academic/candidacy/manageingressiontype/ingressiontype/update";
        }

        return "redirect:/academic/candidacy/manageingressiontype/ingressiontype/read/"
                + getIngressionType(model).getExternalId();
    }

    @Atomic
    public void updateIngressionType(String code, LocalizedString fullDescription, boolean hasEntryPhase,
            boolean isDirectAccessFrom1stCycle, boolean isExternalCourseChange, boolean isFirstCycleAttribution,
            boolean isHandicappedContingent, boolean isInternal2ndCycleAccess, boolean isInternal3rdCycleAccess,
            boolean isInternalCourseChange, boolean isIsolatedCurricularUnits, boolean isMiddleAndSuperiorCourses,
            boolean isOver23, boolean isReIngression, boolean isTransfer, Model m) {

        getIngressionType(m).setCode(code);
        getIngressionType(m).setFullDescription(fullDescription);
        getIngressionType(m).setHasEntryPhase(hasEntryPhase);
        getIngressionType(m).setIsDirectAccessFrom1stCycle(isDirectAccessFrom1stCycle);
        getIngressionType(m).setIsExternalCourseChange(isExternalCourseChange);
        getIngressionType(m).setIsFirstCycleAttribution(isFirstCycleAttribution);
        getIngressionType(m).setIsHandicappedContingent(isHandicappedContingent);
        getIngressionType(m).setIsInternal2ndCycleAccess(isInternal2ndCycleAccess);
        getIngressionType(m).setIsInternal3rdCycleAccess(isInternal3rdCycleAccess);
        getIngressionType(m).setIsInternalCourseChange(isInternalCourseChange);
        getIngressionType(m).setIsIsolatedCurricularUnits(isIsolatedCurricularUnits);
        getIngressionType(m).setIsMiddleAndSuperiorCourses(isMiddleAndSuperiorCourses);
        getIngressionType(m).setIsOver23(isOver23);
        getIngressionType(m).setIsReIngression(isReIngression);
        getIngressionType(m).setIsTransfer(isTransfer);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "academic/candidacy/manageingressiontype/ingressiontype/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(
            value = "fulldescription", required = false) org.fenixedu.commons.i18n.LocalizedString fullDescription,
            @RequestParam(value = "hasentryphase", required = false) boolean hasEntryPhase, @RequestParam(
                    value = "isdirectaccessfrom1stcycle", required = false) boolean isDirectAccessFrom1stCycle, @RequestParam(
                    value = "isexternalcoursechange", required = false) boolean isExternalCourseChange, @RequestParam(
                    value = "isfirstcycleattribution", required = false) boolean isFirstCycleAttribution, @RequestParam(
                    value = "ishandicappedcontingent", required = false) boolean isHandicappedContingent, @RequestParam(
                    value = "isinternal2ndcycleaccess", required = false) boolean isInternal2ndCycleAccess, @RequestParam(
                    value = "isinternal3rdcycleaccess", required = false) boolean isInternal3rdCycleAccess, @RequestParam(
                    value = "isinternalcoursechange", required = false) boolean isInternalCourseChange, @RequestParam(
                    value = "isisolatedcurricularunits", required = false) boolean isIsolatedCurricularUnits, @RequestParam(
                    value = "ismiddleandsuperiorcourses", required = false) boolean isMiddleAndSuperiorCourses, @RequestParam(
                    value = "isover23", required = false) boolean isOver23, @RequestParam(value = "isreingression",
                    required = false) boolean isReIngression,
            @RequestParam(value = "istransfer", required = false) boolean isTransfer, Model model) {
        IngressionType ingressionType = null;
        try {

            ingressionType =
                    createIngressionType(code, fullDescription, hasEntryPhase, isDirectAccessFrom1stCycle,
                            isExternalCourseChange, isFirstCycleAttribution, isHandicappedContingent, isInternal2ndCycleAccess,
                            isInternal3rdCycleAccess, isInternalCourseChange, isIsolatedCurricularUnits,
                            isMiddleAndSuperiorCourses, isOver23, isReIngression, isTransfer);
        } catch (DomainException e) {
            addErrorMessage(e.getLocalizedMessage(), model);
            return create(model);
        }

        model.addAttribute("ingressionType", ingressionType);

        return "redirect:/academic/candidacy/manageingressiontype/ingressiontype/read/"
                + getIngressionType(model).getExternalId();
    }

    @Atomic
    public IngressionType createIngressionType(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString fullDescription,
            boolean hasEntryPhase, boolean isDirectAccessFrom1stCycle, boolean isExternalCourseChange,
            boolean isFirstCycleAttribution, boolean isHandicappedContingent, boolean isInternal2ndCycleAccess,
            boolean isInternal3rdCycleAccess, boolean isInternalCourseChange, boolean isIsolatedCurricularUnits,
            boolean isMiddleAndSuperiorCourses, boolean isOver23, boolean isReIngression, boolean isTransfer) {

        IngressionType ingressionType = IngressionType.createIngressionType(code, fullDescription);
        ingressionType.setHasEntryPhase(hasEntryPhase);
        ingressionType.setIsDirectAccessFrom1stCycle(isDirectAccessFrom1stCycle);
        ingressionType.setIsExternalCourseChange(isExternalCourseChange);
        ingressionType.setIsFirstCycleAttribution(isFirstCycleAttribution);
        ingressionType.setIsHandicappedContingent(isHandicappedContingent);
        ingressionType.setIsInternal2ndCycleAccess(isInternal2ndCycleAccess);
        ingressionType.setIsInternal3rdCycleAccess(isInternal3rdCycleAccess);
        ingressionType.setIsInternalCourseChange(isInternalCourseChange);
        ingressionType.setIsIsolatedCurricularUnits(isIsolatedCurricularUnits);
        ingressionType.setIsMiddleAndSuperiorCourses(isMiddleAndSuperiorCourses);
        ingressionType.setIsOver23(isOver23);
        ingressionType.setIsReIngression(isReIngression);
        ingressionType.setIsTransfer(isTransfer);
        return ingressionType;
    }
}
