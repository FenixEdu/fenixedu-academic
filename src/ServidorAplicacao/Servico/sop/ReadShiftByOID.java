/*
 * Created on 2003/07/30
 * 
 *  
 */
package ServidorAplicacao.Servico.sop;

import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
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
public class ReadShiftByOID implements IServico
{

    private static ReadShiftByOID service = new ReadShiftByOID();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadShiftByOID getService()
    {
        return service;
    }

    /**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadShiftByOID";
    }

    public InfoShift run(Integer oid) throws FenixServiceException
    {

        InfoShift result = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ITurnoPersistente shiftDAO = sp.getITurnoPersistente();
            ITurno shift = (ITurno) shiftDAO.readByOID(Turno.class, oid);

            if (shift != null)
            {
                result = Cloner.copyIShift2InfoShift(shift);
            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}
