package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerSalas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

public class LerSalas implements IServico {

    private static LerSalas _servico = new LerSalas();

    /**
     * The singleton access method of this class.
     */
    public static LerSalas getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private LerSalas() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "LerSalas";
    }

    public Object run() {

        List salas = null;
        List infoSalas = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            salas = sp.getISalaPersistente().readAll();

            Iterator iterator = salas.iterator();
            infoSalas = new ArrayList();
            while (iterator.hasNext()) {
                IRoom elem = (IRoom) iterator.next();
                infoSalas.add(new InfoRoom(elem.getNome(), elem.getEdificio(), elem.getPiso(), elem
                        .getTipo(), elem.getCapacidadeNormal(), elem.getCapacidadeExame()));
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoSalas;
    }

}