package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

/**
 * Specialized renderer to show the weight and the corresponding percentage
 * of a {@link UnitSiteBanner}. 
 * 
 * @author cfgi
 */
public class UnitSiteBannerWeightRenderer extends OutputRenderer {

	private String percentClass;
	private String percentStyle;
	
	public String getPercentClass() {
		return percentClass;
	}

	/**
	 * @property
	 */
	public void setPercentClass(String percentClass) {
		this.percentClass = percentClass;
	}

	public String getPercentStyle() {
		return percentStyle;
	}

	/**
	 * @property
	 */
	public void setPercentStyle(String percentStyle) {
		this.percentStyle = percentStyle;
	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new Layout() {

			@Override
			public HtmlComponent createComponent(Object object, Class type) {

				Integer weight = (Integer) object;
				float percentage = calculatePercentage(weight);

				HtmlContainer container = new HtmlInlineContainer();
				
				container.addChild(getWeightComponent(object));
				container.addChild(getPercentageComponent(percentage));
				
				return container;
			}

			private HtmlComponent getWeightComponent(Object object) {
				if (object == null) {
					return new HtmlText("-");
				}
				else {
					return renderValue(object, null, null);
				}
			}

			private HtmlText getPercentageComponent(float percentage) {
				HtmlText text = new HtmlText(String.format("(%.1f%%)", percentage));
				
				text.setClasses(getPercentClass());
				text.setStyle(getPercentStyle());
				
				return text;
			}
			
			private float calculatePercentage(Integer weight) {
				UnitSiteBanner banner = getBanner();
				return banner.getWeightPercentage();
			}

			private UnitSiteBanner getBanner() {
				return (UnitSiteBanner) getContext().getParentContext().getMetaObject().getObject();
			}
			
		};
	}

}
