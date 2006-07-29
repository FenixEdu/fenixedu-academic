package net.sourceforge.fenixedu.presentationTier.renderers.htmlEditor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;

/**
 * This converter converts an HTML fragment to plain text while preserving
 * some of the formatting like paragraphs, lists, quotations, smiles, etc. 
 * 
 * @author cfgi
 */
public class HtmlToTextConverter extends TidyConverter {

    private static final String DEFAULT_INDENT = "    ";
    
    private StringBuilder buffer;
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
     * Sets the line length used when wrapping text. This value is ignored if 
     * {@link #isWrap()} returns <code>false</code>.
     */
    public void setLineLength(int lineLength) {
        this.lineLength = lineLength;
    }

    /**
     * If this converter is wrapping text acording to the line length specified with
     * {@link #setLineLength(int)}.
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
            Writer writer = new OutputStreamWriter(outStream);
            writer.write(this.buffer.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
            }
            else if (name.equals("blockquote")) {
                ensureBlankLine();
                addCodeText(indent + DEFAULT_INDENT);
                parseNodeChildren(tidy, element, indent + DEFAULT_INDENT);
                ensureBlankLine();
                addCodeText(indent);
            }
            else if (name.equals("ul") || name.equals("ol")) {
                ensureLineBreak();
                parseList(tidy, element, name.equals("ol"), indent);
                ensureLineBreak();
                addCodeText(indent);
            }
            else if (name.equals("br")) {
                addLineBreak();
                addCodeText(indent);
            }
            else if (name.equals("hr")) {
                ensureLineBreak();
                addText("----------", indent);
                ensureLineBreak();
                addCodeText(indent);
            }
            else if (name.equals("pre")) {
                ensureBlankLine();
                addCodeText(indent);
                addCodeText(getChildTextContent(tidy, element));
                ensureBlankLine();
                addCodeText(indent);
            }
            else if (name.equals("code")) {
                addCodeText(getChildTextContent(tidy, element));
            }
            else if (name.equals("a")) {
                parseNodeChildren(tidy, element, indent);
                addText("(" + element.getAttribute("href") + ")", indent);
            }
            else if (name.equals("img")) {
                parseSmile(tidy, element, indent);
            }
            else {
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

    private void parseSmile(Tidy tidy, Element element, String indent) {
        String source = element.getAttribute("src");
        
        if (source == null) {
            return;
        }
        
        if (! source.matches(".*?smiley-[^.]+\\.gif")) { // TODO: check this convention
            return;
        }

        int indexStart = source.lastIndexOf("smiley-") + "smiley-".length();
        int indexEnd = source.lastIndexOf(".");
        
        String smiley = source.substring(indexStart, indexEnd);
        String emoticon = EmoticonMap.getEmoticon(smiley);
        
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
            e.printStackTrace();
            throw new ConversionException("renderers.converter.text.write");
        }

        return new String(outStream.toByteArray());
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
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            
            if (word.length() == 0) {
                continue;
            }
            
            if (pos + word.length() + 1 > getLineLength()) {
                buffer.append("\n" + indent);
                this.buffer.append(word + " ");
                pos = indent.length() + word.length() + 1;
            }
            else {
                this.buffer.append(word + " ");
                pos += word.length() + 1;
            }
        }
    }

    private String unescapeHtml(String htmlText) {
        String text = htmlText;
        
        text = HTMLEntities.unhtmlentities(text);
        text = HTMLEntities.unhtmlAmpersand(text);
        text = HTMLEntities.unhtmlAngleBrackets(text);
        text = HTMLEntities.unhtmlQuotes(text);
        
        return text;
    }

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
    
    public static void main(String[] a) {
        HtmlToTextConverter c = new HtmlToTextConverter();
        
        System.out.print(c.convert(null, HTMLEntities.htmlentities( 
                "<p>a<br/></p><p>b</p>" +
                "&lt;&quot;Isto é que não pá&quot;&gt;. <a href=\"http://www.google.com\">testing</a><br/>\n" +
                "<ol>\n"+
                "  <li> pois é <ol><li>um dois tres um dois tres um dois tres um dois tres um dois tres um dois tres um dois tres um dois tres um dois tres um dois tres <li>dois</ol>\n"+
                "  <li> Isto é outro teste\n"+
                "</ol>\n" +
                "<hr>\n"+
                "<blockquote>isto é um teste <p>com bastante texto</p> para fazer wrap desta coisa isto ldksjfslkdj flskdfj slakfjsalk lsdkfj slakfj salkjd lakfj " +
                "<blockquote>tem que crescer mais um bocado para fazer wrap<br/>asdasdsa</blockquote>asdsadsadds</blockquote>" +
                "<hr>\n" +
                "<p>" +
                "agora é código da classe <code>BlaBlabla</code>\n<hr>" +
                "<pre>\n" +
                "    ...\n" +
                "    java() {\n" +
                "      print();\n" +
                "    }</pre><hr>")));
    }

}
