package pt.utl.ist.codeGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.codeGenerator.persistenceTier.PersistenceSupportClassDescriptor;

public class DAOGenerator {

    public static void main(String[] args) {
        final String src_gen = "src_gen";

        final Class clazzToImplement = ISuportePersistente.class;
        final Class extendsClazz = null;

        try {
            final String nameVOPersistenceClaszz = "net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsPersistenceSupport";
            final String voPersistenceSupportClazzContents = generateVersionedObjectsPersistenceSupportClazz(
                    nameVOPersistenceClaszz, extendsClazz, clazzToImplement);
            final String voPersistenceSupportClazzFilename = src_gen + "/"
                    + nameVOPersistenceClaszz.replaceAll("\\.", "/") + ".java";
            write(voPersistenceSupportClazzFilename, voPersistenceSupportClazzContents);

            final String nameDelegatePersistenceClaszz = "net.sourceforge.fenixedu.persistenceTier.versionedObjects.DelegatePersistenceSupport";
            final Class mainPersistenceSupportClazz = SuportePersistenteOJB.class;
            final Class secondaryPersistenceSupportClazz = null;
            final String delegatePersistenceSupportClazzContents = generateVersionedObjectsPersistenceSupportClazz(
                    nameDelegatePersistenceClaszz, extendsClazz, clazzToImplement,
                    mainPersistenceSupportClazz, secondaryPersistenceSupportClazz);
            final String delegatePersistenceSupportClazzFilename = src_gen + "/"
                    + nameDelegatePersistenceClaszz.replaceAll("\\.", "/") + ".java";
            write(delegatePersistenceSupportClazzFilename, delegatePersistenceSupportClazzContents);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("Generation Complete.");
    }

    private static String generateVersionedObjectsPersistenceSupportClazz(
            final String absoluteClazzName, final Class extendsClazz, final Class implementsClazz,
            final Class mainPersistenceSupportClazz, final Class secondaryPersistenceSupportClazz) throws IOException {

        final PersistenceSupportClassDescriptor classDescriptor = new PersistenceSupportClassDescriptor(
                absoluteClazzName, extendsClazz, implementsClazz, mainPersistenceSupportClazz,
                secondaryPersistenceSupportClazz);
        classDescriptor.addUnimplementedMethods();

        return classDescriptor.toString();
    }

    private static String generateVersionedObjectsPersistenceSupportClazz(
            final String absoluteClazzName, final Class extendsClazz, final Class implementsClazz) throws IOException {

        final PersistenceSupportClassDescriptor classDescriptor = new PersistenceSupportClassDescriptor(
                absoluteClazzName, extendsClazz, implementsClazz);
        classDescriptor.addUnimplementedMethods();

        return classDescriptor.toString();
    }

    private static void write(final String filename, final String fileContents) throws IOException {
        final File file = new File(filename);
        file.getParentFile().mkdirs();
        final FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContents);
        fileWriter.close();
    }

}
