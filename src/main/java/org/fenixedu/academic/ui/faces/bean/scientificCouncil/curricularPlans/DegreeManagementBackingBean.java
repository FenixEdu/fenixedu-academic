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
package org.fenixedu.academic.ui.faces.bean.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.faces.component.html.HtmlInputText;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeInfo;
import org.fenixedu.academic.domain.DegreeOfficialPublication;
import org.fenixedu.academic.domain.DegreeSpecializationArea;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.scientificCouncil.curricularPlans.ChangeDegreeOfficialPublicationReference;
import org.fenixedu.academic.service.services.scientificCouncil.curricularPlans.CreateDegree;
import org.fenixedu.academic.service.services.scientificCouncil.curricularPlans.CreateDegreeOfficialPublication;
import org.fenixedu.academic.service.services.scientificCouncil.curricularPlans.CreateDegreeSpecializationArea;
import org.fenixedu.academic.service.services.scientificCouncil.curricularPlans.DeleteDegree;
import org.fenixedu.academic.service.services.scientificCouncil.curricularPlans.DeleteDegreeSpecializationArea;
import org.fenixedu.academic.service.services.scientificCouncil.curricularPlans.EditDegree;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.commons.i18n.LocalizedString.Builder;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import pt.ist.fenixframework.FenixFramework;

public class DegreeManagementBackingBean extends FenixBackingBean {

    private static final Logger logger = LoggerFactory.getLogger(DegreeManagementBackingBean.class);

    private final String NO_SELECTION = "noSelection";

    private String degreeId;

    private Degree degree;

    private String name;

    private String nameEn;

    private String associatedInstitutions;

    private String associatedInstitutionsEn;

    private String acronym;

    private String bolonhaDegreeType;

    private String gradeScale;

    private Double ectsCredits;

    private String prevailingScientificArea;

    private String selectedExecutionYearID;

    private String academicAdminOfficeId;

    private String code;

    private String ministryCode;

    private HtmlInputText nameInputComponent;

    private HtmlInputText nameEnInputComponent;

    private HtmlInputText associatedInstitutionsInputComponent;

    private HtmlInputText associatedInstitutionsEnInputComponent;

    private final List<OfficialPubBeanPrint> officialPublicationsBeanPrettyPrints =
            new ArrayList<DegreeManagementBackingBean.OfficialPubBeanPrint>();

    private OfficialPublicationBean officialPublicationBean;

    private boolean pairsCreated;

    private String degreeOfficialPublicationId;

    public String getDegreeOfficialPublicationId() {
        return degreeOfficialPublicationId;
    }

    public void setDegreeOfficialPublicationId(String degreeOfficialPublicationId) {
        this.degreeOfficialPublicationId = degreeOfficialPublicationId;
    }

    private List<Degree> getDegrees() {
        if (getDegreeType() == null) {
            return Degree.readNotEmptyDegrees();
        }

        return Degree.readNotEmptyDegrees().stream().filter(degree -> degree.getDegreeType().equals(getDegreeType()))
                .collect(Collectors.toList());
    }

