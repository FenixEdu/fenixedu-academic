/*
 * Created on Nov 10, 2003
 *  
 */
package ServidorAplicacao.Servicos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

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
			List propertiesValues = new ArrayList();
			if (object.getClass().equals(infoType)) {
				for (int i = 0; i < values.length && encontrou; i++) {
					Object object2 = PropertyUtils.getSimpleProperty(objects.get(i), property);
					propertiesValues.add(object2);
					encontrou = Arrays.asList(values).contains(object2);

				}
				if (!encontrou) {
					System.out.println("expected Value Type->" + values[0].getClass().toString());
					System.out.println(
						"object Type Received->"
							+ propertiesValues
								.get(propertiesValues.size() - 1)
								.getClass()
								.toString());

					System.out.println("Wrong Values Returned!");
					System.out.println("Was Expecting: " + Arrays.asList(values));
					System.out.println("But Received: " + propertiesValues);
				}
				return encontrou;

			} else {
				System.out.println(
					"Wrong Class Returned! Was Expecting: "
						+ infoType
						+ " but received: "
						+ object.getClass());
				return false;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
