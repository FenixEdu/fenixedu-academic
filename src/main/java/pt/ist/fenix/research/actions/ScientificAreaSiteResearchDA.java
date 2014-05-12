package pt.ist.fenix.research.actions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.publico.scientificalArea.PublicScientificAreaSiteDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

@Mapping(module = "publico", path = "/scientificArea/viewSiteResearch")
@Forwards({ @Forward(name = "showPublications", path = "/publico/scientificAreaSite/showPublications.jsp",
        tileProperties = @Tile(extend = "definition.public.basicUnit")) })
public class ScientificAreaSiteResearchDA extends PublicScientificAreaSiteDA {
    public ActionForward showPublications(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Unit unit = getUnit(request);

        if (unit != null && unit.getSite() != null) {
            OldCmsSemanticURLHandler.selectSite(request, unit.getSite());
        }
        prepareResearchersForResponse(request, unit);

        return mapping.findForward("showPublications");
    }

    protected void prepareResearchersForResponse(HttpServletRequest request, Unit unit) {
        putResearchersOnRequest(request, unit, false);
    }

    protected void putResearchersOnRequest(HttpServletRequest request, Unit unit, boolean checkSubunits) {
        Set<Person> people = new HashSet<>();
        people.addAll((Collection<Person>) unit.getChildParties(Person.class));

        if (checkSubunits) {
            for (Unit child : unit.getAllSubUnits()) {
                people.addAll((Collection<? extends Person>) child.getChildParties(Person.class));
            }
        }

        Iterable<String> ids = Iterables.transform(people, new Function<Person, String>() {
            @Override
            public String apply(Person person) {
                return person.getUsername();
            }
        });
        request.setAttribute("researchers", Joiner.on(',').join(ids));
    }
}
