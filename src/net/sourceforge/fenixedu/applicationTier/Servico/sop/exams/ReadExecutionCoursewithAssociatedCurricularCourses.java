/*
 * Created on 30/Out/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ana e Ricardo
 *  
 */
public class ReadExecutionCoursewithAssociatedCurricularCourses implements IServico {
    private static ReadExecutionCoursewithAssociatedCurricularCourses service = new ReadExecutionCoursewithAssociatedCurricularCourses();

    /**
     * The singleton access method of this class.
     */
    public static ReadExecutionCoursewithAssociatedCurricularCourses getService() {
        return service;
    }

    /**
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadExecutionCoursewithAssociatedCurricularCourses";
    }

    public InfoExecutionCourse run(Integer oid) throws FenixServiceException {

        InfoExecutionCourse result = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionDegreeDAO = sp.getIPersistentExecutionCourse();
            final IExecutionCourse executionCourse = (IExecutionCourse) executionDegreeDAO.readByOID(
                    ExecutionCourse.class, oid);
            if (executionCourse != null) {
                result = (InfoExecutionCourse) Cloner.get(executionCourse);
            } else {
                throw new FenixServiceException("Unexisting Execution Course");
            }

            result.setAssociatedInfoCurricularCourses(new ArrayList());
            Iterator iterator = executionCourse.getAssociatedCurricularCourses().iterator();
            while (iterator.hasNext()) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

                InfoCurricularCourse infoCurricularCourse = Cloner
                        .copyCurricularCourse2InfoCurricularCourseWithCurricularCourseScopes(curricularCourse);
                CollectionUtils.filter(infoCurricularCourse.getInfoScopes(), new Predicate() {

                    public boolean evaluate(Object arg0) {
                        InfoCurricularCourseScope scope = (InfoCurricularCourseScope) arg0;
                        return scope.getInfoCurricularSemester().getSemester().equals(
                                executionCourse.getExecutionPeriod().getSemester());
                    }
                });
                if (infoCurricularCourse.getInfoScopes().size() > 0) {
                    result.getAssociatedInfoCurricularCourses().add(infoCurricularCourse);
                }
            }
            if (result.getAssociatedInfoCurricularCourses().isEmpty()) {
                result.setAssociatedInfoCurricularCourses(null);
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }

}