package net.sourceforge.fenixedu.domain.research.result;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile.FileResultPermittedGroupType;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.ResultPredicates;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;

public abstract class ResearchResult extends ResearchResult_Base {

    public ResearchResult() {

        super();
        setRootDomainObject(Bennu.getInstance());
        setOnCreateAtributes();
    }

    public abstract String getResume();

    protected String getParticipationsAndTitleString() {
        String resume = "";
        int i = 0;
        for (ResultParticipation participation : getOrderedResultParticipations()) {
            resume = resume + participation.getPerson().getName();
            i++;
            if (i < getResultParticipationsSet().size()) {
                resume = resume + ", ";
            } else {
                resume = resume + " - ";
            }
        }
        resume = resume + getTitle() + " - ";
        return resume;
    }

    protected String finishResume(String resume) {
        if ((resume.charAt(resume.length() - 1) == ',')) {
            resume = resume.substring(0, resume.length() - 1);
        } else if ((resume.charAt(resume.length() - 2) == ',')) {
            resume = resume.substring(0, resume.length() - 2);
        } else if ((resume.charAt(resume.length() - 2) == '-')) {
            resume = resume.substring(0, resume.length() - 2);
        }
        return resume;
    }

    private void setOnCreateAtributes() {
        check(this, ResultPredicates.createPredicate);
        if (Authenticate.getUser() != null) {
            super.setModifiedBy(AccessControl.getPerson().getName());
            setCreator(AccessControl.getPerson());
        }
        super.setLastModificationDate(new DateTime());
    }

    public ResultParticipation setCreatorParticipation(Person participator, ResultParticipationRole role) {
        check(this, ResultPredicates.createPredicate);
        return new ResultParticipation(this, participator, role, this.getResultParticipationsSet().size());
    }

    public ResultParticipation addParticipation(Person participator, ResultParticipationRole role) {
        check(this, ResultPredicates.writePredicate);
        final ResultParticipation participation =
                new ResultParticipation(this, participator, role, this.getResultParticipationsSet().size());
        updateModifiedByAndDate();
        return participation;
    }

    public void removeParticipation(ResultParticipation participation) {
        check(this, ResultPredicates.writePredicate);

        Person person = participation.getPerson();
        if (person.equals(this.getCreator())) {
            throw new DomainException("error.researcher.Result.cannotRemoveCreatorParticipation");
        } else {
            participation.deleteIfNotLast();
            this.reOrderParticipations(this.getOrderedResultParticipations());
            updateModifiedByAndDate();
        }
    }

    public boolean isAssociatedWithUnit(Unit unit) {
        for (ResultUnitAssociation association : getResultUnitAssociations()) {
            if (association.getUnit() == unit) {
                return true;
            }
        }
        return false;
    }

    public ResultUnitAssociation addUnitAssociation(Unit unit, ResultUnitAssociationRole role) {
        check(this, ResultPredicates.writePredicate);
        final ResultUnitAssociation association = new ResultUnitAssociation(this, unit, role);
        updateModifiedByAndDate();
        return association;
    }

    public void removeUnitAssociation(ResultUnitAssociation association) {
        check(this, ResultPredicates.writePredicate);
        association.delete();
        updateModifiedByAndDate();
    }

    public ResearchResultDocumentFile addDocumentFile(byte[] content, String filename, String displayName,
            FileResultPermittedGroupType permittedGroupType) {
        check(this, ResultPredicates.writePredicate);
        return addDocumentFile(content, filename, displayName, permittedGroupType, Boolean.TRUE);
    }

    public ResearchResultDocumentFile addDocumentFile(byte[] content, String filename, String displayName,
            FileResultPermittedGroupType permittedGroupType, Boolean isVisible) {
        check(this, ResultPredicates.writePredicate);
        final ResearchResultDocumentFile documentFile =
                new ResearchResultDocumentFile(content, this, filename, displayName, permittedGroupType);
        documentFile.setVisible(isVisible);
        updateModifiedByAndDate();
        return documentFile;
    }

    public void removeDocumentFile(ResearchResultDocumentFile documentFile) {
        check(this, ResultPredicates.writePredicate);
        documentFile.delete();
        updateModifiedByAndDate();
    }

    public void setParticipationsOrder(List<ResultParticipation> newParticipationsOrder) {
        check(this, ResultPredicates.writePredicate);
        reOrderParticipations(newParticipationsOrder);
        updateModifiedByAndDate();
    }

    public void setModifiedByAndDate() {
        check(this, ResultPredicates.writePredicate);
        updateModifiedByAndDate();
    }

