/*
 * SelectRooms.java
 *
 * Created on January 12th, 2003, 01:25
 */

package ServidorAplicacao.Servico.publico;

/**
 * Service SelectRooms.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.IRoom;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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
            salasView.add(Cloner.copyRoom2InfoRoom(sala));
        }

        return salasView;
    }

}