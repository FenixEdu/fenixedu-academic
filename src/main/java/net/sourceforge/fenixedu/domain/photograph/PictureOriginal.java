package net.sourceforge.fenixedu.domain.photograph;

import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

public class PictureOriginal extends PictureOriginal_Base {

    private PictureOriginal() {
        super();
    }

    public PictureOriginal(Photograph photograph, ByteArray pictureData, ContentType pictureFileFormat) {
        this();
        setPhotograph(photograph);
        setPictureData(pictureData);
        setPictureFileFormat(pictureFileFormat);
        setupPictureMetadata(pictureData);
    }

    protected RootDomainObject getRootDomainObject() {
        return getPhotograph().getRootDomainObject();
    }

    @Override
    public void delete() {
        setPhotograph(null);
        super.delete();
    }

}
