package net.sourceforge.fenixedu.presentationTier.renderers.htmlEditor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessageListener;

public abstract class TidyConverter extends Converter {

    public static final String TIDY_PROPERTIES = "HtmlEditor-Tidy.properties";
    private static final String ENCODING = "iso-8859-1";
    
    @Override
    public Object convert(Class type, Object value) {
        String htmlText = (String) value;

        if (htmlText == null || htmlText.length() == 0) {
            return null;
        }

        ByteArrayInputStream inStream = new ByteArrayInputStream(htmlText.getBytes());
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
            return new String(outStream.toByteArray(), ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ConversionException("tidy.converter.ending.notSupported.critical");
        }
    }

    private Tidy createTidyParser() {
        Tidy tidy = new Tidy();

        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(TIDY_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
        }

        tidy.setConfigurationFromProps(properties);

        return tidy;
    }
    
    protected abstract void parseDocument(OutputStream outStream, Tidy tidy, Document document);
    
    class TidyErrorsListener implements TidyMessageListener {

        boolean bogus;

        public boolean isBogus() {
            return this.bogus;
        }

        public void setBogus(boolean bogus) {
            this.bogus = bogus;
        }

        public void messageReceived(TidyMessage message) {
            if (message.getLevel().equals(TidyMessage.Level.ERROR)) {
                setBogus(true);
            }
        }

    }
}
