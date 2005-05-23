/*
 * Created on 22/Out/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationMethod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author tfs
 *  
 */
public class CopySiteExecutionCourse implements IService {
    public void run(Integer executionCourseIDFrom, Integer executionCourseIDTo)
            throws ExcepcaoPersistencia {
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSite persistentSite = sp.getIPersistentSite();

        //Course From
        ISite siteFrom = persistentSite.readByExecutionCourse(executionCourseIDFrom);
        if (siteFrom == null)
            return;
        
        IExecutionCourse executionCourseFrom = siteFrom.getExecutionCourse();

        //Course To
        ISite siteTo = persistentSite.readByExecutionCourse(executionCourseIDTo);
        if (siteTo == null)
            siteTo = new Site();
   
        //begin copy
        persistentSite.simpleLockWrite(siteTo);

        //copy course if it isn't
        IExecutionCourse executionCourseTo = siteTo.getExecutionCourse();
        if (executionCourseTo == null) {
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            executionCourseTo = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseIDTo);
            siteTo.setExecutionCourse(executionCourseTo);
        }

        siteTo.setAlternativeSite(siteFrom.getAlternativeSite());
        siteTo.setInitialStatement(siteFrom.getInitialStatement());
        siteTo.setIntroduction(siteFrom.getIntroduction());
        siteTo.setMail(siteFrom.getMail());
        siteTo.setStyle(siteFrom.getStyle());

        //copy section and them itens, but before delete them if exists
        IPersistentSection persistentSection = sp.getIPersistentSection();
        List sectionsFrom = persistentSection.readBySite(siteFrom);
        if (sectionsFrom != null || sectionsFrom.size() > 0) {
            //Delete sections and itens
            List sectionsTo = persistentSection.readBySite(siteTo);
            IPersistentItem persistentItem = sp.getIPersistentItem();
            if (sectionsTo != null && sectionsTo.size() > 0) {
                ListIterator iteratorSection = sectionsTo.listIterator();
                while (iteratorSection.hasNext()) {
                    ISection section = (ISection) iteratorSection.next();
                    List itens = persistentItem.readAllItemsBySection(section.getIdInternal(),
                            section.getSite().getExecutionCourse().getSigla(),
                            section.getSite().getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear(),
                            section.getSite().getExecutionCourse().getExecutionPeriod().getName());
                    if (itens != null && itens.size() > 0) {
                        ListIterator iteratorItens = itens.listIterator();
                        while (iteratorItens.hasNext()) {
                            IItem item = (IItem) iteratorItens.next();
                            persistentItem.deleteByOID(Item.class, item.getIdInternal());
                        }
                    }
                    persistentSection.deleteByOID(Section.class, section.getIdInternal());
                }
            }

            //Copy sections and itens
            ListIterator iterator = sectionsFrom.listIterator();
            sectionsTo = new ArrayList();
            while (iterator.hasNext()) {
                ISection sectionFrom = (ISection) iterator.next();

                ISection sectionNew = new Section();
                persistentSection.simpleLockWrite(sectionNew);
                sectionNew.setName(sectionFrom.getName());
                sectionNew.setSectionOrder(sectionFrom.getSectionOrder());
                sectionNew.setSite(siteTo);

                //find the superior section
                ISection sectionSuperiorTo = null;
                if (sectionFrom.getSuperiorSection() != null) {
                    ListIterator iteratorsSectionsTo = sectionsTo.listIterator();
                    while (iteratorsSectionsTo.hasNext()) {
                        ISection sectionTo = (ISection) iteratorsSectionsTo.next();
                        if (sectionTo.getName().equals(sectionFrom.getSuperiorSection().getName())) {
                            sectionSuperiorTo = sectionTo;
                            break;
                        }
                    }
                }
                sectionNew.setSuperiorSection(sectionSuperiorTo);

                sectionNew.setLastModifiedDate(Calendar.getInstance().getTime());

                //section's itens
                List itens = persistentItem.readAllItemsBySection(sectionFrom.getIdInternal(),
                        sectionFrom.getSite().getExecutionCourse().getSigla(),
                        sectionFrom.getSite().getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear(),
                        sectionFrom.getSite().getExecutionCourse().getExecutionPeriod().getName());
                if (itens != null && itens.size() > 0) {
                    ListIterator iteratorItens = itens.listIterator();
                    while (iteratorItens.hasNext()) {
                        IItem item = (IItem) iteratorItens.next();

                        Item itemNew = new Item();
                        persistentItem.simpleLockWrite(itemNew);
                        itemNew.setInformation(item.getInformation());
                        itemNew.setItemOrder(item.getItemOrder());
                        itemNew.setName(item.getName());
                        itemNew.setSection(sectionNew);
                        itemNew.setUrgent(item.getUrgent());
                    }
                }

                sectionsTo.add(sectionNew);
            }
        }

        //Copy Bibliographic
        IPersistentBibliographicReference persistentBibliographicReference = sp
                .getIPersistentBibliographicReference();
        List bibliographicReferenceList = persistentBibliographicReference
                .readBibliographicReference(executionCourseFrom.getIdInternal());
        if (bibliographicReferenceList != null && bibliographicReferenceList.size() > 0) {
            ListIterator iterator = bibliographicReferenceList.listIterator();
            while (iterator.hasNext()) {
                IBibliographicReference bibliographicReference = (IBibliographicReference) iterator
                        .next();

                IBibliographicReference bibliographicReferenceTo = new BibliographicReference();
                persistentBibliographicReference.simpleLockWrite(bibliographicReferenceTo);
                bibliographicReferenceTo.setExecutionCourse(executionCourseTo);
                bibliographicReferenceTo.setAuthors(bibliographicReference.getAuthors());
                bibliographicReferenceTo.setOptional(bibliographicReference.getOptional());
                bibliographicReferenceTo.setReference(bibliographicReference.getReference());
                bibliographicReferenceTo.setTitle(bibliographicReference.getTitle());
                bibliographicReferenceTo.setYear(bibliographicReference.getYear());
            }
        }
        //Copy Evaluation Method
        IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
        IEvaluationMethod evaluationMethod = persistentEvaluationMethod
                .readByIdExecutionCourse(executionCourseFrom.getIdInternal());
        if (evaluationMethod != null) {
            IEvaluationMethod evaluationMethodTo = persistentEvaluationMethod
                    .readByIdExecutionCourse(executionCourseTo.getIdInternal());
            if (evaluationMethodTo == null) {
                evaluationMethodTo = new EvaluationMethod();
            }
            persistentEvaluationMethod.simpleLockWrite(evaluationMethodTo);
            evaluationMethodTo.setExecutionCourse(executionCourseTo);
            evaluationMethodTo.setEvaluationElements(evaluationMethod.getEvaluationElements());
            evaluationMethodTo.setEvaluationElementsEn(evaluationMethod.getEvaluationElementsEn());
        }
    }
}