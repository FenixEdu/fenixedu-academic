/*
 * Created on 2003/07/30
 * 
 *  
 */
package ServidorAplicacao.Servico.sop;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IShift;
import Dominio.Shift;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadShiftByOID implements IService {

    /**
     *  
     */
    public ReadShiftByOID() {

    }

    public InfoShift run(Integer oid) throws FenixServiceException {

        InfoShift result = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ITurnoPersistente shiftDAO = sp.getITurnoPersistente();
            IShift shift = (IShift) shiftDAO.readByOID(Shift.class, oid);

            if (shift != null) {
                result = (InfoShift) Cloner.get(shift);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }

}