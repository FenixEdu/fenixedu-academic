package DataBeans.gesdis;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.ViewUtils;


/**
 * This is the view class that contains information about the seccao
 * domain object.
 *
 * @author Joao Pereira
 * 
 **/

public class SectionView {
    protected String name;
    protected Integer order;
    protected Date lastModificationDate;
    protected SiteView site;
    protected SectionView superiorSection;
    protected List superiorSectionsNames;
    protected List inferiorSections;
    protected List items;
    
    public SectionView(String name, Integer order, SiteView site, SectionView superiorSection, List inferiorSections, List items) {
        this.name = name;
        this.order = order;        
        this.site = site;
        this.superiorSection =superiorSection;
		this.superiorSectionsNames =
        ViewUtils.buildQualifiedName(superiorSection);
        
        if ((inferiorSections != null) &&
        !inferiorSections.isEmpty()) {
            this.inferiorSections =
            ViewUtils.buildSectionsList(inferiorSections,
            new ArrayList());
        } else
            this.inferiorSections = new ArrayList();
        
        this.items = new ArrayList();
        if ((items != null) &&
        !items.isEmpty()) {
            
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                ItemView item = (ItemView) iter.next();
                items.add(item);
            }
        }
    }
    
    public SectionView(String name,List ancestors){
        this.name = name;
		this.superiorSectionsNames =ancestors;
		this.superiorSection = null;
		this.order = new Integer(0);
		this.lastModificationDate = new Date();
		this.site = null;
		this.inferiorSections = null;
		this.items = null;
    }
    
    public SectionView(List sonNodes){
		this.name = null;
		this.superiorSection = null;
		this.order = new Integer(0);
		this.lastModificationDate = new Date();
		this.site = null;
		this.inferiorSections = sonNodes;
		this.items = null;
    }
    
    public String getName() {
        return name;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    public Date getLastModificationDate() {
        return lastModificationDate;
    }
    
    public SiteView getSite() {
        return site;
    }
    
    public SectionView getSuperiorSection() {
        return superiorSection;
    }
    
    public List getInferiorSections() {
        return inferiorSections;
    }
    
    public List getItems() {
        return items;
    }
    
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof SectionView) {
            resultado = getName().equals(((SectionView) obj).getName())&&
            getOrder() == ((SectionView) obj).getOrder();
            if(getSite() != null && ((SectionView) obj).getSite() != null)
                resultado = resultado && getSite().equals(((SectionView) obj).getSite());
            else
                resultado = resultado && getSite() == null && ((SectionView) obj).getSite() == null;
        }
        return resultado;
    }
	/**
	 * @return List
	 */
	public List getSuperiorSectionsNames() {
		return superiorSectionsNames;
	}

}