    /**
     * @return all degrees except the empty one
     */
    public List<Degree> getBolonhaDegrees() {
        final List<Degree> result = getDegrees();
        Collections.sort(result, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        return result;
    }

    public List<Degree> getFilteredBolonhaDegrees() {
        final Set<Degree> result = new HashSet<Degree>();
        for (final Degree degree : getDegrees()) {
            for (final DegreeCurricularPlan dcp : degree.getDegreeCurricularPlansSet()) {
                if (dcp.getCurricularStage().equals(CurricularStage.PUBLISHED)
                        || dcp.getCurricularStage().equals(CurricularStage.APPROVED)
                        || dcp.getCurricularPlanMembersGroup().isMember(this.getUserView())) {
                    result.add(degree);
                }
            }
        }

        final List<Degree> orderedResult = new ArrayList<Degree>(result);
        Collections.sort(orderedResult, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

        return orderedResult;
    }

    public String getDegreeId() {
        return (degreeId == null) ? (degreeId = getAndHoldStringParameter("degreeId")) : degreeId;
    }

    public void setDegreeId(String degreeId) {
        this.degreeId = degreeId;
    }

    public Degree getDegree() {
        return (degree == null) ? (degree = FenixFramework.getDomainObject(getDegreeId())) : degree;
    }

    private LocalizedString getNameLocalizedString() {
        return new LocalizedString.Builder().with(LocaleUtils.PT, name).with(LocaleUtils.EN, nameEn).build();
    }

    private String getAssociatedInstitutionsContent(Locale locale, Supplier<String> getter, Consumer<String> setter) {
        if (getter.get() == null && getDegree() != null) {
            ExecutionYear selectedExecutionYear = getSelectedExecutionYear() == null ? ExecutionYear.readCurrentExecutionYear() : getSelectedExecutionYear();
            DegreeInfo degreeInfoFor = getDegreeInfo(selectedExecutionYear);
            String content = degreeInfoFor.getAssociatedInstitutions().getContent(locale);
            setter.accept(content);
            return content;
        }
        return getter.get();
    }

    private LocalizedString getAssociatedInstitutionsLocalizedString() {
        final Builder builder = new LocalizedString.Builder();
        if (!Strings.isNullOrEmpty(getAssociatedInstitutions())) {
            builder.with(LocaleUtils.PT, getAssociatedInstitutions());
        }
        if (!Strings.isNullOrEmpty(getAssociatedInstitutionsEn())) {
            builder.with(LocaleUtils.EN, getAssociatedInstitutionsEn());
        }
        return builder.build();
    }

    public String getAssociatedInstitutions() {
        return getAssociatedInstitutionsContent(LocaleUtils.PT, () -> associatedInstitutions, (val) -> associatedInstitutions = val);
    }

    public void setAssociatedInstitutions(String associatedInstitutions) {
        this.associatedInstitutions = associatedInstitutions;
    }

    public String getAssociatedInstitutionsEn() {
        return getAssociatedInstitutionsContent(LocaleUtils.EN, () -> associatedInstitutionsEn, (val) -> associatedInstitutionsEn = val);
    }

    public void setAssociatedInstitutionsEn(String associatedInstitutionsEn) {
        this.associatedInstitutionsEn = associatedInstitutionsEn;
    }

    public String getName() {
        return (name == null && getDegree() != null) ? (name =
                                                            getDegree().getNameFor(getSelectedExecutionYear()).getContent(org.fenixedu.academic.util.LocaleUtils.PT)) : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return (nameEn == null && getDegree() != null) ? (nameEn =
                                                              getDegree().getNameFor(getSelectedExecutionYear()).getContent(org.fenixedu.academic.util.LocaleUtils.EN)) : nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getAcronym() {
        return (acronym == null && getDegree() != null) ? (acronym = getDegree().getSigla()) : acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getBolonhaDegreeType() {
        return (bolonhaDegreeType == null && getDegree() != null) ? (bolonhaDegreeType =
                getDegree().getDegreeType().getExternalId()) : bolonhaDegreeType;
    }

    public DegreeType getDegreeType() {
        return FenixFramework.getDomainObject(getBolonhaDegreeType());
    }

    public void setBolonhaDegreeType(String bolonhaDegreeType) {
        this.bolonhaDegreeType = bolonhaDegreeType;
    }

    public String getGradeScale() {
        return (gradeScale == null && getDegree() != null) ? (gradeScale =
                (getDegree().getGradeScale() != null ? getDegree().getGradeScale().getName() : null)) : gradeScale;
    }

    public void setGradeScale(String gradeScale) {
        this.gradeScale = gradeScale;
    }

    public String getCode() {
        return (code == null && getDegree() != null) ? (code = getDegree().getCode()) : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMinistryCode() {
        return (ministryCode == null && getDegree() != null) ? (ministryCode = getDegree().getMinistryCode()) : ministryCode;
    }

    public void setMinistryCode(String ministryCode) {
        this.ministryCode = ministryCode;
    }

    public Double getEctsCredits() {
        if (ectsCredits == null) {
            if (getDegree() != null) {
                ectsCredits = getDegree().getEctsCredits();
            } else if (getBolonhaDegreeType() != null && !getBolonhaDegreeType().equals(NO_SELECTION)) {
                ectsCredits = 0.0;
            }
        }
        return ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

    public String getPrevailingScientificArea() {
        return (prevailingScientificArea == null && getDegree() != null) ? (prevailingScientificArea =
                getDegree().getPrevailingScientificArea()) : prevailingScientificArea;
    }

    public void setPrevailingScientificArea(String prevailingScientificArea) {
        this.prevailingScientificArea = prevailingScientificArea;
    }

    /**
     * @return all degrees types except the empty one
     */
    public List<SelectItem> getBolonhaDegreeTypes() {

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(this.NO_SELECTION, BundleUtil.getString(Bundle.SCIENTIFIC, "choose")));

        DegreeType.all().filter(type -> !type.isEmpty()).forEach(type -> {
            result.add(new SelectItem(type.getExternalId(), type.getName().getContent()));
        });

        return result;
    }

    public List<SelectItem> getAcademicAdminOffices() {

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(this.NO_SELECTION, BundleUtil.getString(Bundle.SCIENTIFIC, "choose")));

        for (AdministrativeOffice administrativeOffice : rootDomainObject.getAdministrativeOfficesSet()) {
            result.add(new SelectItem(administrativeOffice.getExternalId(), administrativeOffice.getName().getContent()));
        }

        return result;
    }

    public List<SelectItem> getGradeScales() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        result.add(new SelectItem(this.NO_SELECTION, BundleUtil.getString(Bundle.SCIENTIFIC, "choose")));
        result.add(new SelectItem(GradeScale.TYPE20.name(), BundleUtil.getString(Bundle.ENUMERATION, GradeScale.TYPE20.name())));
        result.add(new SelectItem(GradeScale.TYPE5.name(), BundleUtil.getString(Bundle.ENUMERATION, GradeScale.TYPE5.name())));

        return result;
    }

    public String createDegree() {
        if (this.bolonhaDegreeType.equals(this.NO_SELECTION)) {
            this.setErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "choose.degreeType"));
            return "";
        }
        if (getAcademicAdminOfficeId().equals(this.NO_SELECTION)) {
            this.setErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "choose.administrativeOffice"));
            return "";
        }

