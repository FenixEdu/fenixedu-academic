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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;

/**
 * This converter converts an HTML fragment to plain text while preserving some
 * of the formatting like paragraphs, lists, quotations, smiles, etc.
 * 
 * @author cfgi
 */
public class HtmlToTextConverter extends TidyConverter {

    private static final Logger logger = LoggerFactory.getLogger(HtmlToTextConverter.class);

    private static final String DEFAULT_INDENT = "    ";

    private final StringBuilder buffer;
    private int pos;

    private boolean wrap;
    private int lineLength;

    public HtmlToTextConverter() {
        super();

        this.pos = 0;
        this.buffer = new StringBuilder();

        this.wrap = true;
        this.lineLength = 80;
    }

    public int getLineLength() {
        return this.lineLength;
    }

    /**
     * Sets the line length used when wrapping text. This value is ignored if {@link #isWrap()} returns <code>false</code>.
     */
    public void setLineLength(int lineLength) {
        this.lineLength = lineLength;
    }

    /**
     * If this converter is wrapping text acording to the line length specified
     * with {@link #setLineLength(int)}.
     */
    public boolean isWrap() {
        return this.wrap;
    }

    /**
     * Chooses wether this converter should do line wrapping or not.
     */
    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    @Override
    protected void parseDocument(OutputStream outStream, Tidy tidy, Document document) {
        tidy.setPrintBodyOnly(false);

        parseNode(tidy, document, "");

        try {
            Writer writer = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
            writer.write(this.buffer.toString());
            writer.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ConversionException("renderers.converter.text.write");
        }
    }

    private void parseNode(Tidy tidy, Node node, String indent) {
        switch (node.getNodeType()) {
        case Node.DOCUMENT_NODE:
            parseNodeChildren(tidy, node, indent);
            break;
        case Node.ELEMENT_NODE:
            Element element = (Element) node;
            String name = element.getNodeName().toLowerCase();

            if (name.equals("p")) {
                ensureBlankLine();
                addCodeText(indent);
                parseNodeChildren(tidy, element, indent);
                ensureBlankLine();
                addCodeText(indent);
            } else if (name.equals("blockquote")) {
                ensureBlankLine();
                addCodeText(indent + DEFAULT_INDENT);
                parseNodeChildren(tidy, element, indent + DEFAULT_INDENT);
                ensureBlankLine();
                addCodeText(indent);
            } else if (name.equals("ul") || name.equals("ol")) {
                ensureLineBreak();
                parseList(tidy, element, name.equals("ol"), indent);
                ensureLineBreak();
                addCodeText(indent);
            } else if (name.equals("br")) {
                addLineBreak();
                addCodeText(indent);
            } else if (name.equals("hr")) {
                ensureLineBreak();
                addText("----------", indent);
                ensureLineBreak();
                addCodeText(indent);
            } else if (name.equals("pre")) {
                ensureBlankLine();
                addCodeText(indent);
                addCodeText(getChildTextContent(tidy, element));
                ensureBlankLine();
                addCodeText(indent);
            } else if (name.equals("code")) {
                addCodeText(getChildTextContent(tidy, element));
            } else if (name.equals("a")) {
                parseNodeChildren(tidy, element, indent);
                addText("(" + element.getAttribute("href") + ")", indent);
            } else if (name.equals("img")) {
                parseSmile(tidy, element, indent);
            } else {
                parseNodeChildren(tidy, node, indent);
            }

            break;
        case Node.TEXT_NODE:
            addText(getTextContent(tidy, node), indent);
            break;
        default:
            break;
        }
    }

    private void parseList(Tidy tidy, Element element, boolean ordered, String indent) {
        NodeList itemList = element.getChildNodes();
        for (int i = 0; i < itemList.getLength(); i++) {
            Node item = itemList.item(i);

            if (item.getNodeType() != Node.ELEMENT_NODE || !item.getNodeName().equalsIgnoreCase("li")) {
                continue;
            }

            addCodeText(indent + DEFAULT_INDENT);
            addText(ordered ? String.valueOf(i + 1) + ". " : "* ", indent);
            parseNodeChildren(tidy, item, indent + DEFAULT_INDENT);
            addLineBreak();
        }
    }

    private static final Map<String, String> emoticons;

