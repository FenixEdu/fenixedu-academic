package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.ByteArray;

public class PhotographContent extends PhotographContent_Base {

    public PhotographContent(Photograph photograph, PhotographContentSize size, ByteArray content) {
        super();
        setSize(size);
        setPhotograph(photograph);
        setContent(content);
    }

    protected RootDomainObject getRootDomainObject() {
        return getPhotograph().getRootDomainObject();
    }

    public byte[] getBytes() {
        return getContent().getBytes();
    }
}
