/*
 * Created on 14/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadAllExecutionYears implements IService
{

    /**
	 * The constructor of this class.
	 */
    public ReadAllExecutionYears()
    {
    }

    /**
	 * Executes the service. Returns the current collection of infoTeachers.
	 */
    public List run() throws FenixServiceException
    {
        ISuportePersistente sp;
        List allExecutionYears = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            allExecutionYears = sp.getIPersistentExecutionYear().readAllExecutionYear();
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allExecutionYears == null || allExecutionYears.isEmpty())
            return allExecutionYears;

        // build the result of this service
        Iterator iterator = allExecutionYears.iterator();
        List result = new ArrayList(allExecutionYears.size());

        while (iterator.hasNext())
            result.add(Cloner.get((IExecutionYear) iterator.next()));

        return result;
    }
}