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
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import net.sourceforge.fenixedu.domain.accessControl.Group;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 8:38:20,24/Nov/2005
 * @version $Id: AccesControlChecksInjector.java,v 1.4 2006/01/16 10:32:14 cfgi
 *          Exp $
 */
public class AccesControlChecksInjector {
    private boolean verbose = false;

    private ClassPool classPool = ClassPool.getDefault();

    public AccesControlChecksInjector(boolean verbose, Collection<String> searchPaths)
            throws NotFoundException {
        this.verbose = verbose;
        this.classPool.appendSystemPath();
        for (Iterator<String> iter = searchPaths.iterator(); iter.hasNext();) {
            this.classPool.appendClassPath(iter.next());
        }
    }

    public boolean perform(String className, String targetPath, AccessControlCodeGenerator generator,
            Class<? extends Annotation> checkedAnnotation) throws NotFoundException,
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
        Method[] methods = clazz.getDeclaredMethods();
        for (int methodIndexer = 0; methodIndexer < methods.length; methodIndexer++) {
            Method currentMethod = methods[methodIndexer];
            Class[] parametersTypes = currentMethod.getParameterTypes();
            if (currentMethod.isAnnotationPresent(checkedAnnotation)) {
                Annotation checkTag = currentMethod.getAnnotation(checkedAnnotation);
                Method valueMethod = checkedAnnotation.getMethod("value", new Class[] {});
                String value = (String) valueMethod.invoke(checkTag, new Object[] {});

                if (verbose) {
                    System.out
                            .printf(
                                    "Access Control Annotation found on Method %s. Proceeding to code injection <----\n",
                                    currentMethod.getName());
                }
                injectedClass = this.inject(className, currentMethod, parametersTypes, targetPath,
                        generator, value, checkedAnnotation);
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

    private CtClass inject(String className, Method method, Class[] parametersTypes, String targetPath,
            AccessControlCodeGenerator generator, String value,
            Class<? extends Annotation> checkedAnnotation) throws NotFoundException,
            CannotCompileException, IOException, ClassNotFoundException,
            NoGetterAvaliableForSpecifiedSlot, InvalidReturnTypeForGetter {
//        char firstChar = Character.toUpperCase(value.charAt(0));
//        StringBuilder getterNameBuffer = new StringBuilder().append("get").append(firstChar).append(
//                value.substring(1));
        //String getterName = getterNameBuffer.toString();
        //this.verifyGetterPresence(getterName, className);
        String codeToInject = generator.getCode(value);

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

    private void verifyGetterPresence(String getterName, String className)
            throws ClassNotFoundException, NoGetterAvaliableForSpecifiedSlot, InvalidReturnTypeForGetter {
        Class clazz = Class.forName(className);
        try {
            Method m = clazz.getMethod(getterName, new Class[] {});
            Class returnType = m.getReturnType();
            if (!Group.class.isAssignableFrom(returnType)) {
                throw new InvalidReturnTypeForGetter(m.getName() + " must return an instance of "
                        + Group.class.getName() + " and its returning an instance of "
                        + returnType.getClass().getName());
            }
        } catch (SecurityException e) {
            throw new NoGetterAvaliableForSpecifiedSlot("Could not access method " + getterName
                    + " in class " + className, e);
        } catch (NoSuchMethodException e) {
            throw new NoGetterAvaliableForSpecifiedSlot("Could not access method " + getterName
                    + " in class " + className, e);
        }
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
