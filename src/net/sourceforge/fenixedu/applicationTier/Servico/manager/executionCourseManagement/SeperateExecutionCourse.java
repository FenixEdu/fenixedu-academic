/*
 * Created on 2004/11/17
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.ITurnoAluno;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ResponsibleFor;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationMethod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Luis Cruz
 *  
 */
public class SeperateExecutionCourse implements IService {

    public void run(final Integer originExecutionCourseOid, final Integer destinationExecutionCourseId,
            final Integer[] shiftIdsToTransfer, final Integer[] curricularCourseIdsToTransfer)
            throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = sp.getIPersistentObject();

        final IExecutionCourse originExecutionCourse = (IExecutionCourse) persistentObject.readByOID(
                ExecutionCourse.class, originExecutionCourseOid);
        IExecutionCourse destinationExecutionCourse = (IExecutionCourse) persistentObject.readByOID(
                ExecutionCourse.class, destinationExecutionCourseId);
        if (destinationExecutionCourse == null) {
            destinationExecutionCourse = createNewExecutionCourse(persistentObject, originExecutionCourse);
            createNewSite(persistentObject, destinationExecutionCourse);
            copyEvaluationMethod(sp, persistentObject, destinationExecutionCourse, originExecutionCourse);
            copyBibliographicReferences(sp, persistentObject, destinationExecutionCourse, originExecutionCourse);
            //copySummaries(sp, persistentObject, destinationExecutionCourse, originExecutionCourse);
        }

        final Collection curricularCoursesToTransfer = getCurricularCoursesToTransfer(
                curricularCourseIdsToTransfer, originExecutionCourse.getAssociatedCurricularCourses());

        persistentObject.simpleLockWrite(originExecutionCourse);
        persistentObject.simpleLockWrite(destinationExecutionCourse);

        final Collection transferedStudents = transferAttends(persistentObject,
                curricularCourseIdsToTransfer, originExecutionCourse.getAttends(),
                destinationExecutionCourse);

        transferShifts(persistentObject, sp, getShifts(sp, originExecutionCourse), shiftIdsToTransfer,
                transferedStudents, destinationExecutionCourse);

        originExecutionCourse.getAssociatedCurricularCourses().removeAll(curricularCoursesToTransfer);
        destinationExecutionCourse.getAssociatedCurricularCourses().addAll(curricularCoursesToTransfer);
    }

