/*
 * Created on Oct 27, 2003
 *
 */
package DataBeans.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;

import Dominio.IDomainObject;

/**
 * @author Luis Cruz & João Luz
 *
 */
public class CopyUtils {

	private static ArrayList interfacesNotToCopy = null;

	static {
		interfacesNotToCopy = new ArrayList();
		interfacesNotToCopy.add(Collection.class);
		interfacesNotToCopy.add(IDomainObject.class);
	}

	public static Object copyProperties(
		Object destinationBean,
		Object sourceBean)
		throws
			InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		PropertyDescriptor[] fields = PropertyUtils.getPropertyDescriptors(sourceBean.getClass());
		for (int i = 0; i < fields.length; i++) {
			PropertyDescriptor propertyDescriptor = fields[i];
			Class fieldClass = propertyDescriptor.getPropertyType();
			Class[] interfaces = fieldClass.getInterfaces();
			List interfacesList = Arrays.asList(interfaces);
			
			if (CollectionUtils
				.intersection(interfacesNotToCopy, interfacesList)
				.isEmpty()) {
				String propertyName = propertyDescriptor.getName();
				boolean isReadable = PropertyUtils.isReadable(sourceBean, propertyName);
				if (isReadable) // readable on source
				{
					boolean isWriteable = PropertyUtils.isWriteable(destinationBean, propertyName);
					if (isWriteable) // writeable on destination
					{
						Object value = PropertyUtils.getSimpleProperty(sourceBean, propertyName);
						try
						{
							BeanUtils.copyProperty(destinationBean, propertyName, value);
						} catch (IllegalArgumentException e)
						{
							System.out.println("Property " + propertyName + "class " + sourceBean.getClass().getName());
							System.out.println("is not of the same type that			 in class " + destinationBean.getClass().getName());
						}

					}
				}
			}
		}
		return destinationBean;
	}
}