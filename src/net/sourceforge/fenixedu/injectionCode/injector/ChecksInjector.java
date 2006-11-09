/**
 * 
 */

package net.sourceforge.fenixedu.injectionCode.injector;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;

import org.apache.commons.collections.map.HashedMap;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 8:38:20,24/Nov/2005
 * @version $Id: AccesControlChecksInjector.java,v 1.4 2006/01/16 10:32:14 cfgi
 *          Exp $
 */
public class ChecksInjector {
    private boolean verbose = false;

    private ClassPool classPool = ClassPool.getDefault();

    public ChecksInjector(boolean verbose, Collection<String> searchPaths)
	    throws NotFoundException {
	this.verbose = verbose;
	this.classPool.appendSystemPath();
	for (Iterator<String> iter = searchPaths.iterator(); iter.hasNext();) {
	    this.classPool.appendClassPath(iter.next());
	}
    }

    public boolean perform(String className, String targetPath, CodeGenerator generator,
	    Class<? extends Annotation> annotationParameter) throws NotFoundException,
	    ClassNotFoundException, CannotCompileException, IOException, SecurityException,
	    NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
	    InvocationTargetException, NoGetterAvaliableForSpecifiedSlot, InvalidReturnTypeForGetter {

	boolean classChanged = false;
	if (verbose) {
	    System.out.printf("Processing class %s\n", className);
	}

	Class clazz = Class.forName(className);
	CtClass injectedClass = null;

	if (verbose) {
	    System.out.printf("Class %s sucessfully loaded\n", className);
	}
	
	// Verify Methods
	Method[] methods = clazz.getDeclaredMethods();
	for (int methodIndexer = 0; methodIndexer < methods.length; methodIndexer++) {
	    Method currentMethod = methods[methodIndexer];
	    Class[] parametersTypes = currentMethod.getParameterTypes();
	    if (currentMethod.isAnnotationPresent(annotationParameter)) {
		Annotation annotation = currentMethod.getAnnotation(annotationParameter);		
		Map<String, Object> annotationMap = new HashedMap();
		Method[] annotationMethods = annotationParameter.getMethods();		
		for (Method method : annotationMethods) {
		    if(!method.getName().equals("hashCode") && !method.getName().equals("equals")) {
			annotationMap.put(method.getName(), method.invoke(annotation, new Object[] {}));
		    }
		}		
		if (verbose) {
		    System.out
			    .printf(
				    "Access Control Annotation found on Method %s. Proceeding to code injection <----\n",
				    currentMethod.getName());
		}
		injectedClass = this.injectMethod(className, currentMethod, parametersTypes, targetPath,
			generator, annotationMap, annotationParameter);
		classChanged = true;
	    }
	}

	// Verify Constructors
	Constructor[] contructors = clazz.getConstructors();
	for (int contructorIndexer = 0; contructorIndexer < contructors.length; contructorIndexer++) {
	    Constructor currentContructor = contructors[contructorIndexer];
	    Class[] parametersTypes = currentContructor.getParameterTypes();
	    if (currentContructor.isAnnotationPresent(annotationParameter)) {
		Annotation annotation = currentContructor.getAnnotation(annotationParameter);		
		Map<String, Object> annotationMap = new HashedMap();
		Method[] annotationMethods = annotationParameter.getMethods();
		for (Method method : annotationMethods) {
		    if(!method.getName().equals("hashCode") && !method.getName().equals("equals")) {
			annotationMap.put(method.getName(), method.invoke(annotation, new Object[] {}));
		    }
		}		
		if (verbose) {
		    System.out
			    .printf(
				    "Access Control Annotation found on Contructor %s. Proceeding to code injection <----\n",
				    currentContructor.getName());
		}
		injectedClass = this.injectConstructor(className, currentContructor, parametersTypes, targetPath,
			generator, annotationMap, annotationParameter);
		classChanged = true;
	    }
	}

	if (classChanged) {
	    this.writeClass(className, targetPath);
	    if (injectedClass != null) {
		injectedClass.detach();
	    }

	}
	return classChanged;
    }

    private void writeClass(String className, String targetPath) throws NotFoundException,
	    CannotCompileException, IOException {

	CtClass classToInject = this.classPool.get(className);
	classToInject.writeFile(targetPath);
	if (verbose) {
	    System.out.printf("Class %s sucessfully written to %s\n", className, targetPath);
	}

    }

