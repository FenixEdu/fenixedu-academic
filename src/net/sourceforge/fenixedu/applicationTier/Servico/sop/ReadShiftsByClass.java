/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 *
 * Created on 2003/08/12
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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