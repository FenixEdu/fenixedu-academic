/*
 * Created on Nov 10, 2003
 *  
 */
package ServidorAplicacao.Servicos;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import DataBeans.InfoExamStudentRoom;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class UtilsTestCase {

	/**
	 * @param objects
	 * @param values
	 * @param args
	 * @param property
	 * @param infoType
	 * @return
	 */
	public static boolean readTestList(
		List objects,
		Object[] values,
		String property,
		Class infoType) {
		boolean encontrou = true;

		if ((objects.size() == 0) || (objects.size() != values.length))
			return false;

		if (values.length == 0)
			return true;

		try {
			Object object = objects.get(0);

			if (object.getClass().equals(infoType)) {

				for (int i = 0; i < values.length && encontrou; i++) {
					Object temp = values[i];
					System.out.println(InfoExamStudentRoom.class);
					Object object2 =
						PropertyUtils.getSimpleProperty(
							objects.get(i),
							property);
					encontrou = Arrays.asList(values).contains(object2);

				}
				return encontrou;

			} else
				return false;

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
