package Dominio;


/**
 * @author Fernanda Quitério
 * 23/09/2003
 * 
 */
public class WebSiteSection extends DomainObject implements IWebSiteSection {

	private String name;
	private String ftpName;
	private Integer size;
	private String sortingOrder;
	private String whatToSort;
	private Integer excerptSize;

	private IWebSite webSite;
	private Integer keyWebSite;
	/**
	 * @return
	 */
	public String getWhatToSort() {
		return whatToSort;
	}

	/**
	 * @param whatToSort
	 */
	public void setWhatToSort(String whatToSort) {
		this.whatToSort = whatToSort;
	}

	public WebSiteSection() {
	}

	/**
	 * @return
	 */
	public Integer getExcerptSize() {
		return excerptSize;
	}

	/**
	 * @param excerptSize
	 */
	public void setExcerptSize(Integer excerptSize) {
		this.excerptSize = excerptSize;
	}

	/**
	 * @return
	 */
	public Integer getKeyWebSite() {
		return keyWebSite;
	}

	/**
	 * @param keyWebSite
	 */
	public void setKeyWebSite(Integer keyWebSite) {
		this.keyWebSite = keyWebSite;
	}

	/**
	 * @return
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * @param size
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * @return
	 */
	public String getSortingOrder() {
		return sortingOrder;
	}

	/**
	 * @param sortingOrder
	 */
	public void setSortingOrder(String sortingOrder) {
		this.sortingOrder = sortingOrder;
	}

	/**
	 * @return
	 */
	public IWebSite getWebSite() {
		return webSite;
	}

	/**
	 * @param webSite
	 */
	public void setWebSite(IWebSite webSite) {
		this.webSite = webSite;
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
	 * @return String
	 */
	public String getFtpName() {
		return ftpName;
	}

	/**
	 * Sets the ftpName.
	 * @param name The ftpName to set
	 */
	public void setFtpName(String ftpName) {
		this.ftpName = ftpName;
	}
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IWebSiteSection) {
			IWebSiteSection webSiteSection = (IWebSiteSection) arg0;

			if (((webSiteSection.getName() == null && this.getName() == null)
				|| (webSiteSection.getName() != null && this.getName() != null && webSiteSection.getName().equals(this.getName())))
				&& ((webSiteSection.getExcerptSize() == null && this.getExcerptSize() == null)
					|| (webSiteSection.getExcerptSize() != null
						&& this.getExcerptSize() != null
						&& webSiteSection.getExcerptSize().equals(this.getExcerptSize())))
				&& ((webSiteSection.getSortingOrder() == null && this.getSortingOrder() == null)
					|| (webSiteSection.getSortingOrder() != null
						&& this.getSortingOrder() != null
						&& webSiteSection.getSortingOrder().equals(this.getSortingOrder())))
				&& ((webSiteSection.getWhatToSort() == null && this.getWhatToSort() == null)
					|| (webSiteSection.getWhatToSort() != null
						&& this.getWhatToSort() != null
						&& webSiteSection.getWhatToSort().equals(this.getWhatToSort())))
				&& ((webSiteSection.getWebSite() == null && this.getWebSite() == null)
					|| (webSiteSection.getWebSite() != null
						&& this.getWebSite() != null
						&& webSiteSection.getWebSite().equals(
							this.getWebSite())))
				//				&& (CollectionUtils.isEqualCollection(webSiteSection.getItemsList(), this.getItemsList()))
				&& ((webSiteSection.getSize() == null && this.getSize() == null)
					|| (webSiteSection.getSize() != null
						&& this.getSize() != null
						&& webSiteSection.getSize().equals(this.getSize())))) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[WEBSITESECTION";
		result += ", codInt=" + getIdInternal();
		result += ", name=" + getName();
		result += ", ftpName=" + getFtpName();
		result += ", size=" + getSize();
		result += ", sortingOrder=" + getSortingOrder();
		result += ", whatToSort=" + getWhatToSort();
		result += ", excerptSize=" + getExcerptSize();
		result += ", webSite=" + getWebSite();
		result += "]";

		return result;
	}
}
