/*
 * Created on 28/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.posgrad;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Branch;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class MigrateAreasCientificas2FenixBranch {


	PersistenceBroker broker = null;
	
	
	public MigrateAreasCientificas2FenixBranch() {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}


	public static void main(String args[]) throws Exception{
		MigrateAreasCientificas2FenixBranch migrateAreasCientificas2FenixBrach = new MigrateAreasCientificas2FenixBranch();
		
//		migrateAreasCientificas2FenixBrach.broker.beginTransaction();
//		migrateAreasCientificas2FenixBrach.broker.clearCache();
		migrateAreasCientificas2FenixBrach.migratePosgradAreaCientifica2FenixBranch();
		
//		migrateAreasCientificas2FenixBrach.broker.commitTransaction();
	}

	private void migratePosgradAreaCientifica2FenixBranch() throws Exception{
		IBranch branch2Write = null;
		Posgrad_area_cientifica areaCientifica = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
		DegreeCurricularPlan degreeCurricularPlan = null;
		try {
			System.out.print("Reading PosGrad Areas Cientificas ...");
			
			broker.beginTransaction();
			broker.clearCache();

			List areasCientificasPG = getAreasCientificas();
			System.out.println("  Done !");

			broker.commitTransaction();
			
			System.out.println("Migrating " + areasCientificasPG.size() + " PosGrad Areas Cientificas to Fenix Branch ...");
			Iterator iterator = areasCientificasPG.iterator();
			while(iterator.hasNext()){
				areaCientifica = (Posgrad_area_cientifica) iterator.next();
				
				broker.beginTransaction();
				broker.clearCache();
				
				
				// Delete unwanted Areas Cientificas
				if (areaCientifica.getNome().equals("DISCIPLINAS DE ESCOLHA LIVRE") ||
					areaCientifica.getNome().equals("DISCIPLINAS PROPEDÊUTICAS") ||
					(areaCientifica.getCodigocursomestrado() == 15) ||
					(areaCientifica.getCodigocursomestrado() == 31) ||
					(areaCientifica.getCodigocursomestrado() == 50)) {
						broker.commitTransaction();
						continue;
				}
				branch2Write = new Branch();
				branch2Write.setName(areaCientifica.getNome());
				
				// Read Corresponding Posgrad Degree
				criteria = new Criteria();
				criteria.addEqualTo("codigoInterno", new Integer(String.valueOf(areaCientifica.getCodigocursomestrado())));
				query = new QueryByCriteria(Posgrad_curso_mestrado.class,criteria);
				result = (List) broker.getCollectionByQuery(query);
				
				if (result.size() != 1) {
					throw new Exception("Error Reading PosGrad-Curso Mestrado [" + result.size() + "]");
				}
				Posgrad_curso_mestrado posgrad_curso_mestrado = (Posgrad_curso_mestrado) result.get(0);
				
				
				// Read Fenix Degree
				
				criteria = new Criteria();
				criteria.addEqualTo("name", posgrad_curso_mestrado.getNomemestrado());
				criteria.addEqualTo("tipoCurso", new TipoCurso(TipoCurso.MESTRADO));
				query = new QueryByCriteria(Curso.class,criteria);
				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Fenix Degree [" + result.size() + "]");
				}
				
				Curso degree = (Curso) result.get(0); 
				
				// Read Fenix Degree Curricular Plan
				
				criteria = new Criteria();
				criteria.addEqualTo("degreeKey", degree.getIdInternal());
				query = new QueryByCriteria(DegreeCurricularPlan.class,criteria);
				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Fenix Degree Curricular Plan");
				}
				
				degreeCurricularPlan = (DegreeCurricularPlan) result.get(0); 
								
				branch2Write.setDegreeCurricularPlan(degreeCurricularPlan);					

				int numOfChars = 1;
				
				// Check if Branch Exists
			
				boolean writableBranch = false;
				while(writableBranch == false){

					// Check if Branch Exists				
									
					criteria = new Criteria();
					branch2Write.setCode(NameUtils.generateCode(areaCientifica.getNome(), ++numOfChars));
					criteria.addEqualTo("code", branch2Write.getCode());
					criteria.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
					query = new QueryByCriteria(Branch.class,criteria);
					result = (List) broker.getCollectionByQuery(query);

					if (result.size() == 0)
						writableBranch = true;
				}


				if (areaCientifica.getCodigocursomestrado() == 14) {
					createTransportationBranch((Branch) branch2Write, areaCientifica, broker);					
				} else {
					broker.store(branch2Write);
				
					areaCientifica.setCodigoInternoRamo(branch2Write.getIdInternal());
				}
				broker.store(areaCientifica);
				broker.commitTransaction();
			}
			System.out.println("  Done !");

		} catch (Exception e){
			System.out.println();
			throw new Exception("Error Migrating Area Cientifica " + areaCientifica.getNome() + "\nCurso: " +
								degreeCurricularPlan.getDegree().getNome(), e);
			
		}
	}

	private List getAreasCientificas() throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("anoLectivo", "2002/2003");
		QueryByCriteria query = new QueryByCriteria(Posgrad_area_cientifica.class, criteria);
		return (List) broker.getCollectionByQuery(query);
	}
	
	private void createTransportationBranch(Branch branch2Write, Posgrad_area_cientifica posgrad_area_cientifica, PersistenceBroker broker) throws IllegalAccessException, InvocationTargetException {
		Branch branchAux = new Branch();
		BeanUtils.copyProperties(branchAux, branch2Write);
		
		branchAux.setIdInternal(null);
		branchAux.setName(branch2Write.getName() + " (Perfil A)");
		branchAux.setCode(branch2Write.getCode() + " (A)");
		broker.store(branchAux);
		posgrad_area_cientifica.setCodigoInternoRamo(branchAux.getIdInternal());
		
		
		branchAux = new Branch();
		BeanUtils.copyProperties(branchAux, branch2Write);
		
		branchAux.setIdInternal(null);
		branchAux.setName(branch2Write.getName() + " (Perfil B)");
		branchAux.setCode(branch2Write.getCode() + " (B)");
		broker.store(branchAux);
		
		branchAux = new Branch();
		BeanUtils.copyProperties(branchAux, branch2Write);

		branchAux.setIdInternal(null);
		branchAux.setName(branch2Write.getName() + " (Perfil C)");
		branchAux.setCode(branch2Write.getCode() + " (C)");
		broker.store(branchAux);
		
	}
}
