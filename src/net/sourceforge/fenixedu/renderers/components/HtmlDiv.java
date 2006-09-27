package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlDiv extends HtmlComponent {

    private List<HtmlTable> tables = new ArrayList<HtmlTable>();
    
    private List<HtmlDiv> divs = new ArrayList<HtmlDiv>();
    
    public HtmlDiv() {
    }

    public HtmlTable createTable() {
	HtmlTable table = new HtmlTable();
	tables.add(table);
        return table;
    }

    public void removeTable(HtmlTable table) {
	tables.remove(table);
    }

    public List<HtmlTable> getTables() {
        return tables;
    }
    
    public HtmlDiv createDiv() {
	HtmlDiv div = new HtmlDiv();
	
	divs.add(div);
	
        return div;
    }

    public void removeDiv(HtmlDiv div) {
        divs.remove(div);
    }

    public List<HtmlDiv> getDivs() {
        return divs;
    }
    
    @Override
    public List<HtmlComponent> getChildren() {
        List<HtmlComponent> children = new ArrayList<HtmlComponent>(super.getChildren());
        
        for (final HtmlTable table : this.tables) {
            children.add(table);    
        }
        
        for (final HtmlDiv div : this.divs) {
            children.add(div);    
        }
        
        return children;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("div");
        
        if (!this.tables.isEmpty()) {
            for (final HtmlTable table : this.tables) {
        	tag.addChild(table.getOwnTag(context));
            }
            
        }
        
        if (!this.divs.isEmpty()) {
            for (final HtmlDiv div : this.divs) {
        	tag.addChild(div.getOwnTag(context));
            }
        }
        
        // Always generate end tag
        if (tag.getChildren().isEmpty()) {
            tag.addChild(new HtmlTag(null));
        }
        
        return tag;
    }

}
