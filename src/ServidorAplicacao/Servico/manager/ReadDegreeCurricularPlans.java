/*
 * Created on 4/Set/2003, 13:55:41
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.manager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import DataBeans.InfoDegreeCurricularPlanWithDegree;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 4/Set/2003, 13:55:41
 * 
 */
public class ReadDegreeCurricularPlans implements IServico
{
    private static ReadDegreeCurricularPlans service = new ReadDegreeCurricularPlans();

      /**
       * The singleton access method of this class.
       */
      public static ReadDegreeCurricularPlans getService() {
        return service;
      }

      /**
       * The constructor of this class.
       */
      private ReadDegreeCurricularPlans() { }

      /**
       * Service name
       */
      public final String getNome() {
        return "manager.ReadDegreeCurricularPlans";
      }

      /**
       * Executes the service. Returns the current InfoDegreeCurricularPlan.
       */
      public List run() throws FenixServiceException {
        List curricularPlans = new LinkedList();
        List infoCurricularPlans = new LinkedList();
        
        try {
                ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            curricularPlans = sp.getIPersistentDegreeCurricularPlan().readAll();
        } catch (ExcepcaoPersistencia excepcaoPersistencia){
            throw new FenixServiceException(excepcaoPersistencia);
        }
     
        for (Iterator iter= curricularPlans.iterator(); iter.hasNext();)
			{
				IDegreeCurricularPlan curricularPlan= (IDegreeCurricularPlan) iter.next();
				
				//CLONER
				//infoCurricularPlans.add(Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(curricularPlan));
				infoCurricularPlans.add(InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(curricularPlan));
			}  
        return infoCurricularPlans;
      }
}
