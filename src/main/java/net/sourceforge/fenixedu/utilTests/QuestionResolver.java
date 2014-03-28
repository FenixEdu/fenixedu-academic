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
public class QuestionResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        return new InputSource(getClass().getClassLoader().getResourceAsStream("ims/qtiasiv1p2.dtd"));
    }

}