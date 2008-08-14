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

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DegreeManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle scouncilBundle = getResourceBundle("resources/ScientificCouncilResources");

    private final ResourceBundle enumerationBundle = getResourceBundle("resources/EnumerationResources");

    private final ResourceBundle domainExceptionBundle = getResourceBundle("resources/DomainExceptionResources");

    private final String NO_SELECTION = "noSelection";

    private Integer degreeId;

    private Degree degree;

    private String name;

    private String nameEn;

    private String acronym;

    private String bolonhaDegreeType;

    private String gradeScale;

    private Double ectsCredits;

    private String prevailingScientificArea;

    private Integer selectedExecutionYearID;

    private HtmlInputText nameInputComponent;

    private HtmlInputText nameEnInputComponent;

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

    public Integer getDegreeId() {
	return (degreeId == null) ? (degreeId = getAndHoldIntegerParameter("degreeId")) : degreeId;
    }

    public void setDegreeId(Integer degreeId) {
	this.degreeId = degreeId;
    }

    public Degree getDegree() {
	return (degree == null) ? (degree = rootDomainObject.readDegreeByOID(getDegreeId())) : degree;
    }

    public String getName() {
	return (name == null && getDegree() != null) ? (name = getDegree().getDegreeInfoFor(getSelectedExecutionYear()).getName()
		.getContent(Language.pt)) : name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getNameEn() {
	return (nameEn == null && getDegree() != null) ? (nameEn = getDegree().getDegreeInfoFor(getSelectedExecutionYear())
		.getName().getContent(Language.en)) : nameEn;
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
	return (bolonhaDegreeType == null && getDegree() != null) ? (bolonhaDegreeType = getDegree().getDegreeType().getName())
		: bolonhaDegreeType;
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

	if (getDegree() != null && !getDegree().getDegreeType().isBolonhaType()) {
	    ectsCredits = getDegree().getEctsCredits();
	} else if (getBolonhaDegreeType() != null && !getBolonhaDegreeType().equals(NO_SELECTION)) {
	    ectsCredits = DegreeType.valueOf(getBolonhaDegreeType()).getDefaultEctsCredits();
	}

	return ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
	this.ectsCredits = ectsCredits;
    }

    public String getPrevailingScientificArea() {
	return (prevailingScientificArea == null && getDegree() != null) ? (prevailingScientificArea = getDegree()
		.getPrevailingScientificArea()) : prevailingScientificArea;
    }

    public void setPrevailingScientificArea(String prevailingScientificArea) {
	this.prevailingScientificArea = prevailingScientificArea;
    }

    public List<SelectItem> getBolonhaDegreeTypes() {

	List<SelectItem> result = new ArrayList<SelectItem>();
	result.add(new SelectItem(this.NO_SELECTION, scouncilBundle.getString("choose")));

	for (DegreeType degreeType : DegreeType.values()) {
	    if (degreeType.isBolonhaType()) {
		result.add(new SelectItem(degreeType.name(), enumerationBundle.getString(degreeType.getName()) + " ("
			+ degreeType.getYears() + " ano(s))"));
	    }
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
	if (this.bolonhaDegreeType.equals(this.NO_SELECTION)) {// ||
	    // this.gradeScale.equals(this.NO_SELECTION))
	    // {
	    this.setErrorMessage(scouncilBundle.getString("choose.request"));
	    return "";
	}

	if (this.name == null || this.name.length() == 0 || this.nameEn == null || this.nameEn.length() == 0
		|| this.acronym == null || this.acronym.length() == 0) {
	    this.addErrorMessage(scouncilBundle.getString("please.fill.mandatory.fields"));
	    return "";
	}

	Object[] args = { this.name, this.nameEn, this.acronym, DegreeType.valueOf(this.bolonhaDegreeType),
		this.getEctsCredits(), null, this.prevailingScientificArea };
	return changeDegree("CreateDegree", args, "degree.created", "error.creatingDegree");
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

	Object[] args = { this.getDegreeId(), name, nameEn, this.acronym, DegreeType.valueOf(getBolonhaDegreeType()),
		this.getEctsCredits(), null, this.prevailingScientificArea, getSelectedExecutionYear() };
	return changeDegree("EditDegree", args, "degree.edited", "error.editingDegree");
    }

    private String changeDegree(String serviceName, Object[] args, String successfulMsg, String errorMsg) {
	try {
	    ServiceUtils.executeService(serviceName, args);
	} catch (FenixFilterException e) {
	    this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
	    return "curricularPlansManagement";
	} catch (FenixServiceException e) {
	    this.addErrorMessage(scouncilBundle.getString(e.getMessage()));
	    return "";
	} catch (DomainException e) {
	    this.addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
	    return "";
	} catch (Exception e) {
	    this.addErrorMessage(scouncilBundle.getString(errorMsg));
	    return "curricularPlansManagement";
	}

	this.addInfoMessage(scouncilBundle.getString(successfulMsg));
	return "curricularPlansManagement";
    }

    public String deleteDegree() {
	Object[] args = { this.getDegreeId() };
	return changeDegree("DeleteDegree", args, "degree.deleted", "error.deletingDegree");
    }

    public void setSelectedExecutionYearId(Integer executionYearId) {
	this.selectedExecutionYearID = executionYearId;
    }

    public void onChangeExecutionYear(ValueChangeEvent valueChangeEvent) {
	Integer executionYearId = (Integer) valueChangeEvent.getNewValue();
	if (getSelectedExecutionYearId() != executionYearId && getDegree() != null) {
	    setSelectedExecutionYearId(executionYearId);
	    ExecutionYear executionYear = getSelectedExecutionYear();
	    DegreeInfo degreeInfo = getDegree().getMostRecentDegreeInfo(executionYear);
	    if (degreeInfo == null) {
		addErrorMessage(getFormatedMessage(scouncilBundle, "error.Degree.doesnot.have.degreeInfo.for.year", executionYear
			.getName()));
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

    public Integer getSelectedExecutionYearId() {
	if (selectedExecutionYearID == null) {
	    selectedExecutionYearID = getAndHoldIntegerParameter("selectedExecutionYearId");
	    if (selectedExecutionYearID == null) {
		selectedExecutionYearID = ExecutionYear.readCurrentExecutionYear().getIdInternal();
	    }
	}
	return selectedExecutionYearID;
    }

    public ExecutionYear getSelectedExecutionYear() {
	return rootDomainObject.readExecutionYearByOID(getSelectedExecutionYearId());
    }

    public List<SelectItem> getOpenExecutionYears() {
	List<SelectItem> selectItems = new ArrayList<SelectItem>();
	for (ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
	    selectItems.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear()));
	}
	return selectItems;
    }

    public HtmlInputText getNameInputComponent() {
	if (this.nameInputComponent == null) {
	    this.nameInputComponent = new HtmlInputText();
	    ExecutionYear executionYear = (getSelectedExecutionYear() != null) ? getSelectedExecutionYear() : ExecutionYear
		    .readCurrentExecutionYear();

	    final DegreeInfo degreeInfo = getDegreeInfo(executionYear);
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
	    ExecutionYear executionYear = (getSelectedExecutionYear() != null) ? getSelectedExecutionYear() : ExecutionYear
		    .readCurrentExecutionYear();
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
	    // getIdInternal());
	}
	return degreeInfo;
    }

    public boolean isAbleToEditName() {
	if (getSelectedExecutionYear().isAfter(ExecutionYear.readCurrentExecutionYear())) {
	    return true;
	} else if (getSelectedExecutionYear().isCurrent()) {
	    return new YearMonthDay().isBefore(getSelectedExecutionYear().getFirstExecutionPeriod().getBeginDateYearMonthDay());
	} else {
	    return false;
	}
    }
}
