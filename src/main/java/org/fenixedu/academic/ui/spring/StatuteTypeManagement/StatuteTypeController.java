package org.fenixedu.academic.ui.spring.StatuteTypeManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.student.StatuteType;
import org.fenixedu.academic.ui.spring.AcademicBaseController;
import org.fenixedu.academic.ui.spring.AcademicController;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.Atomic;

@SpringFunctionality(app = AcademicController.class, title = "label.title.StatuteTypeManagement", accessGroup = "#managers")
@RequestMapping("/academic/statutetypemanagement/statutetype")
public class StatuteTypeController extends AcademicBaseController {
    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "redirect:/academic/statutetypemanagement/statutetype/";
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
        return "academic/statutetypemanagement/statutetype/search";
    }

    private List<StatuteType> collectSearchStatuteTypeDataSet() {
        return new ArrayList<StatuteType>(StatuteType.readAll());
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
        return "academic/statutetypemanagement/statutetype/read";
    }

    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") StatuteType statuteType, Model model) {

        setStatuteType(statuteType, model);
        try {
            //call the Atomic delete function
            deleteStatuteType(statuteType);

            addInfoMessage("Sucess deleting StatuteType ...", model);
            return "redirect:/academic/statutetypemanagement/statutetype/";
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage("Error deleting the StatuteType due to " + ex.getMessage(), model);
        }

        //The default mapping is the same Read View
        return "academic/statutetypemanagement/statutetype/read/" + getStatuteType(model).getExternalId();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "academic/statutetypemanagement/statutetype/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(
            @RequestParam(value = "code", required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name,
            @RequestParam(value = "active", required = false) java.lang.Boolean active,
            @RequestParam(value = "visible", required = false) boolean visible,
            @RequestParam(value = "appliedonregistration", required = false) java.lang.Boolean appliedOnRegistration,
            @RequestParam(value = "explicitcreation", required = false) boolean explicitCreation,
            @RequestParam(value = "grantsworkingstudentstatute", required = false) java.lang.Boolean grantsWorkingStudentStatute,
            @RequestParam(value = "associativeLeaderStatute", required = false) java.lang.Boolean associativeLeaderStatute,
            @RequestParam(value = "specialSeasonGrantedByRequest", required = false) java.lang.Boolean specialSeasonGrantedByRequest,
            @RequestParam(value = "grantOwnerStatute", required = false) java.lang.Boolean grantOwnerStatute, @RequestParam(
                    value = "seniorStatute", required = false) java.lang.Boolean seniorStatute, @RequestParam(
                    value = "handicappedStatute", required = false) java.lang.Boolean handicappedStatute, Model model) {

        StatuteType statuteType;
        try {
            statuteType =
                    createStatuteType(code, name, active, visible, appliedOnRegistration, explicitCreation,
                            grantsWorkingStudentStatute, associativeLeaderStatute, specialSeasonGrantedByRequest,
                            grantOwnerStatute, seniorStatute, handicappedStatute);
        } catch (DomainException ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
            return create(model);
        }

        //Add the bean to be used in the View
        model.addAttribute("statuteType", statuteType);

        return "redirect:/academic/statutetypemanagement/statutetype/";
    }

    @Atomic
    public StatuteType createStatuteType(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name,
            java.lang.Boolean active, boolean visible, java.lang.Boolean appliedOnRegistration, boolean explicitCreation,
            java.lang.Boolean grantsWorkingStudentStatute, java.lang.Boolean associativeLeaderStatute,
            java.lang.Boolean specialSeasonGrantedByRequest, java.lang.Boolean grantOwnerStatute,
            java.lang.Boolean seniorStatute, java.lang.Boolean handicappedStatute) {
        return new StatuteType(code, name, appliedOnRegistration, grantsWorkingStudentStatute, associativeLeaderStatute,
                specialSeasonGrantedByRequest, grantOwnerStatute, seniorStatute, handicappedStatute, active, explicitCreation,
                visible);
    }

    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") StatuteType statuteType, Model model) {
        setStatuteType(statuteType, model);
        return "academic/statutetypemanagement/statutetype/update";
    }

    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") StatuteType statuteType,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name,
            @RequestParam(value = "active", required = false) java.lang.Boolean active,
            @RequestParam(value = "visible", required = false) boolean visible,
            @RequestParam(value = "appliedonregistration", required = false) java.lang.Boolean appliedOnRegistration,
            @RequestParam(value = "explicitcreation", required = false) boolean explicitCreation,
            @RequestParam(value = "grantsworkingstudentstatute", required = false) java.lang.Boolean grantsWorkingStudentStatute,
            @RequestParam(value = "associativeLeaderStatute", required = false) java.lang.Boolean associativeLeaderStatute,
            @RequestParam(value = "specialSeasonGrantedByRequest", required = false) java.lang.Boolean specialSeasonGrantedByRequest,
            @RequestParam(value = "grantOwnerStatute", required = false) java.lang.Boolean grantOwnerStatute, @RequestParam(
                    value = "seniorStatute", required = false) java.lang.Boolean seniorStatute, @RequestParam(
                    value = "handicappedStatute", required = false) java.lang.Boolean handicappedStatute, Model model) {

        setStatuteType(statuteType, model);

        updateStatuteType(name, active, visible, appliedOnRegistration, explicitCreation, grantsWorkingStudentStatute,
                associativeLeaderStatute, specialSeasonGrantedByRequest, grantOwnerStatute, seniorStatute, handicappedStatute,
                model);

        return "redirect:/academic/statutetypemanagement/statutetype/read/" + getStatuteType(model).getExternalId();
    }

    @Atomic
    public void updateStatuteType(org.fenixedu.commons.i18n.LocalizedString name, java.lang.Boolean active, boolean visible,
            java.lang.Boolean appliedOnRegistration, boolean explicitCreation, java.lang.Boolean grantsWorkingStudentStatute,
            java.lang.Boolean associativeLeaderStatute, java.lang.Boolean specialSeasonGrantedByRequest,
            java.lang.Boolean grantOwnerStatute, java.lang.Boolean seniorStatute, java.lang.Boolean handicappedStatute, Model m) {
        getStatuteType(m).setName(name);
        getStatuteType(m).setActive(active);
        getStatuteType(m).setVisible(visible);
        getStatuteType(m).setAppliedOnRegistration(appliedOnRegistration);
        getStatuteType(m).setExplicitCreation(explicitCreation);
        getStatuteType(m).setGrantsWorkingStudentStatute(grantsWorkingStudentStatute);
        getStatuteType(m).setAssociativeLeaderStatute(associativeLeaderStatute);
        getStatuteType(m).setSpecialSeasonGrantedByRequest(specialSeasonGrantedByRequest);
        getStatuteType(m).setGrantOwnerStatute(grantOwnerStatute);
        getStatuteType(m).setSeniorStatute(seniorStatute);
        getStatuteType(m).setHandicappedStatute(handicappedStatute);
    }

}
