package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.tests.NewPictureMaterial;
import net.sourceforge.fenixedu.domain.tests.NewTestElement;
import net.sourceforge.fenixedu.domain.tests.PictureMaterialFile;

import org.apache.commons.io.FileUtils;

import pt.ist.fenixframework.Atomic;

public class CreatePictureMaterial {
    @Atomic
    public static NewPictureMaterial run(Teacher teacher, NewTestElement testElement, Boolean inline, File mainFile,
            String originalFilename, String displayName) throws FenixServiceException, DomainException, IOException {

        final PictureMaterialFile pictureMaterialFile =
                new PictureMaterialFile(originalFilename, displayName, FileUtils.readFileToByteArray(mainFile), null);

        return new NewPictureMaterial(testElement, inline, pictureMaterialFile);

    }
}