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
public class QuestionResolver implements EntityResolver
{
	private String path;
	private String xmlDocumentDtd = "WEB-INF/ims/qtiasiv1p2.dtd";

	public QuestionResolver(String dtdPath)
	{
		this.path = dtdPath;
	}

	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
	{
		return new InputSource(new String("file:///" + path.concat(xmlDocumentDtd)));

	}

}
