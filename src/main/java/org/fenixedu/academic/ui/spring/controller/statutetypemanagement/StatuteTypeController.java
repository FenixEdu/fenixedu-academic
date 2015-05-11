package org.fenixedu.academic.ui.spring.controller.statutetypemanagement;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.student.StatuteType;
import org.fenixedu.academic.ui.spring.ControllerHelper;
import org.fenixedu.academic.ui.spring.controller.AcademicAdministrationSpringApplication;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "label.title.StatuteTypeManagement",
        accessGroup = "#managers")
@RequestMapping("/academic-configurations/statutetypemanagement/statutetype")
public class StatuteTypeController {
    @RequestMapping
    public String home(Model model) {
        return "forward:/academic-configurations/statutetypemanagement/statutetype/";
    }

    private StatuteType getStatuteType(Model m) {
        return (StatuteType) m.asMap().get("statuteType");
    }

    private void setStatuteType(StatuteType statuteType, Model m) {
        m.addAttribute("statuteType", statuteType);
    }

    @Atomic
    public void deleteStatuteType(StatuteType statuteType) {
        statuteType.delete();
    }

    @RequestMapping(value = "/")
    public String search(@RequestParam(value = "code", required = false) java.lang.String code, Model model) {
        List<StatuteType> searchstatutetypeResultsDataSet = filterSearchStatuteType(code);

        //add the results dataSet to the model
        model.addAttribute("searchstatutetypeResultsDataSet", searchstatutetypeResultsDataSet);
        return "academic-configurations/statutetypemanagement/statutetype/search";
    }

    private List<StatuteType> collectSearchStatuteTypeDataSet() {
        return StatuteType.readAll().collect(Collectors.toList());
    }

