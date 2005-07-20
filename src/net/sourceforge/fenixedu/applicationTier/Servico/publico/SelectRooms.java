/*
 * SelectRooms.java
 *
 * Created on January 12th, 2003, 01:25
 */

package net.sourceforge.fenixedu.applicationTier.Servico.publico;

/**
 * Service SelectRooms.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class SelectRooms implements IServico {
    private static SelectRooms _servico = new SelectRooms();

    /**
     * The singleton access method of this class.
     */
    public static SelectRooms getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private SelectRooms() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "SelectRooms";
    }

    /**
     * The run method of this Service class.
     */
    public Object run(InfoRoom infoRoom) {
        List salas = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            Integer tipo = infoRoom.getTipo() != null ? infoRoom.getTipo().getTipo() : null;

            salas = sp.getISalaPersistente().readSalas(infoRoom.getNome(), infoRoom.getEdificio(),
                    infoRoom.getPiso(), tipo, infoRoom.getCapacidadeNormal(),
                    infoRoom.getCapacidadeExame());
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            return null;
        }

        if (salas == null)
            return new ArrayList();

        Iterator iter = salas.iterator();
        List salasView = new ArrayList();
        IRoom sala;

        while (iter.hasNext()) {
            sala = (IRoom) iter.next();
            salasView.add(InfoRoom.newInfoFromDomain(sala));
        }

        return salasView;
    }

}