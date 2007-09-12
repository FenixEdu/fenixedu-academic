package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class PersonalCardRenderer extends OutputRenderer {

    private String noPhotoAvailableImage;

    private String formatImageURL;
    
    private String imageDescription;

    private String imageName;

    private String imageHeight; 

    private String imageWidth;

    private String photoCellClasses;

    private String infoCellClasses;

    private String classes;

    private boolean contextRelative;

    private String subLayout;
    
    private String subSchema;
    
    private Map<String,String> properties = new HashMap<String,String>();
    
    private Map<String,String> getPropertiesMap() {
	return properties;
    }
    
    public void setSubProperty(String property, String value) {
	properties.put(property, value);
    }
    
    public String getSubProperty(String property) {
	return properties.get(property);
    }
    
    public String getSubLayout() {
        return subLayout;
    }

    public void setSubLayout(String subLayout) {
        this.subLayout = subLayout;
    }

    public String getSubSchema() {
        return subSchema;
    }

    public void setSubSchema(String subSchema) {
        this.subSchema = subSchema;
    }

    public String getClasses() {
	return classes;
    }

    public void setClasses(String classes) {
	this.classes = classes;
    }

    public String getInfoCellClasses() {
	return infoCellClasses;
    }

    public void setInfoCellClasses(String infoCellClasses) {
	this.infoCellClasses = infoCellClasses;
    }

    public String getPhotoCellClasses() {
	return photoCellClasses;
    }

    public void setPhotoCellClasses(String photoCellClasses) {
	this.photoCellClasses = photoCellClasses;
    }

    public String getImageDescription() {
	return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
	this.imageDescription = imageDescription;
    }

    public String getImageHeight() {
	return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
	this.imageHeight = imageHeight;
    }

    public String getImageName() {
	return imageName;
    }

    public void setImageName(String imageName) {
	this.imageName = imageName;
    }

    public String getImageWidth() {
	return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
	this.imageWidth = imageWidth;
    }

    public boolean isContextRelative() {
	return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
	this.contextRelative = contextRelative;
    }

    public String getNoPhotoAvailableImage() {
	return noPhotoAvailableImage;
    }

    public void setNoPhotoAvailableImage(String noPhotoAvailableImage) {
	this.noPhotoAvailableImage = noPhotoAvailableImage;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new PersonalCardRendererLayout();
    }

    private class PersonalCardRendererLayout extends Layout {

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    Person person = (Person) object;
	    HtmlBlockContainer container = new HtmlBlockContainer();
	    container.setClasses(getClasses());
	    HtmlTable table = createPersonalCard(person,type);
	    container.addChild(table);
	    return container;
	}

	private HtmlTable createPersonalCard(Person person,Class type) {
	    HtmlTable table = new HtmlTable();
	    HtmlTableRow row = table.createRow();
	    HtmlTableCell imageCell = row.createCell();
	    imageCell.setClasses(getPhotoCellClasses());
	    imageCell.setBody(createImageContainer(person));
	    HtmlTableCell dataCell = row.createCell();
	    dataCell.setBody(createInformationList(person,type));
	    dataCell.setClasses(getInfoCellClasses());
	    return table;
	}

	private HtmlComponent createInformationList(Person person, Class type) {
	    Schema schema = RenderKit.getInstance().findSchema(getSubSchema());
            return renderValue(person, type, schema, getSubLayout(), getProperties());
	}

	 private Properties getProperties() {
		Properties properties = new Properties();
		Map<String,String> map = getPropertiesMap(); 
		for(String property : map.keySet()) {
		    properties.put(property, map.get(property));
		}
		return properties;
	    }

	private HtmlComponent createImageContainer(Person person) {
	    HtmlBlockContainer imageBlock = new HtmlBlockContainer();
	    String imageSource = person.isPhotoPubliclyAvailable() ? RenderUtils.getFormattedProperties(getFormatImageURL(), person)
		    : getNoPhotoAvailableImage();
	    HtmlImage image = new HtmlImage();
	    image.setSource((isContextRelative() ? RenderUtils.getContextRelativePath("") : "")
		    + imageSource);
	    image.setWidth(getImageWidth());
	    image.setHeight(getImageHeight());
	    image.setName(getImageName());
	    image.setDescription(getImageDescription());
	    imageBlock.addChild(image);
	    return imageBlock;
	}

    }

    public String getFormatImageURL() {
        return formatImageURL;
    }

    public void setFormatImageURL(String formatImageURL) {
        this.formatImageURL = formatImageURL;
    }
}
