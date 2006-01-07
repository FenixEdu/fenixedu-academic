/**
 * 
 */


package net.sourceforge.fenixedu.accessControl.injector;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.accessControl.FenixAccessControlCallsGenerator;
import net.sourceforge.fenixedu.domain.accessControl.UserGroup;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 8:38:20,24/Nov/2005
 * @version $Id$
 */
public class AccesControlChecksInjector
{
	private boolean verbose = false;

	private ClassPool classPool = ClassPool.getDefault();

	public AccesControlChecksInjector(boolean verbose, Collection<String> searchPaths)
			throws NotFoundException
	{
		this.verbose = verbose;
		this.classPool.appendSystemPath();
		for (Iterator<String> iter = searchPaths.iterator(); iter.hasNext();)
		{
			this.classPool.appendClassPath(iter.next());
		}
	}

	public static void main(String[] args) throws NotFoundException, ClassNotFoundException,
			CannotCompileException, IOException, SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException,
			NoGetterAvaliableForSpecifiedSlot, InvalidReturnTypeForGetter
	{
		Collection<String> searchpaths = new ArrayList<String>();
		AccesControlChecksInjector a = new AccesControlChecksInjector(true, searchpaths);

		searchpaths.add("build/WEB-INF/classes");
		for (int i = 0; i < 100; i++)
		{
			a.perform("net.sourceforge.fenixedu.domain.Advisory", "/tmp", new FenixAccessControlCallsGenerator(), Checked.class);
		}
	}

	public boolean perform(String className, String targetPath, AccessControlCodeGenerator generator,
			Class<? extends Annotation> annotation) throws NotFoundException, ClassNotFoundException,
			CannotCompileException, IOException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			NoGetterAvaliableForSpecifiedSlot, InvalidReturnTypeForGetter
	{
		boolean classChanged = false;
		if (verbose)
		{
			System.out.printf("Processing class %s\n", className);
		}

		Class clazz = Class.forName(className);

		if (verbose)
		{
			System.out.printf("Class %s sucessfully loaded\n", className);
		}

		Method[] methods = clazz.getDeclaredMethods();
		for (int methodIndexer = 0; methodIndexer < methods.length; methodIndexer++)
		{
			Method currentMethod = methods[methodIndexer];
			Class[] parametersTypes = currentMethod.getParameterTypes();
			if (currentMethod.isAnnotationPresent(annotation))
			{
				Annotation checkTag = currentMethod.getAnnotation(annotation);
				Method valueMethod = annotation.getMethod("value", new Class[] {});
				String value = (String) valueMethod.invoke(checkTag, new Object[] {});

				if (verbose)
				{
					System.out.printf("Access Control Annotation found on Method %s. Proceeding to code injection <----\n", currentMethod.getName());
				}
				this.inject(className, currentMethod, parametersTypes, targetPath, generator, value);
				classChanged = true;
			}
		}
		if (classChanged)
		{
			this.writeClass(className, targetPath);
		}
		return classChanged;
	}

	private void writeClass(String className, String targetPath) throws NotFoundException,
			CannotCompileException, IOException
	{

		CtClass classToInject = this.classPool.get(className);
		classToInject.writeFile(targetPath);
		if (verbose)
		{
			System.out.printf("Class %s sucessfully written to %s\n", className, targetPath);
		}

	}

	private void inject(String className, Method method, Class[] parametersTypes, String targetPath,
			AccessControlCodeGenerator generator, String value) throws NotFoundException,
			CannotCompileException, IOException, ClassNotFoundException,
			NoGetterAvaliableForSpecifiedSlot, InvalidReturnTypeForGetter
	{
		char firstChar = Character.toUpperCase(value.charAt(0));
		StringBuffer getterNameBuffer = new StringBuffer().append("get").append(firstChar).append(value.substring(1));
		String getterName = getterNameBuffer.toString();
		this.verifyGetterPresence(getterName, className);
		String codeToInject = generator.getCode(getterName);

		CtClass classToInject = this.classPool.get(className);
		CtClass[] wrappedParametersTypes = new CtClass[parametersTypes.length];
		for (int i = 0; i < parametersTypes.length; i++)
		{
			wrappedParametersTypes[i] = this.classPool.get(parametersTypes[i].getName());
		}
		CtMethod ctMethod = classToInject.getDeclaredMethod(method.getName(), wrappedParametersTypes);
		if (verbose)
		{
			System.out.printf("Ready to inject code to method %s \n", method.getName());
		}
		ctMethod.insertBefore(codeToInject);
		classToInject.detach();
	}

	private void verifyGetterPresence(String getterName, String className)
			throws ClassNotFoundException, NoGetterAvaliableForSpecifiedSlot, InvalidReturnTypeForGetter
	{
		Class clazz = Class.forName(className);
		try
		{
			Method m = clazz.getMethod(getterName, new Class[] {});
			Class returnType = m.getReturnType();
			if (!UserGroup.class.isAssignableFrom(returnType))
			{
				throw new InvalidReturnTypeForGetter(m.getName() + " must return an instance of " + UserGroup.class.getName()+ " and its returning an instance of " + returnType.getClass().getName());
			}
		}
		catch (SecurityException e)
		{
			throw new NoGetterAvaliableForSpecifiedSlot("Could not access method " + getterName
					+ " in class " + className, e);
		}
		catch (NoSuchMethodException e)
		{
			throw new NoGetterAvaliableForSpecifiedSlot("Could not access method " + getterName
					+ " in class " + className, e);
		}
	}

	public class NoGetterAvaliableForSpecifiedSlot extends Exception
	{
		private static final long serialVersionUID = -2084214886744889171L;

		public NoGetterAvaliableForSpecifiedSlot(String msg, Throwable cause)
		{
			super(msg, cause);
		}
	}
	
	public class InvalidReturnTypeForGetter extends Exception
	{
		private static final long serialVersionUID = 9177377177379590539L;

		public InvalidReturnTypeForGetter(String msg)
		{
			super(msg);
		}
	}
}
