/*
 * Created on 22/Out/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author tfs
 *  
 */
public class CopySiteExecutionCourse implements IService {
    
    public void run(Integer executionCourseFromID, Integer executionCourseToID)
            throws ExcepcaoPersistencia, FenixServiceException, DomainException {        
        
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();      
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        
        final IExecutionCourse executionCourseFrom = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseFromID);
        if (executionCourseFrom == null)
            throw new InvalidArgumentsServiceException();
        
        final IExecutionCourse executionCourseTo = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseToID);
        if (executionCourseTo == null)
            throw new InvalidArgumentsServiceException();
                
        ISite siteFrom = executionCourseFrom.getSite();
        if (siteFrom == null) {
            throw new FenixServiceException();            
        }
        
        ISite siteTo = executionCourseTo.getSite();
        if (siteTo == null) {
            executionCourseTo.createSite();
            siteTo = executionCourseTo.getSite();
        }
                
        persistentExecutionCourse.simpleLockWrite(executionCourseTo);               
        siteTo.edit(siteFrom.getInitialStatement(), siteFrom.getIntroduction(), siteFrom.getMail(), siteFrom.getAlternativeSite());
        
        copySectionsAndItems(siteFrom, siteTo);
        copyBibliographicReference(executionCourseFrom, executionCourseTo);
        copyEvaluationMethod(executionCourseFrom, executionCourseTo);
        
    }

    private void copySectionsAndItems(ISite siteFrom, ISite siteTo) throws DomainException {        
        
        deleteSectionsAndItemsIfExistFrom(siteTo);
        
        if (siteFrom.getAssociatedSectionsCount() > 0) {
            Iterator associatedSections = siteFrom.getAssociatedSectionsIterator();
            while (associatedSections.hasNext()) {                
                ISection sectionFrom = (ISection) associatedSections.next();
                if (sectionFrom.getSuperiorSection() == null) {
                    ISection sectionTo = siteTo.createSection(sectionFrom.getName(), null, sectionFrom.getSectionOrder());
                    copyItemsFrom(sectionFrom, sectionTo);
                    copySubSectionsAndItemsFrom(sectionFrom, sectionTo, siteTo);
                }
            }
        }
    }

    private void copySubSectionsAndItemsFrom(ISection sectionFrom, ISection sectionTo, ISite siteTo) {                
        if (sectionFrom.getAssociatedSectionsCount() > 0) {
            Iterator associatedSections = sectionFrom.getAssociatedSectionsIterator();
            while (associatedSections.hasNext()) {                
                ISection subSectionFrom = (ISection) associatedSections.next();
                if (subSectionFrom.getSuperiorSection() != null) {
                    ISection subSectionTo = siteTo.createSection(subSectionFrom.getName(), sectionTo, subSectionFrom.getSectionOrder());
                    copyItemsFrom(subSectionFrom, subSectionTo);
                    copySubSectionsAndItemsFrom(subSectionFrom, subSectionTo, siteTo);
                }
            }
        }                
    }

    private void copyItemsFrom(ISection sectionFrom, ISection sectionTo) {
        if (sectionFrom.getAssociatedItemsCount() > 0) {
            Iterator associatedItems = sectionFrom.getAssociatedItemsIterator();
            while (associatedItems.hasNext()) {
                IItem item = (IItem) associatedItems.next();
                //TODO: sectionTo.insertItem(item.getName(), item.getInformation(), item.getUrgent(), item.getItemOrder());
            }
        }        
    }

    private void deleteSectionsAndItemsIfExistFrom(ISite siteTo) throws DomainException {
        if (siteTo.getAssociatedSectionsCount() > 0) {
            Iterator associatedSections = siteTo.getAssociatedSectionsIterator();
            while (associatedSections.hasNext()) {
                ISection section = (ISection) associatedSections.next();
                section.delete();
            }            
        }        
    }

    private void copyBibliographicReference(final IExecutionCourse executionCourseFrom, IExecutionCourse executionCourseTo) {
        if (executionCourseFrom.getAssociatedBibliographicReferencesCount() > 0) {
            Iterator bibliographicReferences = executionCourseFrom.getAssociatedBibliographicReferencesIterator();
            while (bibliographicReferences.hasNext()) {
                IBibliographicReference bibliographicReference = (IBibliographicReference) bibliographicReferences.next();
                executionCourseTo.createBibliographicReference(bibliographicReference.getTitle(), bibliographicReference.getAuthors(), bibliographicReference.getReference(), bibliographicReference.getYear(), bibliographicReference.getOptional());
            }
        }        
    }

    private void copyEvaluationMethod(final IExecutionCourse executionCourseFrom, IExecutionCourse executionCourseTo) {
        if (executionCourseFrom.getEvaluationMethod() != null) {
            IEvaluationMethod evaluationMethod = executionCourseFrom.getEvaluationMethod();
            executionCourseTo.createEvaluationMethod(evaluationMethod.getEvaluationElements(), evaluationMethod.getEvaluationElementsEn());
        }
    }
}