/*
 * Created on 13/Nov/2003
 */
package middleware.databaseClean;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Branch;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */
public class correctBranchCodesAndAcronyms {

	public static void main(String[] args) {

		System.out.println("Running DeleteCommonBranchesCodes script");
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.clearCache();
		broker.beginTransaction();

		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Branch.class, criteria);
		List branchList = (List) broker.getCollectionByQuery(query);

		Iterator iterator = branchList.iterator();
		while (iterator.hasNext()) {

			Branch branch = (Branch) iterator.next();

			if (branch.getCode().equals("")) {
				branch.setCode("");
				broker.store(branch);
			}
			else {
				
			}

		}

		broker.commitTransaction();
		broker.close();
	}

}