    static {
        emoticons = new HashMap<String, String>();

        emoticons.put("cool", "B-)");
        emoticons.put("cry", ":'-(");
        emoticons.put("embarassed", ":-$");
        emoticons.put("foot-in-mouth", ":-!");
        emoticons.put("frown", ":-(");
        emoticons.put("innocent", "O:-)");
        emoticons.put("kiss", ":-*");
        emoticons.put("laughing", ":-D");
        emoticons.put("money-mouth", ":-$");
        emoticons.put("sealed", ":-x");
        emoticons.put("suprised", ":-o");
        emoticons.put("tongue-out", ":-P");
        emoticons.put("undecided", ":-/");
        emoticons.put("wink", ";-)");
        emoticons.put("yell", ":-O");
    }

    private void parseSmile(Tidy tidy, Element element, String indent) {
        String source = element.getAttribute("src");

        if (source == null) {
            return;
        }

        if (!source.matches(".*?smiley-[^.]+\\.gif")) { // TODO: check this
            // convention
            return;
        }

        int indexStart = source.lastIndexOf("smiley-") + "smiley-".length();
        int indexEnd = source.lastIndexOf(".");

        String smiley = source.substring(indexStart, indexEnd);
        String emoticon = emoticons.get(smiley);

        if (emoticon != null) {
            addText(emoticon, indent);
        }
    }

    private String getTextContent(Tidy tidy, Node node) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        tidy.pprint(node, outStream);

        try {
            outStream.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ConversionException("renderers.converter.text.write");
        }

