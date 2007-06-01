package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlList;
import net.sourceforge.fenixedu.renderers.components.HtmlListItem;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class TagCloudRenderer extends OutputRenderer {

	private String linkFormat;

	private String classes;

	private String styles;

	private boolean moduleRelative;

	private boolean contextRelative;

	private int numberOfLevels = 6;

	private int popularCount = 20;

	private float minimumLevel = 0.4f;

	private String sortBy;

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public float getMinimumLevel() {
		return minimumLevel;
	}

	public void setMinimumLevel(float minimumLevel) {
		this.minimumLevel = minimumLevel;
	}

	public int getPopularCount() {
		return popularCount;
	}

	public void setPopularCount(int popularCount) {
		this.popularCount = popularCount;
	}

	public int getNumberOfLevels() {
		return numberOfLevels;
	}

	public void setNumberOfLevels(int numberOfLevels) {
		this.numberOfLevels = numberOfLevels;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public boolean isContextRelative() {
		return contextRelative;
	}

	public void setContextRelative(boolean contextRelative) {
		this.contextRelative = contextRelative;
	}

	public String getLinkFormat() {
		return linkFormat;
	}

	public void setLinkFormat(String linkFormat) {
		this.linkFormat = linkFormat;
	}

	public boolean isModuleRelative() {
		return moduleRelative;
	}

	public void setModuleRelative(boolean moduleRelative) {
		this.moduleRelative = moduleRelative;
	}

	public String getStyles() {
		return styles;
	}

	public void setStyles(String styles) {
		this.styles = styles;
	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new Layout() {

			@Override
			public HtmlComponent createComponent(Object object, Class type) {
				List<UnitFileTag> tags = (getSortBy() != null) ? RenderUtils.sortCollectionWithCriteria(
						(List<UnitFileTag>) object, getSortBy()) : new ArrayList<UnitFileTag>(
						(List<UnitFileTag>) object);

				Person person = AccessControl.getPerson();
				int maximum = getMaximum(tags, person);

				HtmlList container = new HtmlList();

				for (UnitFileTag tag : tags) {
					if (tag.isTagAccessibleToUser(person)) {
						HtmlLink link = new HtmlLink();
						link.setModuleRelative(isModuleRelative());
						link.setContextRelative(isContextRelative());
						link.setUrl(RenderUtils.getFormattedProperties(getLinkFormat(), tag));
						HtmlText text = new HtmlText(tag.getName());
						text.setClasses(getHtmlClass(maximum, tag, person));
						link.setBody(text);
						HtmlListItem item = container.createItem();
						item.addChild(link);
					}
				}
				return container;
			}

			private String getHtmlClass(Integer maximum, UnitFileTag tag, Person person) {
				Double level = getLevel(tag, maximum);
				Double min = Math.min(level, getNumberOfLevels() - 1);

				return "tcloudlevel" + getNumberOfLevels() + "-" + (min.intValue() + 1);
			}

			private double getLevel(UnitFileTag tag, Integer maxFrequency) {
				float level = Math.min(getNumberOfLevels() - weight(maxFrequency), getNumberOfLevels()
						* maxFrequency / getPopularCount());
				return Math.log10(tag.getFileTagCount(AccessControl.getPerson())) * level
						/ Math.log10(1 + maxFrequency) + weight(maxFrequency);
			}

			private float weight(int maximumFrequency) {
				float value = getMinimumLevel() * getNumberOfLevels();
				return Math.min(value, value * getPopularCount() / maximumFrequency);
			}

			private int getMaximum(List<UnitFileTag> tags, Person person) {
				int max = -1;
				for (UnitFileTag tag : tags) {
					max = Math.max(max, tag.getFileTagCount(person));
				}
				return max;
			}
		};
	}

}
