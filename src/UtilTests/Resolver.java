/*
 * Created on Nov 13, 2003
 *
 */
package UtilTests;

import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Susana Fernandes
 *
 */
public class Resolver implements EntityResolver {
	private String path;
	private String metadataDtd = "WEB-INF/ims/imsmd2_rootv1p2.dtd";
	private String xmlDocumentDtd = "WEB-INF/ims/qtiasiv1p2.dtd";

	public Resolver(String dtdPath) {
		this.path = dtdPath;
	}

	public InputSource resolveEntity(String publicId, String systemId)
		throws SAXException, IOException {
		if (publicId
			.equals("-//Technical Superior Institute//DTD Test Metadata 1.1//EN")) {
			return new InputSource(
				new String("file://" + path.concat(metadataDtd)));
		} else if (
			publicId.equals(
				"-//Technical Superior Institute//DTD Test XmlDocument 1.1//EN")) {
			return new InputSource(
				new String("file://" + path.concat(xmlDocumentDtd)));
		} else
			return null;
	}

}
