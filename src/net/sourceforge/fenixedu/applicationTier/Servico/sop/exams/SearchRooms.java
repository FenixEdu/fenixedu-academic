/*
 * CreateExamNew.java
 *
 * Created on 2003/10/28
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

/**
 * Service CreateExamNew
 * 
 * @author Ana e Ricardo
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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