/*
 * Created on 2004/03/09
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.dissertation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.dissertation.Dissertation;
import net.sourceforge.fenixedu.domain.dissertation.DissertationState;
import net.sourceforge.fenixedu.domain.dissertation.Scheduling;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Luis Cruz
 * 
 */
@SuppressWarnings("serial")
public class DissertationHeader extends InfoObject {

    private final Dissertation dissertationDomainReference;

    private final List<ExecutionDegree> executionDegreesDomainReference;

    public DissertationHeader(final Dissertation dissertation) {
        dissertationDomainReference = dissertation;
        executionDegreesDomainReference = dissertation.getExecutionDegrees();
    }

    public DissertationHeader(final Dissertation dissertation, final List<ExecutionDegree> executionDegrees) {
        dissertationDomainReference = dissertation;
        executionDegreesDomainReference = executionDegrees;
    }

    public static DissertationHeader newInfoFromDomain(final Dissertation dissertation) {
        return dissertation == null ? null : new DissertationHeader(dissertation);
    }

    public static DissertationHeader newInfoFromDomain(final Dissertation dissertation, final List<ExecutionDegree> executionDegrees) {
        return dissertation == null ? null : new DissertationHeader(dissertation, executionDegrees);
    }

    private Dissertation getDissertation() {
        return dissertationDomainReference;
    }

    private List<ExecutionDegree> getExecutionDegrees() {
        return executionDegreesDomainReference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DissertationHeader
                && getDissertation() == ((DissertationHeader) obj).getDissertation()
                && getExecutionDegrees() == ((DissertationHeader) obj).getExecutionDegrees();
    }

    @Override
    public int hashCode() {
        return getDissertation().hashCode();
    }

    @Override
    public Integer getIdInternal() {
        return getDissertation().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

    /**
     * @return Returns the OID.
     */

    public Long getDissertationOID() {
        return getDissertation().getOID();
    }

    /**
     * @return Returns the status.
     */
    public DissertationState getDissertationState() {
        return getDissertation().getDissertationState();
    }

    /**
     * @return Returns the coorientatorName.
     */
    public String getCoorientatorName() {
        return getDissertation().hasCoorientator() ? getDissertation().getCoorientator().getName() : StringUtils.EMPTY;
    }

    /**
     * @return Returns the coorientatorOID.
     */
    public Integer getCoorientatorOID() {
        return getDissertation().getCoorientator().getIdInternal();
    }

    /**
     * @return Returns the orientatorName.
     */
    public String getOrientatorName() {
        return getDissertation().getOrientator().getName();
    }

    /**
     * @return Returns the orientatorOID.
     */
    public Integer getOrientatorOID() {
        return getDissertation().getOrientator().getIdInternal();
    }

    /**
     * @return Returns the title.
     */
    public MultiLanguageString getTitle() {
        return getDissertation().getTitle();
    }

    /**
     * @return Returns the proposalNumber.
     */
    public String getProposalNumber() {
        return getDissertation().getProposalNumber();
    }

    public Boolean getEditable(Scheduling scheduling) {

        return scheduling != null && scheduling.getStartOfProposalPeriod() != null
                && scheduling.getEndOfProposalPeriod() != null
                && scheduling.getStartOfProposalPeriod().before(Calendar.getInstance().getTime())
                && scheduling.getEndOfProposalPeriod().after(Calendar.getInstance().getTime());
    }

}
