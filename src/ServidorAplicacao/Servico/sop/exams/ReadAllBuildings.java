/*
 * CreateExamNew.java
 *
 * Created on 2003/10/28
 */

package ServidorAplicacao.Servico.sop.exams;

/**
 * Service CreateExamNew
 * 
 * @author Ana e Ricardo
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadAllBuildings implements IServico {

    private static ReadAllBuildings _servico = new ReadAllBuildings();

    /**
     * The singleton access method of this class.
     */
    public static ReadAllBuildings getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadAllBuildings() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadAllBuildings";
    }

    public List run() throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ISalaPersistente salaDAO = sp.getISalaPersistente();

            List buildings = salaDAO.readAllBuildings();
            List buildingNames = new ArrayList();
            Iterator iter = buildings.iterator();
            while (iter.hasNext()) {
                ISala room = (ISala) iter.next();
                if (!buildingNames.contains(room.getEdificio())) {
                    buildingNames.add(room.getEdificio());
                }
            }

            return buildingNames;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }
    }
}