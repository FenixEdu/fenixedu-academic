package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerSalas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoRoom;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            salas = sp.getISalaPersistente().readAll();

            Iterator iterator = salas.iterator();
            infoSalas = new ArrayList();
            while (iterator.hasNext()) {
                ISala elem = (ISala) iterator.next();
                infoSalas.add(new InfoRoom(elem.getNome(), elem.getEdificio(), elem.getPiso(), elem
                        .getTipo(), elem.getCapacidadeNormal(), elem.getCapacidadeExame()));
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoSalas;
    }

}