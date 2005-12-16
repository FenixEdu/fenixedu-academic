package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.utils.ExecutionCourseUtils;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class SeperateExecutionCourse implements IService {

    public void run(final Integer originExecutionCourseOid, final Integer destinationExecutionCourseId,
            final Integer[] shiftIdsToTransfer, final Integer[] curricularCourseIdsToTransfer)
            throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = sp.getIPersistentObject();

        final IExecutionCourse originExecutionCourse = (IExecutionCourse) persistentObject.readByOID(ExecutionCourse.class, originExecutionCourseOid);
        IExecutionCourse destinationExecutionCourse = (IExecutionCourse) persistentObject.readByOID(ExecutionCourse.class, destinationExecutionCourseId);
        if (destinationExecutionCourse == null) {
            destinationExecutionCourse = createNewExecutionCourse(persistentObject, originExecutionCourse);
            destinationExecutionCourse.createSite();
            ExecutionCourseUtils.copyBibliographicReference(originExecutionCourse, destinationExecutionCourse);
            ExecutionCourseUtils.copyEvaluationMethod(originExecutionCourse, destinationExecutionCourse);           
        }

        transferCurricularCourses(originExecutionCourse, destinationExecutionCourse, curricularCourseIdsToTransfer);

        transferAttends(originExecutionCourse, destinationExecutionCourse);

        transferShifts(originExecutionCourse, destinationExecutionCourse, shiftIdsToTransfer);

        fixStudentShiftEnrolements(originExecutionCourse);
        fixStudentShiftEnrolements(destinationExecutionCourse);

        associateGroupings(originExecutionCourse, destinationExecutionCourse);
    }

    private void transferCurricularCourses(final IExecutionCourse originExecutionCourse, final IExecutionCourse destinationExecutionCourse, 
            final Integer[] curricularCourseIdsToTransfer) {
        for (final Integer curricularCourseID : curricularCourseIdsToTransfer) {
            final ICurricularCourse curricularCourse = (ICurricularCourse) findDomainObjectByID(
                    originExecutionCourse.getAssociatedCurricularCourses(), curricularCourseID);
            destinationExecutionCourse.addAssociatedCurricularCourses(curricularCourse);
            originExecutionCourse.removeAssociatedCurricularCourses(curricularCourse);
        }
    }

    private void transferAttends(final IExecutionCourse originExecutionCourse, final IExecutionCourse destinationExecutionCourse) {
        final List<ICurricularCourse> curricularCourses = destinationExecutionCourse.getAssociatedCurricularCourses();
        for (int i = 0; i < originExecutionCourse.getAttends().size(); i++) {
            final IAttends attends = originExecutionCourse.getAttends().get(i);
            final IEnrolment enrolment = attends.getEnrolment();
            if (enrolment != null && curricularCourses.contains(enrolment.getCurricularCourse())) {
                attends.setDisciplinaExecucao(destinationExecutionCourse);
                i--;
            }
        }
    }

    private void transferShifts(final IExecutionCourse originExecutionCourse, final IExecutionCourse destinationExecutionCourse, 
            final Integer[] shiftIdsToTransfer) {
        for (final Integer shiftId : shiftIdsToTransfer) {
            final IShift shift = (IShift) findDomainObjectByID(originExecutionCourse.getAssociatedShifts(), shiftId);
            shift.setDisciplinaExecucao(destinationExecutionCourse);
        }
    }

    private IDomainObject findDomainObjectByID(final List domainObjects, final Integer id) {
        for (final IDomainObject domainObject : (List<IDomainObject>) domainObjects) {
            if (domainObject.getIdInternal().equals(id)) {
                return domainObject;
            }
        }
        return null;
    }

    private void fixStudentShiftEnrolements(final IExecutionCourse executionCourse) {
        for (final IShift shift : executionCourse.getAssociatedShifts()) {
            for (int i = 0; i < shift.getStudents().size(); i++) {
                final IStudent student = shift.getStudents().get(i);
                if (!student.attends(executionCourse)) {
                    shift.removeStudents(student);
                }
            }
        }
    }

    private void associateGroupings(final IExecutionCourse originExecutionCourse, final IExecutionCourse destinationExecutionCourse) {
        for (final IGrouping grouping : originExecutionCourse.getGroupings()) {
            for (final IStudentGroup studentGroup : grouping.getStudentGroups()) {
                studentGroup.getAttends().clear();
                studentGroup.delete();
            }
            grouping.delete();
        }
    }

    private IExecutionCourse createNewExecutionCourse(IPersistentObject persistentObject,
            IExecutionCourse originExecutionCourse) throws ExcepcaoPersistencia {
        IExecutionCourse destinationExecutionCourse = DomainFactory.makeExecutionCourse();
        destinationExecutionCourse.setComment("");
        destinationExecutionCourse.setExecutionPeriod(originExecutionCourse.getExecutionPeriod());
        destinationExecutionCourse.setLabHours(originExecutionCourse.getLabHours());
        destinationExecutionCourse.setNome(originExecutionCourse.getNome());
        destinationExecutionCourse.setPraticalHours(originExecutionCourse.getPraticalHours());
        destinationExecutionCourse.setSigla(originExecutionCourse.getSigla() + System.currentTimeMillis());

        for (int i = 0; i < originExecutionCourse.getProfessorships().size(); i++) {
            IProfessorship professorship = originExecutionCourse.getProfessorships().get(i);
            IProfessorship newProfessorship = DomainFactory.makeProfessorship();
            newProfessorship.setExecutionCourse(destinationExecutionCourse);
            newProfessorship.setTeacher(professorship.getTeacher());
            newProfessorship.setResponsibleFor(professorship.getResponsibleFor());
            destinationExecutionCourse.getProfessorships().add(newProfessorship);
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
        List executionCourses = executionPeriod.getAssociatedExecutionCourses();
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
