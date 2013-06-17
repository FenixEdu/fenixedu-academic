/*
 * Created on Mar 7, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.dissertation;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.dissertation.DissertationState;

public class InfoDissertationEditor extends InfoObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;

    private InfoPerson orientator;

    private InfoPerson coorientator;

    private Integer orientatorCreditsPercentage;

    private Integer coorientatorCreditsPercentage;

    private String framing;

    private String objectives;

    private String description;

    private String requirements;

    private String deliverable;

    private String url;

    private String observations;

    private DissertationState dissertationState;

    private String proposalNumber;

    /* Construtores */
    public InfoDissertationEditor() {
        super();
    }

    @Override
    public String toString() {
        String result = "[Dissertation";
        result += ", title=" + getTitle();
        result += "]";
        return result;
    }
    
    public InfoPerson getCoorientator() {
        return coorientator;
    }

    public void setCoorientator(InfoPerson coorientator) {
        this.coorientator = coorientator;
    }

    public Integer getCoorientatorCreditsPercentage() {
        return coorientatorCreditsPercentage;
    }

    public void setCoorientatorCreditsPercentage(Integer coorientatorCreditsPercentage) {
        this.coorientatorCreditsPercentage = coorientatorCreditsPercentage;
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

    public Integer getOrientatorCreditsPercentage() {
        return orientatorCreditsPercentage;
    }

    public void setOrientatorCreditsPercentage(Integer orientatorCreditsPercentage) {
        this.orientatorCreditsPercentage = orientatorCreditsPercentage;
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

    public DissertationState getDissertationState() {
        return dissertationState;
    }

    public void setDissertationState(DissertationState dissertationState) {
        this.dissertationState = dissertationState;
    }

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

}
