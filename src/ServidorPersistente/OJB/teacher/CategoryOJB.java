/*
 * Created on 11/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorPersistente.OJB.teacher;

import java.util.ArrayList;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.teacher.Category;
import Dominio.teacher.ICategory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.teacher.IPersistentCategory;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class CategoryOJB
	extends ObjectFenixOJB
	implements IPersistentCategory {

	/**
	 *  
	 */
	public CategoryOJB() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.IPersistentCategory#readAll()
	 */
	public List readAll() throws ExcepcaoPersistencia {
		
		List categories = new ArrayList();
		Criteria criteria = new Criteria();
		categories = queryList(Category.class, criteria);
		
		return categories;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.IPersistentCategory#readCategoryByCode(java.lang.String)
	 */
	public ICategory readCategoryByCode(String code)
		throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		return (ICategory) queryObject(Category.class, criteria);
	}

}
