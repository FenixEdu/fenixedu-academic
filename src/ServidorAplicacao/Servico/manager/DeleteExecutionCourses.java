/*
 * Created on 30/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISite;
import Dominio.ISummary;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExamExecutionCourse;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1 modified by Fernanda Quitério
 *  
 */

public class DeleteExecutionCourses implements IService {

    // delete a set of execution courses
    public List run(List internalIds) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            IPersistentExamExecutionCourse persistentExamExecutionCourse = sp
                    .getIPersistentExamExecutionCourse();
            IPersistentProfessorship persistentProfessorShip = sp.getIPersistentProfessorship();
            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            IPersistentSite persistentSite = sp.getIPersistentSite();

            Iterator iter = internalIds.iterator();
            Iterator iterator;
            List shifts = null;
            List attends = null;
            List exams = null;
            List professorShips = null;
            List responsibles = null;
            Integer internalId;
            IProfessorship professorShip;
            IResponsibleFor responsibleFor;
            List undeletedExecutionCoursesCodes = new ArrayList();
            ISite site;

            while (iter.hasNext()) {
                internalId = (Integer) iter.next();
                IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                        .readByOID(ExecutionCourse.class, internalId);
                if (executionCourse != null) {
                    shifts = persistentShift.readByExecutionCourse(executionCourse);
                    if (shifts != null && !shifts.isEmpty())
                        undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
                    else {
                        attends = persistentAttend.readByExecutionCourse(executionCourse);
                        if (attends != null && !attends.isEmpty()) {
                            undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
                        } else {
                            exams = persistentExamExecutionCourse.readByExecutionCourse(executionCourse);
                            if (exams != null && !exams.isEmpty()) {
                                undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
                            } else {
                                persistentExecutionCourse.deleteExecutionCourse(executionCourse);
                                professorShips = persistentProfessorShip
                                        .readByExecutionCourse(executionCourse);
                                if (professorShips != null) {
                                    iterator = professorShips.iterator();
                                    while (iterator.hasNext()) {
                                        professorShip = (IProfessorship) iterator.next();

                                        IPersistentSummary persistentSummary = sp.getIPersistentSummary();
                                        List summaryList = persistentSummary.readByTeacher(professorShip.getExecutionCourse(), professorShip.getTeacher());
                                        if (summaryList != null && !summaryList.isEmpty()) {
                                            for (Iterator iteratorSummaries = summaryList.iterator(); iteratorSummaries.hasNext(); ) {
                                                ISummary summary = (ISummary) iteratorSummaries.next();
                                                persistentSummary.simpleLockWrite(summary);
                                                summary.setProfessorship(null);
                                                summary.setKeyProfessorship(null);
                                            }
                                        }


                                        persistentProfessorShip.delete(professorShip);
                                    }
                                }
                                responsibles = persistentResponsibleFor
                                        .readByExecutionCourse(executionCourse);
                                if (responsibles != null) {
                                    iterator = responsibles.iterator();
                                    while (iterator.hasNext()) {
                                        responsibleFor = (IResponsibleFor) iterator.next();
                                        persistentResponsibleFor.delete(responsibleFor);
                                    }
                                }
                                site = persistentSite.readByExecutionCourse(executionCourse);
                                if (site != null) {
                                    persistentSite.delete(site);
                                }
                            }
                        }
                    }
                }
            }
            return undeletedExecutionCoursesCodes;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}