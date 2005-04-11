/*
 * Created on 30/Jun/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 * 30/Jun/2003 fenix-branch ServidorAplicacao.Servico.sop
 *  
 */
public class ReadAvailableClassesForShift implements IService {

    public List run(Integer shiftOID) throws FenixServiceException {

        List infoClasses = null;
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

            IShift shift = (IShift) shiftDAO.readByOID(Shift.class, shiftOID);

            List curricularCourses = shift.getDisciplinaExecucao().getAssociatedCurricularCourses();
            List scopes = new ArrayList();
            for (int i = 0; i < curricularCourses.size(); i++) {
                ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourses.get(i);
                scopes.addAll(curricularCourse.getScopes());
            }

            IExecutionCourse executionCourse = shift.getDisciplinaExecucao();

            ITurmaPersistente classDAO = sp.getITurmaPersistente();
            List classes = classDAO.readByExecutionPeriod(executionCourse.getExecutionPeriod());

            infoClasses = new ArrayList();
            Iterator iter = classes.iterator();
            while (iter.hasNext()) {
                ISchoolClass classImpl = (ISchoolClass) iter.next();
                if (!shift.getAssociatedClasses().contains(classImpl)
                        && containsScope(scopes, classImpl)) {
                    InfoClass infoClass = Cloner.copyClass2InfoClass(classImpl);
                    infoClasses.add(infoClass);
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoClasses;
    }

    /**
     * @param scopes
     * @param classImpl
     * @return
     */
    private boolean containsScope(List scopes, ISchoolClass classImpl) {
        for (int i = 0; i < scopes.size(); i++) {
            ICurricularCourseScope scope = (ICurricularCourseScope) scopes.get(i);

            if (scope.getCurricularCourse().getDegreeCurricularPlan().equals(
                    classImpl.getExecutionDegree().getDegreeCurricularPlan())
                    && scope.getCurricularSemester().getCurricularYear().getYear().equals(
                            classImpl.getAnoCurricular()))
                return true;
        }

        return false;
    }

}