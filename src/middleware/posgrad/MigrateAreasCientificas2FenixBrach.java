/*
 * Created on 28/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.posgrad;

import java.util.Iterator;
import java.util.List;

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
public class MigrateAreasCientificas2FenixBrach {


	PersistenceBroker broker = null;
	
	
	public MigrateAreasCientificas2FenixBrach() {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}


	public void main(String args[]) throws Exception{
		MigrateAreasCientificas2FenixBrach migrateAreasCientificas2FenixBrach = new MigrateAreasCientificas2FenixBrach();
		
		broker.beginTransaction();
		broker.clearCache();
		migrateAreasCientificas2FenixBrach.migratePosgradAreaCientifica2FenixBrach();
		
		broker.commitTransaction();
	}

	private void migratePosgradAreaCientifica2FenixBrach() throws Exception{
		IBranch branch2Write = null;
		Posgrad_area_cientifica areaCientifica = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
		QueryByCriteria queryByCriteria = null;
		try {
			System.out.print("Reading PosGrad Areas Cientificas ...");
			List areasCientificasPG = getAreasCientificas();
			System.out.println("  Done !");
			
			System.out.println("Migrating " + areasCientificasPG.size() + " PosGrad Areas Cientificas to Fenix Branch ...");
			Iterator iterator = areasCientificasPG.iterator();
			while(iterator.hasNext()){
				areaCientifica = (Posgrad_area_cientifica) iterator.next();
				
				// Delete unwanted Courses
				if (areaCientifica.getNome().equals("DISCIPLINAS DE ESCOLHA LIVRE") ||
					areaCientifica.getNome().equals("DISCIPLINAS PROPEDÊUTICAS") ||
					(areaCientifica.getCodigocursomestrado() == 15) ||
					(areaCientifica.getCodigocursomestrado() == 31) ||
					(areaCientifica.getCodigocursomestrado() == 50)) {
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
				
				DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) result.get(0); 
								
				branch2Write.setDegreeCurricularPlan(degreeCurricularPlan);					

				int numOfChars = 1;
				
				// Check if Branch Exists
				
				boolean writableBranch = false;
				while(writableBranch == false){

					// Check if Branch Exists				
										
					criteria = new Criteria();
					branch2Write.setCode(NameUtils.generateCode(areaCientifica.getNome(), ++numOfChars));
					criteria.addEqualTo("name", branch2Write.getName());
					criteria.addEqualTo("code", branch2Write.getCode());
					criteria.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
					query = new QueryByCriteria(Branch.class,criteria);
					result = (List) broker.getCollectionByQuery(query);

					if (result.size() == 0)
						writableBranch = true;
				}
				broker.store(branch2Write);
			}
			System.out.println("  Done !");

		} catch (Exception e){
			System.out.println();
			throw new Exception("Error Migrating Area Cientifica " + areaCientifica.getNome() , e);
			
		}
	}

	private List getAreasCientificas() throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("anoLectivo", "2002/2003");
		QueryByCriteria query = new QueryByCriteria(Posgrad_area_cientifica.class, criteria);
		return (List) broker.getCollectionByQuery(query);
	}




}
