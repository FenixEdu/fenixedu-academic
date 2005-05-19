package pt.utl.ist.codeGenerator;

import java.io.IOException;

import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsPersistenceSupport;
import pt.utl.ist.codeGenerator.persistenceTier.PersistenceSupportClassDescriptor;

public class DAOGenerator {

    public static void main(String[] args) {
        // Directory where generated code will be written
        final String dir = "src_gen";

        // Class signature details
        final Class clazzToImplement = ISuportePersistente.class;
        final Class extendsClazz = null;

        // Classes to create
        //final String versionedObjectsPersistenceSupportClaszzName = "net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsPersistenceSupport";
        final String delegatePersistenceSupportClaszzName = "net.sourceforge.fenixedu.persistenceTier.delegatedObjects.DelegatePersistenceSupport";

        // Persistence Support Class where the "original" implementation resides
        final Class mainPersistenceSupportToDelegateTo = SuportePersistenteOJB.class;

        try {
            //generatePersistenceSupportClazz(dir, versionedObjectsPersistenceSupportClaszzName, extendsClazz, clazzToImplement);

            final Class secondaryPersistenceSupportClazz = VersionedObjectsPersistenceSupport.class;
            generatePersistenceSupportClazz(dir, delegatePersistenceSupportClaszzName, extendsClazz, clazzToImplement,
                    mainPersistenceSupportToDelegateTo, secondaryPersistenceSupportClazz);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("Generation Complete.");
    }

//    private static void generatePersistenceSupportClazz(final String dir,
//            final String absoluteClazzName, final Class extendsClazz, final Class implementsClazz) throws IOException {
//        final VersionedObjectsPersistenceSupportClassDescriptor classDescriptor =
//                new VersionedObjectsPersistenceSupportClassDescriptor(absoluteClazzName, extendsClazz, implementsClazz);
//        generatePersistenceSupportClazz(dir, classDescriptor);
//    }

    private static void generatePersistenceSupportClazz(final String dir,
            final String absoluteClazzName, final Class extendsClazz, final Class implementsClazz,
            final Class mainPersistenceSupportClazz, final Class secondaryPersistenceSupportClazz) throws IOException {
        final PersistenceSupportClassDescriptor classDescriptor =
                new PersistenceSupportClassDescriptor(absoluteClazzName, extendsClazz, implementsClazz, mainPersistenceSupportClazz, secondaryPersistenceSupportClazz);
        generatePersistenceSupportClazz(dir, classDescriptor);
    }

    private static void generatePersistenceSupportClazz(final String dir, final ClassDescriptor classDescriptor) throws IOException {
        classDescriptor.addUnimplementedMethods();
        classDescriptor.writeToFile(dir);
    }

}