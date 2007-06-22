package net.sourceforge.fenixedu.renderers;

import java.util.Collection;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer provides a tabular presentation for a collection that allows
 * rows of the table to be sorted by clicking the table headers. The behaviour
 * of this renderer is very similar to the one of {@link net.sourceforge.fenixedu.renderers.CollectionRenderer}.
 * 
 * <p>
 * Example:
 *  <table border="1">
 *      <thead>
 *          <th><a href="#">Name</a></th>
 *          <th>V <a href="#">Age</a></th>
 *      </thead>
 *      <tr>
 *          <td>Name A</td>
 *          <td>20</td>
 *      </tr>
 *      <tr>
 *          <td>Name C</td>
 *          <td>21</td>
 *      </tr>
 *      <tr>
 *          <td>Name B</td>
 *          <td>22</td>
 *      </tr>
 *  </table>
 * 
 * @author cfgi
 */
public class OrderableCollectionRenderer extends CollectionRenderer {

    private String sortParameter;
    
    private String sortUrl;
    
    private String ascendingClasses;
    
    private String descendingClasses;
    
    private String ascendingImage;
    
    private String descendingImage;

    private boolean contextRelative;
    
    private String sortableSlots;
    
    private boolean sortIgnored;
    
    public boolean isSortIgnored() {
		return sortIgnored;
	}

    /**
     * 
     * @property
     */
	public void setSortIgnored(boolean sortIgnored) {
		this.sortIgnored = sortIgnored;
	}

	public OrderableCollectionRenderer() {
        setContextRelative(true);
    }
    
    public String getAscendingClasses() {
        return this.ascendingClasses;
    }
    
    public String getDescendingClasses() {
        return this.descendingClasses;
    }
    
    public String getAscendingImage() {
        return this.ascendingImage;
    }
    
    public String getDescendingImage() {
        return this.descendingImage;
    }
    
    public boolean isContextRelative() {
        return this.contextRelative;
    }

    public String getSortParameter() {
        return this.sortParameter;
    }

    /**
     * 
     * @property
     */
    public void setSortParameter(String sortParameter) {
        this.sortParameter = sortParameter;
    }

    public String getSortUrl() {
        return this.sortUrl;
    }

    /**
     * 
     * @property
     */
    public void setSortUrl(String sortUrl) {
        this.sortUrl = sortUrl;
    }

    /**
     * The classes to use in a header when the corresponding column is 
     * ordered in <strong>ascending</strong> mode. This property can be used 
     * to use a custom style that denotes an ascending ordering.
     * 
     * @property
     */
    public void setAscendingClasses(String ascendingClasses) {
        this.ascendingClasses = ascendingClasses;
    }

    /**
     * The classes to use in a header when the corresponding column is 
     * ordered in <strong>descending</strong> mode. This property can be 
     * used to use a custom style that denotes an descending ordering.
     * 
     * @property
     */
    public void setDescendingClasses(String descendingClasses) {
        this.descendingClasses = descendingClasses;
    }

    /**
     * If this property is specified an image will be placed to the left
     * of the header title. This image will be used when the header is
     * clicked and the ordering is <strong>ascending</strong>.
     * 
     * @property
     */
    public void setAscendingImage(String ascendingImage) {
        this.ascendingImage = ascendingImage;
    }

    /**
     * If this property is specified an image will be placed to the left
     * of the header title. This image will be used when the header is
     * clicked and the ordering is <strong>descending</strong>.
     * 
     * @property
     */
    public void setDescendingImage(String descendingImage) {
        this.descendingImage = descendingImage;
    }

    /**
     * This property specifies whether the image url given in 
     * {@link #setAscendingImage(String) ascendingImage} or 
     * {@link #setDescendingImage(String) descendingImage} is relative
     * to the application context or not. 
     * 
     * @property
     */
    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

	public String getSortableSlots() {
		return sortableSlots;
	}

	/**
	 * Colon separated values of slot names. If this property is not specified then
	 * all slots are considered sortable. If this property is sent then only the slots 
	 * indicated are sortable.
	 * <p>
	 * Example: <code>"a, b, c"</code>
	 * 
	 * @property
	 */
	public void setSortableSlots(String sortableSlots) {
		this.sortableSlots = sortableSlots;
	}
    
    private String getImagePath(String path) {
        if (isContextRelative()) {
            return getContext().getViewState().getRequest().getContextPath() + path;    
        }
        else {
            return path;
        }
    }
    
    @Override
    protected Layout getLayout(Object object, Class type) {
        Collection sortedCollection = (isSortIgnored()) ? (Collection) object : RenderUtils.sortCollectionWithCriteria((Collection) object, getSortBy());
        
        return new CollectionTabularLayout(sortedCollection) {

            @Override
            protected HtmlComponent getHeaderComponent(int columnIndex) {
                if (columnIndex == 0 && isCheckable()) {
                    return new HtmlText();
                }
                else if (columnIndex < getNumberOfColumns() - getNumberOfLinkColumns()) {
                    HtmlComponent component = super.getHeaderComponent(columnIndex);
                    String slotName = getObject(0).getSlots().get(columnIndex - (isCheckable() ? 1 : 0)).getName();

                    if (! isSortable(slotName)) {
                    	return component;
                    }
                    
                    HtmlLink link = new HtmlLink();
                    
                    if (getSortUrl() != null) {
                        link.setUrl(getSortUrl());
                    }
                    else {
                        ViewDestination destination = getContext().getViewState().getInputDestination();
                        
                        link.setModule(destination.getModule());
                        link.setUrl(destination.getPath());
                    }
                    
                    link.setBody(component);

                    if (getSortBy() != null && getSortBy().contains(slotName)) {
                        if (getSortBy().contains("=desc")) {
                            link.setParameter(getSortParameter(), slotName + "=ascending");
                            component = wrapComponent(link, false);
                        }
                        else {
                            link.setParameter(getSortParameter(), slotName + "=descending");
                            component = wrapComponent(link, true);
                        }
                    }
                    else {
                        link.setParameter(getSortParameter(), slotName + "=ascending");
                        component = wrapComponent(link, false);
                    }
                    
                    return link;
                }
                else {
                    return new HtmlText();
                }
                
            }

            private boolean isSortable(String slotName) {
            	String sortableSlots = getSortableSlots();
            	
            	if (sortableSlots == null) {
            		return true;
            	}
            	
            	String[] slots = sortableSlots.split(",");
            	for (int i = 0; i < slots.length; i++) {
					String trimmed = slots[i].trim();
					
					if (trimmed.length() == 0) {
						continue;
					}
					
					if (trimmed.equals(slotName)) {
						return true;
					}
				}
            	
				return false;
			}

			private HtmlComponent wrapComponent(HtmlComponent component, boolean ascending) {
                String image = null;
                
                if (ascending) {
                    if (getAscendingClasses() != null) {
                        component.setClasses(getAscendingClasses());
                    }
                    
                    image = getAscendingImage();
                }
                else {
                    if (getDescendingClasses() != null) {
                        component.setClasses(getDescendingClasses());
                    }
                    
                    image = getDescendingImage();
                }
                
                if (image == null) {
                    return component;
                }
                
                HtmlContainer container = new HtmlInlineContainer();
                
                HtmlImage htmlImage = new HtmlImage();
                htmlImage.setSource(getImagePath(image));
                
                container.addChild(htmlImage);
                container.addChild(component);
                
                return container;
            }

        };
    }

}
