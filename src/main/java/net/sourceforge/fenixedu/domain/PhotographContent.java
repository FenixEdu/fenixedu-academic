package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.ByteArray;

public class PhotographContent extends PhotographContent_Base {

    public PhotographContent(Photograph photograph, PhotographContentSize size, ByteArray content) {
        super();
        setSize(size);
        setPhotograph(photograph);
        setContent(content);
    }

    public byte[] getBytes() {
        return getContent().getBytes();
    }

    @Deprecated
    public boolean hasPhotograph() {
        return getPhotograph() != null;
    }

    @Deprecated
    public boolean hasContent() {
        return getContent() != null;
    }

    @Deprecated
    public boolean hasSize() {
        return getSize() != null;
    }

}
