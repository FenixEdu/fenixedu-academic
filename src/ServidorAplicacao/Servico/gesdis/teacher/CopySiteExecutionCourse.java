/*
 * Created on Jan 18, 2005
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.BibliographicReference;
import Dominio.EvaluationMethod;
import Dominio.ExecutionCourse;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseEquivalence;
import Dominio.ICurriculum;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionCourse;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Item;
import Dominio.Section;
import Dominio.Site;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.teacher.DeleteBibliographicReference;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author mrsp and jdnf
 */
public class CopySiteExecutionCourse implements IService {
    
    public CopySiteExecutionCourse() {}
    
    public String getNome() {
        return "CopySiteExecutionCourse";
    }
    
    public void run(Integer executionCourseIDFrom, Integer executionCourseIDTo) throws FenixServiceException{
        try{
            
            //Persistent Initializations
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            
            //Course From	        
            IExecutionCourse executionCourseFrom = getExecutionCourse(executionCourseIDFrom, persistentExecutionCourse);
            ISite siteFrom = getSiteFrom(persistentSite, executionCourseFrom);
            
            //Course To
            IExecutionCourse executionCourseTo = getExecutionCourse(executionCourseIDTo, persistentExecutionCourse);
            ISite siteTo = getSiteTo(persistentSite, executionCourseTo);  
            
            //Begin copy
            persistentSite.simpleLockWrite(siteTo);
            
            //copy course if it isn't
            if(siteTo.getExecutionCourse() == null){
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
                
                List sectionsTo = persistentSection.readBySite(siteTo);
                IPersistentItem persistentItem = sp.getIPersistentItem();
                
                if (sectionsTo != null && sectionsTo.size() > 0) {
                    //Delete sections and itens
                    deleteSectionsAndItems(sp, siteTo, persistentSection, sectionsTo, persistentItem);
                }
                
                //Copy sections and itens
                copySectionsAndItems(siteTo, persistentSection, sectionsFrom, persistentItem, sectionsTo);
            }
                           
            deleteBibliographicReference(sp, executionCourseTo);                       
            copyBibliographicReferences(sp, executionCourseFrom, executionCourseTo);           
            copyEvaluationMethod(sp, executionCourseFrom, executionCourseTo);
            
            }catch(ExcepcaoPersistencia e){
                throw new FenixServiceException(e.getMessage());
            }
        }

    /**
     * @param sp
     * @param executionCourseTo
     * @throws ExcepcaoPersistencia
     */
    private void deleteBibliographicReference(ISuportePersistente sp, IExecutionCourse executionCourseTo) throws ExcepcaoPersistencia {
       IPersistentBibliographicReference persistentBibliographicReference = sp.getIPersistentBibliographicReference();
       
       List bibliographicReferences = persistentBibliographicReference.readBibliographicReference(executionCourseTo);
       
       if(bibliographicReferences != null)
           for (Iterator iter = bibliographicReferences.iterator(); iter.hasNext();) {
               IBibliographicReference element = (IBibliographicReference) iter.next();
               persistentBibliographicReference.delete(element);    
           }
    }

    /**
     * @param sp
     * @param executionCourseFrom
     * @param executionCourseTo
     * @throws ExcepcaoPersistencia
     */
    protected void copyEvaluationMethod(ISuportePersistente sp, IExecutionCourse executionCourseFrom, IExecutionCourse executionCourseTo) throws ExcepcaoPersistencia {
        IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
        IEvaluationMethod evaluationMethod = persistentEvaluationMethod.readByExecutionCourse(executionCourseFrom);
        if (evaluationMethod != null) {
            IEvaluationMethod evaluationMethodTo = persistentEvaluationMethod.readByExecutionCourse(executionCourseTo);
            if (evaluationMethodTo == null) {
                evaluationMethodTo = new EvaluationMethod();
            }
            persistentEvaluationMethod.simpleLockWrite(evaluationMethodTo);
            evaluationMethodTo.setExecutionCourse(executionCourseTo);
            evaluationMethodTo.setEvaluationElements(evaluationMethod.getEvaluationElements());
            evaluationMethodTo.setEvaluationElementsEn(evaluationMethod.getEvaluationElementsEn());
        }
    }

    /**
     * @param sp
     * @param executionCourseFrom
     * @param executionCourseTo
     * @throws ExcepcaoPersistencia
     */
    protected void copyBibliographicReferences(ISuportePersistente sp, IExecutionCourse executionCourseFrom, IExecutionCourse executionCourseTo) throws ExcepcaoPersistencia {
        IPersistentBibliographicReference persistentBibliographicReference = sp.getIPersistentBibliographicReference();
        List bibliographicReferenceList = persistentBibliographicReference.readBibliographicReference(executionCourseFrom);
        if (bibliographicReferenceList != null && bibliographicReferenceList.size() > 0) {
            ListIterator iterator = bibliographicReferenceList.listIterator();
            while (iterator.hasNext()) {
                IBibliographicReference bibliographicReference = (IBibliographicReference) iterator.next();
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
    }

    /**
     * @param siteTo
     * @param persistentSection
     * @param sectionsFrom
     * @param persistentItem
     * @throws ExcepcaoPersistencia
     */
    protected void copySectionsAndItems(ISite siteTo, IPersistentSection persistentSection, List sectionsFrom, IPersistentItem persistentItem, List sectionsTo) throws ExcepcaoPersistencia {
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
            List itens = persistentItem.readAllItemsBySection(sectionFrom);
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

    /**
     * @param sp
     * @param siteTo
     * @param persistentSection
     * @return
     * @throws ExcepcaoPersistencia
     */
    protected void deleteSectionsAndItems(ISuportePersistente sp, ISite siteTo, IPersistentSection persistentSection, List sectionsTo, IPersistentItem persistentItem) throws ExcepcaoPersistencia {        
        ListIterator iteratorSection = sectionsTo.listIterator();
        while (iteratorSection.hasNext()) {
            ISection section = (ISection) iteratorSection.next();
            List itens = persistentItem.readAllItemsBySection(section);
          
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

    /**
     * @param persistentSite
     * @param executionCourseTo
     * @return
     * @throws ExcepcaoPersistencia
     */
    protected ISite getSiteTo(IPersistentSite persistentSite, IExecutionCourse executionCourseTo) throws ExcepcaoPersistencia {
        ISite siteTo = persistentSite.readByExecutionCourse(executionCourseTo);
        if (siteTo == null) {
            siteTo = new Site();
        }
        return siteTo;
    }

    /**
     * @param executionCourseIDFrom
     * @param persistentExecutionCourse
     * @return
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     */
    protected IExecutionCourse getExecutionCourse(Integer executionCourseIDFrom, IPersistentExecutionCourse persistentExecutionCourse) throws ExcepcaoPersistencia, FenixServiceException {
        IExecutionCourse executionCourseFrom = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseIDFrom);
        if(executionCourseFrom == null)
            throw new FenixServiceException("no.execution.course");
        return executionCourseFrom;
    }

    /**
     * @param persistentSite
     * @param executionCourseFrom
     * @return
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     */
    protected ISite getSiteFrom(IPersistentSite persistentSite, IExecutionCourse executionCourseFrom) throws ExcepcaoPersistencia, FenixServiceException {
        ISite siteFrom = persistentSite.readByExecutionCourse(executionCourseFrom);
        if (siteFrom == null) {
            throw new FenixServiceException("no.site");
        }
        return siteFrom;
    }
    }
