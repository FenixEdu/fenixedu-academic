/*
 * Created on 14/Jun/2004
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoPeriod;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IPeriod;
import Dominio.Period;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Ana e Ricardo
 */
public class EditExecutionDegreePeriods implements IServico
{

    private static EditExecutionDegreePeriods service = new EditExecutionDegreePeriods();

    public static EditExecutionDegreePeriods getService()
    {
        return service;
    }

    private EditExecutionDegreePeriods()
    {
    }

    public final String getNome()
    {
        return "EditExecutionDegreePeriods";
    }

    public void run(InfoExecutionDegree infoExecutionDegree) throws FenixServiceException
    {

        ICursoExecucaoPersistente persistentExecutionDegree = null;

        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentExecutionDegree = persistentSuport.getICursoExecucaoPersistente();
            CursoExecucao execDegree = new CursoExecucao();
            execDegree.setIdInternal(infoExecutionDegree.getIdInternal());

            ICursoExecucao oldExecutionDegree = (ICursoExecucao) persistentExecutionDegree.readByOId(
                    execDegree, false);

           	InfoPeriod infoPeriodExamsFirstSemester = infoExecutionDegree.getInfoPeriodExamsFirstSemester();    	
           	IPeriod periodExamsFirstSemester = setCompositePeriod(infoPeriodExamsFirstSemester);        	        	
        	
        	InfoPeriod infoPeriodExamsSecondSemester = infoExecutionDegree.getInfoPeriodExamsSecondSemester();    	
        	IPeriod periodExamsSecondSemester = setCompositePeriod(infoPeriodExamsSecondSemester);
        	
        	InfoPeriod infoPeriodLessonsFirstSemester = infoExecutionDegree.getInfoPeriodLessonsFirstSemester();    	
        	IPeriod periodLessonsFirstSemester = setCompositePeriod(infoPeriodLessonsFirstSemester);
        	
        	InfoPeriod infoPeriodLessonsSecondSemester = infoExecutionDegree.getInfoPeriodLessonsSecondSemester();    	
        	IPeriod periodLessonsSecondSemester = setCompositePeriod(infoPeriodLessonsSecondSemester);    
        	
/*			IPeriod periodLessonsFirstSemester = (IPeriod) periodDAO.readByCalendarAndNextPeriod(
					infoExecutionDegree.getInfoPeriodLessonsFirstSemester().getStartDate(),
					infoExecutionDegree.getInfoPeriodLessonsFirstSemester().getEndDate(), null);
					
			if(periodLessonsFirstSemester == null)
			{ 
				Calendar startDate = infoExecutionDegree.getInfoPeriodLessonsFirstSemester().getStartDate();
				Calendar endDate = infoExecutionDegree.getInfoPeriodLessonsFirstSemester().getEndDate();
				periodLessonsFirstSemester = new Period(startDate, endDate);
				periodDAO.simpleLockWrite(periodLessonsFirstSemester);				
			}
			
			IPeriod periodLessonsSecondSemester = (IPeriod) periodDAO.readByCalendarAndNextPeriod(
					infoExecutionDegree.getInfoPeriodLessonsSecondSemester().getStartDate(),
					infoExecutionDegree.getInfoPeriodLessonsSecondSemester().getEndDate(), null);
			if(periodLessonsSecondSemester == null)
			{ 
				Calendar startDate = infoExecutionDegree.getInfoPeriodLessonsSecondSemester().getStartDate();
				Calendar endDate = infoExecutionDegree.getInfoPeriodLessonsSecondSemester().getEndDate();
				periodLessonsSecondSemester = new Period(startDate, endDate);
				periodDAO.simpleLockWrite(periodLessonsSecondSemester);		
			}
            
			IPeriod periodExamsFirstSemester = (IPeriod) periodDAO.readByCalendarAndNextPeriod(
								infoExecutionDegree.getInfoPeriodExamsFirstSemester().getStartDate(),
								infoExecutionDegree.getInfoPeriodExamsFirstSemester().getEndDate(), null);
			if(periodExamsFirstSemester == null)
			{ 
				Calendar startDate = infoExecutionDegree.getInfoPeriodExamsFirstSemester().getStartDate();
				Calendar endDate = infoExecutionDegree.getInfoPeriodExamsFirstSemester().getEndDate();
				periodExamsFirstSemester = new Period(startDate, endDate);
				periodDAO.simpleLockWrite(periodExamsFirstSemester);				
			}
			
			IPeriod periodExamsSecondSemester = (IPeriod) periodDAO.readByCalendarAndNextPeriod(
								infoExecutionDegree.getInfoPeriodExamsSecondSemester().getStartDate(),
								infoExecutionDegree.getInfoPeriodExamsSecondSemester().getEndDate(), null);
			if(periodExamsSecondSemester == null)
			{ 
				Calendar startDate = infoExecutionDegree.getInfoPeriodExamsSecondSemester().getStartDate();
				Calendar endDate = infoExecutionDegree.getInfoPeriodExamsSecondSemester().getEndDate();
				periodExamsSecondSemester = new Period(startDate, endDate);
				periodDAO.simpleLockWrite(periodExamsSecondSemester);
			}*/

            persistentExecutionDegree.simpleLockWrite(oldExecutionDegree);
            oldExecutionDegree.setPeriodLessonsFirstSemester(periodLessonsFirstSemester);
			oldExecutionDegree.setPeriodLessonsSecondSemester(periodLessonsSecondSemester);
			oldExecutionDegree.setPeriodExamsFirstSemester(periodExamsFirstSemester);
			oldExecutionDegree.setPeriodExamsSecondSemester(periodExamsSecondSemester);
			
        }
        catch (ExistingPersistentException ex)
        {
            throw new ExistingServiceException(ex);
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }        
    
    
    //retorna o primeiro period do executiondegree
    private IPeriod setCompositePeriod(InfoPeriod infoPeriod) throws FenixServiceException
    {
    	try
		{
	    	ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
	        IPersistentPeriod periodDAO = persistentSuport.getIPersistentPeriod();
	    	List infoPeriodList = new ArrayList();
	    		    	
	    	infoPeriodList.add(infoPeriod);
	    	
	    	while(infoPeriod.getNextPeriod() != null)
	    	{
	    		infoPeriodList.add(infoPeriod.getNextPeriod());
	    		infoPeriod = infoPeriod.getNextPeriod();
	    	}
	    	
	    	//inicializacao
	    	int infoPeriodListSize = infoPeriodList.size();
	    	
	    	InfoPeriod infoPeriodNew = (InfoPeriod) infoPeriodList.get(infoPeriodListSize-1);
    		
    		IPeriod period = (IPeriod) periodDAO.readByCalendarAndNextPeriod(
    				infoPeriodNew.getStartDate(),
    				infoPeriodNew.getEndDate(), null);	
	    	
	    	if(period == null)
	    	{
	    		Calendar startDate = infoPeriodNew.getStartDate();
				Calendar endDate = infoPeriodNew.getEndDate();
				period = new Period(startDate, endDate);
				periodDAO.simpleLockWrite(period);					    			
	    	}
	    	
	    	//iteracoes
	    	for(int i = infoPeriodListSize-2 ; i >= 0; i--)
	    	{
	    		Integer keyNextPeriod = period.getIdInternal();
	    		
	    		IPeriod nextPeriod = period;
	    		
	    		infoPeriodNew = (InfoPeriod) infoPeriodList.get(i);
	    		
	    		period = (IPeriod) periodDAO.readByCalendarAndNextPeriod(
	    				infoPeriodNew.getStartDate(),
	    				infoPeriodNew.getEndDate(), keyNextPeriod);
	    		
	    		if(period == null)
	    		{
		    		Calendar startDate = infoPeriodNew.getStartDate();
					Calendar endDate = infoPeriodNew.getEndDate();
					period = new Period(startDate, endDate);
					periodDAO.simpleLockWrite(period);
					period.setNextPeriod(nextPeriod);				
	    		}
	    	}
	    	
	    	return period;
	    		    	
		} catch (ExcepcaoPersistencia excepcaoPersistencia)
	    {
	        throw new FenixServiceException(excepcaoPersistencia);
	    }	    	
    }
    
    
}