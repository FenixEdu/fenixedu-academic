package ServidorAplicacao.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadClassesWithShiftService implements IServico {
    private static ReadClassesWithShiftService serviceInstance = new ReadClassesWithShiftService();

    /**
     * The singleton access method of this class.
     */
    public static ReadClassesWithShiftService getService() {
        return serviceInstance;
    }

    /**
     * The actor of this class.
     */
    private ReadClassesWithShiftService() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadClassesWithShiftService";
    }

    public Object run(InfoShift infoShift) {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurmaTurnoPersistente classShiftDAO = sp.getITurmaTurnoPersistente();

            ITurno shift = Cloner.copyInfoShift2Shift(infoShift);

            List shiftClasses = classShiftDAO.readClassesWithShift(shift);

            Iterator iterator = shiftClasses.iterator();

            List infoClasses = new ArrayList();
            while (iterator.hasNext()) {
                ITurmaTurno element = (ITurmaTurno) iterator.next();
                infoClasses.add(Cloner.copyClass2InfoClass(element.getTurma()));
            }
            return infoClasses;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace(System.out);
            return null;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return null;
        }
    }
}