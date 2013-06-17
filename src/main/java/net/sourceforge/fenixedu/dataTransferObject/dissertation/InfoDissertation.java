/*
 * Created on Mar 7, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.dissertation;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.dissertation.Dissertation;
import net.sourceforge.fenixedu.domain.dissertation.DissertationState;

public class InfoDissertation extends InfoObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dissertation dissertationDomainReference;

    public InfoDissertation(final Dissertation dissertation) {
        dissertationDomainReference = dissertation;
    }

    public static InfoDissertation newInfoFromDomain(final Dissertation dissertation) {
        return dissertation == null ? null : new InfoDissertation(dissertation);
    }

    private Dissertation getDissertation() {
        return dissertationDomainReference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoDissertation && getDissertation() == ((InfoDissertation) obj).getDissertation();
    }

    @Override
    public int hashCode() {
        return getDissertation().hashCode();
    }

    @Override
    public String toString() {
        return getDissertation().toString();
    }

    /**
     * @return Returns the coorientatorsCreditsPercentage.
     */
    public Integer getCoorientatorsCreditsPercentage() {
        return getDissertation().getCoorientatorCreditsDistribution();
    }

    /**
     * @return Returns the description.
     */
    public MultiLanguageString getDescription() {
        return getDissertation().getDescription();
    }

    /**
     * @return Returns the objectives.
     */
    public MultiLanguageString getObjectives() {
        return getDissertation().getObjectives();
    }

    /**
     * @return Returns the orientatorsCreditsPercentage.
     */
    public Integer getOrientatorsCreditsPercentage() {
        return getDissertation().getOrientatorCreditsDistribution();
    }

    /**
     * @return Returns the requirements.
     */
    public MultiLanguageString getRequirements() {
        return getDissertation().getRequirements();
    }

    /**
     * @return Returns the title.
     */
    public MultiLanguageString getTitle() {
        return getDissertation().getTitle();
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return getDissertation().getUrl();
    }

    /**
     * @return Returns the framing.
     */
    public MultiLanguageString getFraming() {
        return getDissertation().getFraming();
    }

    /**
     * @return Returns the deliverable.
     */
    public MultiLanguageString getDeliverable() {
        return getDissertation().getDeliverable();
    }

    /**
     * @return Returns the proposalNumber.
     */
    public String getProposalNumber() {
        return getDissertation().getProposalNumber();
    }

    /**
     * @return Returns the status.
     */
    public DissertationState getDissertationState() {
        return getDissertation().getDissertationState();
    }

    /**
     * @return Returns the orientator.
     */
    public InfoPerson getOrientator() {
        return InfoPerson.newInfoFromDomain(getDissertation().getOrientator());
    }

    /**
     * @return Returns the coorientator.
     */
    public InfoPerson getCoorientator() {
        return InfoPerson.newInfoFromDomain(getDissertation().getCoorientator());
    }

    /**
     * @return Returns the orientatorsDepartment.
     */
    public InfoDepartment getOrientatosDepartment() {
        return getDissertation().getOrientator().getTeacher() == null ? null : InfoDepartment.newInfoFromDomain(getDissertation()
                .getOrientator().getTeacher().getCurrentWorkingDepartment());
    }

    /**
     * @return Returns the coorientatorsDepartment.
     */
    public InfoDepartment getCoorientatorDepartment() {
        return getDissertation().getCoorientator().getTeacher() == null ? null : InfoDepartment.newInfoFromDomain(getDissertation()
                .getCoorientator().getTeacher().getCurrentWorkingDepartment());
    }

}
