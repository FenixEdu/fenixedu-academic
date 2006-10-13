package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlActionLink;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTextArea;

/**
 * This renderer extends the {@link MultiLanguageStringInputRenderer}. The only difference
 * is that the input is done in an text area instead of a text field. Because of that, this
 * renderer ignores the {@link #setSize(Integer) size} property and provides additional
 * properties to configure the {@link #setColumns(Integer) columns} and 
 * {@link #setRows(Integer) rows} of each text area.
 * 
 * @author cfgi
 */
public class MultiLanguageTextInputRenderer extends MultiLanguageStringInputRenderer {

    public Integer rows;
    public Integer columns;
    
    /**
     * Allows you to configure the columns if the text area used for the input in each language.
     * 
     * @property
     */
    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Integer getColumns() {
        return this.columns;
    }

    /**
     * Allows you to configure the rows if the text area used for the input in each language.
     * 
     * @property
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getRows() {
        return this.rows;
    }
    
    @Override
    protected void configureLanguageContainer(HtmlContainer languageContainer, HtmlSimpleValueComponent input, HtmlSimpleValueComponent languageComponent, HtmlActionLink removeLink) {
        languageComponent.setStyle("display: block;");
        
        languageContainer.addChild(languageComponent);
        languageContainer.addChild(input);
        languageContainer.addChild(removeLink);
    }

    @Override
    protected HtmlSimpleValueComponent getInputComponent() {
        HtmlTextArea textArea = new HtmlTextArea();
        
        textArea.setColumns(getColumns());
        textArea.setRows(getRows());
        
        return textArea;
    }
    
}
