package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.component.html.HtmlInputText;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.ChangeDegreeOfficialPublicationReference;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.CreateDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.CreateDegreeOfficialPublication;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.CreateDegreeSpecializationArea;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.DeleteDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.DeleteDegreeSpecializationArea;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.EditDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.domain.DegreeSpecializationArea;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DegreeManagementBackingBean extends FenixBackingBean {

    private static final String SC_PACKAGE = "net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans";

    private final ResourceBundle scouncilBundle = getResourceBundle("resources/ScientificCouncilResources");

    private final ResourceBundle enumerationBundle = getResourceBundle("resources/EnumerationResources");

    private final ResourceBundle domainExceptionBundle = getResourceBundle("resources/DomainExceptionResources");

    private final String NO_SELECTION = "noSelection";

    private String degreeId;

    private Degree degree;

    private String name;

    private String nameEn;

    private String acronym;

    private String bolonhaDegreeType;

    private String gradeScale;

    private Double ectsCredits;

    private String prevailingScientificArea;

    private String selectedExecutionYearID;

    private String academicAdminOfficeId;

    private HtmlInputText nameInputComponent;

    private HtmlInputText nameEnInputComponent;

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

    public List<Degree> getBolonhaDegrees() {
        final List<Degree> result = Degree.readBolonhaDegrees();
        Collections.sort(result, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        return result;
    }

    public List<Degree> getFilteredBolonhaDegrees() {
        final Set<Degree> result = new HashSet<Degree>();
        for (final Degree degree : Degree.readBolonhaDegrees()) {
            for (final DegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
                if (dcp.getCurricularStage().equals(CurricularStage.PUBLISHED)
                        || dcp.getCurricularStage().equals(CurricularStage.APPROVED)
                        || dcp.getCurricularPlanMembersGroup().isMember(this.getUserView().getPerson())) {
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

    public String getName() {
        return (name == null && getDegree() != null) ? (name =
                getDegree().getNameFor(getSelectedExecutionYear()).getContent(Language.pt)) : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return (nameEn == null && getDegree() != null) ? (nameEn =
                getDegree().getNameFor(getSelectedExecutionYear()).getContent(Language.en)) : nameEn;
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
        return (bolonhaDegreeType == null && getDegree() != null) ? (bolonhaDegreeType = getDegree().getDegreeType().getName()) : bolonhaDegreeType;
    }

    public void setBolonhaDegreeType(String bolonhaDegreeType) {
        this.bolonhaDegreeType = bolonhaDegreeType;
    }

    public String getGradeScale() {
        return (gradeScale == null && getDegree() != null) ? (gradeScale = getDegree().getGradeScale().getName()) : gradeScale;
    }

    public void setGradeScale(String gradeScale) {
        this.gradeScale = gradeScale;
    }

    public Double getEctsCredits() {
        if (ectsCredits == null) {
            if (getDegree() != null) {
                ectsCredits = getDegree().getEctsCredits();
            } else if (getBolonhaDegreeType() != null && !getBolonhaDegreeType().equals(NO_SELECTION)) {
                ectsCredits = DegreeType.valueOf(getBolonhaDegreeType()).getDefaultEctsCredits();
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

    public List<SelectItem> getBolonhaDegreeTypes() {

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(this.NO_SELECTION, scouncilBundle.getString("choose")));

        for (DegreeType degreeType : DegreeType.NOT_EMPTY_BOLONHA_VALUES) {
            result.add(new SelectItem(degreeType.name(), enumerationBundle.getString(degreeType.getName()) + " ("
                    + degreeType.getYears() + " ano(s))"));
        }

        return result;
    }

    public List<SelectItem> getAcademicAdminOffices() {

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(this.NO_SELECTION, scouncilBundle.getString("choose")));

        for (AdministrativeOffice administrativeOffice : rootDomainObject.getAdministrativeOfficesSet()) {
            result.add(new SelectItem(administrativeOffice.getExternalId(), administrativeOffice.getUnit().getName()));
        }

        return result;
    }

    public List<SelectItem> getGradeScales() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        result.add(new SelectItem(this.NO_SELECTION, scouncilBundle.getString("choose")));
        result.add(new SelectItem(GradeScale.TYPE20.name(), enumerationBundle.getString(GradeScale.TYPE20.name())));
        result.add(new SelectItem(GradeScale.TYPE5.name(), enumerationBundle.getString(GradeScale.TYPE5.name())));

        return result;
    }

    public String createDegree() {
        if (this.bolonhaDegreeType.equals(this.NO_SELECTION)) {
            this.setErrorMessage(scouncilBundle.getString("choose.degreeType"));
            return "";
        }
        if (getAcademicAdminOfficeId().equals(this.NO_SELECTION)) {
            this.setErrorMessage(scouncilBundle.getString("choose.administrativeOffice"));
            return "";
        }

        if (this.name == null || this.name.length() == 0 || this.nameEn == null || this.nameEn.length() == 0
                || this.acronym == null || this.acronym.length() == 0) {
            this.addErrorMessage(scouncilBundle.getString("please.fill.mandatory.fields"));
            return "";
        }

        try {
            AdministrativeOffice administrativeOffice = FenixFramework.getDomainObject(getAcademicAdminOfficeId());
            CreateDegree.run(this.name, this.nameEn, this.acronym, DegreeType.valueOf(this.bolonhaDegreeType),
                    this.getEctsCredits(), null, this.prevailingScientificArea, administrativeOffice);
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (DomainException e) {
            this.addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(scouncilBundle.getString("error.creatingDegree"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(scouncilBundle.getString("degree.created"));
        return "curricularPlansManagement";
    }

    public String editDegree() {
        if (this.bolonhaDegreeType != null && this.bolonhaDegreeType.equals(this.NO_SELECTION)) {// ||
            // this.gradeScale.equals(this.NO_SELECTION))
            // {
            this.setErrorMessage(scouncilBundle.getString("choose.request"));
            return "";
        }

        String name = (String) getNameInputComponent().getValue();
        String nameEn = (String) getNameEnInputComponent().getValue();

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(nameEn) || StringUtils.isEmpty(this.acronym)) {
            this.addErrorMessage(scouncilBundle.getString("please.fill.mandatory.fields"));
            return "";
        }

        try {
            EditDegree.run(this.getDegreeId(), name, nameEn, this.acronym, DegreeType.valueOf(getBolonhaDegreeType()),
                    this.getEctsCredits(), null, this.prevailingScientificArea, getSelectedExecutionYear());
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (DomainException e) {
            this.addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(scouncilBundle.getString("error.editingDegree"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(scouncilBundle.getString("degree.edited"));
        return "curricularPlansManagement";
    }

    public String deleteDegree() {
        try {
            DeleteDegree.run(this.getDegreeId());
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (DomainException e) {
            this.addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(scouncilBundle.getString("error.deletingDegree"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(scouncilBundle.getString("degree.deleted"));
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
                addErrorMessage(getFormatedMessage(scouncilBundle, "error.Degree.doesnot.have.degreeInfo.for.year",
                        executionYear.getName()));
                degreeInfo = getDegree().getMostRecentDegreeInfo();
            }

            if (degreeInfo != null) {
                this.name = degreeInfo.getName().getContent(Language.pt);
                this.nameEn = degreeInfo.getName().getContent(Language.en);
                this.nameInputComponent.setValue(degreeInfo.getName().getContent(Language.pt));
                this.nameEnInputComponent.setValue(degreeInfo.getName().getContent(Language.en));
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

    public HtmlInputText getNameInputComponent() {
        if (this.nameInputComponent == null) {
            this.nameInputComponent = new HtmlInputText();
            ExecutionYear executionYear =
                    (getSelectedExecutionYear() != null) ? getSelectedExecutionYear() : ExecutionYear.readCurrentExecutionYear();

            DegreeInfo degreeInfo = getDegreeInfo(executionYear);
            setSelectedExecutionYearId(degreeInfo.getExecutionYear().getExternalId());
            this.nameInputComponent.setValue(degreeInfo.getName().getContent(Language.pt));
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
            this.nameEnInputComponent.setValue(degreeInfo.getName().getContent(Language.en));
        }

        return this.nameEnInputComponent;
    }

    public void setNameEnInputComponent(HtmlInputText nameEnInputComponent) {
        this.nameEnInputComponent = nameEnInputComponent;
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
        if (!lastActiveDegreeCurricularPlan.hasAnyExecutionDegrees()) {
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

        for (DegreeOfficialPublication degreeOfficialPublication : getDegree().getOfficialPublication()) {
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
        private final String specializationsAreas;

        public OfficialPubBeanPrint(DegreeOfficialPublication degreeOfficialPublication) {
            this.degreeOfficialPublication = degreeOfficialPublication;
            this.date = degreeOfficialPublication.getPublication().toString();
            this.officialReference = degreeOfficialPublication.getOfficialReference();
            this.specializationsAreas = fetchSpecializationAreas(degreeOfficialPublication);
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

        public String fetchSpecializationAreas(DegreeOfficialPublication degreeOfficialPublication) {
            String specializationAreas = "";

            // TODO: Remove
            if (degreeOfficialPublication == null) {
                throw new RuntimeException("que bodega..");
            }

            for (DegreeSpecializationArea specializationArea : degreeOfficialPublication.getSpecializationArea()) {
                specializationAreas +=
                        (specializationAreas.compareTo("") == 0 ? "" : ", ") + specializationArea.getName().toString();
            }

            return specializationAreas;
        }

    }

    public class OfficialPublicationBean extends DegreeManagementBackingBean {

        private String date;
        private String officialReference;
        private String officialPubId;
        private DegreeOfficialPublication degreeOfficialPublication;
        private String newOfficialReference;
        private String specializationNameEng;
        private String specializationNamePt;
        private List<Pair> specializationNames;
        private String specializationIdToDelete;
        private DegreeSpecializationArea specializationAreaToDelete;
        private DegreeOfficialPublication degreeOfficialPublicationGoBack;

        public class Pair extends FenixBackingBean {
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

        public String makeAndInsertDegreeOfficialPublication() {

            // to remove
            if (date == null) {
                this.addErrorMessage(scouncilBundle.getString("error.protocol.invalidDates"));
                return "";
            }
            String[] dateFields = date.split("/");

            if (dateFields.length != 3) {
                this.addErrorMessage(scouncilBundle.getString("error.protocol.invalidDates"));
                return "";
            }

            if (dateFields[0].length() != 2 || dateFields[1].length() != 2 || dateFields[2].length() != 4) {
                this.addErrorMessage(scouncilBundle.getString("error.protocol.invalidDates"));
                return "";
            }

            if (officialReference.isEmpty() || officialReference.compareTo("") == 0) {
                this.addErrorMessage(scouncilBundle.getString("confirm.error.edit.reference.officialPublication"));
                return "";
            }

            LocalDate localDate =
                    new LocalDate(Integer.parseInt(dateFields[2]), Integer.parseInt(dateFields[1]),
                            Integer.parseInt(dateFields[0]));

            Degree degree = getDegree();

            try {
                CreateDegreeOfficialPublication.run(degree, localDate, officialReference);
            } catch (IllegalDataAccessException e) {
                this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
                return "";
            } catch (DomainException e) {
                this.addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
                return "";
            } catch (FenixServiceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            this.addInfoMessage(scouncilBundle.getString("degreeOfficialPublication.created"));
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
                e.printStackTrace();
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
                e.printStackTrace();
            }
            return "editDegreeOfficialPublication";
        }

        // public String removeOfficialPublication() {
        // try {
        // DeleteDegreeOfficialPublication.run(getDegreeOfficialPublication());
        // } catch (FenixServiceException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // return "editDegree";
        // }

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
                for (DegreeSpecializationArea area : getDegreeOfficialPublication().getSpecializationArea()) {
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
                e.printStackTrace();
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
            System.out.println(this.degreeOfficialPublication);
        }

    }

}
