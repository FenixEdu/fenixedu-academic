/*
 * Created on Nov 13, 2003
 *
 */
package net.sourceforge.fenixedu.utilTests;

import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Susana Fernandes
 *  
 */
public class MetadataResolver implements EntityResolver {
    private String path;

    private String metadataDtd = "WEB-INF/ims/imsmd2_rootv1p2.dtd";

    public MetadataResolver(String dtdPath) {
        this.path = dtdPath;
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return new InputSource(new String("file:///" + path.concat(metadataDtd)));
    }

}