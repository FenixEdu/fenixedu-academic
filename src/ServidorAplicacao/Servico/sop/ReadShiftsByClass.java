/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 *
 * Created on 2003/08/12
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ILesson;
import Dominio.ISchoolClass;
import Dominio.IShift;
import Dominio.SchoolClass;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadShiftsByClass implements IService {

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        ISchoolClass shcoolClass = (ISchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                infoClass.getIdInternal());

        List shifts = sp.getITurmaTurnoPersistente().readByClass(shcoolClass);

        return CollectionUtils.collect(shifts, new Transformer() {
            public Object transform(Object arg0) {
                IShift shift = (IShift) arg0;
                InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                infoShift.setInfoLessons((List) CollectionUtils.collect(shift.getAssociatedLessons(),
                        new Transformer() {
                            public Object transform(Object arg0) {
                                InfoLesson infoLesson = Cloner.copyILesson2InfoLesson((ILesson) arg0);
                                IShift shift = ((ILesson) arg0).getShift();
                                InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                                infoLesson.setInfoShift(infoShift);

                                return infoLesson;
                            }
                        }));
                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(shift
                        .getDisciplinaExecucao());
                infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
                return infoShift;
            }
        });
    }

}