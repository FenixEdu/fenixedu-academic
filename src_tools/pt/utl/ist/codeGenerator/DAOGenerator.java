package pt.utl.ist.codeGenerator;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class DAOGenerator {

    private static final Logger logger = Logger.getLogger(DAOGenerator.class.getName());

    private class ClassDescriptor {

        private class MethodDescriptor {
            String methodName;
        }

        final String packageName;

        final Set<String> imports = new HashSet<String>();

        final String clazzName;

        final String extendsClazzName;

        final Set<String> implementsClazzNames = new HashSet<String>();;

        final Set<MethodDescriptor> methodDescriptors = new HashSet<MethodDescriptor>();

        private ClassDescriptor(final String absoluteClazzName, final String absoluteExtendsClassName,
                final String absoluteImplementsClazzNames) {
            packageName = getPackageName(absoluteClazzName);
            clazzName = getSimpleClassName(absoluteClazzName);

            imports.add(absoluteExtendsClassName);
            extendsClazzName = getSimpleClassName(absoluteExtendsClassName);

            final String[] absoluteImplementsClazzNamesArray = absoluteImplementsClazzNames.split(",");
            for (int i = 0; i < absoluteImplementsClazzNamesArray.length; i++) {
                final String absoluteImplementsClazzName = absoluteImplementsClazzNamesArray[i];
                imports.add(absoluteImplementsClazzName);
                implementsClazzNames.add(getSimpleClassName(absoluteImplementsClazzName));
            }
        }

        private String getSimpleClassName(final String completeClassName) {
            final int lastDotIndex = completeClassName.lastIndexOf('.');
            return completeClassName.substring(lastDotIndex + 1);
        }

        private String getPackageName(final String completeClassName) {
            final int lastDotIndex = completeClassName.lastIndexOf('.');
            return completeClassName.substring(0, lastDotIndex);
        }

        public String toString() {
            final StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("package ");
            stringBuilder.append(packageName);
            stringBuilder.append("\n\n");
            for (final String someImport : imports) {
                stringBuilder.append("import ");
                stringBuilder.append(someImport);
                stringBuilder.append(";\n");
            }
            
            
//            stringBuilder.append();
//            stringBuilder.append();
//            stringBuilder.append();
//            stringBuilder.append();
//            stringBuilder.append();

            stringBuilder.append("\n");
            

            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        logger.info("Hello world.");

        final String namePersistenceClaszzToCreate = "net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsPersistenceSupport";
        final String nameClazzToImplement = "net.sourceforge.fenixedu.persistenceTier.ISuportePersistente";

        final String namePersistenceClaszzToReplicate = "net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB";

        try {
            final DAOGenerator generator = new DAOGenerator();
            generator.generatePersistenceSupportClazz(namePersistenceClaszzToCreate,
                    nameClazzToImplement, namePersistenceClaszzToReplicate);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        logger.info("Generation Complete.");
    }

    private void generatePersistenceSupportClazz(final String namePersistenceClaszzToCreate,
            final String nameClazzToImplement, final String namePersistenceClaszzToReplicate)
            throws ClassNotFoundException {

        final ClassDescriptor classDescriptor = new ClassDescriptor(namePersistenceClaszzToCreate, "java.lang.Object", nameClazzToImplement);

        final Class persistenceClaszzToReplicate = Class.forName(namePersistenceClaszzToReplicate);
        final Method[] methods = persistenceClaszzToReplicate.getMethods();
        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];

        }

        System.out.println(classDescriptor.toString());
    }

}
