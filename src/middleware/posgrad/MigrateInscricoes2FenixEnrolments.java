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

import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class MigrateInscricoes2FenixEnrolments {


	PersistenceBroker broker = null;
	
	
	public MigrateInscricoes2FenixEnrolments() {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}


	public static void main(String args[]) throws Exception{
		MigrateInscricoes2FenixEnrolments migrateAreasCientificas2FenixBrach = new MigrateInscricoes2FenixEnrolments();
		
		migrateAreasCientificas2FenixBrach.broker.beginTransaction();
		migrateAreasCientificas2FenixBrach.broker.clearCache();
		migrateAreasCientificas2FenixBrach.migratePosgradInscricoes2FenixEnrolment();
		
		migrateAreasCientificas2FenixBrach.broker.commitTransaction();
	}

	private void migratePosgradInscricoes2FenixEnrolment() throws Exception{
		IBranch branch2Write = null;
		Posgrad_disc_area_aluno inscricao = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
		QueryByCriteria queryByCriteria = null;
		DegreeCurricularPlan degreeCurricularPlan = null;
		int enrolmentsWritten = 0;
		int evaluationsWritten = 0;
		
		try {
			System.out.print("Reading PosGrad Inscricoes ...");
			List inscricoes = getInscricoes();
			System.out.println("  Done !");
			
			System.out.println("Migrating " + inscricoes.size() + " Inscricoes to Fenix Enrolment ...");
			Iterator iterator = inscricoes.iterator();
			while(iterator.hasNext()){
				 inscricao = (Posgrad_disc_area_aluno) iterator.next();
				
				// Read Disciplina Area
				
				criteria = new Criteria();
				criteria.addEqualTo("codigointerno", new Integer(String.valueOf(inscricao.getCodigoareadisciplina())));
				query = new QueryByCriteria(Posgrad_disc_area.class,criteria);
				
				result = (List) broker.getCollectionByQuery(query);		
				
				if (result.size() != 1) {
					throw new Exception("Error Reading Disciplina Area");
				}
								
				Posgrad_disc_area discArea = (Posgrad_disc_area) result.get(0);
				
				// Read Disciplina

				criteria = new Criteria();
				criteria.addEqualTo("codigointerno", new Integer(String.valueOf(discArea.getCodigodisciplina())));
				query = new QueryByCriteria(Posgrad_disciplina.class,criteria);
				
				result = (List) broker.getCollectionByQuery(query);		
				
				if (result.size() != 1) {
					throw new Exception("Error Reading Disciplina ");
				}

				
				// Read Area Cientifica
				
				
				// Read Curricular Course
				
				
				// Read Branch
				
				
				// Read Curricular Course Scope
				
				
				// Create Enrolment
				
				// Create Evaluation If Needed 
				
			}
			System.out.println("  Done !");

		} catch (Exception e){
			System.out.println();
			throw new Exception("Error Migrating Inscricao " + inscricao.getCodigointerno() , e);
		}
	}

	private List getInscricoes() throws Exception {
		Criteria criteria = new Criteria();
		QueryByCriteria query = new QueryByCriteria(Posgrad_disc_area_aluno.class, criteria);
		return (List) broker.getCollectionByQuery(query);
	}
	
}
