package ServidorPersistente.OJB;

/**
 * @author jmota
 *
 */

import java.util.Enumeration;

import org.apache.ojb.broker.query.Criteria;

public class OQLQueryByCriteriaBuilder extends ObjectFenixOJB {

	/**
	 * Constructor for OQLQueryByCriteriaBuilder.
	 */
	public OQLQueryByCriteriaBuilder() {
		super();
	}

	/* public String CreateOQLQuery(
		String queryTarget,
		java.lang.Class targetClass,
		Criteria crit) {

		String test = crit.toString().trim();
		StringTokenizer tokenizer = new StringTokenizer(test, "?");
		String aux = "";
		int i = 1;
		String token = null;
		while (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			aux += token + "$" + i;
			i++;
		}
		String oqlQuery =
			"select "
				+ queryTarget
				+ " from "
				+ targetClass.getName()
				+ " where ";
		oqlQuery += aux;
		oqlQuery = oqlQuery.replaceAll("\\(", "").replaceAll("\\)", "");
		oqlQuery =
			oqlQuery.replaceAll("AND", "and").replaceAll(
				"OR",
				"or").replaceAll(
				"NONE",
				"none");

		return oqlQuery;
	}
     */

	public String CreateOQLQuery(
			String queryTarget,
			java.lang.Class targetClass,
			Criteria crit) {

			
			String aux = "";

			Enumeration enum=crit.getElements();
			Object elem=null;
			while (enum.hasMoreElements()) {
				elem=enum.nextElement();
				aux+=" and "+ elem.toString();
				
			}
			aux=aux.replaceFirst(" and "," ");
			String oqlQuery =
				"select "
					+ queryTarget
					+ " from "
					+ targetClass.getName()
					+ " where ";
			oqlQuery += aux;
		
			return oqlQuery;
		}




/*	public void QueryBinder(OQLQuery query, List list) {

		try {
			ListIterator iter = list.listIterator();
			Object obj = null;
			while (iter.hasNext()) {
				obj = iter.next();
				query.bind(obj);

			}
		} catch (QueryParameterCountInvalidException e) {
		} catch (QueryParameterTypeInvalidException e) {
		}
	}

*/



}
