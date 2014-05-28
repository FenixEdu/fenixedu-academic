/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import com.google.common.base.Predicate;

abstract public class PhdProgramProcess extends PhdProgramProcess_Base {

    protected PhdProgramProcess() {
        super();
    }

    public PhdProgramProcessDocument addDocument(PhdProgramDocumentUploadBean each, Person responsible) {
        return new PhdProgramProcessDocument(this, each.getType(), each.getRemarks(), each.getFileContent(), each.getFilename(),
                responsible);
    }

    protected void addDocuments(List<PhdProgramDocumentUploadBean> documents, Person responsible) {
        for (final PhdProgramDocumentUploadBean each : documents) {
            addDocument(each, responsible);
        }
    }

    private Set<PhdProgramProcessDocument> getDocumentsByType(PhdIndividualProgramDocumentType type) {
        final Set<PhdProgramProcessDocument> result = new HashSet<PhdProgramProcessDocument>();

        for (final PhdProgramProcessDocument document : getDocumentsSet()) {
            if (document.getDocumentType() == type) {
                result.add(document);
            }
        }

        return result;
    }

    protected Set<PhdProgramProcessDocument> filterLatestDocumentVersions(Collection<PhdProgramProcessDocument> documentsToFilter) {
        final Set<PhdProgramProcessDocument> result = new HashSet<PhdProgramProcessDocument>();

        for (final PhdProgramProcessDocument document : documentsToFilter) {
            if (!document.getDocumentAccepted()) {
                continue;
            }

            result.add(document.getLastVersion());
        }

        return result;
    }

    public Set<PhdProgramProcessDocument> getLatestDocumentsByType(PhdIndividualProgramDocumentType type) {
        final Collection<PhdProgramProcessDocument> documents = new HashSet<PhdProgramProcessDocument>();
        for (final PhdProgramProcessDocument document : getDocumentsSet()) {
            if (document.getDocumentType() == type) {
                documents.add(document);
            }
        }

        return filterLatestDocumentVersions(documents);
    }

    public Integer getLastVersionNumber(PhdIndividualProgramDocumentType type) {
        Set<PhdProgramProcessDocument> documentsByType = getDocumentsByType(type);

        return documentsByType.isEmpty() ? 0 : documentsByType.size();
    }

    public Set<PhdProgramProcessDocument> getAllDocumentVersionsOfType(PhdIndividualProgramDocumentType type) {
        return getDocumentsByType(type);
    }

    public PhdProgramProcessDocument getLatestDocumentVersionFor(PhdIndividualProgramDocumentType type) {
        if (!type.isVersioned()) {
            throw new DomainException("error.PhdProgramProcess.latest.document.version.method.only.for.versioned.types");
        }

        final SortedSet<PhdProgramProcessDocument> documents =
                new TreeSet<PhdProgramProcessDocument>(PhdProgramProcessDocument.COMPARATOR_BY_VERSION);

        for (PhdProgramProcessDocument document : getDocumentsByType(type)) {
            if (document.getDocumentAccepted()) {
                documents.add(document);
            }
        }

        return documents.isEmpty() ? null : documents.iterator().next();
    }

    public Set<PhdProgramProcessDocument> getLatestDocumentVersions() {
        return filterLatestDocumentVersions(getDocumentsSet());
    }

    public Set<PhdProgramProcessDocument> getLatestDocumentVersionsAvailableToStudent() {

        final Collection<PhdIndividualProgramDocumentType> documentTypesVisibleToStudent =
                PhdIndividualProgramDocumentType.getDocumentTypesVisibleToStudent();

        final Collection<PhdProgramProcessDocument> documents = new HashSet<PhdProgramProcessDocument>();
        for (final PhdProgramProcessDocument document : getDocumentsSet()) {
            if (documentTypesVisibleToStudent.contains(document.getDocumentType())) {
                documents.add(document);
            }
        }

        return filterLatestDocumentVersions(documents);
    }

    static public boolean isParticipant(PhdProgramProcess process, User userView) {
        return process.isAllowedToManageProcess(userView)
                || process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())
                || process.getIndividualProgramProcess().isGuiderOrAssistentGuider(userView.getPerson())
                || process.getIndividualProgramProcess().getPerson() == userView.getPerson()
                || process.getIndividualProgramProcess().isParticipant(userView.getPerson());
    }

    public PhdProcessState getMostRecentState() {
        return hasAnyStates() ? Collections.max(getStates(), PhdProcessState.COMPARATOR_BY_DATE) : null;
    }

    abstract public boolean hasAnyStates();

    abstract public Collection<? extends PhdProcessState> getStates();

    public Collection<? extends PhdProcessState> getOrderedStates() {
        List<? extends PhdProcessState> states = new ArrayList<PhdProcessState>(getStates());
        Collections.sort(states, PhdProcessState.COMPARATOR_BY_DATE);

        return states;
    }

    public List<PhdProcessState> getOrderedStatesByType(final PhdProcessStateType type) {
        List<PhdProcessState> result = new ArrayList<PhdProcessState>();

        Collection<? extends PhdProcessState> orderedStates = getOrderedStates();

        for (PhdProcessState phdProcessState : orderedStates) {
            if (type.equals(phdProcessState.getType())) {
                result.add(phdProcessState);
            }
        }

        return result;
    }

    public PhdProcessState getMostRecentStateByType(final PhdProcessStateType type) {
        List<PhdProcessState> orderedStatesByType = getOrderedStatesByType(type);
        Collections.reverse(orderedStatesByType);

        if (orderedStatesByType.isEmpty()) {
            return null;
        }

        return orderedStatesByType.iterator().next();
    }

    public PhdProcessStateType getActiveState() {
        final PhdProcessState state = getMostRecentState();
        return state != null ? state.getType() : null;
    }

    public String getActiveStateRemarks() {
        return getMostRecentState().getRemarks();
    }

    public boolean hasState(PhdProcessStateType type) {
        final List<PhdProcessState> states = new ArrayList<PhdProcessState>(getStates());
        Collections.sort(states, PhdCandidacyProcessState.COMPARATOR_BY_DATE);

        for (final PhdProcessState state : states) {
            if (state.getType().equals(type)) {
                return true;
            }
        }

        return false;
    }

    abstract protected PhdIndividualProgramProcess getIndividualProgramProcess();

    abstract protected Person getPerson();

    /**
     * Used to determine whether the specified person is allowed to manage the
     * Process, according to the Rule system.
     * 
     * @see AcademicOperationType
     */
    abstract protected boolean isAllowedToManageProcess(User userView);

    public static final Predicate<PhdProgramProcess> IS_ALLOWED_TO_MANAGE_PROCESS_PREDICATE = new Predicate<PhdProgramProcess>() {
        @Override
        public boolean apply(PhdProgramProcess process) {
            return process.isAllowedToManageProcess(Authenticate.getUser());
        }
    };

    public boolean isProcessCandidacy() {
        return false;
    }

    public boolean isProcessIndividualProgram() {
        return false;
    }

    public boolean isProcessThesis() {
        return false;
    }

    public boolean isProcessPublicPresentationSeminar() {
        return false;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.log.PhdLogEntry> getLogEntries() {
        return getLogEntriesSet();
    }

    @Deprecated
    public boolean hasAnyLogEntries() {
        return !getLogEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument> getDocuments() {
        return getDocumentsSet();
    }

    @Deprecated
    public boolean hasAnyDocuments() {
        return !getDocumentsSet().isEmpty();
    }

}
