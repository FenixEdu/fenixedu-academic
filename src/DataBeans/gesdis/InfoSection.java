package DataBeans.gesdis;

import java.util.Date;


/**
 * This is the view class that contains information about the seccao
 * domain object.
 *
 * @author Joao Pereira
 * 
 **/

public class InfoSection implements Comparable {
	private Integer internalCode;
    protected String name;
    protected Integer sectionOrder;
    protected Date lastModifiedDate;
    protected InfoSite infoSite;
    protected InfoSection infoSuperiorSection;
    protected Integer sectionDepth = new Integer(0);

    
	/** 
	* Construtor
	*/
	
	public InfoSection(){
		}
	
	/** 
	* Construtor
	*/
	
	public InfoSection(String name,Integer sectionOrder,InfoSite infoSite){
		
		this.name =name;
		this.sectionOrder= sectionOrder;
		this.infoSite = infoSite;
		
	}
	
	
	/** 
	* Construtor
	*/
	
    public InfoSection(String name, Integer sectionOrder, InfoSite infoSite, InfoSection infoSuperiorSection) {
        this.name = name;
        this.sectionOrder = sectionOrder;        
        this.infoSite = infoSite;
        this.infoSuperiorSection =infoSuperiorSection;
        this.sectionDepth = calculateDepth();
		
        
//        if ((inferiorInfoSections != null) &&
//        !inferiorInfoSections.isEmpty()) {
//            this.inferiorInfoSections =
//            ViewUtils.buildSectionsList(inferiorInfoSections,
//            new ArrayList());
//        } else
//            this.inferiorInfoSections = new ArrayList();
//        
//        this.infoItems = new ArrayList();
//        if ((infoItems != null) &&
//        !infoItems.isEmpty()) {
//            
//            Iterator iter = infoItems.iterator();
//            while (iter.hasNext()) {
//                InfoItem item = (InfoItem) iter.next();
//                infoItems.add(item);
//            }
//        }
    }
    

	
	/**
	* @return Integer
	*/
	public Integer getInternalCode() {
		return internalCode;
	}

	

	/**
	* Sets the internalCode.
	* @param internalCode The internalCode to set
	*/
	public void setInternalCode(Integer internalCode) {
		this.internalCode=internalCode;
	}
	
	
	/**
	* @return String
	*/
    public String getName() {
        return name;
    }
    
    
	/**
	* Sets the name.
	* @param name The name to set
	*/
	public void setName(String name) {
		  this.name = name;
	}
	  
	
	/**
	* @return Integer
	*/	  
    public Integer getSectionOrder() {
        return sectionOrder;
    }


	/**
	* Sets the sectionOrder.
	* @param sectionOrder The sectionOrder to set
	*/
	public void setSectionOrder(Integer sectionOrder) {
		this.sectionOrder=sectionOrder;
		this.sectionDepth = calculateDepth();
		}
    
	
	/**
	* @return Date
	*/	     
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    

	/**
	* Sets the lastModifiedDate.
	* @param lastModifiedDate The lastModifiedDate to set
	*/
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
    
    
	/**
	* @return InfoSite
	*/
    public InfoSite getInfoSite() {
        return infoSite;
    }
    

	/**
	* Sets the infoSite.
	* @param infoSite The infoSite to set
	*/
	public void setInfoSite(InfoSite infoSite) {
		this.infoSite = infoSite;
	}
    
    
	/**
	* @return InfoSection
	*/
    public InfoSection getSuperiorInfoSection() {
        return infoSuperiorSection;
    }
    
    
	/**
	* Sets the infoSite.
	* @param infoSite The infoSite to set
	*/
	public void setSuperiorInfoSection(InfoSection infoSuperiorSection) {
		this.infoSuperiorSection = infoSuperiorSection;
	}
    
    
	
	
	
	
	
//
//	/**
//	 * @return List
//	 */	
//    public List getInferiorInfoSections() {
//        return inferiorInfoSections;
//    }
//
//
//	/**
//	* Sets the inferiorInfoSections.
//	* @param inferiorInfoSections The inferiorInfoSections to set
//	*/
//	public void setInferiorInfoSections(List inferiorInfoSections) {
//		this.inferiorInfoSections = inferiorInfoSections;
//	}
//
//
//	/**
//	 * @return List
//	 */	    
//    public List getInfoItems() {
//        return infoItems;
//    }
//
//
//	/**
//	* Sets the infoItems.
//	* @param infoItems The infoItems to set
//	*/
//	public void setInfoItems(List infoItems) {
//		this.infoItems = infoItems;
//	}


	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFOSECTION";
		result += ", internalCode=" + getInternalCode();
		result += ", name=" + getName();
		result += ", sectionOrder=" + getSectionOrder();
		result += ", infoSite=" + getInfoSite();
		result += ", superiorInfoSection=" + getSuperiorInfoSection();
		result += ", sectionDepth=" + getSectionDepth();
		result += "]";
		return result;
	}
	
	
	
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoSection) {
            resultado = getName().equals(((InfoSection) obj).getName())&&
            getSectionOrder() == ((InfoSection) obj).getSectionOrder();
            if(getInfoSite() != null && ((InfoSection) obj).getInfoSite() != null)
                resultado = resultado && getInfoSite().equals(((InfoSection) obj).getInfoSite());
            else
                resultado = resultado && getInfoSite() == null && ((InfoSection) obj).getInfoSite() == null;
        }
        return resultado;
    }


	public Integer calculateDepth(){
		InfoSection section=this;
		int depth=0;
		while (section.getSuperiorInfoSection()!=null){
			depth++;
			section=section.getSuperiorInfoSection();
		}
		return new Integer(depth);
	}

	/**
	 * @return int
	 */
	public Integer getSectionDepth() {
		return sectionDepth;
	}

	/**
	 * Sets the depth.
	 * @param depth The depth to set
	 */
	public void setSectionDepth(Integer depth) {
		this.sectionDepth = depth;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg0) {	
		
		return naturalOrder()-((InfoSection)arg0).naturalOrder();
	}

	public int naturalOrder(){
		if (getSuperiorInfoSection()==null) {
			return getSectionOrder().intValue();
		}
		else {
			return getSectionOrder().intValue() + getSuperiorInfoSection().naturalOrder();
		}
		
	}

}
