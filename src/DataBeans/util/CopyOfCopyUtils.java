/*
 * Created on Oct 27, 2003
 *  
 */
package DataBeans.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.FastHashMap;

import DataBeans.InfoPerson;
import Dominio.IDomainObject;
import Dominio.Pessoa;

/**
 * @author Luis Cruz & João Luz
 *  
 */
public class CopyOfCopyUtils
{

	private static ArrayList interfacesNotToCopy = null;
	private static FastHashMap propertyDescriptorsCache = new FastHashMap();
	static {
		interfacesNotToCopy = new ArrayList();
		interfacesNotToCopy.add(Collection.class);
		interfacesNotToCopy.add(IDomainObject.class);
	}

	/**
	 * Copy properties that readable on sourceBean and writeable on
	 * destinationBean only they have the same name and the same type. It
	 * doesn't copy attributes that implement @link IDomainObject or @link
	 * Collection interfaces.
	 * 
	 * <b>Only copy simple properties
	 * </b>
	 * 
	 * @see PropertyUtils
	 * @see BeanUtils
	 * @param destinationBean
	 * @param sourceBean
	 * @return destinationBean
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static void copyProperties(Object destinationBean, Object sourceBean)
		throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		BeanUtils.copyProperties(destinationBean, sourceBean);
//		ClassDescriptor classInfo = (ClassDescriptor) propertyDescriptorsCache.get(sourceBean.getClass().getName());
//		if (classInfo == null)
//		{
//			try
//			{
//				BeanInfo beanInfo = Introspector.getBeanInfo(sourceBean.getClass());
//				/**
//				 * Copiar o nome das propriedades e cachar o nome das
//				 * propriedades
//				 */
//				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//
//				Class[] fieldClasses = new Class[propertyDescriptors.length];
//				String[] fieldNames = new String[propertyDescriptors.length];
//
//				for (int i = 0; i < propertyDescriptors.length; i++)
//				{
//					PropertyDescriptor descriptor = propertyDescriptors[i];
//					fieldClasses[i] = descriptor.getPropertyType();
//					fieldNames[i] = new String(descriptor.getName());
//				}
//				classInfo = new ClassDescriptor(fieldClasses, fieldNames);
//				propertyDescriptorsCache.put(sourceBean.getClass().getName(), classInfo);
//			} catch (IntrospectionException e1)
//			{
//				e1.printStackTrace(System.out);
//				throw new RuntimeException(e1);
//			}
//		}
//		Class[] fieldsClasses = classInfo.getFieldClass();
//		String[] fieldNames = classInfo.getFieldsNames();
//		//		PropertyDescriptor[] fields = beanInfo.getPropertyDescriptors();
//		for (int i = 0; i < fieldNames.length; i++)
//		{
//
//			String propertyName = fieldNames[i];
//			Class fieldClass = fieldsClasses[i];
//			Class[] interfaces = fieldClass.getInterfaces();
//			List interfacesList = Arrays.asList(interfaces);
//
//			if (CollectionUtils.intersection(interfacesNotToCopy, interfacesList).isEmpty())
//			{
//
//				boolean isReadable = PropertyUtils.isReadable(sourceBean, propertyName);
//				if (isReadable) // readable on source
//				{
//					boolean isWriteable = PropertyUtils.isWriteable(destinationBean, propertyName);
//					if (isWriteable) // writeable on destination
//					{
//						Object value = PropertyUtils.getSimpleProperty(sourceBean, propertyName);
//						try
//						{
//							BeanUtils.copyProperty(destinationBean, propertyName, value);
//						} catch (IllegalArgumentException e)
//						{
//							System.out.println("Property " + propertyName + "class " + sourceBean.getClass().getName());
//							System.out.println("is not of the same type that			 in class " + destinationBean.getClass().getName());
//						}
//
//					}
//				}
//
//			}
//		}
	}

}