    public void delete() {
        check(this, ResultPredicates.writePredicate);
        Person requestingPerson = AccessControl.getPerson();
        if (!hasPersonParticipation(requestingPerson) && !this.getCreator().equals(requestingPerson)) {
            throw new DomainException("error.researcher.Result.onlyParticipantsCanDelete");
        }

        for (; !getResultTeachers().isEmpty(); getResultTeachers().iterator().next().delete()) {
            ;
        }
        removeAssociations();
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public final static ResearchResult readByOid(String oid) {
        final ResearchResult result = FenixFramework.getDomainObject(oid);

        if (result == null) {
            throw new DomainException("error.researcher.Result.null");
        }
        return result;
    }

    /**
     * Returns participations list ordered.
     */
    public List<ResultParticipation> getOrderedResultParticipations() {
        return Collections.unmodifiableList(sort(super.getResultParticipationsSet()));
    }

    private List<ResultParticipation> filterResultParticipationsByRole(ResultParticipationRole role) {
        List<ResultParticipation> authorsParticipations = new ArrayList<ResultParticipation>();
        for (ResultParticipation participation : getResultParticipations()) {
            if (participation.getRole().equals(role)) {
                authorsParticipations.add(participation);
            }
        }
        return authorsParticipations;
    }

    public List<ResultParticipation> getOrderedAuthorsResultParticipations() {
        return Collections.unmodifiableList(sort(filterResultParticipationsByRole(ResultParticipationRole.Author)));
    }

    public List<ResultParticipation> getOrderedEditorsResultParticipations() {
        return Collections.unmodifiableList(sort(filterResultParticipationsByRole(ResultParticipationRole.Editor)));
    }

    /**
     * Returns true if already exists a result participation with the given
     * person and role.
     */
    public boolean hasPersonParticipationWithRole(Person person, ResultParticipationRole role) {
        if (this.hasAnyResultParticipations()) {
            for (ResultParticipation participation : this.getResultParticipations()) {
                if (participation.getPerson() != null && participation.getPerson().equals(person)
                        && participation.getRole().equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if already exists a result participation with the given
     * person. (Role not relevant. Used for access control verification).
     */
    public boolean hasPersonParticipation(Person person) {
        if (this.hasAnyResultParticipations()) {
            for (ResultParticipation participation : this.getResultParticipations()) {
                if (participation.getPerson() != null && participation.getPerson().equals(person)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if exists an association between result and the given unit
     * and role.
     */
    public boolean hasAssociationWithUnitRole(Unit unit, ResultUnitAssociationRole role) {
        if (unit != null && role != null && this.hasAnyResultUnitAssociations()) {
            final Collection<ResultUnitAssociation> list = this.getResultUnitAssociations();

            for (ResultUnitAssociation association : list) {
                if (association.getUnit() != null && association.getUnit().equals(unit) && association.getRole().equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean getIsPossibleSelectPersonRole() {
        return false;
    }

    public final boolean acceptsParticipationRole(ResultParticipationRole role) {
        if (role == null) {
            return false;
        }
        if (this instanceof Book || this instanceof BookPart || this instanceof Inproceedings || this instanceof Proceedings) {
            if (role.equals(ResultParticipationRole.Editor) || role.equals(ResultParticipationRole.Author)) {
                return true;
            }
        }
        if (role.equals(ResultParticipationRole.Author)) {
            return true;
        }
        return false;
    }

    private void updateModifiedByAndDate() {
        if (Authenticate.getUser() != null) {
            super.setModifiedBy(AccessControl.getPerson().getName());
        }
        super.setLastModificationDate(new DateTime());
    }

    /**
     * Order result participations by person order.
     */
    private static <T extends ResultParticipation> List<T> sort(Collection<T> resultParticipations) {
        List<T> sorted = new ArrayList<T>(resultParticipations);
        Collections.sort(sorted, new ResultParticipation.OrderComparator());

        return sorted;
    }

    /**
     * Method responsible for updates on result participations order.
     */
    private void reOrderParticipations(List<ResultParticipation> newParticipationsOrder) {
        int order = 0;
        for (ResultParticipation participation : newParticipationsOrder) {
            participation.setPersonOrder(order++);
        }
    }

    /**
     * Removes all references to result.
     */
    private void removeAssociations() {
        super.setCountry(null);
        setCreator(null);
        for (; !getAllResultDocumentFiles().isEmpty(); getAllResultDocumentFiles().iterator().next().delete()) {

        }

        for (; !getPrizes().isEmpty(); getPrizes().iterator().next().delete()) {

        }

        for (; !getResultUnitAssociations().isEmpty(); getResultUnitAssociations().iterator().next().delete()) {

        }

        for (; !getResultTeachers().isEmpty(); getResultTeachers().iterator().next().delete()) {

        }
        // These should be the last to remove, because of access control
        // verifications.
        for (; !getResultParticipations().isEmpty(); getResultParticipations().iterator().next().delete()) {

        }
    }

    // @Override
    // public void setModifiedBy(String modifiedBy) {
    // throw new DomainException("error.researcher.Result.call",
    // "setModifiedBy");
    // }

    @Override
    public void setLastModificationDate(DateTime lastModificationDate) {
        throw new DomainException("error.researcher.Result.call", "setLastModificationDate");
    }

    /**
     * Block operations on relation lists.
     */
    @Override
    public void addResultParticipations(ResultParticipation resultParticipations) {
        throw new DomainException("error.researcher.Result.call", "addResultParticipations");
    }

    @Override
    public void removeResultParticipations(ResultParticipation resultParticipations) {
        throw new DomainException("error.researcher.Result.call", "removeResultParticipations");
    }

    @Override
    public Set<ResultParticipation> getResultParticipationsSet() {
        return Collections.unmodifiableSet(super.getResultParticipationsSet());
    }

    @Override
    public void addResultUnitAssociations(ResultUnitAssociation resultUnitAssociations) {
        throw new DomainException("error.researcher.Result.call", "addResultUnitAssociations");
    }

    @Override
    public void removeResultUnitAssociations(ResultUnitAssociation resultUnitAssociations) {
        throw new DomainException("error.researcher.Result.call", "removeResultUnitAssociations");
    }

    @Override
    public Set<ResultUnitAssociation> getResultUnitAssociationsSet() {
        return Collections.unmodifiableSet(super.getResultUnitAssociationsSet());
    }

    @Override
    public Set<ResearchResultDocumentFile> getResultDocumentFilesSet() {
        return Collections.unmodifiableSet(getVisibleResultDocumentFilesSet());
    }

    public Set<ResearchResultDocumentFile> getAllResultDocumentFiles() {
        return Collections.unmodifiableSet(super.getResultDocumentFilesSet());
    }

    private List<ResearchResultDocumentFile> getVisibleResultDocumentFiles() {

        List<ResearchResultDocumentFile> visibleFiles = new ArrayList<ResearchResultDocumentFile>();
        Collection visibleDocuments = getVisibleFiles();

        if (visibleDocuments.size() > 0) {
            visibleFiles.addAll(visibleDocuments);
        }
        return visibleFiles;
    }

    private Set<ResearchResultDocumentFile> getVisibleResultDocumentFilesSet() {
        Collection visibleDocuments = getVisibleFiles();
        Set<ResearchResultDocumentFile> visibleFiles = new HashSet<ResearchResultDocumentFile>();

        if (visibleDocuments.size() > 0) {
            visibleFiles.addAll(visibleDocuments);
        }
        return visibleFiles;
    }

    private List<ResearchResultDocumentFile> getVisibleFiles() {
        Set<ResearchResultDocumentFile> files = super.getResultDocumentFilesSet();
        List<ResearchResultDocumentFile> visibleDocuments = new ArrayList<ResearchResultDocumentFile>();
        for (ResearchResultDocumentFile file : files) {
            if (file.getVisible()) {
                visibleDocuments.add(file);
            }
        }
        return visibleDocuments;
    }

    public boolean isEditableByUser(Person person) {
        return getCreator().equals(person) || hasPersonParticipation(person);
    }

    public boolean isEditableByCurrentUser() {
        return isEditableByUser(AccessControl.getPerson());
    }

    public boolean isDeletableByUser(Person person) {
        return getCreator().equals(person);
    }

    public boolean isDeletableByCurrentUser() {
        return isDeletableByUser(AccessControl.getPerson());
    }

    public boolean isAssociateWith(Unit unit) {
        for (ResultUnitAssociation association : getResultUnitAssociations()) {
            if (association.getUnit().equals(unit)) {
                return true;
            }
        }
        return false;
    }

    public abstract String getSchema();

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.result.ResultParticipation> getResultParticipations() {
        return getResultParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyResultParticipations() {
        return !getResultParticipationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation> getResultUnitAssociations() {
        return getResultUnitAssociationsSet();
    }

    @Deprecated
    public boolean hasAnyResultUnitAssociations() {
        return !getResultUnitAssociationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.Prize> getPrizes() {
        return getPrizesSet();
    }

    @Deprecated
    public boolean hasAnyPrizes() {
        return !getPrizesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile> getResultDocumentFiles() {
        return getResultDocumentFilesSet();
    }

    @Deprecated
    public boolean hasAnyResultDocumentFiles() {
        return !getResultDocumentFilesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.result.ResultTeacher> getResultTeachers() {
        return getResultTeachersSet();
    }

    @Deprecated
    public boolean hasAnyResultTeachers() {
        return !getResultTeachersSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUrl() {
        return getUrl() != null;
    }

    @Deprecated
    public boolean hasCreator() {
        return getCreator() != null;
    }

    @Deprecated
    public boolean hasModifiedBy() {
        return getModifiedBy() != null;
    }

    @Deprecated
    public boolean hasLastModificationDate() {
        return getLastModificationDate() != null;
    }

    @Deprecated
    public boolean hasNote() {
        return getNote() != null;
    }

    @Deprecated
    public boolean hasCountry() {
        return getCountry() != null;
    }

    @Deprecated
    public boolean hasUniqueStorageId() {
        return getUniqueStorageId() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

}
