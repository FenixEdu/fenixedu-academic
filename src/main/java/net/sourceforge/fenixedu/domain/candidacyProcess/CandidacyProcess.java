package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

abstract public class CandidacyProcess extends CandidacyProcess_Base {

    protected CandidacyProcess() {
        super();
    }

    public ExecutionInterval getCandidacyExecutionInterval() {
        return hasCandidacyPeriod() ? getCandidacyPeriod().getExecutionInterval() : null;
    }

    public DateTime getCandidacyStart() {
        return hasCandidacyPeriod() ? getCandidacyPeriod().getStart() : null;
    }

    public DateTime getCandidacyEnd() {
        return hasCandidacyPeriod() ? getCandidacyPeriod().getEnd() : null;
    }

    public boolean hasStarted() {
        return !getCandidacyStart().isAfterNow();
    }

    public boolean hasOpenCandidacyPeriod() {
        return hasCandidacyPeriod() && getCandidacyPeriod().isOpen();
    }

    public boolean hasOpenCandidacyPeriod(final DateTime date) {
        return hasCandidacyPeriod() && getCandidacyPeriod().isOpen(date);
    }

    public boolean hasState() {
        return getState() != null;
    }

    public boolean isInStandBy() {
        return getState() == CandidacyProcessState.STAND_BY;
    }

    public boolean isSentToJury() {
        return getState() == CandidacyProcessState.SENT_TO_JURY;
    }

    public boolean isSentToCoordinator() {
        return getState() == CandidacyProcessState.SENT_TO_COORDINATOR;
    }

    public boolean isSentToScientificCouncil() {
        return getState() == CandidacyProcessState.SENT_TO_SCIENTIFIC_COUNCIL;
    }

    public boolean isPublished() {
        return getState() == CandidacyProcessState.PUBLISHED;
    }

    @Override
    public String getDisplayName() {
        return ResourceBundle.getBundle("resources/CaseHandlingResources").getString(getClass().getSimpleName());
    }

    public static <T extends CandidacyProcess> T getCandidacyProcessByDate(Class<T> clazz, final DateTime date) {
        Set<T> candidacyProcessList = DomainObjectUtil.readAllDomainObjects(clazz);

        for (T process : candidacyProcessList) {
            if (process.hasCandidacyPeriod() && process.getCandidacyPeriod().isOpen(date)) {
                return process;
            }
        }

        return null;
    }

    public static <T extends CandidacyProcess> T getCandidacyProcessByExecutionInterval(Class<T> clazz,
            final ExecutionInterval executionInterval) {
        Set<T> candidacyProcessList = DomainObjectUtil.readAllDomainObjects(clazz);

        for (T process : candidacyProcessList) {
            if (process.hasCandidacyPeriod() && executionInterval.equals(process.getCandidacyPeriod().getExecutionInterval())) {
                return process;
            }
        }

        return null;
    }

    public IndividualCandidacyProcess getChildProcessByDocumentId(IDDocumentType type, String identification) {
        for (IndividualCandidacyProcess individualCandidacyProcess : getChildProcesses()) {
            if (individualCandidacyProcess.getCandidacy() != null
                    && identification
                            .equals(individualCandidacyProcess.getCandidacy().getPersonalDetails().getDocumentIdNumber())
                    && type.equals(individualCandidacyProcess.getCandidacy().getPersonalDetails().getIdDocumentType())) {
                return individualCandidacyProcess;
            }
        }

        return null;
    }

    public IndividualCandidacyProcess getOpenChildProcessByDocumentId(IDDocumentType documentType, String documentId) {
        for (IndividualCandidacyProcess child : this.getChildProcesses()) {
            if (documentId.equals(child.getCandidacy().getPersonalDetails().getDocumentIdNumber())
                    && !child.isCandidacyCancelled()) {
                return child;
            }
        }

        return null;
    }

    public IndividualCandidacyProcess getOpenChildProcessByEidentifier(final String eidentifier) {
        if (StringUtils.isEmpty(eidentifier)) {
            return null;
        }

        for (IndividualCandidacyProcess child : this.getChildProcesses()) {
            if (child.isCandidacyCancelled()) {
                continue;
            }

            if (StringUtils.isEmpty(child.getPersonalDetails().getEidentifier())) {
                continue;
            }

            if (eidentifier.equalsIgnoreCase(child.getPersonalDetails().getEidentifier())) {
                return child;
            }
        }

        return null;
    }

    public List<IndividualCandidacyProcess> getChildsWithMissingRequiredDocuments() {
        List<IndividualCandidacyProcess> childs = new ArrayList<IndividualCandidacyProcess>();

        CollectionUtils.select(getChildProcesses(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                IndividualCandidacyProcess process = (IndividualCandidacyProcess) arg0;
                return !process.isCandidacyCancelled() && process.isProcessMissingRequiredDocumentFiles();
            }

        }, childs);

        return childs;
    }

    public boolean isMobility() {
        return false;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Degree> getDegree() {
        return getDegreeSet();
    }

    @Deprecated
    public boolean hasAnyDegree() {
        return !getDegreeSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess> getChildProcesses() {
        return getChildProcessesSet();
    }

    @Deprecated
    public boolean hasAnyChildProcesses() {
        return !getChildProcessesSet().isEmpty();
    }

    @Deprecated
    public boolean hasCandidacyPeriod() {
        return getCandidacyPeriod() != null;
    }

}
