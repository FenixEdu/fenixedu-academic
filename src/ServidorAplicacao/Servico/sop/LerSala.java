package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerSala.
 * 
 * @author tfc130
 * @version
 */

import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import Dominio.IRoom;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerSala implements IServico {

    private static LerSala _servico = new LerSala();

    /**
     * The singleton access method of this class.
     */
    public static LerSala getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private LerSala() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "LerSala";
    }

    public Object run(RoomKey keySala) {

        InfoRoom infoSala = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IRoom sala = sp.getISalaPersistente().readByName(keySala.getNomeSala());
            if (sala != null)
                infoSala = new InfoRoom(sala.getNome(), sala.getEdificio(), sala.getPiso(), sala
                        .getTipo(), sala.getCapacidadeNormal(), sala.getCapacidadeExame());
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoSala;
    }

}