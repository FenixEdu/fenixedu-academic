/*
 * Created on 22/Out/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.BibliographicReference;
import Dominio.EvaluationMethod;
import Dominio.ExecutionCourse;
import Dominio.IBibliographicReference;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionCourse;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Item;
import Dominio.Section;
import Dominio.Site;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author tfs
 *  
 */
public class CopySiteExecutionCourse implements IService {
    public void run(Integer executionCourseIDFrom, Integer executionCourseIDTo)
            throws Exception {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentSite persistentSite = sp.getIPersistentSite();

            //Course From
            IExecutionCourse executionCourseFrom = new ExecutionCourse();
            executionCourseFrom.setIdInternal(executionCourseIDFrom);
            ISite siteFrom = persistentSite
                    .readByExecutionCourse(executionCourseFrom);
            if (siteFrom == null) {
                return;
            }
            executionCourseFrom = siteFrom.getExecutionCourse();

            //Course To
            IExecutionCourse executionCourseTo = new ExecutionCourse();
            executionCourseTo.setIdInternal(executionCourseIDTo);
            ISite siteTo = persistentSite
                    .readByExecutionCourse(executionCourseTo);
            if (siteTo == null) {
                siteTo = new Site();
            }

            //begin copy
            persistentSite.simpleLockWrite(siteTo);

            //copy course if it isn't
            executionCourseTo = siteTo.getExecutionCourse();
            if (executionCourseTo == null) {
                IPersistentExecutionCourse persistentExecutionCourse = sp
                        .getIPersistentExecutionCourse();
                executionCourseTo = (IExecutionCourse) persistentExecutionCourse
                        .readByOID(ExecutionCourse.class, executionCourseIDTo);
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

                        List itens = persistentItem
                                .readAllItemsBySection(section);
                        if (itens != null && itens.size() > 0) {
                            ListIterator iteratorItens = itens.listIterator();
                            while (iteratorItens.hasNext()) {
                                IItem item = (IItem) iteratorItens.next();
                                persistentItem.deleteByOID(Item.class, item
                                        .getIdInternal());
                            }
                        }
                        persistentSection.deleteByOID(Section.class, section
                                .getIdInternal());
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
                        ListIterator iteratorsSectionsTo = sectionsTo
                                .listIterator();
                        while (iteratorsSectionsTo.hasNext()) {
                            ISection sectionTo = (ISection) iteratorsSectionsTo
                                    .next();
                            if (sectionTo.getName().equals(
                                    sectionFrom.getSuperiorSection().getName())) {
                                sectionSuperiorTo = sectionTo;
                                break;
                            }
                        }
                    }
                    sectionNew.setSuperiorSection(sectionSuperiorTo);

                    sectionNew.setLastModifiedDate(Calendar.getInstance()
                            .getTime());

                    //section's itens
                    List itens = persistentItem
                            .readAllItemsBySection(sectionFrom);
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
                    .readBibliographicReference(executionCourseFrom);
            if (bibliographicReferenceList != null
                    && bibliographicReferenceList.size() > 0) {
                ListIterator iterator = bibliographicReferenceList
                        .listIterator();
                while (iterator.hasNext()) {
                    IBibliographicReference bibliographicReference = (IBibliographicReference) iterator
                            .next();

                    IBibliographicReference bibliographicReferenceTo = new BibliographicReference();
                    persistentBibliographicReference
                            .simpleLockWrite(bibliographicReferenceTo);
                    bibliographicReferenceTo
                            .setExecutionCourse(executionCourseTo);
                    bibliographicReferenceTo.setAuthors(bibliographicReference
                            .getAuthors());
                    bibliographicReferenceTo.setOptional(bibliographicReference
                            .getOptional());
                    bibliographicReferenceTo
                            .setReference(bibliographicReference.getReference());
                    bibliographicReferenceTo.setTitle(bibliographicReference
                            .getTitle());
                    bibliographicReferenceTo.setYear(bibliographicReference
                            .getYear());
                }
            }            
            //Copy Evaluation Method
            IPersistentEvaluationMethod persistentEvaluationMethod = sp
                    .getIPersistentEvaluationMethod();
            IEvaluationMethod evaluationMethod = persistentEvaluationMethod
                    .readByExecutionCourse(executionCourseFrom);
            if (evaluationMethod != null) {
                IEvaluationMethod evaluationMethodTo = persistentEvaluationMethod
                        .readByExecutionCourse(executionCourseTo);
                if (evaluationMethodTo == null) {
                    evaluationMethodTo = new EvaluationMethod();
                }
                persistentEvaluationMethod.simpleLockWrite(evaluationMethodTo);
                evaluationMethodTo.setExecutionCourse(executionCourseTo);
                evaluationMethodTo.setEvaluationElements(evaluationMethod
                        .getEvaluationElements());
                evaluationMethodTo.setEvaluationElementsEn(evaluationMethod
                        .getEvaluationElementsEn());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}