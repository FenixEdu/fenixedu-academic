package org.fenixedu.academic.ui.spring.controller.candidacy.manageingressiontype;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.spring.ControllerHelper;
import org.fenixedu.academic.ui.spring.controller.AcademicAdministrationSpringApplication;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "label.title.candidacy.manageIngressionType",
        accessGroup = "#managers")
@RequestMapping("/academic-configurations/candidacy/manageingressiontype/ingressiontype")
public class IngressionTypeController {
    @RequestMapping
    public String home(Model model) {
        return "forward:/academic-configurations/candidacy/manageingressiontype/ingressiontype/";
    }

    private IngressionType getIngressionType(Model m) {
        return (IngressionType) m.asMap().get("ingressionType");
    }

    private void setIngressionType(IngressionType ingressionType, Model m) {
        m.addAttribute("ingressionType", ingressionType);
    }

    @RequestMapping(value = "/delete/{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") IngressionType ingressionType, Model model, RedirectAttributes redirectAttributes) {

        setIngressionType(ingressionType, model);
        try {
            deleteIngressionType(ingressionType);
        } catch (DomainException e) {
            ControllerHelper.addErrorMessage(e.getLocalizedMessage(), model);
            return ControllerHelper.redirect("/academic-configurations/candidacy/manageingressiontype/ingressiontype/", model,
                    redirectAttributes);
        }
        ControllerHelper.addInfoMessage(BundleUtil.getString(Bundle.APPLICATION, "label.success.ingressionType.delete"), model);
        return ControllerHelper.redirect("/academic-configurations/candidacy/manageingressiontype/ingressiontype/", model,
                redirectAttributes);
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
        return "academic-configurations/candidacy/manageingressiontype/ingressiontype/search";
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
        return "academic-configurations/candidacy/manageingressiontype/ingressiontype/read";
    }

    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") IngressionType ingressionType, Model model) {
        setIngressionType(ingressionType, model);
        return "academic-configurations/candidacy/manageingressiontype/ingressiontype/update";
    }

    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") IngressionType ingressionType,
            @RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "description",
                    required = false) org.fenixedu.commons.i18n.LocalizedString description, @RequestParam(
                    value = "hasentryphase", required = false) boolean hasEntryPhase, @RequestParam(
                    value = "isdirectaccessfrom1stcycle", required = false) boolean isDirectAccessFrom1stCycle, @RequestParam(
                    value = "isexternaldegreechange", required = false) boolean isExternalDegreeChange, @RequestParam(
                    value = "isfirstcycleattribution", required = false) boolean isFirstCycleAttribution, @RequestParam(
                    value = "ishandicappedcontingent", required = false) boolean isHandicappedContingent, @RequestParam(
                    value = "isinternal2ndcycleaccess", required = false) boolean isInternal2ndCycleAccess, @RequestParam(
                    value = "isinternal3rdcycleaccess", required = false) boolean isInternal3rdCycleAccess, @RequestParam(
                    value = "isinternaldegreechange", required = false) boolean isInternalDegreeChange, @RequestParam(
                    value = "isisolatedcurricularunits", required = false) boolean isIsolatedCurricularUnits, @RequestParam(
                    value = "ismiddleandsuperiorcourses", required = false) boolean isMiddleAndSuperiorCourses, @RequestParam(
                    value = "isover23", required = false) boolean isOver23, @RequestParam(value = "isreingression",
                    required = false) boolean isReIngression,
            @RequestParam(value = "istransfer", required = false) boolean isTransfer, Model model,
            RedirectAttributes redirectAttributes) {

        setIngressionType(ingressionType, model);

        try {
            updateIngressionType(code, description, hasEntryPhase, isDirectAccessFrom1stCycle, isExternalDegreeChange,
                    isFirstCycleAttribution, isHandicappedContingent, isInternal2ndCycleAccess, isInternal3rdCycleAccess,
                    isInternalDegreeChange, isIsolatedCurricularUnits, isMiddleAndSuperiorCourses, isOver23, isReIngression,
                    isTransfer, model);
        } catch (DomainException e) {
            ControllerHelper.addErrorMessage(e.getLocalizedMessage(), model);
            return "academic-configurations/candidacy/manageingressiontype/ingressiontype/update";
        }

        return ControllerHelper.redirect("/academic-configurations/candidacy/manageingressiontype/ingressiontype/read/"
                + getIngressionType(model).getExternalId(), model, redirectAttributes);
    }

    @Atomic
    public void updateIngressionType(String code, LocalizedString description, boolean hasEntryPhase,
            boolean isDirectAccessFrom1stCycle, boolean isExternalDegreeChange, boolean isFirstCycleAttribution,
            boolean isHandicappedContingent, boolean isInternal2ndCycleAccess, boolean isInternal3rdCycleAccess,
            boolean isInternalDegreeChange, boolean isIsolatedCurricularUnits, boolean isMiddleAndSuperiorCourses,
            boolean isOver23, boolean isReIngression, boolean isTransfer, Model m) {

        IngressionType ingressionType = getIngressionType(m);
        ingressionType.setCode(code);
        ingressionType.setDescription(description);
        ingressionType.editState(hasEntryPhase, isDirectAccessFrom1stCycle, isExternalDegreeChange, isFirstCycleAttribution,
                isHandicappedContingent, isInternal2ndCycleAccess, isInternal3rdCycleAccess, isInternalDegreeChange,
                isIsolatedCurricularUnits, isMiddleAndSuperiorCourses, isOver23, isReIngression, isTransfer);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "academic-configurations/candidacy/manageingressiontype/ingressiontype/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(
            value = "description", required = false) org.fenixedu.commons.i18n.LocalizedString description, @RequestParam(
            value = "hasentryphase", required = false) boolean hasEntryPhase, @RequestParam(value = "isdirectaccessfrom1stcycle",
            required = false) boolean isDirectAccessFrom1stCycle, @RequestParam(value = "isexternaldegreechange",
            required = false) boolean isExternalDegreeChange,
            @RequestParam(value = "isfirstcycleattribution", required = false) boolean isFirstCycleAttribution, @RequestParam(
                    value = "ishandicappedcontingent", required = false) boolean isHandicappedContingent, @RequestParam(
                    value = "isinternal2ndcycleaccess", required = false) boolean isInternal2ndCycleAccess, @RequestParam(
                    value = "isinternal3rdcycleaccess", required = false) boolean isInternal3rdCycleAccess, @RequestParam(
                    value = "isinternaldegreechange", required = false) boolean isInternalDegreeChange, @RequestParam(
                    value = "isisolatedcurricularunits", required = false) boolean isIsolatedCurricularUnits, @RequestParam(
                    value = "ismiddleandsuperiorcourses", required = false) boolean isMiddleAndSuperiorCourses, @RequestParam(
                    value = "isover23", required = false) boolean isOver23, @RequestParam(value = "isreingression",
                    required = false) boolean isReIngression,
            @RequestParam(value = "istransfer", required = false) boolean isTransfer, Model model,
            RedirectAttributes redirectAttributes) {
        IngressionType ingressionType = null;
        try {

            ingressionType =
                    createIngressionType(code, description, hasEntryPhase, isDirectAccessFrom1stCycle, isExternalDegreeChange,
                            isFirstCycleAttribution, isHandicappedContingent, isInternal2ndCycleAccess, isInternal3rdCycleAccess,
                            isInternalDegreeChange, isIsolatedCurricularUnits, isMiddleAndSuperiorCourses, isOver23,
                            isReIngression, isTransfer);
        } catch (DomainException e) {
            ControllerHelper.addErrorMessage(e.getLocalizedMessage(), model);
            return create(model);
        }

        model.addAttribute("ingressionType", ingressionType);

        return ControllerHelper.redirect("/academic-configurations/candidacy/manageingressiontype/ingressiontype/read/"
                + getIngressionType(model).getExternalId(), model, redirectAttributes);
    }

    @Atomic
    public IngressionType createIngressionType(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString description,
            boolean hasEntryPhase, boolean isDirectAccessFrom1stCycle, boolean isExternalDegreeChange,
            boolean isFirstCycleAttribution, boolean isHandicappedContingent, boolean isInternal2ndCycleAccess,
            boolean isInternal3rdCycleAccess, boolean isInternalDegreeChange, boolean isIsolatedCurricularUnits,
            boolean isMiddleAndSuperiorCourses, boolean isOver23, boolean isReIngression, boolean isTransfer) {

        IngressionType ingressionType = IngressionType.createIngressionType(code, description);
        ingressionType.editState(hasEntryPhase, isDirectAccessFrom1stCycle, isExternalDegreeChange, isFirstCycleAttribution,
                isHandicappedContingent, isInternal2ndCycleAccess, isInternal3rdCycleAccess, isInternalDegreeChange,
                isIsolatedCurricularUnits, isMiddleAndSuperiorCourses, isOver23, isReIngression, isTransfer);
        return ingressionType;
    }
}
