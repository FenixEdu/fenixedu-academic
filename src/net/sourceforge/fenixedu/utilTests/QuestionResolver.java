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
    private String path;

    private String xmlDocumentDtd = "WEB-INF/ims/qtiasiv1p2.dtd";

    public QuestionResolver(String dtdPath) {
        this.path = dtdPath;
    }

    public InputSource resolveEntity(String publicId, String systemId) {
        return new InputSource(new String("file:///" + path.concat(xmlDocumentDtd)));

    }

}