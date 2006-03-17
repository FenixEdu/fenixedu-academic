/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadCategories extends Service {

	public List run() throws FenixServiceException, ExcepcaoPersistencia {		
		List categories = (List) persistentObject.readAll(Category.class);

		List result = (List) CollectionUtils.collect(categories, new Transformer() {
			public Object transform(Object input) {
				Category category = (Category) input;
				return InfoCategory.newInfoFromDomain(category);
			}
		});
		return result;
	}
}