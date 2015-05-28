/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers.htmlEditor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessageListener;

import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public abstract class TidyConverter extends Converter {

    private static final Logger logger = LoggerFactory.getLogger(TidyConverter.class);

    public static final String TIDY_PROPERTIES = "/HtmlEditor-Tidy.properties";

    private static final String ENCODING = Charset.defaultCharset().name();

    public String getTidyProperties() {
        return TIDY_PROPERTIES;
    }

    @Override
    public Object convert(Class type, Object value) {
        String htmlText = (String) value;

        if (htmlText == null || htmlText.length() == 0) {
            return null;
        }

        ByteArrayInputStream inStream = new ByteArrayInputStream(htmlText.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        Tidy tidy = createTidyParser();

        TidyErrorsListener errorListener = new TidyErrorsListener();
        tidy.setMessageListener(errorListener);

        Document document = tidy.parseDOM(inStream, null);

        if (errorListener.isBogus()) {
            throw new ConversionException("renderers.converter.safe.invalid");
        }

        parseDocument(outStream, tidy, document);

        try {
            return filterOutput(new String(outStream.toByteArray(), ENCODING));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            throw new ConversionException("tidy.converter.ending.notSupported.critical");
        }
    }

    protected String filterOutput(String output) {
        return output;
    }

    private Tidy createTidyParser() {
        Tidy tidy = new Tidy();

        Properties properties = new Properties();
        try (InputStream stream = getClass().getResourceAsStream(getTidyProperties())) {
            properties.load(stream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        tidy.setConfigurationFromProps(properties);

        return tidy;
    }

    protected abstract void parseDocument(OutputStream outStream, Tidy tidy, Document document);

    private static class TidyErrorsListener implements TidyMessageListener {

        boolean bogus;

        public boolean isBogus() {
            return this.bogus;
        }

        public void setBogus(boolean bogus) {
            this.bogus = bogus;
        }

        @Override
        public void messageReceived(TidyMessage message) {
            if (message.getLevel().equals(TidyMessage.Level.ERROR)) {
                setBogus(true);
            }
        }

    }
}
