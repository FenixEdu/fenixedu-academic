package ServidorAplicacao.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ISchoolClassShift;
import Dominio.IShift;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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