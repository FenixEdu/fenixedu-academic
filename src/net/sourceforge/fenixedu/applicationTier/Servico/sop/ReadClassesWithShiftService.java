package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ISchoolClassShift;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaTurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class ReadClassesWithShiftService implements IService {

    public Object run(InfoShift infoShift) throws ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        ITurmaTurnoPersistente classShiftDAO = sp.getITurmaTurnoPersistente();

        IShift shift = Cloner.copyInfoShift2Shift(infoShift);

        List shiftClasses = classShiftDAO.readClassesWithShift(shift);

        Iterator iterator = shiftClasses.iterator();

        List infoClasses = new ArrayList();
        while (iterator.hasNext()) {
            ISchoolClassShift element = (ISchoolClassShift) iterator.next();
            infoClasses.add(Cloner.copyClass2InfoClass(element.getTurma()));
        }
        return infoClasses;
    }
}