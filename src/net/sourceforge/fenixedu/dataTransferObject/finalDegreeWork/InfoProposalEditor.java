/*
 * Created on Mar 7, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

/**
 * @author Luis Cruz
 *  
 */
public class InfoProposalEditor extends InfoObject {

    private InfoExecutionDegree executionDegree;

    private String title;

    private InfoPerson orientator;

    private InfoPerson coorientator;

    private String companionName;

    private String companionMail;

    private Integer companionPhone;

    private Integer orientatorsCreditsPercentage;

    private Integer coorientatorsCreditsPercentage;

    private String framing;

    private String objectives;

    private String description;

    private String requirements;

    private String deliverable;

    private String url;

    private Integer minimumNumberOfGroupElements;

    private Integer maximumNumberOfGroupElements;

    private String location;

    private DegreeType degreeType;

    private String observations;

    private String companyName;

    private String companyAdress;

    private List branches;

    private FinalDegreeWorkProposalStatus status;

    /* Construtores */
    public InfoProposalEditor() {
        super();
    }

    public String toString() {
        String result = "[Proposal";
        result += ", idInternal=" + getIdInternal();
        result += ", title=" + getTitle();
        result += ", InfoDegreeCurricularPlan=" + getExecutionDegree();
        result += "]";
        return result;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public InfoPerson getCoorientator() {
        return coorientator;
    }

    public void setCoorientator(InfoPerson coorientator) {
        this.coorientator = coorientator;
    }

    public Integer getCoorientatorsCreditsPercentage() {
        return coorientatorsCreditsPercentage;
    }

    public void setCoorientatorsCreditsPercentage(Integer coorientatorsCreditsPercentage) {
        this.coorientatorsCreditsPercentage = coorientatorsCreditsPercentage;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public InfoPerson getOrientator() {
        return orientator;
    }

    public void setOrientator(InfoPerson orientator) {
        this.orientator = orientator;
    }

    public Integer getOrientatorsCreditsPercentage() {
        return orientatorsCreditsPercentage;
    }

    public void setOrientatorsCreditsPercentage(Integer orientatorsCreditsPercentage) {
        this.orientatorsCreditsPercentage = orientatorsCreditsPercentage;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InfoExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(InfoExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public String getFraming() {
        return framing;
    }

    public void setFraming(String framing) {
        this.framing = framing;
    }

    public String getDeliverable() {
        return deliverable;
    }

    public void setDeliverable(String deliverable) {
        this.deliverable = deliverable;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMaximumNumberOfGroupElements() {
        return maximumNumberOfGroupElements;
    }

    public void setMaximumNumberOfGroupElements(Integer maximumNumberOfGroupElements) {
        this.maximumNumberOfGroupElements = maximumNumberOfGroupElements;
    }

    public Integer getMinimumNumberOfGroupElements() {
        return minimumNumberOfGroupElements;
    }

    public void setMinimumNumberOfGroupElements(Integer minimumNumberOfGroupElements) {
        this.minimumNumberOfGroupElements = minimumNumberOfGroupElements;
    }

    public String getCompanionMail() {
        return companionMail;
    }

    public void setCompanionMail(String companionMail) {
        this.companionMail = companionMail;
    }

    public String getCompanionName() {
        return companionName;
    }

    public void setCompanionName(String companionName) {
        this.companionName = companionName;
    }

    public Integer getCompanionPhone() {
        return companionPhone;
    }

    public void setCompanionPhone(Integer companionPhone) {
        this.companionPhone = companionPhone;
    }

    public String getCompanyAdress() {
        return companyAdress;
    }

    public void setCompanyAdress(String companyAdress) {
        this.companyAdress = companyAdress;
    }

    public List getBranches() {
        return branches;
    }

    public void setBranches(List branches) {
        this.branches = branches;
    }

    public FinalDegreeWorkProposalStatus getStatus() {
        return status;
    }

    public void setStatus(FinalDegreeWorkProposalStatus status) {
        this.status = status;
    }

}