    private List<StatuteType> filterSearchStatuteType(java.lang.String code) {

        return collectSearchStatuteTypeDataSet()
                .stream()
                .filter(statuteType -> code == null
                        || code.length() == 0
                        || (statuteType.getCode() != null && statuteType.getCode().length() > 0 && statuteType.getCode()
                                .toLowerCase().contains(code.toLowerCase()))).collect(Collectors.toList());
    }

    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") StatuteType statuteType, Model model) {
        setStatuteType(statuteType, model);
        return "academic-configurations/statutetypemanagement/statutetype/read";
    }

    @RequestMapping(value = "/delete/{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") StatuteType statuteType, Model model, RedirectAttributes redirectAttributes) {

        setStatuteType(statuteType, model);
        try {
            //call the Atomic delete function
            deleteStatuteType(statuteType);

            ControllerHelper.addInfoMessage(BundleUtil.getString(Bundle.APPLICATION, "success.studentStatute.delete"), model);
            return ControllerHelper.redirect("/academic-configurations/statutetypemanagement/statutetype/", model,
                    redirectAttributes);
        } catch (DomainException ex) {
            //Add error messages to the list
            ControllerHelper.addErrorMessage(
                    BundleUtil.getString(Bundle.APPLICATION, "error.studentStatute.delete") + ex.getMessage(), model);
        }

        //The default mapping is the same Read View
        return "academic-configurations/statutetypemanagement/statutetype/read/" + getStatuteType(model).getExternalId();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "academic-configurations/statutetypemanagement/statutetype/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "name",
            required = false) org.fenixedu.commons.i18n.LocalizedString name,
            @RequestParam(value = "active", required = false) boolean active,
            @RequestParam(value = "visible", required = false) boolean visible, @RequestParam(value = "specialSeasonGranted",
                    required = false) boolean specialSeasonGranted,
            @RequestParam(value = "explicitcreation", required = false) boolean explicitCreation, @RequestParam(
                    value = "workingstudentstatute", required = false) boolean workingStudentStatute, @RequestParam(
                    value = "associativeLeaderStatute", required = false) boolean associativeLeaderStatute, @RequestParam(
                    value = "specialSeasonGrantedByRequest", required = false) boolean specialSeasonGrantedByRequest,
            @RequestParam(value = "grantOwnerStatute", required = false) boolean grantOwnerStatute, @RequestParam(
                    value = "seniorStatute", required = false) boolean seniorStatute, @RequestParam(value = "handicappedStatute",
                    required = false) boolean handicappedStatute, Model model, RedirectAttributes redirectAttributes) {

        StatuteType statuteType;
        try {
            statuteType =
                    createStatuteType(code, name, active, visible, specialSeasonGranted, explicitCreation, workingStudentStatute,
                            associativeLeaderStatute, specialSeasonGrantedByRequest, grantOwnerStatute, seniorStatute,
                            handicappedStatute);
        } catch (DomainException ex) {
            ControllerHelper.addErrorMessage(ex.getLocalizedMessage(), model);
            return create(model);
        }

        //Add the bean to be used in the View
        model.addAttribute("statuteType", statuteType);

        return ControllerHelper
                .redirect("/academic-configurations/statutetypemanagement/statutetype/", model, redirectAttributes);
    }

    @Atomic
    public StatuteType createStatuteType(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name, boolean active,
            boolean visible, boolean specialSeasonGranted, boolean explicitCreation, boolean workingStudentStatute,
            boolean associativeLeaderStatute, boolean specialSeasonGrantedByRequest, boolean grantOwnerStatute,
            boolean seniorStatute, boolean handicappedStatute) {
        return new StatuteType(code, name, workingStudentStatute, associativeLeaderStatute, specialSeasonGrantedByRequest,
                grantOwnerStatute, seniorStatute, handicappedStatute, active, explicitCreation, visible, specialSeasonGranted);
    }

    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") StatuteType statuteType, Model model) {
        setStatuteType(statuteType, model);
        return "academic-configurations/statutetypemanagement/statutetype/update";
    }

    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") StatuteType statuteType,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, @RequestParam(
                    value = "active", required = false) boolean active,
            @RequestParam(value = "visible", required = false) boolean visible, @RequestParam(value = "specialSeasonGranted",
                    required = false) boolean specialSeasonGranted,
            @RequestParam(value = "explicitcreation", required = false) boolean explicitCreation, @RequestParam(
                    value = "workingstudentstatute", required = false) boolean workingStudentStatute, @RequestParam(
                    value = "associativeLeaderStatute", required = false) boolean associativeLeaderStatute, @RequestParam(
                    value = "specialSeasonGrantedByRequest", required = false) boolean specialSeasonGrantedByRequest,
            @RequestParam(value = "grantOwnerStatute", required = false) boolean grantOwnerStatute, @RequestParam(
                    value = "seniorStatute", required = false) boolean seniorStatute, @RequestParam(value = "handicappedStatute",
                    required = false) boolean handicappedStatute, Model model, RedirectAttributes redirectAttributes) {

        setStatuteType(statuteType, model);

        updateStatuteType(code, name, active, visible, specialSeasonGranted, explicitCreation, workingStudentStatute,
                associativeLeaderStatute, specialSeasonGrantedByRequest, grantOwnerStatute, seniorStatute, handicappedStatute,
                model);

        return ControllerHelper.redirect(
                "/academic-configurations/statutetypemanagement/statutetype/read/" + getStatuteType(model).getExternalId(),
                model, redirectAttributes);
    }

    @Atomic
    public void updateStatuteType(String code, org.fenixedu.commons.i18n.LocalizedString name, boolean active, boolean visible,
            boolean specialSeasonGranted, boolean explicitCreation, boolean workingStudentStatute,
            boolean associativeLeaderStatute, boolean specialSeasonGrantedByRequest, boolean grantOwnerStatute,
            boolean seniorStatute, boolean handicappedStatute, Model m) {
        StatuteType statuteType = getStatuteType(m);
        statuteType.setCode(code);
        statuteType.setName(name);
        statuteType.setActive(active);
        statuteType.setVisible(visible);
        statuteType.setSpecialSeasonGranted(specialSeasonGranted);
        statuteType.setExplicitCreation(explicitCreation);
        statuteType.setWorkingStudentStatute(workingStudentStatute);
        statuteType.setAssociativeLeaderStatute(associativeLeaderStatute);
        statuteType.setSpecialSeasonGrantedByRequest(specialSeasonGrantedByRequest);
        statuteType.setGrantOwnerStatute(grantOwnerStatute);
        statuteType.setSeniorStatute(seniorStatute);
        statuteType.setHandicappedStatute(handicappedStatute);
    }

}
