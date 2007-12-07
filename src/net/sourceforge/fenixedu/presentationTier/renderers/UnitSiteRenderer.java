package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collection;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

import org.apache.commons.collections.Predicate;

/**
 * 
 * Renderers a Unit name with it's website if available. Can also display it's
 * parent Unit and it's like.
 * 
 * @author pcma
 * 
 */
public class UnitSiteRenderer extends OutputRenderer {

    private String classes;

    private String unitSchema;

    private String unitLayout;

    private boolean parenteShown;

    private boolean targetBlank;

    private boolean moduleRelative;

    private boolean contextRelative;

    private String separator;

    private String style;

    public boolean isContextRelative() {
	return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
	this.contextRelative = contextRelative;
    }

    public boolean isModuleRelative() {
	return moduleRelative;
    }

    public void setModuleRelative(boolean moduleRelative) {
	this.moduleRelative = moduleRelative;
    }

    public String getClasses() {
	return classes;
    }

    public void setClasses(String classes) {
	this.classes = classes;
    }

    public boolean isParenteShown() {
	return parenteShown;
    }

    public void setParenteShown(boolean parenteShown) {
	this.parenteShown = parenteShown;
    }

    public boolean isTargetBlank() {
	return targetBlank;
    }

    public void setTargetBlank(boolean targetBlank) {
	this.targetBlank = targetBlank;
    }

    public String getUnitSchema() {
	return unitSchema;
    }

    public void setUnitSchema(String unitSchema) {
	this.unitSchema = unitSchema;
    }

    public String getUnitLayout() {
	return unitLayout;
    }

    public void setUnitLayout(String unitLayout) {
	this.unitLayout = unitLayout;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new UnitSiteLayout();
    }

    private class UnitSiteLayout extends Layout {

	private Schema findSchema() {
	    return RenderKit.getInstance().findSchema(getUnitSchema());
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    Unit unit = (Unit) object;
	    HtmlBlockContainer unitPresentation = new HtmlBlockContainer();
	    unitPresentation.addChild(getUnitComponent(unit));
	    if (isParenteShown()) {

		Collection<Unit> parentUnits = CollectionUtils.select(unit.getParentUnits(),
			new Predicate() {
			    public boolean evaluate(Object arg0) {
				return !((Unit) arg0).isAggregateUnit();
			    }

			});

		if (!parentUnits.isEmpty()) {
		    unitPresentation.addChild(new HtmlText(getSeparator(), false));
		    int i = 0;
		    int size = parentUnits.size();
		    for (Unit parentUnit : parentUnits) {
			unitPresentation.addChild(getUnitComponent(parentUnit));
			if (i < size - 1) {
			    unitPresentation.addChild(new HtmlText(","));
			}
			i++;
		    }
		}
	    }
	    return unitPresentation;
	}

	private HtmlComponent getUnitComponent(Unit unit) {
	    HtmlComponent component;
	    if (unitHasSite(unit)) {
		HtmlLink link = new HtmlLink();
		link.setUrl(resolveUnitURL(unit));
		link.setBody(renderValue(unit, findSchema(), getUnitLayout()));
		if (isTargetBlank()) {
		    link.setTarget("_blank");
		}
		link.setModuleRelative(isModuleRelative());
		link.setContextRelative(isContextRelative());
		component = link;
	    } else {
		component = renderValue(unit, findSchema(), getUnitLayout());
	    }
	    return component;
	}
	
	private boolean unitHasSite(Unit unit) {
	    return (unit.isDegreeUnit()) ? unit.getDegree().hasSite() : unit.hasSite();
	}

	private String resolveUnitURL(Unit unit) {
	    MetaDomainObject metaDomainObject = MetaDomainObject.getMeta(unit.getSite().getClass());
	    return metaDomainObject == null ? null : metaDomainObject.getAssociatedPortal().getName()
		    .getContent()
		    + "/" + unit.getUnitPath("/");
	}
    }

    public String getSeparator() {
	return separator;
    }

    public void setSeparator(String separator) {
	this.separator = separator;
    }

    public String getStyle() {
	return style;
    }

    public void setStyle(String style) {
	this.style = style;
    }

}