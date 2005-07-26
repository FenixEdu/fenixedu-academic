/*
 * Created on Jul 26, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.applicationTier.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExecutionCourseUtils {

    public static void copySectionsAndItems(ISite siteFrom, ISite siteTo) throws DomainException {

        if (siteFrom.getAssociatedSectionsCount() > 0) {
            Iterator associatedSections = siteFrom.getAssociatedSectionsIterator();
            while (associatedSections.hasNext()) {
                ISection sectionFrom = (ISection) associatedSections.next();
                if (sectionFrom.getSuperiorSection() == null) {
                    ISection sectionTo = siteTo.createSection(sectionFrom.getName(), null, sectionFrom
                            .getSectionOrder());
                    copyItemsFrom(sectionFrom, sectionTo);
                    copySubSectionsAndItemsFrom(sectionFrom, sectionTo, siteTo);
                }
            }
        }
    }

    private static void copySubSectionsAndItemsFrom(ISection sectionFrom, ISection sectionTo,
            ISite siteTo) throws DomainException {
        if (sectionFrom.getAssociatedSectionsCount() > 0) {
            Iterator associatedSections = sectionFrom.getAssociatedSectionsIterator();
            while (associatedSections.hasNext()) {
                ISection subSectionFrom = (ISection) associatedSections.next();
                if (subSectionFrom.getSuperiorSection() != null) {
                    ISection subSectionTo = siteTo.createSection(subSectionFrom.getName(), sectionTo,
                            subSectionFrom.getSectionOrder());
                    copyItemsFrom(subSectionFrom, subSectionTo);
                    copySubSectionsAndItemsFrom(subSectionFrom, subSectionTo, siteTo);
                }
            }
        }
    }

    private static void copyItemsFrom(ISection sectionFrom, ISection sectionTo) throws DomainException {
        if (sectionFrom.getAssociatedItemsCount() > 0) {
            Iterator associatedItems = sectionFrom.getAssociatedItemsIterator();
            while (associatedItems.hasNext()) {
                IItem item = (IItem) associatedItems.next();
                sectionTo.insertItem(item.getName(), item.getInformation(), item.getUrgent(), item
                        .getItemOrder());
            }
        }
    }

    public static void deleteSectionsAndItemsIfExistFrom(ISite siteTo) throws DomainException {
        if (siteTo.getAssociatedSectionsCount() > 0) {
            Iterator associatedSections = siteTo.getAssociatedSectionsIterator();
            while (associatedSections.hasNext()) {
                ISection section = (ISection) associatedSections.next();
                section.delete();
            }
        }
    }

    public static List<IBibliographicReference> copyBibliographicReference(final IExecutionCourse executionCourseFrom,
            IExecutionCourse executionCourseTo) {
        final List<IBibliographicReference> notCopiedBibliographicReferences = new ArrayList();
        
        if (executionCourseFrom.getAssociatedBibliographicReferencesCount() > 0) {
            Iterator bibliographicReferences = executionCourseFrom
                    .getAssociatedBibliographicReferencesIterator();
            while (bibliographicReferences.hasNext()) {
                IBibliographicReference bibliographicReference = (IBibliographicReference) bibliographicReferences
                        .next();
                if (canAddBibliographicReferenceTo(executionCourseTo, bibliographicReference)) {
                    executionCourseTo.createBibliographicReference(bibliographicReference.getTitle(),
                            bibliographicReference.getAuthors(), bibliographicReference.getReference(),
                            bibliographicReference.getYear(), bibliographicReference.getOptional());
                }
                else {
                    notCopiedBibliographicReferences.add(bibliographicReference);                
                }
            }
        }
        return notCopiedBibliographicReferences;
    }

    private static boolean canAddBibliographicReferenceTo(final IExecutionCourse executionCourse,
            final IBibliographicReference bibliographicReferenceToAdd) {
        final Iterator associatedBibliographicReferences = executionCourse
                .getAssociatedBibliographicReferencesIterator();
        while (associatedBibliographicReferences.hasNext()) {
            IBibliographicReference bibliographicReference = (IBibliographicReference) associatedBibliographicReferences
                    .next();
            if (bibliographicReference.getTitle().equals(bibliographicReferenceToAdd.getTitle())) {
                return false;
            }
        }
        return true;
    }

    public static void copyEvaluationMethod(final IExecutionCourse executionCourseFrom,
            IExecutionCourse executionCourseTo) {
        if (executionCourseFrom.getEvaluationMethod() != null) {
            IEvaluationMethod evaluationMethod = executionCourseFrom.getEvaluationMethod();
            executionCourseTo.createEvaluationMethod(evaluationMethod.getEvaluationElements(),
                    evaluationMethod.getEvaluationElementsEn());
        }
    }

}
