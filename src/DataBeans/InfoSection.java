package DataBeans;

import java.util.Date;

/**
 * This is the view class that contains information about the seccao
 * domain object.
 *
 * @author Joao Pereira
 * 
 **/

public class InfoSection implements Comparable,ISiteComponent {
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
		setSectionDepth(calculateDepth());
		}
	
	/** 
	* Construtor
	*/
	
	public InfoSection(String name,Integer sectionOrder,InfoSite infoSite){
		
		this.name =name;
		this.sectionOrder= sectionOrder;
		this.infoSite = infoSite;
		setSectionDepth(calculateDepth());
	}
	
	
	/** 
	* Construtor
	*/
	
    public InfoSection(String name, Integer sectionOrder, InfoSite infoSite, InfoSection infoSuperiorSection) {
        this.name = name;
        this.sectionOrder = sectionOrder;        
        this.infoSite = infoSite;
        this.infoSuperiorSection =infoSuperiorSection;
        setSectionDepth(calculateDepth());
		
        

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
		setSectionDepth(calculateDepth());
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
		setSectionDepth(calculateDepth());
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
		setSectionDepth(calculateDepth());
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
		setSectionDepth(calculateDepth());
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
		setSectionDepth(calculateDepth());
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
		setSectionDepth(calculateDepth());
	}
    
  
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFOSECTION";
		result += ", internalCode=" + getInternalCode();
		result += ", name=" + getName();
		result += ", sectionOrder=" + getSectionOrder();
		result += ", sectionDepth=" + getSectionDepth();
		result += ", superiorInfoSection=" + getSuperiorInfoSection();
		result += ", infoSite=" + getInfoSite();
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
	
		InfoSection section =(InfoSection) arg0;
		
		if (getSectionDepth().intValue()== section.getSectionDepth().intValue()) {
			if (getSuperiorInfoSection()==null)  {
				 return getSectionOrder().intValue()-section.getSectionOrder().intValue();
					 }				
			else {
				if (getSuperiorInfoSection().equals(section.getSuperiorInfoSection())) {
					return getSectionOrder().intValue()-section.getSectionOrder().intValue();
				}
				else {
					return getSuperiorInfoSection().compareTo(section.getSuperiorInfoSection());
				}		
			}}
		else {
			if (getSectionDepth().intValue()>section.getSectionDepth().intValue()) {
				int aux=getSuperiorInfoSection().compareTo(section);
				if (aux==0) {return 1;
				}
				else {return aux;
				}
				
			}
			else {
				return compareTo(section.getSuperiorInfoSection());	
			}
		}
		
	}

	

}