//    private void copySummaries(ISuportePersistente sp, IPersistentObject persistentObject, IExecutionCourse destinationExecutionCourse, IExecutionCourse originExecutionCourse) throws ExcepcaoPersistencia {
//        IPersistentSummary persistentSummary = sp.getIPersistentSummary();
//        List summaries = persistentSummary.readByExecutionCourse(originExecutionCourse);
//        for (int i = 0; i < summaries.size(); i++) {
//            ISummary summary = (ISummary) summaries.get(i);
//            ISummary newSummary = new Summary();
//            persistentObject.simpleLockWrite(newSummary);
//            summary.setExecutionCourse(destinationExecutionCourse);
//            summary.setIsExtraLesson(summary.getIsExtraLesson());
//            summary.setLastModifiedDate(summary.getLastModifiedDate());
//            summary.setProfessorship();
//            summary.setRoom(summary.getRoom());
//            summary.setShift(summary.getShift());
//            summary.setStudentsNumber(summary.getStudentsNumber());
//            summary.setSummaryDate(summary.getSummaryDate());
//            summary.setSummaryHour(summary.getSummaryHour());
//            summary.setSummaryText(summary.getSummaryText());
//            summary.setSummaryType(summary.getSummaryType());
//            summary.setTeacher(summary.getTeacher());
//            summary.setTeacherName(summary.getTeacherName());
//            summary.setTitle(summary.getTitle());
//        }
//    }

    private void copyBibliographicReferences(ISuportePersistente sp, IPersistentObject persistentObject, IExecutionCourse destinationExecutionCourse, IExecutionCourse originExecutionCourse) throws ExcepcaoPersistencia {
        IPersistentBibliographicReference persistentBibliographicReference = sp.getIPersistentBibliographicReference();
        List bibliographicReferences = persistentBibliographicReference.readBibliographicReference(originExecutionCourse);
        for (int i = 0; i < bibliographicReferences.size(); i++) {
            IBibliographicReference bibliographicReference = (IBibliographicReference) bibliographicReferences.get(i);
            IBibliographicReference newibliographicReference = new BibliographicReference();
            persistentObject.simpleLockWrite(newibliographicReference);
            newibliographicReference.setAuthors(bibliographicReference.getAuthors());
            newibliographicReference.setExecutionCourse(destinationExecutionCourse);
            newibliographicReference.setOptional(bibliographicReference.getOptional());
            newibliographicReference.setReference(bibliographicReference.getReference());
            newibliographicReference.setTitle(bibliographicReference.getTitle());
            newibliographicReference.setYear(bibliographicReference.getYear());
        }
    }

    private void copyEvaluationMethod(ISuportePersistente sp, IPersistentObject persistentObject, IExecutionCourse destinationExecutionCourse, IExecutionCourse originExecutionCourse) throws ExcepcaoPersistencia {
        IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
        IEvaluationMethod evaluationMethod = persistentEvaluationMethod.readByExecutionCourse(originExecutionCourse);
        if (evaluationMethod != null) {
            IEvaluationMethod newEvaluationMethod = new EvaluationMethod();
            persistentObject.simpleLockWrite(newEvaluationMethod);
            newEvaluationMethod.setEvaluationElements(evaluationMethod.getEvaluationElements());
            newEvaluationMethod.setEvaluationElementsEn(evaluationMethod.getEvaluationElementsEn());
            newEvaluationMethod.setExecutionCourse(destinationExecutionCourse);
        }
    }

    private void createNewSite(IPersistentObject persistentObject, IExecutionCourse destinationExecutionCourse) throws ExcepcaoPersistencia {
        ISite site = new Site();
        persistentObject.simpleLockWrite(site);
        site.setExecutionCourse(destinationExecutionCourse);
    }

    private List getShifts(final ISuportePersistente sp, final IExecutionCourse originExecutionCourse)
            throws ExcepcaoPersistencia {
        final ITurnoPersistente persistentShift = sp.getITurnoPersistente();
        return persistentShift.readByExecutionCourseID(originExecutionCourse.getIdInternal());
    }

    private void transferShifts(final IPersistentObject persistentObject, final ISuportePersistente sp,
            final List shifts, final Integer[] shiftIdsToTransfer, final Collection transferedStudents,
            final IExecutionCourse destinationExecutionCourse) throws ExcepcaoPersistencia {
        final ITurnoAlunoPersistente persistentStudentShift = sp.getITurnoAlunoPersistente();

        for (Iterator iterator = shifts.iterator(); iterator.hasNext();) {
            IShift shift = (IShift) iterator.next();
            if (contains(shiftIdsToTransfer, shift.getIdInternal())) {
                persistentObject.simpleLockWrite(shift);
                shift.setDisciplinaExecucao(destinationExecutionCourse);
                
                // This is very ugly code to find in a service. It should not
                // be necessary. The above lock and set should take care of 
                // everything. However, the first shift doesn't get transfered...
                // and I don't know why. This is the workaround we found unit 
                // the mystory can be resolved.
                try {
                    ((SuportePersistenteOJB) sp).currentBroker().store(shift);
                } catch (Exception ex) {
                    throw new ExcepcaoPersistencia("Failed to store shift", ex);
                }
            }

            for (Iterator studentShiftIterator = shift.getStudentShifts().iterator(); studentShiftIterator
                    .hasNext();) {
                ITurnoAluno studentShift = (ITurnoAluno) studentShiftIterator.next();
                if (!transferedStudents.contains(studentShift.getStudent().getIdInternal())) {
                    persistentStudentShift.delete(studentShift);
                }
            }
        }
    }

    private Collection transferAttends(final IPersistentObject persistentObject,
            final Integer[] curricularCourseIdsToTransfer, Collection attends,
            final IExecutionCourse destinationExecutionCourse) throws ExcepcaoPersistencia {

        final Set transferedStudents = new HashSet();

        for (Iterator iterator = attends.iterator(); iterator.hasNext();) {
            final IAttends attend = (IAttends) iterator.next();
            if (attend.getEnrolment() != null
                    && contains(curricularCourseIdsToTransfer, attend.getEnrolment()
                            .getCurricularCourse().getIdInternal())) {
                persistentObject.simpleLockWrite(attend);
                attend.setDisciplinaExecucao(destinationExecutionCourse);

                transferedStudents.add(attend.getAluno().getIdInternal());
            }
        }

        return transferedStudents;
    }

    private Collection getCurricularCoursesToTransfer(final Integer[] curricularCourseIdsToTransfer,
            final Collection curricularCourses) {
        return CollectionUtils.select(curricularCourses, new Predicate() {
            public boolean evaluate(Object arg0) {
                ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                return contains(curricularCourseIdsToTransfer, curricularCourse.getIdInternal());
            }
        });
    }

    private IExecutionCourse createNewExecutionCourse(IPersistentObject persistentObject, IExecutionCourse originExecutionCourse)
            throws ExcepcaoPersistencia {
        IExecutionCourse destinationExecutionCourse = new ExecutionCourse();
        destinationExecutionCourse.setAssociatedCurricularCourses(new ArrayList());
        destinationExecutionCourse.setAssociatedEvaluations(new ArrayList());
        destinationExecutionCourse.setAssociatedExams(new ArrayList());
        destinationExecutionCourse.setAttendingStudents(new ArrayList());
        destinationExecutionCourse.setAttends(new ArrayList());
        destinationExecutionCourse.setComment("");
        destinationExecutionCourse.setExecutionCourseProperties(new ArrayList());
        destinationExecutionCourse.setExecutionPeriod(originExecutionCourse.getExecutionPeriod());
        destinationExecutionCourse.setLabHours(originExecutionCourse.getLabHours());
        destinationExecutionCourse.setNome(originExecutionCourse.getNome());
        destinationExecutionCourse.setPraticalHours(originExecutionCourse.getPraticalHours());

        destinationExecutionCourse.setProfessorships(new ArrayList());
        for (int i = 0; i < originExecutionCourse.getProfessorships().size(); i++) {
            IProfessorship professorship = (IProfessorship) originExecutionCourse.getProfessorships().get(i);
            IProfessorship newProfessorship = new Professorship();
            persistentObject.simpleLockWrite(newProfessorship);
            newProfessorship.setExecutionCourse(destinationExecutionCourse);
            newProfessorship.setTeacher(professorship.getTeacher());
            destinationExecutionCourse.getProfessorships().add(newProfessorship);
        }

        destinationExecutionCourse.setResponsibleTeachers(new ArrayList());
        for (int i = 0; i < originExecutionCourse.getResponsibleTeachers().size(); i++) {
            IResponsibleFor responsibleFor = (IResponsibleFor) originExecutionCourse.getResponsibleTeachers().get(i);
            IResponsibleFor newResponsibleFor = new ResponsibleFor();
            persistentObject.simpleLockWrite(newResponsibleFor);
            newResponsibleFor.setExecutionCourse(destinationExecutionCourse);
            newResponsibleFor.setTeacher(responsibleFor.getTeacher());
            destinationExecutionCourse.getResponsibleTeachers().add(newResponsibleFor);
        }

        destinationExecutionCourse.setSigla(getUniqueExecutionCourseCode(
                originExecutionCourse.getNome(), originExecutionCourse.getExecutionPeriod(),
                originExecutionCourse.getSigla()));
        destinationExecutionCourse.setTheoPratHours(originExecutionCourse.getTheoPratHours());
        destinationExecutionCourse.setTheoreticalHours(originExecutionCourse.getTheoreticalHours());
        return destinationExecutionCourse;
    }

    private String getUniqueExecutionCourseCode(final String executionCourseName,
            final IExecutionPeriod executionPeriod, final String originalExecutionCourseCode)
            throws ExcepcaoPersistencia {
        Set executionCourseCodes = getExecutionCourseCodes(executionPeriod);

        StringBuffer executionCourseCode = new StringBuffer(originalExecutionCourseCode);
        executionCourseCode.append("-1");
        int startVariablePartIndex = executionCourseCode.length() - 1;
        String destinationExecutionCourseCode = executionCourseCode.toString();
        for (int i = 1; executionCourseCodes.contains(destinationExecutionCourseCode); ++i) {
            executionCourseCode.replace(startVariablePartIndex, executionCourseCode.length(), "");
            executionCourseCode.append(i);
            destinationExecutionCourseCode = executionCourseCode.toString();
        }

        return destinationExecutionCourseCode;
    }

    private Set getExecutionCourseCodes(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

        List executionCourses = persistentExecutionCourse.readByExecutionPeriod(executionPeriod);
        return new HashSet(CollectionUtils.collect(executionCourses, new Transformer() {
            public Object transform(Object arg0) {
                IExecutionCourse executionCourse = (IExecutionCourse) arg0;
                return executionCourse.getSigla().toUpperCase();
            }
        }));
    }

    boolean contains(Integer[] integerArray, Integer integer) {
        for (int i = 0; i < integerArray.length; i++) {
            if (integer.equals(integerArray[i])) {
                return true;
            }
        }

        return false;
    }

}