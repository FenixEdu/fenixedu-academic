/*
 * Created on Mar 7, 2004
 *  
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author Luis Cruz
 *  
 */
public class Proposal extends DomainObject implements IProposal {

    private Integer proposalNumber;

    private Integer keyExecutionDegree;

    private IExecutionDegree executionDegree;

    private String title;

    private Integer keyOrientator;

    private ITeacher orientator;

    private Integer keyCoorientator;

    private ITeacher coorientator;

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

    private TipoCurso degreeType;

    private String observations;

    private String companyName;

    private String companyAdress;

    private List branches;

    private FinalDegreeWorkProposalStatus status;

    private List groupProposals;

    private Integer keyGroupAttributedByTeacher;

    private IGroup groupAttributedByTeacher;

    private Integer keyGroupAttributed;

    private IGroup groupAttributed;

    /* Construtores */
    public Proposal() {
        super();
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IProposal) {
            IProposal proposal = (IProposal) obj;

            result = getIdInternal().equals(proposal.getIdInternal());
        }
        return result;
    }

    public String toString() {
        String result = "[Proposal";
        result += ", idInternal=" + getIdInternal();
        result += ", title=" + getTitle();
        result += ", degreeCurricularPlan=" + getExecutionDegree();
        result += "]";
        return result;
    }

    /**
     * @return Returns the companyName.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName
     *            The companyName to set.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return Returns the coorientator.
     */
    public ITeacher getCoorientator() {
        return coorientator;
    }

    /**
     * @param coorientator
     *            The coorientator to set.
     */
    public void setCoorientator(ITeacher coorientator) {
        this.coorientator = coorientator;
    }

    /**
     * @return Returns the coorientatorsCreditsPercentage.
     */
    public Integer getCoorientatorsCreditsPercentage() {
        return coorientatorsCreditsPercentage;
    }

    /**
     * @param coorientatorsCreditsPercentage
     *            The coorientatorsCreditsPercentage to set.
     */
    public void setCoorientatorsCreditsPercentage(Integer coorientatorsCreditsPercentage) {
        this.coorientatorsCreditsPercentage = coorientatorsCreditsPercentage;
    }

    /**
     * @return Returns the degreeCurricularPlan.
     */
    public IExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    /**
     * @param degreeCurricularPlan
     *            The degreeCurricularPlan to set.
     */
    public void setExecutionDegree(IExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    /**
     * @return Returns the degreeType.
     */
    public TipoCurso getDegreeType() {
        return degreeType;
    }

    /**
     * @param degreeType
     *            The degreeType to set.
     */
    public void setDegreeType(TipoCurso degreeType) {
        this.degreeType = degreeType;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the keyCoorientator.
     */
    public Integer getKeyCoorientator() {
        return keyCoorientator;
    }

    /**
     * @param keyCoorientator
     *            The keyCoorientator to set.
     */
    public void setKeyCoorientator(Integer keyCoorientator) {
        this.keyCoorientator = keyCoorientator;
    }

    /**
     * @return Returns the keyDegreeCurricularPlan.
     */
    public Integer getKeyExecutionDegree() {
        return keyExecutionDegree;
    }

    /**
     * @param keyDegreeCurricularPlan
     *            The keyDegreeCurricularPlan to set.
     */
    public void setKeyExecutionDegree(Integer keyExecutionDegree) {
        this.keyExecutionDegree = keyExecutionDegree;
    }

    /**
     * @return Returns the keyOrientator.
     */
    public Integer getKeyOrientator() {
        return keyOrientator;
    }

    /**
     * @param keyOrientator
     *            The keyOrientator to set.
     */
    public void setKeyOrientator(Integer keyOrientator) {
        this.keyOrientator = keyOrientator;
    }

    /**
     * @return Returns the objectives.
     */
    public String getObjectives() {
        return objectives;
    }

    /**
     * @param objectives
     *            The objectives to set.
     */
    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    /**
     * @return Returns the observations.
     */
    public String getObservations() {
        return observations;
    }

    /**
     * @param observations
     *            The observations to set.
     */
    public void setObservations(String observations) {
        this.observations = observations;
    }

    /**
     * @return Returns the orientator.
     */
    public ITeacher getOrientator() {
        return orientator;
    }

    /**
     * @param orientator
     *            The orientator to set.
     */
    public void setOrientator(ITeacher orientator) {
        this.orientator = orientator;
    }

    /**
     * @return Returns the orientatorsCreditsPercentage.
     */
    public Integer getOrientatorsCreditsPercentage() {
        return orientatorsCreditsPercentage;
    }

    /**
     * @param orientatorsCreditsPercentage
     *            The orientatorsCreditsPercentage to set.
     */
    public void setOrientatorsCreditsPercentage(Integer orientatorsCreditsPercentage) {
        this.orientatorsCreditsPercentage = orientatorsCreditsPercentage;
    }

    /**
     * @return Returns the requirements.
     */
    public String getRequirements() {
        return requirements;
    }

    /**
     * @param requirements
     *            The requirements to set.
     */
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return Returns the deliverable.
     */
    public String getDeliverable() {
        return deliverable;
    }

    /**
     * @param deliverable
     *            The deliverable to set.
     */
    public void setDeliverable(String deliverable) {
        this.deliverable = deliverable;
    }

    /**
     * @return Returns the framing.
     */
    public String getFraming() {
        return framing;
    }

    /**
     * @param framing
     *            The framing to set.
     */
    public void setFraming(String framing) {
        this.framing = framing;
    }

    /**
     * @return Returns the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     *            The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return Returns the maximumNumberOfGroupElements.
     */
    public Integer getMaximumNumberOfGroupElements() {
        return maximumNumberOfGroupElements;
    }

    /**
     * @param maximumNumberOfGroupElements
     *            The maximumNumberOfGroupElements to set.
     */
    public void setMaximumNumberOfGroupElements(Integer maximumNumberOfGroupElements) {
        this.maximumNumberOfGroupElements = maximumNumberOfGroupElements;
    }

    /**
     * @return Returns the minimumNumberOfGroupElements.
     */
    public Integer getMinimumNumberOfGroupElements() {
        return minimumNumberOfGroupElements;
    }

    /**
     * @param minimumNumberOfGroupElements
     *            The minimumNumberOfGroupElements to set.
     */
    public void setMinimumNumberOfGroupElements(Integer minimumNumberOfGroupElements) {
        this.minimumNumberOfGroupElements = minimumNumberOfGroupElements;
    }

    /**
     * @return Returns the companionMail.
     */
    public String getCompanionMail() {
        return companionMail;
    }

    /**
     * @param companionMail
     *            The companionMail to set.
     */
    public void setCompanionMail(String companionMail) {
        this.companionMail = companionMail;
    }

    /**
     * @return Returns the companionName.
     */
    public String getCompanionName() {
        return companionName;
    }

    /**
     * @param companionName
     *            The companionName to set.
     */
    public void setCompanionName(String companionName) {
        this.companionName = companionName;
    }

    /**
     * @return Returns the companionPhone.
     */
    public Integer getCompanionPhone() {
        return companionPhone;
    }

    /**
     * @param companionPhone
     *            The companionPhone to set.
     */
    public void setCompanionPhone(Integer companionPhone) {
        this.companionPhone = companionPhone;
    }

    /**
     * @return Returns the companyAdress.
     */
    public String getCompanyAdress() {
        return companyAdress;
    }

    /**
     * @param companyAdress
     *            The companyAdress to set.
     */
    public void setCompanyAdress(String companyAdress) {
        this.companyAdress = companyAdress;
    }

    /**
     * @return Returns the branches.
     */
    public List getBranches() {
        return branches;
    }

    /**
     * @param branches
     *            The branches to set.
     */
    public void setBranches(List branches) {
        this.branches = branches;
    }

    /**
     * @return Returns the proposalNumber.
     */
    public Integer getProposalNumber() {
        return proposalNumber;
    }

    /**
     * @param proposalNumber
     *            The proposalNumber to set.
     */
    public void setProposalNumber(Integer proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    /**
     * @return Returns the status.
     */
    public FinalDegreeWorkProposalStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *            The status to set.
     */
    public void setStatus(FinalDegreeWorkProposalStatus status) {
        this.status = status;
    }

    /**
     * @return Returns the groupProposals.
     */
    public List getGroupProposals() {
        return groupProposals;
    }

    /**
     * @param groupProposals
     *            The groupProposals to set.
     */
    public void setGroupProposals(List groupProposals) {
        this.groupProposals = groupProposals;
    }

    /**
     * @return Returns the groupAttributedByTeacher.
     */
    public IGroup getGroupAttributedByTeacher() {
        return groupAttributedByTeacher;
    }

    /**
     * @param groupAttributedByTeacher
     *            The groupAttributedByTeacher to set.
     */
    public void setGroupAttributedByTeacher(IGroup groupAttributedByTeacher) {
        this.groupAttributedByTeacher = groupAttributedByTeacher;
    }

    /**
     * @return Returns the keyGroupAttributedByTeacher.
     */
    public Integer getKeyGroupAttributedByTeacher() {
        return keyGroupAttributedByTeacher;
    }

    /**
     * @param keyGroupAttributedByTeacher
     *            The keyGroupAttributedByTeacher to set.
     */
    public void setKeyGroupAttributedByTeacher(Integer keyGroupAttributedByTeacher) {
        this.keyGroupAttributedByTeacher = keyGroupAttributedByTeacher;
    }

    /**
     * @return Returns the groupAttributed.
     */
    public IGroup getGroupAttributed() {
        return groupAttributed;
    }

    /**
     * @param groupAttributed
     *            The groupAttributed to set.
     */
    public void setGroupAttributed(IGroup groupAttributed) {
        this.groupAttributed = groupAttributed;
    }

    /**
     * @return Returns the keyGroupAttributed.
     */
    public Integer getKeyGroupAttributed() {
        return keyGroupAttributed;
    }

    /**
     * @param keyGroupAttributed
     *            The keyGroupAttributed to set.
     */
    public void setKeyGroupAttributed(Integer keyGroupAttributed) {
        this.keyGroupAttributed = keyGroupAttributed;
    }
}