        return new String(outStream.toByteArray(), StandardCharsets.UTF_8);
    }

    private String getChildTextContent(Tidy tidy, Node node) {
        StringBuilder builder = new StringBuilder();

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            builder.append(getTextContent(tidy, children.item(i)));
        }

        return builder.toString();
    }

    private void parseNodeChildren(Tidy tidy, Node node, String indent) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            parseNode(tidy, children.item(i), indent);
        }
    }

    private void addText(String htmlText, String indent) {
        if (htmlText == null) {
            return;
        }

        String text = unescapeHtml(htmlText);

        String[] words = text.split("\\p{Space}+");
        for (String word : words) {
            if (word.length() == 0) {
                continue;
            }

            if (pos + word.length() + 1 > getLineLength()) {
                buffer.append("\n" + indent);
                this.buffer.append(word + " ");
                pos = indent.length() + word.length() + 1;
            } else {
                this.buffer.append(word + " ");
                pos += word.length() + 1;
            }
        }
    }

    private String unescapeHtml(String htmlText) {
        String text = htmlText;

        text = unhtmlentities(text);
        text = unhtmlAmpersand(text);
        text = unhtmlAngleBrackets(text);
        text = unhtmlQuotes(text);

        return text;
    }

    private static String unhtmlQuotes(String str) {
        str = unhtmlDoubleQuotes(str); //convert double quotes
        str = unhtmlSingleQuotes(str); //convert single quotes
        return str;
    }

    private static String unhtmlSingleQuotes(String str) {
        return str.replaceAll("&rsquo;", "\'");
    }

    private static String unhtmlDoubleQuotes(String str) {
        return str.replaceAll("&quot;", "\"");
    }

    private static String unhtmlAngleBrackets(String str) {
        str = str.replaceAll("&lt;", "<");
        str = str.replaceAll("&gt;", ">");
        return str;
    }

    private static String unhtmlAmpersand(String str) {
        return str.replaceAll("&amp;", "&");
    }

    private static String unhtmlentities(String str) {

        //initialize html translation maps table the first time is called
        if (unhtmlentities_map.isEmpty()) {
            initializeEntitiesTables();
        }

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == '&') {
                int semi = str.indexOf(';', i + 1);
                if ((semi == -1) || ((semi - i) > 7)) {
                    buf.append(ch);
                    continue;
                }
                String entity = str.substring(i, semi + 1);
                Integer iso;
                if (entity.charAt(1) == ' ') {
                    buf.append(ch);
                    continue;
                }
                if (entity.charAt(1) == '#') {
                    if (entity.charAt(2) == 'x') {
                        iso = Integer.valueOf(Integer.parseInt(entity.substring(3, entity.length() - 1), 16));
                    } else {
                        iso = Integer.valueOf(entity.substring(2, entity.length() - 1));
                    }
                } else {
                    iso = unhtmlentities_map.get(entity);
                }
                if (iso == null) {
                    buf.append(entity);
                } else {
                    buf.append((char) (iso.intValue()));
                }
                i = semi;
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    private static void initializeEntitiesTables() {
        // initialize html translation maps
        for (Object[] element : html_entities_table) {
            unhtmlentities_map.put((String) element[0], (Integer) element[1]);
        }
    }

    private static final Object[][] html_entities_table = { { "&Aacute;", 193 }, { "&aacute;", 225 }, { "&Acirc;", 194 },
            { "&acirc;", 226 }, { "&acute;", 180 }, { "&AElig;", 198 }, { "&aelig;", 230 }, { "&Agrave;", 192 },
            { "&agrave;", 224 }, { "&alefsym;", 8501 }, { "&Alpha;", 913 }, { "&alpha;", 945 }, { "&amp;", 38 },
            { "&and;", 8743 }, { "&ang;", 8736 }, { "&Aring;", 197 }, { "&aring;", 229 }, { "&asymp;", 8776 },
            { "&Atilde;", 195 }, { "&atilde;", 227 }, { "&Auml;", 196 }, { "&auml;", 228 }, { "&bdquo;", 8222 },
            { "&Beta;", 914 }, { "&beta;", 946 }, { "&brvbar;", 166 }, { "&bull;", 8226 }, { "&cap;", 8745 },
            { "&Ccedil;", 199 }, { "&ccedil;", 231 }, { "&cedil;", 184 }, { "&cent;", 162 }, { "&Chi;", 935 }, { "&chi;", 967 },
            { "&circ;", 710 }, { "&clubs;", 9827 }, { "&cong;", 8773 }, { "&copy;", 169 }, { "&crarr;", 8629 },
            { "&cup;", 8746 }, { "&curren;", 164 }, { "&dagger;", 8224 }, { "&Dagger;", 8225 }, { "&darr;", 8595 },
            { "&dArr;", 8659 }, { "&deg;", 176 }, { "&Delta;", 916 }, { "&delta;", 948 }, { "&diams;", 9830 },
            { "&divide;", 247 }, { "&Eacute;", 201 }, { "&eacute;", 233 }, { "&Ecirc;", 202 }, { "&ecirc;", 234 },
            { "&Egrave;", 200 }, { "&egrave;", 232 }, { "&empty;", 8709 }, { "&emsp;", 8195 }, { "&ensp;", 8194 },
            { "&Epsilon;", 917 }, { "&epsilon;", 949 }, { "&equiv;", 8801 }, { "&Eta;", 919 }, { "&eta;", 951 },
            { "&ETH;", 208 }, { "&eth;", 240 }, { "&Euml;", 203 }, { "&euml;", 235 }, { "&euro;", 8364 }, { "&exist;", 8707 },
            { "&fnof;", 402 }, { "&forall;", 8704 }, { "&frac12;", 189 }, { "&frac14;", 188 }, { "&frac34;", 190 },
            { "&frasl;", 8260 }, { "&Gamma;", 915 }, { "&gamma;", 947 }, { "&ge;", 8805 }, { "&harr;", 8596 },
            { "&hArr;", 8660 }, { "&hearts;", 9829 }, { "&hellip;", 8230 }, { "&Iacute;", 205 }, { "&iacute;", 237 },
            { "&Icirc;", 206 }, { "&icirc;", 238 }, { "&iexcl;", 161 }, { "&Igrave;", 204 }, { "&igrave;", 236 },
            { "&image;", 8465 }, { "&infin;", 8734 }, { "&int;", 8747 }, { "&Iota;", 921 }, { "&iota;", 953 },
            { "&iquest;", 191 }, { "&isin;", 8712 }, { "&Iuml;", 207 }, { "&iuml;", 239 }, { "&Kappa;", 922 },
            { "&kappa;", 954 }, { "&Lambda;", 923 }, { "&lambda;", 955 }, { "&lang;", 9001 }, { "&laquo;", 171 },
            { "&larr;", 8592 }, { "&lArr;", 8656 }, { "&lceil;", 8968 }, { "&ldquo;", 8220 }, { "&le;", 8804 },
            { "&lfloor;", 8970 }, { "&lowast;", 8727 }, { "&loz;", 9674 }, { "&lrm;", 8206 }, { "&lsaquo;", 8249 },
            { "&lsquo;", 8216 }, { "&macr;", 175 }, { "&mdash;", 8212 }, { "&micro;", 181 }, { "&middot;", 183 },
            { "&minus;", 8722 }, { "&Mu;", 924 }, { "&mu;", 956 }, { "&nabla;", 8711 }, { "&nbsp;", 160 }, { "&ndash;", 8211 },
            { "&ne;", 8800 }, { "&ni;", 8715 }, { "&not;", 172 }, { "&notin;", 8713 }, { "&nsub;", 8836 }, { "&Ntilde;", 209 },
            { "&ntilde;", 241 }, { "&Nu;", 925 }, { "&nu;", 957 }, { "&Oacute;", 211 }, { "&oacute;", 243 }, { "&Ocirc;", 212 },
            { "&ocirc;", 244 }, { "&OElig;", 338 }, { "&oelig;", 339 }, { "&Ograve;", 210 }, { "&ograve;", 242 },
            { "&oline;", 8254 }, { "&Omega;", 937 }, { "&omega;", 969 }, { "&Omicron;", 927 }, { "&omicron;", 959 },
            { "&oplus;", 8853 }, { "&or;", 8744 }, { "&ordf;", 170 }, { "&ordm;", 186 }, { "&Oslash;", 216 },
            { "&oslash;", 248 }, { "&Otilde;", 213 }, { "&otilde;", 245 }, { "&otimes;", 8855 }, { "&Ouml;", 214 },
            { "&ouml;", 246 }, { "&para;", 182 }, { "&part;", 8706 }, { "&permil;", 8240 }, { "&perp;", 8869 }, { "&Phi;", 934 },
            { "&phi;", 966 }, { "&Pi;", 928 }, { "&pi;", 960 }, { "&piv;", 982 }, { "&plusmn;", 177 }, { "&pound;", 163 },
            { "&prime;", 8242 }, { "&Prime;", 8243 }, { "&prod;", 8719 }, { "&prop;", 8733 }, { "&Psi;", 936 }, { "&psi;", 968 },
            { "&radic;", 8730 }, { "&rang;", 9002 }, { "&raquo;", 187 }, { "&rarr;", 8594 }, { "&rArr;", 8658 },
            { "&rceil;", 8969 }, { "&rdquo;", 8221 }, { "&real;", 8476 }, { "&reg;", 174 }, { "&rfloor;", 8971 },
            { "&Rho;", 929 }, { "&rho;", 961 }, { "&rlm;", 8207 }, { "&rsaquo;", 8250 }, { "&rsquo;", 8217 },
            { "&sbquo;", 8218 }, { "&Scaron;", 352 }, { "&scaron;", 353 }, { "&sdot;", 8901 }, { "&sect;", 167 },
            { "&shy;", 173 }, { "&Sigma;", 931 }, { "&sigma;", 963 }, { "&sigmaf;", 962 }, { "&sim;", 8764 },
            { "&spades;", 9824 }, { "&sub;", 8834 }, { "&sube;", 8838 }, { "&sum;", 8721 }, { "&sup1;", 185 }, { "&sup2;", 178 },
            { "&sup3;", 179 }, { "&sup;", 8835 }, { "&supe;", 8839 }, { "&szlig;", 223 }, { "&Tau;", 932 }, { "&tau;", 964 },
            { "&there4;", 8756 }, { "&Theta;", 920 }, { "&theta;", 952 }, { "&thetasym;", 977 }, { "&thinsp;", 8201 },
            { "&THORN;", 222 }, { "&thorn;", 254 }, { "&tilde;", 732 }, { "&times;", 215 }, { "&trade;", 8482 },
            { "&Uacute;", 218 }, { "&uacute;", 250 }, { "&uarr;", 8593 }, { "&uArr;", 8657 }, { "&Ucirc;", 219 },
            { "&ucirc;", 251 }, { "&Ugrave;", 217 }, { "&ugrave;", 249 }, { "&uml;", 168 }, { "&upsih;", 978 },
            { "&Upsilon;", 933 }, { "&upsilon;", 965 }, { "&Uuml;", 220 }, { "&uuml;", 252 }, { "&weierp;", 8472 },
            { "&Xi;", 926 }, { "&xi;", 958 }, { "&Yacute;", 221 }, { "&yacute;", 253 }, { "&yen;", 165 }, { "&yuml;", 255 },
            { "&Yuml;", 376 }, { "&Zeta;", 918 }, { "&zeta;", 950 }, { "&zwj;", 8205 }, { "&zwnj;", 8204 } };

    private static final Map<String, Integer> unhtmlentities_map = new HashMap<String, Integer>();

    private void addCodeText(String htmlText) {
        if (htmlText == null) {
            return;
        }

        String text = unescapeHtml(htmlText);

        this.buffer.append(text);
        pos += text.length() + 1;
    }

    private void addLineBreak() {
        buffer.append("\n");
        pos = 0;
    }

    private void ensureLineBreak() {
        if (buffer.length() == 0) {
            return;
        }

        if (buffer.lastIndexOf("\n") == buffer.length() - 1) {
            return;
        }

        addLineBreak();
    }

    private void ensureBlankLine() {
        if (buffer.length() == 0) {
            return;
        }

        ensureLineBreak();

        if (buffer.lastIndexOf("\n\n") == buffer.length() - 2) {
            return;
        }

        addLineBreak();
    }

}
