/*
 * Created on 30/Jun/2003
 *
 * 
 */
package ServidorAplicacao.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IExecutionCourse;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

            ITurno shift = (ITurno) shiftDAO.readByOID(Turno.class, shiftOID);

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
                ITurma classImpl = (ITurma) iter.next();
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
    private boolean containsScope(List scopes, ITurma classImpl) {
        for (int i = 0; i < scopes.size(); i++) {
            ICurricularCourseScope scope = (ICurricularCourseScope) scopes.get(i);

            if (scope.getCurricularCourse().getDegreeCurricularPlan().equals(
                    classImpl.getExecutionDegree().getCurricularPlan())
                    && scope.getCurricularSemester().getCurricularYear().getYear().equals(
                            classImpl.getAnoCurricular()))
                return true;
        }

        return false;
    }

}