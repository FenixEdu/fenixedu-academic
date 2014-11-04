/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.util.tests;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseExternalization {

    private static final Logger logger = LoggerFactory.getLogger(ResponseExternalization.class);

    public static String externalize(Response source) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(out);
        encoder.writeObject(source);
        encoder.close();
        try {
            // I think that this is wrong and that we should get the
            // bytes of the ByteArrayOutputStream interpreted as
            // UTF-8, which is what the XMLEncoder produces in the
            // first place.
            // WARNING: If this is changed, the internalize method
            // should be changed accordingly.
            return out.toString(Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            return out.toString();
        }
    }

    public static Response internalize(String source) {
        return getResponse(source);
    }

    private static Response getResponse(String xmlResponse) {
        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(new ByteArrayInputStream(xmlResponse.getBytes(CharEncoding.UTF_8)));
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1.getMessage(), e1);
        }
        Response response = null;
        try {
            response = (Response) decoder.readObject();
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error(e.getMessage(), e);
        }
        decoder.close();
        return response;
    }
}
