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

import DataBeans.util.Cloner;
import Dominio.IRoom;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class SearchRooms implements IServico {

    private static SearchRooms _servico = new SearchRooms();

    /**
     * The singleton access method of this class.
     */
    public static SearchRooms getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private SearchRooms() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "SearchRooms";
    }

    public List run(String name, String building, Integer floor, Integer type, Integer normal,
            Integer exam) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ISalaPersistente salaDAO = sp.getISalaPersistente();

            List rooms = salaDAO.readSalas(name, building, floor, type, normal, exam);
            List infoRooms = new ArrayList();
            Iterator iter = rooms.iterator();
            while (iter.hasNext()) {
                IRoom room = (IRoom) iter.next();
                infoRooms.add(Cloner.copyRoom2InfoRoom(room));
            }

            return infoRooms;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }
    }
}