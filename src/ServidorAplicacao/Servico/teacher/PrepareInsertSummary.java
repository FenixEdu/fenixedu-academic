/*
 * Created on 7/Abr/2004
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteSummaries;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ISala;
import Dominio.ISite;
import Dominio.ITeacher;
import Dominio.ITurno;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 */
public class PrepareInsertSummary implements IService {

    public PrepareInsertSummary() {
    }

    public SiteView run(Integer executionCourseId, String userLogged)
            throws FenixServiceException {
        SiteView siteView;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentExecutionCourse persistentExecutionCourse = sp
                    .getIPersistentExecutionCourse();

            IExecutionCourse executionCourse = new ExecutionCourse();
            executionCourse.setIdInternal(executionCourseId);
            executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOId(executionCourse, false);
            if (executionCourse == null) { throw new FenixServiceException(
                    "no.executionCourse"); }

            IPersistentSite persistentSite = sp.getIPersistentSite();
            ISite site = persistentSite.readByExecutionCourse(executionCourse);
            if (site == null) { throw new FenixServiceException("no.site"); }

            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            List shifts = persistentShift
                    .readByExecutionCourse(executionCourse);
            List infoShifts = new ArrayList();
            if (shifts != null && shifts.size() > 0) {
                infoShifts = (List) CollectionUtils.collect(shifts,
                        new Transformer() {

                            public Object transform(Object arg0) {
                                ITurno turno = (ITurno) arg0;
                                return Cloner.copyShift2InfoShift(turno);
                            }
                        });
            }

            IPersistentProfessorship persistentProfessorship = sp
                    .getIPersistentProfessorship();
            List professorships = persistentProfessorship
                    .readByExecutionCourse(executionCourse);
            List infoProfessorships = new ArrayList();
            if (professorships != null && professorships.size() > 0) {
                infoProfessorships = (List) CollectionUtils.collect(
                        professorships, new Transformer() {

                            public Object transform(Object arg0) {
                                IProfessorship professorship = (IProfessorship) arg0;
                                return Cloner
                                        .copyIProfessorship2InfoProfessorship(professorship);
                            }
                        });
            }

            ISalaPersistente persistentRoom = sp.getISalaPersistente();
            List rooms = persistentRoom.readAll();
            List infoRooms = new ArrayList();
            if (rooms != null && rooms.size() > 0) {
                infoRooms = (List) CollectionUtils.collect(rooms,
                        new Transformer() {

                            public Object transform(Object arg0) {
                                ISala room = (ISala) arg0;
                                return Cloner.copyRoom2InfoRoom(room);
                            }
                        });
            }
            Collections.sort(infoRooms, new BeanComparator("nome"));

            //teacher logged
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher
                    .readTeacherByUsername(userLogged);
            Integer professorshipSelect = null;
            if (teacher != null) {
                IProfessorship professorship = persistentProfessorship
                        .readByTeacherAndExecutionCourse(teacher,
                                executionCourse);
                if (professorship != null) {
                    professorshipSelect = professorship.getIdInternal();
                }
            }

            InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
            bodyComponent.setExecutionCourse((InfoExecutionCourse) Cloner
                    .get(executionCourse));
            bodyComponent.setInfoShifts(infoShifts);
            bodyComponent.setInfoProfessorships(infoProfessorships);
            bodyComponent.setInfoRooms(infoRooms);
            bodyComponent.setTeacherId(professorshipSelect);
            
            TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                    .getInstance();
            ISiteComponent commonComponent = componentBuilder.getComponent(
                    new InfoSiteCommon(), site, null, null, null);

            siteView = new ExecutionCourseSiteView(commonComponent,
                    bodyComponent);

        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }

        return siteView;
    }
}