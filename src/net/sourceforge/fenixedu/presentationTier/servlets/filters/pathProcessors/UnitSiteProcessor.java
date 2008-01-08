package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.StringUtils;

public class UnitSiteProcessor extends PathProcessor {

    public static final String PREFIX1 = "unidades";

    public static final String PREFIX2 = "units";

    public UnitSiteProcessor(String uri) {
	super(uri);
    }

    public UnitSiteProcessor add(SectionProcessor processor) {
	addChild(processor);
	return this;
    }

    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
	return new UnitSiteContext(parentContext, getForwardURI());
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
	String current = provider.current();
	if (!(current.equalsIgnoreCase(PREFIX1) || current.equalsIgnoreCase(PREFIX2))) {
	    return false;
	}

	UnitSiteContext ownContext = (UnitSiteContext) context;

	int i;
	for (i = 1;; i++) {
	    current = provider.peek(i);

	    if (!ownContext.addUnit(current)) {
		break;
	    }
	}

	while (i > 1) {
	    provider.next();
	    i--;
	}

	return ownContext.getUnit() != null && ownContext.getUnit().hasSite();
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
	if (provider.hasNext()) {
	    return false;
	} else {
	    UnitSiteContext ownContext = (UnitSiteContext) context;
	    return doForward(context, new Object[] { "presentation", ownContext.getUnit().getIdInternal() });
	}
    }

    public static class UnitSiteContext extends ProcessingContext implements SiteContext {

	private String contextURI;

	private List<Unit> units;

	public UnitSiteContext(ProcessingContext parent, String contextUri) {
	    super(parent);

	    this.contextURI = contextUri;
	    this.units = new ArrayList<Unit>();
	}

	public boolean addUnit(String part) {
	    if(part == null) {
		return false;
	    }
	    Unit unit = findUnit(part);
	    if (unit != null) {
		this.units.add(unit);
		return true;
	    } else {
		return false;
	    }
	}

	private Unit findUnit(String part) {
	    Unit last = getLastUnit();

	    if (last == null) {
		last = RootDomainObject.getInstance().getInstitutionUnit();
	    }

	    return findSubUnit(part, last);
	}

	private Unit findSubUnit(String part, Unit unit) {
	    for (Unit sub : unit.getSubUnits()) {
		PartyTypeEnum type = sub.getPartyType() == null ? null : sub.getPartyType().getType();
		if (type != null && type == PartyTypeEnum.AGGREGATE_UNIT) {
		    Unit found = findSubUnit(part, sub);

		    if (found != null) {
			return found;
		    }
		}

		if (!isTypeAccepted(sub, type)) {
		    continue;
		}

		if (isMatchingName(sub, part)) {
		    return sub;
		}
	    }

	    return null;
	}

	protected boolean isTypeAccepted(Unit unit, PartyTypeEnum type) {
	    return true;
	}

	private boolean isMatchingName(Unit sub, String name) {
	    String acronym = sub.getAcronym();
	    if (acronym == null) {
		return false;
	    }
	    String lowerCaseName = name.toLowerCase();

	    if (acronym != null && acronym.toLowerCase().equals(lowerCaseName)) {
		return true;
	    }

	    MultiLanguageString unitName = sub.getNameI18n();
	    for (String language : unitName.getAllContents()) {
		if (language != null && language.toLowerCase().equals(lowerCaseName)) {
		    return true;
		}
	    }

	    return false;
	}

	public Unit getLastUnit() {
	    List<Unit> units = this.units;

	    if (units.isEmpty()) {
		return null;
	    } else {
		return units.get(units.size() - 1);
	    }
	}

	public UnitSite getSite() {
	    return getUnit().getSite();
	}

	public Unit getUnit() {
	    return getLastUnit();
	}

	public String getSiteBasePath() {
	    return String.format(this.contextURI, "%s", getContextIdInternal());
	}

	protected Integer getContextIdInternal() {
	    return getUnit().getIdInternal();
	}

    }

    public static String getUnitSitePath(Unit unit) {
	return getUnitSitePath(unit, PREFIX2);
    }

    public static String getUnitSitePath(Unit unit, String prefix) {
	List<Unit> path = unit.getParentUnitsPath();
	path.add(unit);
	path.remove(UnitUtils.readInstitutionUnit());

	StringBuilder url = new StringBuilder();
	url.append("/" + prefix);

	for (Unit u : path) {
	    PartyTypeEnum type = u.getPartyType() == null ? null : u.getPartyType().getType();
	    if (type != null && type == PartyTypeEnum.AGGREGATE_UNIT) {
		continue;
	    }

	    url.append("/" + getPathElement(u));
	}

	return url.toString();
    }

    private static String getPathElement(Unit unit) {
	String element = unit.getAcronym();

	if (element == null) {
	    element = unit.getNameI18n().getContent(Language.getApplicationLanguage());
	}

	return StringUtils.normalize(element.toLowerCase().replace(' ', '-').replace('/', '-'));
    }
}
