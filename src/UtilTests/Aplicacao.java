package UtilTests;

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoGroupProperties;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IAttendsSet;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import ServidorAplicacao.Servico.teacher.CreateGroupProperties;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorPersistente.IPersistentAttendsSet;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;


public class Aplicacao {
  public static void main(String[] args) throws Exception {
  	System.out.println("INICIO");
  	System.out.println("sads");
  	System.out.println("sads");
  	System.out.println("sads");
  	System.out.println("sads");
  	System.out.println("sads");
    try {
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        persistentSuport.iniciarTransaccao();
/**    	ReadPrecedencesFromDegreeCurricularPlan service = new ReadPrecedencesFromDegreeCurricularPlan();
    	Map result = (Map) service.run(new Integer(48));
    	persistentSuport.confirmarTransaccao();
        System.out.println(result.toString());**/
/**
        ////AttendsSet teste de mapeamento
        System.out.println("*************AttendsSet teste de mapeamento************");
        IPersistentAttendsSet persistentAttendsSet;
        persistentAttendsSet = persistentSuport.getIPersistentAttendsSet();
        List attendsSetList = persistentAttendsSet.readAttendsSetByName("BASES DE DADOS+PROGRAMAÇÃO COM OBJECTOS");
		
        IAttendsSet attendsSet = null;
        IFrequenta attend = null;
        IStudentGroup studentGroup = null;
        Iterator iterAttendsSet = attendsSetList.iterator();
        Iterator iterAttend;
        Iterator iterStudentGroup;
        while (iterAttendsSet.hasNext()){
        	attendsSet = (IAttendsSet)iterAttendsSet.next();
        	System.out.println(attendsSet.toString());
        	
        	iterAttend = attendsSet.getAttends().iterator();
        	while (iterAttend.hasNext()){
        		attend = (IFrequenta)iterAttend.next();
        		System.out.println(attend.getAluno().getPerson().getIdInternal());
        			
        	}
        }
        
        
        iterAttendsSet = attendsSetList.iterator();
        while (iterAttendsSet.hasNext()){
        	attendsSet = (IAttendsSet)iterAttendsSet.next();
        	System.out.println("AttendSet" + attendsSet.toString());
        	
        	iterStudentGroup = attendsSet.getStudentGroups().iterator();
        	while (iterStudentGroup.hasNext()){
        		studentGroup = (IStudentGroup)iterStudentGroup.next();
        		System.out.println(studentGroup.toString());
        		
        	}
        }
        
        
       /// Attend para AttendsSet
        System.out.println("*************Attend para AttendsSet teste de mapeamento************");
        iterAttendsSet = attendsSetList.iterator();
        while (iterAttendsSet.hasNext()){
        	attendsSet = (IAttendsSet)iterAttendsSet.next();
        	System.out.println("1-AttendSetPartida" + attendsSet.toString());
        	
        	iterAttend = attendsSet.getAttends().iterator();
        	while (iterAttend.hasNext()){
        		attend = (IFrequenta)iterAttend.next();
        		System.out.println("      2-Attend->" + attend.getIdInternal());
        		Iterator iterAttendsSet1 = attend.getAttendsSets().iterator();
        		while (iterAttendsSet1.hasNext()){
        			IAttendsSet attendsSet1 = (IAttendsSet)iterAttendsSet1.next();
                	System.out.println("                 3-AttendSetChegada->" + attendsSet1.toString());
        		}
        	}
        }
        
        
        //// GroupProperties - ExecutionCourse
        
        IPersistentGroupProperties persistentGroupProperties = persistentSuport.getIPersistentGroupProperties();
        List groupPropertiesList = persistentGroupProperties.readGroupPropertiesByName("LEIC");
        
        
        IGroupProperties groupProperties;
        Iterator iterGroupProperties = groupPropertiesList.iterator();
        Iterator iterGroupProperties1;
        while (iterGroupProperties.hasNext()){
        	groupProperties = (IGroupProperties)iterGroupProperties.next();
        	System.out.println("1-GroupPropertiesPartida->" + groupProperties.getName());
        	
        	Iterator iterExecutionCourse = groupProperties.getExecutionCourses().iterator();
        	while (iterExecutionCourse.hasNext()){
        		IExecutionCourse executionCourse = (IExecutionCourse)iterExecutionCourse.next();
        		System.out.println("                 2-ExecutionCourse->" + executionCourse.getNome());
        		
        		iterGroupProperties1 = executionCourse.getGroupProperties().iterator();
        		while (iterGroupProperties1.hasNext()){
        			IGroupProperties groupProperties1 = (IGroupProperties)iterGroupProperties1.next();
                	System.out.println("                 3-GroupPropertiesChegada->" + groupProperties1.getName());
        		}
        	}
        }
        
        
        
        
        InfoGroupProperties infoGroupProperties = Cloner
        .copyIGroupProperties2InfoGroupProperties((IGroupProperties)groupPropertiesList.get(0));
        System.out.println("GroupProperties   " + groupPropertiesList.get(0).toString());
        System.out.println("InfoGroupProperties   " + infoGroupProperties.toString() );
   **/     
        
        
        
        
        CreateGroupProperties cgp = (new CreateGroupProperties());
        InfoGroupProperties igp = new InfoGroupProperties ();
        igp.setGroupMaximumNumber(new Integer(12));
        igp.setMaximumCapacity(new Integer(10));
        igp.setMinimumCapacity(new Integer(8));
        igp.setName("Trabalho de Antenas");
        
        
        cgp.run(new Integer(34021),igp);
        
        
        persistentSuport.confirmarTransaccao();
        persistentSuport.iniciarTransaccao();
        System.out.println("Antes");
        System.out.println((IGroupProperties)((
        		persistentSuport.getIPersistentGroupProperties()
				.readGroupPropertiesByName("Trabalho de Antenas")).get(0)));
        System.out.println("Depois");

        persistentSuport.confirmarTransaccao();
    
        
    } catch (Exception e) {
        throw new FenixActionException(e);
    }

  	System.out.println("FIM");
    System.exit(0);
  }
}
