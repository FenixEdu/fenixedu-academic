/*
 * Created on 28/Ago/2003
 *  
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */
public class ReadGroupPropertiesShifts implements IServico
{

    private static ReadGroupPropertiesShifts service = new ReadGroupPropertiesShifts();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadGroupPropertiesShifts getService()
    {
        return service;
    }
    /**
	 * The constructor of this class.
	 */
    private ReadGroupPropertiesShifts()
    {
    }
    /**
	 * The name of the service
	 */
    public final String getNome()
    {
        return "ReadGroupPropertiesShifts";
    }

    /**
	 * Executes the service.
	 */
    public List run(Integer groupPropertiesCode, Integer shiftCode) throws FenixServiceException
    {

        List infoShifts = new ArrayList();
        IGroupProperties groupProperties = null;
        boolean result = false;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            groupProperties =
                (IGroupProperties) sp.getIPersistentGroupProperties().readByOId(
                    new GroupProperties(groupPropertiesCode),
                    false);
            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory =
                GroupEnrolmentStrategyFactory.getInstance();
            IGroupEnrolmentStrategy strategy =
                enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

            List executionCourseShifts =
                sp.getITurnoPersistente().readByExecutionCourse(groupProperties.getExecutionCourse());

            List shifts = strategy.checkShiftsType(groupProperties, executionCourseShifts);
            if (shifts == null || shifts.isEmpty())
            {

            }
            else
            {

                for (int i = 0; i < shifts.size(); i++)
                {
                    ITurno shift = (ITurno) shifts.get(i);
                    result = strategy.checkNumberOfGroups(groupProperties, shift);
                    if (result)
                    {

                        InfoShift infoShift = (InfoShift) Cloner.get(shift);

                        infoShift.setIdInternal(shift.getIdInternal());

                        infoShifts.add(infoShift);
                    }
                }

                if (shiftCode != null)
                {
                    ITurno oldShift =
                        (ITurno) sp.getITurnoPersistente().readByOId(new Turno(shiftCode), false);
                    infoShifts.add(Cloner.get(oldShift));
                }

            }

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

        return infoShifts;
    }

}
