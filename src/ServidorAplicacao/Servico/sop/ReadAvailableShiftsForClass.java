/*
 * 
 * Created on 2003/08/13
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
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ILesson;
import Dominio.ISchoolClass;
import Dominio.IShift;
import Dominio.SchoolClass;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadAvailableShiftsForClass implements IService {

    public Object run(InfoClass infoClass) throws FenixServiceException {

        List infoShifts = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ISchoolClass schoolClass = (ISchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                    infoClass.getIdInternal());

            List shifts = sp.getITurnoPersistente().readAvailableShiftsForClass(schoolClass);

            infoShifts = (List) CollectionUtils.collect(shifts, new Transformer() {
                public Object transform(Object arg0) {
                    IShift shift = (IShift) arg0;
                    InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                    infoShift.setInfoLessons((List) CollectionUtils.collect(
                            shift.getAssociatedLessons(), new Transformer() {
                                public Object transform(Object arg0) {
                                    InfoLesson infoLesson = Cloner.copyILesson2InfoLesson((ILesson) arg0);
                                    //							IShift shift = ((ILesson) arg0).getShift();
                                    //							InfoShift infoShift =
                                    // Cloner.copyShift2InfoShift(shift);
                                    //							infoLesson.setInfoShift(infoShift);
                                    return infoLesson;
                                }
                            }));
                    return infoShift;
                }
            });

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return infoShifts;
    }

}