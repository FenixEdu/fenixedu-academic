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

public class InfoSection {
    protected String name;
    protected Integer order;
    protected Date lastModificationDate;
    protected InfoSite site;
    protected InfoSection superiorSection;
    protected List superiorSectionsNames;
    protected List inferiorSections;
    protected List items;
    
    public InfoSection(String name, Integer order, InfoSite site, InfoSection superiorSection, List inferiorSections, List items) {
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
                InfoItem item = (InfoItem) iter.next();
                items.add(item);
            }
        }
    }
    
    public InfoSection(String name,List ancestors){
        this.name = name;
		this.superiorSectionsNames =ancestors;
		this.superiorSection = null;
		this.order = new Integer(0);
		this.lastModificationDate = new Date();
		this.site = null;
		this.inferiorSections = null;
		this.items = null;
    }
    
    public InfoSection(List sonNodes){
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
    
    public InfoSite getSite() {
        return site;
    }
    
    public InfoSection getSuperiorSection() {
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
        if (obj instanceof InfoSection) {
            resultado = getName().equals(((InfoSection) obj).getName())&&
            getOrder() == ((InfoSection) obj).getOrder();
            if(getSite() != null && ((InfoSection) obj).getSite() != null)
                resultado = resultado && getSite().equals(((InfoSection) obj).getSite());
            else
                resultado = resultado && getSite() == null && ((InfoSection) obj).getSite() == null;
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