        if (this.name == null || this.name.length() == 0 || this.nameEn == null || this.nameEn.length() == 0
                || this.acronym == null || this.acronym.length() == 0 || this.gradeScale.equals(this.NO_SELECTION)) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "please.fill.mandatory.fields"));
            return "";
        }

        try {
            AdministrativeOffice administrativeOffice = FenixFramework.getDomainObject(getAcademicAdminOfficeId());
            CreateDegree.run(getNameLocalizedString(), this.acronym, getAssociatedInstitutionsLocalizedString(), FenixFramework.getDomainObject(this.bolonhaDegreeType), this.getEctsCredits(), GradeScale.valueOf(gradeScale), this.prevailingScientificArea, administrativeOffice);
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (DomainException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.creatingDegree"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "degree.created"));
        return "curricularPlansManagement";
    }

    public String editDegree() {
        if (this.bolonhaDegreeType != null && this.bolonhaDegreeType.equals(this.NO_SELECTION)
                || this.gradeScale.equals(this.NO_SELECTION)) {
            this.setErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "choose.request"));
            return "";
        }

        name = (String) getNameInputComponent().getValue();
        nameEn = (String) getNameEnInputComponent().getValue();
        associatedInstitutions = (String) getAssociatedInstitutionsInputComponent().getValue();
        associatedInstitutionsEn = (String) getAssociatedInstitutionsEnInputComponent().getValue();

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(nameEn) || StringUtils.isEmpty(this.acronym)) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "please.fill.mandatory.fields"));
            return "";
        }

        try {
            EditDegree.run(this.getDegreeId(), getNameLocalizedString(),  this.acronym, getAssociatedInstitutionsLocalizedString(),
                    FenixFramework.getDomainObject(getBolonhaDegreeType()), this.getEctsCredits(),
                    GradeScale.valueOf(gradeScale), this.prevailingScientificArea, getSelectedExecutionYear(), getCode(),
                    getMinistryCode());
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (DomainException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.editingDegree"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "degree.edited"));
        return "curricularPlansManagement";
    }

    public String editAssociatedInstitutions() {
        associatedInstitutions = (String) getAssociatedInstitutionsInputComponent().getValue();
        associatedInstitutionsEn = (String) getAssociatedInstitutionsEnInputComponent().getValue();

        try {
            EditDegree.run(this.getDegreeId(), getNameLocalizedString(),  this.acronym, getAssociatedInstitutionsLocalizedString(),
                    FenixFramework.getDomainObject(getBolonhaDegreeType()), this.getEctsCredits(),
                    GradeScale.valueOf(gradeScale), this.prevailingScientificArea, getSelectedExecutionYear(), getCode(),
                    getMinistryCode());
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (DomainException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.editingDegree"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "degree.edited"));
        return "curricularPlansManagement";
    }

    public String deleteDegree() {
        try {
            DeleteDegree.run(this.getDegreeId());
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (DomainException e) {
            addErrorMessage(e.getLocalizedMessage());
            return "curricularPlansManagement";
        } catch (Exception e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.deletingDegree"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "degree.deleted"));
        return "curricularPlansManagement";
    }

    public void setSelectedExecutionYearId(String executionYearId) {
        this.selectedExecutionYearID = executionYearId;
    }

    public void onChangeExecutionYear(ValueChangeEvent valueChangeEvent) {
        String executionYearId = (String) valueChangeEvent.getNewValue();
        if (!getSelectedExecutionYearId().equals(executionYearId) && getDegree() != null) {
            setSelectedExecutionYearId(executionYearId);
            ExecutionYear executionYear = getSelectedExecutionYear();
            DegreeInfo degreeInfo = getDegree().getMostRecentDegreeInfo(executionYear);
            if (degreeInfo == null) {
                addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.Degree.doesnot.have.degreeInfo.for.year",
                        executionYear.getName()));
                degreeInfo = getDegree().getMostRecentDegreeInfo();
            }

            if (degreeInfo != null) {
                this.name = degreeInfo.getName().getContent(LocaleUtils.PT);
                this.nameEn = degreeInfo.getName().getContent(LocaleUtils.EN);
                this.nameInputComponent.setValue(this.name);
                this.nameEnInputComponent.setValue(this.nameEn);
                this.associatedInstitutions = degreeInfo.getAssociatedInstitutions().getContent(LocaleUtils.PT);
                this.associatedInstitutionsEn = degreeInfo.getAssociatedInstitutions().getContent(LocaleUtils.EN);
                this.associatedInstitutionsInputComponent.setValue(this.associatedInstitutions);
                this.associatedInstitutionsEnInputComponent.setValue(this.associatedInstitutionsEn);
            }
        }
    }

    public String getSelectedExecutionYearId() {
        if (selectedExecutionYearID == null) {
            selectedExecutionYearID = getAndHoldStringParameter("selectedExecutionYearId");
            if (selectedExecutionYearID == null) {
                selectedExecutionYearID = ExecutionYear.readCurrentExecutionYear().getExternalId();
            }
        }
        return selectedExecutionYearID;
    }

    public ExecutionYear getSelectedExecutionYear() {
        return FenixFramework.getDomainObject(getSelectedExecutionYearId());
    }

    public List<SelectItem> getOpenExecutionYears() {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
            selectItems.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
        }
        return selectItems;
    }

    public String getExistentDegreeInfoYears () {
        return getDegree().getDegreeInfosSet().stream().sorted(DegreeInfo.COMPARATOR_BY_EXECUTION_YEAR).map(di -> di.getExecutionYear().getName()).collect(Collectors.joining(", "));
    }

    public HtmlInputText getNameInputComponent() {
        if (this.nameInputComponent == null) {
            this.nameInputComponent = new HtmlInputText();
            ExecutionYear executionYear =
                    (getSelectedExecutionYear() != null) ? getSelectedExecutionYear() : ExecutionYear.readCurrentExecutionYear();

            DegreeInfo degreeInfo = getDegreeInfo(executionYear);
            setSelectedExecutionYearId(degreeInfo.getExecutionYear().getExternalId());
            this.nameInputComponent.setValue(degreeInfo.getName().getContent(org.fenixedu.academic.util.LocaleUtils.PT));
        }
        return this.nameInputComponent;
    }

    public void setNameInputComponent(HtmlInputText nameInputComponent) {
        this.nameInputComponent = nameInputComponent;
    }

    public HtmlInputText getNameEnInputComponent() {
        if (this.nameEnInputComponent == null) {
            this.nameEnInputComponent = new HtmlInputText();
            ExecutionYear executionYear =
                (getSelectedExecutionYear() != null) ? getSelectedExecutionYear() : ExecutionYear.readCurrentExecutionYear();
            final DegreeInfo degreeInfo = getDegreeInfo(executionYear);
            this.nameEnInputComponent.setValue(degreeInfo.getName().getContent(org.fenixedu.academic.util.LocaleUtils.EN));
        }

        return this.nameEnInputComponent;
    }

    public void setNameEnInputComponent(HtmlInputText nameEnInputComponent) { this.nameEnInputComponent = nameEnInputComponent;}

    public HtmlInputText getAssociatedInstitutionsInputComponent() {
        if (this.associatedInstitutionsInputComponent == null) {
            this.associatedInstitutionsInputComponent = new HtmlInputText();
            this.associatedInstitutionsInputComponent.setValue(getAssociatedInstitutions());
        }

        return this.associatedInstitutionsInputComponent;
    }

    public void setAssociatedInstitutionsInputComponent(HtmlInputText associatedInstitutionsInputComponent) {
        this.associatedInstitutionsInputComponent = associatedInstitutionsInputComponent;
    }

    public HtmlInputText getAssociatedInstitutionsEnInputComponent() {
        if (this.associatedInstitutionsEnInputComponent == null) {
            this.associatedInstitutionsEnInputComponent = new HtmlInputText();
            this.associatedInstitutionsEnInputComponent.setValue(getAssociatedInstitutionsEn());
        }

        return this.associatedInstitutionsEnInputComponent;
    }

    public void setAssociatedInstitutionsEnInputComponent(HtmlInputText associatedInstitutionsEnInputComponent) {
        this.associatedInstitutionsEnInputComponent = associatedInstitutionsEnInputComponent;
    }

    private DegreeInfo getDegreeInfo(final ExecutionYear executionYear) {
        DegreeInfo degreeInfo = getDegree().getDegreeInfoFor(executionYear);
        if (degreeInfo == null) {
            degreeInfo = getDegree().getMostRecentDegreeInfo();
            // setSelectedExecutionYearId(degreeInfo.getExecutionYear().
            // getExternalId());
        }
        return degreeInfo;
    }

    public boolean isAbleToEditName() {
        final DegreeCurricularPlan firstDegreeCurricularPlan = getDegree().getFirstDegreeCurricularPlan();
        final DegreeCurricularPlan lastActiveDegreeCurricularPlan = getDegree().getLastActiveDegreeCurricularPlan();
        if (firstDegreeCurricularPlan == null) {
            return true;
        }
        ExecutionYear firstExecutionYear =
                ExecutionYear.readByDateTime(firstDegreeCurricularPlan.getInitialDateYearMonthDay().toDateTimeAtMidnight());
        if (getSelectedExecutionYear().isBefore(firstExecutionYear)) {
            return true;
        }
        if (lastActiveDegreeCurricularPlan == null) {
            return true;
        }
        if (lastActiveDegreeCurricularPlan.getExecutionDegreesSet().isEmpty()) {
            return true;
        }
        if (getSelectedExecutionYear().isAfter(ExecutionYear.readCurrentExecutionYear())) {
            return true;
        }
        if (getSelectedExecutionYear().isCurrent()) {
            return true;
        }
        return false;
    }

    public OfficialPublicationBean getOfficialPublicationBean() {
        if (this.officialPublicationBean == null) {
            this.officialPublicationBean = new OfficialPublicationBean(this);
        }

        return officialPublicationBean;
    }

    public List<OfficialPubBeanPrint> getOfficialPublicationsBeanPrettyPrints() {

        Degree degree = getDegree();

        // TODO: remove
        if (degree == null) {
            throw new RuntimeException("GONE!!!!");
        }

        // test if the list is already filled, return if so
        if (this.officialPublicationsBeanPrettyPrints.size() == degree.getOfficialPublicationSet().size()) {
            return this.officialPublicationsBeanPrettyPrints;
        }

        this.officialPublicationsBeanPrettyPrints.clear();

        for (DegreeOfficialPublication degreeOfficialPublication : getDegree().getOfficialPublicationSet()) {
            officialPublicationsBeanPrettyPrints.add(new OfficialPubBeanPrint(degreeOfficialPublication));
        }

        return officialPublicationsBeanPrettyPrints;
    }

    public String getAcademicAdminOfficeId() {
        return academicAdminOfficeId;
    }

    public void setAcademicAdminOfficeId(String academicAdminOfficeId) {
        this.academicAdminOfficeId = academicAdminOfficeId;
    }

    public class OfficialPubBeanPrint {

        private final DegreeOfficialPublication degreeOfficialPublication;
        private final String date;
        private final String officialReference;
        private final String linkReference;
        private final Boolean includeInDiplomaSuplement;
        private final String specializationsAreas;

        public OfficialPubBeanPrint(DegreeOfficialPublication degreeOfficialPublication) {
            this.degreeOfficialPublication = degreeOfficialPublication;
            this.date = degreeOfficialPublication.getPublication().toString();
            this.officialReference = degreeOfficialPublication.getOfficialReference();
            this.specializationsAreas = fetchSpecializationAreas(degreeOfficialPublication);
            this.linkReference=degreeOfficialPublication.getLinkReference();
            this.includeInDiplomaSuplement=degreeOfficialPublication.getIncludeInDiplomaSuplement();
        }

        public DegreeOfficialPublication getDegreeOfficialPublication() {
            return degreeOfficialPublication;
        }

        public String getOfficialReference() {
            return officialReference;
        }

        public String getSpecializationsAreas() {
            return this.specializationsAreas;
        }

        public String getDate() {
            return date;
        }
        
        public String getLinkReference() {
            return linkReference;
        }

        public Boolean getIncludeInDiplomaSuplement() {
            return includeInDiplomaSuplement;
        }

        public String fetchSpecializationAreas(DegreeOfficialPublication degreeOfficialPublication) {
            String specializationAreas = "";

            // TODO: Remove
            if (degreeOfficialPublication == null) {
                throw new RuntimeException("que bodega..");
            }

            for (DegreeSpecializationArea specializationArea : degreeOfficialPublication.getSpecializationAreaSet()) {
                specializationAreas +=
                        (specializationAreas.compareTo("") == 0 ? "" : ", ") + specializationArea.getName().getContent();
            }

            return specializationAreas;
        }

    }

    public static class OfficialPublicationBean extends DegreeManagementBackingBean {

        private String date;
        private String officialReference;
        private String linkReference;
        private Boolean includeInDiplomaSuplement;
        private String officialPubId;
        private DegreeOfficialPublication degreeOfficialPublication;
        private String newOfficialReference;
        private String specializationNameEng;
        private String specializationNamePt;
        private List<Pair> specializationNames;
        private String specializationIdToDelete;
        private DegreeSpecializationArea specializationAreaToDelete;
        private DegreeOfficialPublication degreeOfficialPublicationGoBack;

        public static class Pair extends FenixBackingBean {
            private String firstValue;
            private String secondValue;
            private final DegreeSpecializationArea areaReference;

            public Pair(DegreeSpecializationArea area) {
                this.firstValue = (area.getNameEn() == null ? "" : area.getNameEn());
                this.secondValue = (area.getNamePt() == null ? "" : area.getNamePt());
                this.areaReference = area;
            }

            public String getFirstValue() {
                return firstValue;
            }

            public void setFirstValue(String firstValue) {
                this.firstValue = firstValue;
            }

            public String getSecondValue() {
                return secondValue;
            }

            public void setSecondValue(String secondValue) {
                this.secondValue = secondValue;
            }

            public void updateFirstValue() {
                this.firstValue = areaReference.getNameEn();
            }

            public void updateSecondValue() {
                this.secondValue = areaReference.getNamePt();
            }

            public DegreeSpecializationArea getAreaReference() {
                return areaReference;
            }

            public boolean isEnModified() {
                String nameEng = this.areaReference.getNameEn();
                if (nameEng != null) {
                    return nameEng.compareTo(this.firstValue) != 0;
                } else if (this.firstValue != null || this.firstValue.compareTo("") != 0) {
                    return true;
                }
                return false;
            }

            public boolean isPtModified() {
                String namePt = this.areaReference.getNamePt();
                if (namePt != null) {
                    return namePt.compareTo(this.secondValue) != 0;
                } else if (this.secondValue != null || this.secondValue.compareTo("") != 0) {
                    return true;
                }
                return false;
            }

            public boolean isModified() {
                return isEnModified() || isPtModified();
            }

        }

        private final DegreeManagementBackingBean degreeManagementBackingBean;

        public OfficialPublicationBean(DegreeManagementBackingBean degreeManagementBackingBean) {
            this.degreeManagementBackingBean = degreeManagementBackingBean;
            super.pairsCreated = true;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getOfficialReference() {
            return officialReference;
        }

        public void setOfficialReference(String officialReference) {
            this.officialReference = officialReference;
        }
                
        public String getLinkReference() {
            return linkReference;
        }

        public void setLinkReference(String linkReference) {
            this.linkReference = linkReference;
        }

        public Boolean getIncludeInDiplomaSuplement() {
            return includeInDiplomaSuplement;
        }

        public void setIncludeInDiplomaSuplement(Boolean includeInDiplomaSuplement) {
            this.includeInDiplomaSuplement = includeInDiplomaSuplement;
        }

        public String makeAndInsertDegreeOfficialPublication() {

            // to remove
            if (date == null) {
                this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.protocol.invalidDates"));
                return "";
            }
            String[] dateFields = date.split("/");

            if (dateFields.length != 3) {
                this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.protocol.invalidDates"));
                return "";
            }

            if (dateFields[0].length() != 2 || dateFields[1].length() != 2 || dateFields[2].length() != 4) {
                this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.protocol.invalidDates"));
                return "";
            }

            if (officialReference.isEmpty() || officialReference.compareTo("") == 0) {
                this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "confirm.error.edit.reference.officialPublication"));
                return "";
            }

            LocalDate localDate =
                    new LocalDate(Integer.parseInt(dateFields[2]), Integer.parseInt(dateFields[1]),
                            Integer.parseInt(dateFields[0]));

            Degree degree = getDegree();

            try {
                CreateDegreeOfficialPublication.run(degree, localDate, officialReference,linkReference,includeInDiplomaSuplement);
            } catch (IllegalDataAccessException e) {
                this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.notAuthorized"));
                return "";
            } catch (DomainException e) {
                this.addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
                return "";
            } catch (FenixServiceException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
            }

            this.addInfoMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "degreeOfficialPublication.created"));
            return "editDegree";
        }

        public String getOfficialPubId() {

            return (officialPubId == null) ? (officialPubId = getAndHoldStringParameter("officialPubId")) : officialPubId;
        }

        public void setOfficialPubId(String officialPubId) {
            this.officialPubId = officialPubId;
        }

        public DegreeOfficialPublication getDegreeOfficialPublication() {
            this.degreeOfficialPublication =
                    (this.getOfficialPubId() == null ? null : (DegreeOfficialPublication) FenixFramework
                            .getDomainObject(getOfficialPubId()));

            return this.degreeOfficialPublication;
        }

        public void setDegreeOfficialPublication(DegreeOfficialPublication degreeOfficialPublication) {
            this.degreeOfficialPublication = degreeOfficialPublication;
        }

        public String getSpecializationNameEng() {
            return specializationNameEng;
        }

        public void setSpecializationNameEng(String specializationNameEng) {
            this.specializationNameEng = specializationNameEng;
        }

        public String getSpecializationNamePt() {
            return specializationNamePt;
        }

        public void setSpecializationNamePt(String specializationNamePt) {
            this.specializationNamePt = specializationNamePt;
        }

        /**
         * TODO: add error message
         */
        public void addSpecializationArea() {

            try {
                CreateDegreeSpecializationArea.run(this.getDegreeOfficialPublication(), getSpecializationNameEng(),
                        getSpecializationNamePt());
            } catch (FenixServiceException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
            }
        }

        public String getSpecializationIdToDelete() {
            return (specializationIdToDelete == null) ? (this.specializationIdToDelete =
                    getAndHoldStringParameter("specializationId")) : specializationIdToDelete;
        }

        public void setSpecializationIdToDelete(String specializationIdToDelete) {
            this.specializationIdToDelete = specializationIdToDelete;
        }

        public DegreeSpecializationArea getSpecializationAreaToDelete() {
            this.specializationAreaToDelete =
                    (DegreeSpecializationArea) (getSpecializationIdToDelete() == null ? null : FenixFramework
                            .getDomainObject(getSpecializationIdToDelete()));

            if (getDegreeOfficialPublicationGoBack() == null && this.specializationAreaToDelete != null) {
                setDegreeOfficialPublicationGoBack(this.specializationAreaToDelete.getOfficialPublication());
            }
            return this.specializationAreaToDelete;
        }

        public void setSpecializationAreaToDelete(DegreeSpecializationArea specializationAreaToDelete) {
            this.specializationAreaToDelete = specializationAreaToDelete;
        }

        /**
         * TODO: add error message
         */
        public String removeSpecializationAreaToDelete() {
            setDegreeOfficialPublicationGoBack(getSpecializationAreaToDelete().getOfficialPublication());
            try {
                DeleteDegreeSpecializationArea.run(getSpecializationAreaToDelete().getOfficialPublication(),
                        getSpecializationAreaToDelete());
            } catch (FenixServiceException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
            }
            return "editDegreeOfficialPublication";
        }

        public DegreeOfficialPublication getDegreeOfficialPublicationGoBack() {
            return degreeOfficialPublicationGoBack;
        }

        public void setDegreeOfficialPublicationGoBack(DegreeOfficialPublication degreeOfficialPublicationGoBack) {
            this.degreeOfficialPublicationGoBack = degreeOfficialPublicationGoBack;
        }

        public String getNewOfficialReference() {
            if (getDegreeOfficialPublication() != null && (newOfficialReference == null || newOfficialReference.isEmpty())) {
                this.newOfficialReference = getDegreeOfficialPublication().getOfficialReference();
            }
            return newOfficialReference;
        }

        public void setNewOfficialReference(String newOfficialReference) {
            this.newOfficialReference = newOfficialReference;
        }

        public List<Pair> getSpecializationNames() {
            List<Pair> specializations = null;

            if (getDegreeOfficialPublication() != null && specializationNames == null) {
                specializations = new ArrayList<DegreeManagementBackingBean.OfficialPublicationBean.Pair>();
                for (DegreeSpecializationArea area : getDegreeOfficialPublication().getSpecializationAreaSet()) {
                    specializations.add(new Pair(area));
                }
                specializationNames = specializations;
            }

            return specializationNames;
        }

        /**
         * TODO: CREATE SERVICE
         * 
         * 
         */
        public void changeOfficialReference() {

            try {
                ChangeDegreeOfficialPublicationReference.run(getDegreeOfficialPublication(), this.getNewOfficialReference());
            } catch (FenixServiceException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
            }
        }

        public boolean arePairsDifferent() {
            List<Pair> pairs = this.specializationNames;

            for (Pair pair : pairs) {
                if (pair.isModified()) {
                    return true;
                }
            }
            return false;
        }

        public String saveOfficialPublicationContent() throws FenixServiceException {

            String newOfficialReferece = this.getNewOfficialReference();

            String engName = getSpecializationNameEng();
            String portName = getSpecializationNamePt();
            DegreeOfficialPublication degreeOfficialPublication = getDegreeOfficialPublication();

            // Modify officialpublication official reference if its name is
            // changed
            if (newOfficialReferece != null && newOfficialReferece.compareTo("") != 0
                    && newOfficialReferece.compareTo(getDegreeOfficialPublication().getOfficialReference()) != 0) {
                changeOfficialReference();
            }

            // if (arePairsDifferent()) {
            // List<Pair> pairs = this.specializationNames;
            // for(Pair pair:pairs){
            // pair.updateAreaPt();
            // pair.updateAreaEn();
            // }
            // }
            //

            // Add new SpecializationArea
            if (engName != null && engName.compareTo("") != 0 && portName != null && portName.compareTo("") != 0) {
                addSpecializationArea();
                this.specializationNames.clear();
            }
            return "editDegree";
        }

        public void removeOfficialPublication() {
            logger.info(this.degreeOfficialPublication.toString());
        }

    }

}