    private CtClass injectMethod(String className, Method method, Class[] parametersTypes, String targetPath,
	    CodeGenerator generator, Map<String, Object> annotationMap,
	    Class<? extends Annotation> checkedAnnotation) throws NotFoundException,
	    CannotCompileException, IOException, ClassNotFoundException,
	    NoGetterAvaliableForSpecifiedSlot, InvalidReturnTypeForGetter {

	String codeToInject = generator.getCode(annotationMap);
	
	CtClass classToInject = this.classPool.get(className);
	CtClass[] wrappedParametersTypes = new CtClass[parametersTypes.length];
	for (int i = 0; i < parametersTypes.length; i++) {
	    wrappedParametersTypes[i] = this.classPool.get(parametersTypes[i].getName());
	}
	CtMethod ctMethod = classToInject.getDeclaredMethod(method.getName(), wrappedParametersTypes);
	if (verbose) {
	    System.out.printf("Ready to inject code to method %s \n", method.getName());
	}
		
	ctMethod.insertBefore(codeToInject);	
	AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) ctMethod.getMethodInfo()
		.getAttribute(AnnotationsAttribute.visibleTag);

	if (annotationsAttribute != null) {
	    javassist.bytecode.annotation.Annotation[] annotations = annotationsAttribute
		    .getAnnotations();

	    List<javassist.bytecode.annotation.Annotation> newAnnotationsCollection = new ArrayList<javassist.bytecode.annotation.Annotation>();
	    for (int i = 0; i < annotations.length; i++) {
		if (!annotations[i].getTypeName().equals(checkedAnnotation.getName())) {
		    newAnnotationsCollection.add(annotations[i]);
		}
	    }
	    javassist.bytecode.annotation.Annotation[] newAnnotationsArray = new javassist.bytecode.annotation.Annotation[newAnnotationsCollection
		    .size()];
	    for (int i = 0; i < newAnnotationsCollection.size(); i++) {
		newAnnotationsArray[i] = newAnnotationsCollection.get(i);
	    }
	    annotationsAttribute.setAnnotations(newAnnotationsArray);
	}

	return classToInject;
    }

    private CtClass injectConstructor(String className, Constructor constructor, Class[] parametersTypes,
	    String targetPath, CodeGenerator generator, Map<String, Object> annotationMap,
	    Class<? extends Annotation> checkedAnnotation) throws NotFoundException,
	    CannotCompileException, IOException, ClassNotFoundException,
	    NoGetterAvaliableForSpecifiedSlot, InvalidReturnTypeForGetter {
	
	String codeToInject = generator.getCode(annotationMap);

	CtClass classToInject = this.classPool.get(className);
	CtClass[] wrappedParametersTypes = new CtClass[parametersTypes.length];
	for (int i = 0; i < parametersTypes.length; i++) {
	    wrappedParametersTypes[i] = this.classPool.get(parametersTypes[i].getName());
	}

	CtConstructor ctConstructor = classToInject.getDeclaredConstructor(wrappedParametersTypes);
	if (verbose) {
	    System.out.printf("Ready to inject code to constructor %s \n", constructor.getName());
	}

	ctConstructor.insertAfter(codeToInject);
	AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) ctConstructor.getMethodInfo()
		.getAttribute(AnnotationsAttribute.visibleTag);

	if (annotationsAttribute != null) {
	    javassist.bytecode.annotation.Annotation[] annotations = annotationsAttribute
		    .getAnnotations();

	    List<javassist.bytecode.annotation.Annotation> newAnnotationsCollection = new ArrayList<javassist.bytecode.annotation.Annotation>();
	    for (int i = 0; i < annotations.length; i++) {
		if (!annotations[i].getTypeName().equals(checkedAnnotation.getName())) {
		    newAnnotationsCollection.add(annotations[i]);
		}
	    }
	    javassist.bytecode.annotation.Annotation[] newAnnotationsArray = new javassist.bytecode.annotation.Annotation[newAnnotationsCollection
		    .size()];
	    for (int i = 0; i < newAnnotationsCollection.size(); i++) {
		newAnnotationsArray[i] = newAnnotationsCollection.get(i);
	    }
	    annotationsAttribute.setAnnotations(newAnnotationsArray);
	}

	return classToInject;
    }
   
    public class NoGetterAvaliableForSpecifiedSlot extends Exception {
	private static final long serialVersionUID = -2084214886744889171L;

	public NoGetterAvaliableForSpecifiedSlot(String msg, Throwable cause) {
	    super(msg, cause);
	}
    }

    public class InvalidReturnTypeForGetter extends Exception {
	private static final long serialVersionUID = 9177377177379590539L;

	public InvalidReturnTypeForGetter(String msg) {
	    super(msg);
	}
    }
}
