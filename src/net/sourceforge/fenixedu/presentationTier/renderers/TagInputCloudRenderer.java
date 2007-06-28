package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.List;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit.UnitFileBean;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlScript;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class TagInputCloudRenderer extends InputRenderer {

	private String size;
	
	private String tagClasses;
	
	private String tagSort;
	
	private String textClasses;
	
	
	public String getTextClasses() {
		return textClasses;
	}

	public void setTextClasses(String textClasses) {
		this.textClasses = textClasses;
	}

	public String getTagSort() {
		return tagSort;
	}

	public void setTagSort(String tagSort) {
		this.tagSort = tagSort;
	}

	public String getTagClasses() {
		return tagClasses;
	}

	public void setTagClasses(String tagClasses) {
		this.tagClasses = tagClasses;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	@Override
	protected Layout getLayout(Object object, Class type) {

		return new Layout() {

			@Override
			public HtmlComponent createComponent(Object object, Class type) {
				MetaSlot slot = (MetaSlot) getContext().getMetaObject();

				HtmlBlockContainer container = new HtmlBlockContainer();

				String tags = (String) object;
				HtmlTextInput input = new HtmlTextInput();
				if (getSize() != null) {
					input.setSize(getSize());
				}
				input.setValue(tags);
				input.bind(slot);
				input.setId(input.getName());

				TagCloudRenderer tagCloud = new TagCloudRenderer();
				tagCloud.setClasses(getTagClasses());
				tagCloud.setSortBy(getTagSort());
				tagCloud.setLinkFormat("#");
				tagCloud.setOnClick(getAction(input.getName()));
				tagCloud.setOnDblClick(getAction(input.getName()));
				List<UnitFileTag> tagList = getTags();

				PresentationContext newContext = getContext().createSubContext(
						getContext().getMetaObject());
				newContext.setProperties(new Properties());
				newContext.setRenderMode(RenderMode.getMode("output"));

				container.addChild(getScript());
				container.addChild(input);
				container.addChild(new HtmlText("<br/>", false));
				HtmlText text = new HtmlText(RenderUtils.getResourceString("RENDERER_RESOURCES", "renderers.label.tags.are.space.separated"));
				text.setClasses(getTextClasses());
				container.addChild(text);
				container.addChild(RenderKit.getInstance().renderUsing(tagCloud, newContext, tagList,
						tagList.getClass()));

				return container;

			}

			private HtmlComponent getScript() {
				HtmlScript script = new HtmlScript();

				script.setContentType("text/javascript");
				script.setConditional(true);
				script.setScript("\n" + "function addTag(field, tag) {\n"
						+ "var element = document.getElementById(field);\n"
						+ "var tags = element.value;\n"
						+ "if (!tags.match('^' + tag + '$|\\\\s' + tag + '\\\\s|\\\\s' + tag + '$')) {\n" + "if (tags.length > 0) {\n"
						+ "element.value = tags + ' ' + tag;\n" + "}\n" + "else {\n"
						+ "element.value = tag;\n" + "}\n"
						+ "element.focus();\n"
						+ "}\n" + "}\n");
				return script;
			}

			private List<UnitFileTag> getTags() {
				UnitFileBean bean = (UnitFileBean) getContext().getParentContext().getMetaObject()
						.getObject();
				return bean.getUnit().getUnitFileTags();
			}

			private String getAction(String name) {
				return "addTag('" + name + "','${name}');";
			}

		};
	}

}
