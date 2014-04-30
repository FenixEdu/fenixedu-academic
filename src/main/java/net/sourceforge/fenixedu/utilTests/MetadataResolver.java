/*
 * Created on Nov 13, 2003
 *
 */
package net.sourceforge.fenixedu.utilTests;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * @author Susana Fernandes
 * 
 */
public class MetadataResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        return new InputSource(getClass().getClassLoader().getResourceAsStream("ims/imsmd2_rootv1p2.dtd"));
    }

}