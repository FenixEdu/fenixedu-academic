/*
 * Created on 22/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoStudentGroupAttend;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadStudentGroupInformation implements IServico{
	private static ReadStudentGroupInformation _servico = new ReadStudentGroupInformation();
		/**
		 * The singleton access method of this class.
		 **/
		public static ReadStudentGroupInformation getService() {
			return _servico;
		}

		/**
		 * The actor of this class.
		 **/
		private ReadStudentGroupInformation() {
		}

		/**
		 * Returns service name */
		public final String getNome() {
			return "ReadStudentGroupInformation";
		}


		public static List insertWithOrder(List listOfListsToInsertInto,ArrayList listToInsert)
		{
			int size = listOfListsToInsertInto.size();
			List finalList = null;
			int lastNumber;
			if(size==0)
			{
				listOfListsToInsertInto.add(listToInsert);
				return listOfListsToInsertInto;
			}
			if(size==1)
			{
				lastNumber =((Integer) ((List)listOfListsToInsertInto.get(0)).get(0)).intValue(); 
				if(lastNumber<((Integer)listToInsert.get(0)).intValue())
				{
					listOfListsToInsertInto.add(listToInsert);
					return listOfListsToInsertInto;
				}
				else
				{	
					listOfListsToInsertInto.add(0,listToInsert);
					return listOfListsToInsertInto;
				}
			}
			else
				
			lastNumber =((Integer) ((List)listOfListsToInsertInto.get(size-1)).get(0)).intValue(); 
			
				
				if(lastNumber<((Integer)listToInsert.get(0)).intValue())
				{
				
					listOfListsToInsertInto.add(listToInsert);
					 return listOfListsToInsertInto;
				}
				else
				{

					finalList = insertWithOrder(listOfListsToInsertInto.subList(0,size-2),listToInsert); 
					finalList.add(listOfListsToInsertInto.get(size-1));
					return finalList;
				}
				
				
					
		}
		
		public Object run(Integer groupNumber,Integer shiftCode, Integer groupPropertiesCode) {
			
			List studentGroupAttendInformationList = null;
			try 
			{
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				
				IGroupProperties groupProperties =(IGroupProperties) sp.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode), false);
				ITurno shift = (ITurno)sp.getITurnoPersistente().readByOId(new Turno(shiftCode),false);
				 
				IStudentGroup studentGroup = sp.getIPersistentStudentGroup().readStudentGroupByGroupPropertiesAndGroupNumberAndShift(groupProperties,groupNumber,shift);
				List studentGroupAttendList = sp.getIPersistentStudentGroupAttend().readAllByStudentGroup(studentGroup);
				
				studentGroupAttendInformationList = new ArrayList(studentGroupAttendList.size());
				Iterator iter = studentGroupAttendList.iterator();
				ArrayList aux = new ArrayList(3);
				InfoStudentGroupAttend infoStudentGroupAttend = null;
				int size = 0;
				while(iter.hasNext())
				{
					aux = new ArrayList(3);
					infoStudentGroupAttend = Cloner.copyIStudentGroupAttend2InfoStudentGroupAttend((IStudentGroupAttend) iter.next());
					aux.add(infoStudentGroupAttend.getInfoAttend().getAluno().getNumber());
					aux.add(infoStudentGroupAttend.getInfoAttend().getAluno().getInfoPerson().getNome());
					aux.add(infoStudentGroupAttend.getInfoAttend().getAluno().getInfoPerson().getEmail());
					studentGroupAttendInformationList = insertWithOrder(studentGroupAttendInformationList,aux);
					
				}
			
				
			} catch (ExcepcaoPersistencia ex) {
					ex.printStackTrace();
			}
			return studentGroupAttendInformationList;
		}

}
