///*
// * Created on 31/Jul/2003
// */
//package ServidorAplicacao.Servico.manager;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import DataBeans.InfoDegree;
//import DataBeans.InfoDegreeCurricularPlan;
//import Dominio.Curso;
//import Dominio.ICurso;
//import Dominio.IDegreeCurricularPlan;
//import ServidorAplicacao.IServico;
//import ServidorAplicacao.Servico.exceptions.FenixServiceException;
//import ServidorPersistente.ExcepcaoPersistencia;
//import ServidorPersistente.IPersistentDegreeCurricularPlan;
//import ServidorPersistente.ISuportePersistente;
//import ServidorPersistente.OJB.SuportePersistenteOJB;
//import Util.TipoCurso;
//
///**
// * @author lmac1
// */
//public class InsertDegreeCurricularPlanService implements IServico {
//
//	private static InsertDegreeCurricularPlanService service = new InsertDegreeCurricularPlanService();
//
//	public static InsertDegreeCurricularPlanService getService() {
//		return service;
//	}
//
//	private InsertDegreeCurricularPlanService() {
//	}
//
//	public final String getNome() {
//		return "InsertDegreeCurricularPlanService";
//	}
//	
//
//	public List run(InfoDegreeCurricularPlan infoDegreeCurricularPlan) throws FenixServiceException {
//
////		ICurso degree = null;
////		ICursoPersistente persistentDegree = null;
//
//		IDegreeCurricularPlan degreeCurricularPlan = null;
//		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
//		
//
//		
//		try {
//				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
//				persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
//				List degreeCurricularPlans = persistentDegreeCurricularPlan.readAll();
//
//				String name = infoDegreeCurricularPlan.getName();
//				InfoDegree infoDegree = infoDegreeCurricularPlan.getInfoDegree();
//			
//				List errors = new ArrayList(2);
//				errors.add(null);
//				errors.add(null);
//				int modified = 0; 
//				
//				Iterator iter = degreeCurricularPlans.iterator();
//				while(iter.hasNext()) {
//					IDegreeCurricularPlan degreeCurricularPlanIter = (IDegreeCurricularPlan) iter.next();
//					
//					if(name.compareToIgnoreCase(degreeCurricularPlanIter.getName())==0 && infoDegree.equals(degreeCurricularPlanIter.getDegree() ) ){
//						modified++;
//						errors.set(2, name);
//						errors.set(1, type.toString());
//					}
//				}
//
//				if(modified == 0) {
//					errors = null;
//					degree = new Curso(
//										code,
//										name,
//										infoDegree.getTipoCurso());
//	
//					persistentDegree.lockWrite(degree);
//				}
//				return errors;
//			
//		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
//			throw new FenixServiceException(excepcaoPersistencia);
//		}
//	}
//}