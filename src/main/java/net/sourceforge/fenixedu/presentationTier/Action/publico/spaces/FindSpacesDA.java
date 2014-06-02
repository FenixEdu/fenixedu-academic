package net.sourceforge.fenixedu.presentationTier.Action.publico.spaces;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.spaceManager.FindSpacesBean;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.spaces.domain.BlueprintFile;
import org.fenixedu.spaces.domain.BlueprintFile.BlueprintTextRectangles;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.services.SpaceBlueprintsDWGProcessor;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;

@Mapping(module = "publico", path = "/findSpaces", attribute = "findSpacesForm", formBean = "findSpacesForm", scope = "request",
parameter = "method")
@Forwards(value = { @Forward(name = "listFoundSpaces", path = "list-found-spaces"),
        @Forward(name = "viewSelectedSpace", path = "view-selected-space") })
public class FindSpacesDA extends FenixDispatchAction {

    public ActionForward prepareSearchSpaces(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        FindSpacesBean bean = new FindSpacesBean();
        request.setAttribute("bean", bean);
        return mapping.findForward("listFoundSpaces");
    }

    public ActionForward prepareSearchSpacesPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        FindSpacesBean bean = getRenderedObject("beanWithLabelToSearchID");
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("beanWithLabelToSearchID");
        return mapping.findForward("listFoundSpaces");
    }

    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        FindSpacesBean bean = getRenderedObject("beanWithLabelToSearchID");
        if (bean != null) {

            String labelToSearch = bean.getLabelToSearch();
            Space campus = bean.getCampus();
            Space building = bean.getBuilding();

            if (campus != null && building == null && (labelToSearch == null || StringUtils.isEmpty(labelToSearch.trim()))) {
                addActionMessage(request, "error.findSpaces.empty.building");
                request.setAttribute("bean", bean);
                return mapping.findForward("listFoundSpaces");
            }

            if (campus == null && (labelToSearch == null || StringUtils.isEmpty(labelToSearch.trim()))) {
                addActionMessage(request, "error.findSpaces.empty.labelToSearch");
                request.setAttribute("bean", bean);
                return mapping.findForward("listFoundSpaces");
            }

            List<FindSpacesBean> result = new ArrayList<FindSpacesBean>();
            Set<Space> resultSpaces = SpaceUtils.findSpaces(labelToSearch, campus, building, bean.getSearchType());
            for (Space space : resultSpaces) {
                result.add(new FindSpacesBean(space, bean.getSearchType(), AcademicInterval
                        .readDefaultAcademicInterval(AcademicPeriod.SEMESTER)));
            }

            request.setAttribute("foundSpaces", result);
        }

        request.setAttribute("bean", bean);
        return mapping.findForward("listFoundSpaces");
    }

    public ActionForward searchWithExtraOptions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        FindSpacesBean bean = getRenderedObject("beanWithLabelToSearchID");
        bean.setExtraOptions(true);
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("beanWithLabelToSearchID");
        return mapping.findForward("listFoundSpaces");
    }

    public ActionForward searchWithoutExtraOptions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        FindSpacesBean bean = getRenderedObject("beanWithLabelToSearchID");
        bean.setExtraOptions(false);
        bean.setCampus(null);
        bean.setBuilding(null);
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("beanWithLabelToSearchID");
        return mapping.findForward("listFoundSpaces");
    }

    public ActionForward viewSpace(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Space space = getSpaceFromParameter(request);
        if (space != null) {
            setBlueprintTextRectangles(request, space);
            List<Space> containedSpaces =
                    space.getChildren().stream().sorted(SpaceUtils.COMPARATOR_BY_PRESENTATION_NAME).collect(Collectors.toList());
            request.setAttribute("containedSpaces", containedSpaces);
            request.setAttribute("selectedSpace",
                    new FindSpacesBean(space, AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER)));
        }

        return mapping.findForward("viewSelectedSpace");
    }

    private Boolean isToViewBlueprintNumbers(HttpServletRequest request) {
        final String viewBlueprintNumbersString =
                request.getParameterMap().containsKey("viewBlueprintNumbers") ? request.getParameter("viewBlueprintNumbers") : (String) request
                        .getAttribute("viewBlueprintNumbers");
        return viewBlueprintNumbersString != null ? Boolean.valueOf(viewBlueprintNumbersString) : null;
    }

    private Boolean isToViewOriginalSpaceBlueprint(HttpServletRequest request) {
        final String viewOriginalSpaceBlueprintString =
                request.getParameterMap().containsKey("viewOriginalSpaceBlueprint") ? request
                        .getParameter("viewOriginalSpaceBlueprint") : (String) request.getAttribute("viewOriginalSpaceBlueprint");
        return viewOriginalSpaceBlueprintString != null ? Boolean.valueOf(viewOriginalSpaceBlueprintString) : null;
    }

    private Boolean isToViewSpaceIdentifications(HttpServletRequest request) {
        final String viewSpaceIdentificationsString =
                request.getParameterMap().containsKey("viewSpaceIdentifications") ? request
                        .getParameter("viewSpaceIdentifications") : (String) request.getAttribute("viewSpaceIdentifications");
        return viewSpaceIdentificationsString != null ? Boolean.valueOf(viewSpaceIdentificationsString) : null;
    }

    private Boolean isToViewDoorNumbers(HttpServletRequest request) {
        final String viewDoorNumbersString =
                request.getParameterMap().containsKey("viewDoorNumbers") ? request.getParameter("viewDoorNumbers") : (String) request
                        .getAttribute("viewDoorNumbers");
        return viewDoorNumbersString != null ? Boolean.valueOf(viewDoorNumbersString) : null;
    }

    private BigDecimal getScalePercentage(HttpServletRequest request) {
        final String scalePercentageString =
                request.getParameterMap().containsKey("scalePercentage") ? request.getParameter("scalePercentage") : (String) request
                        .getAttribute("scalePercentage");
        return scalePercentageString != null ? BigDecimal.valueOf(Double.valueOf(scalePercentageString)) : null;
    }

    public ActionForward viewSpaceBlueprint(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
//        return new ManageSpaceBlueprintsDA().view(mapping, actionForm, request, response);

        Boolean isToViewOriginalSpaceBlueprint = isToViewOriginalSpaceBlueprint(request);

        Boolean viewBlueprintNumbers = isToViewBlueprintNumbers(request);
        Boolean isToViewIdentifications = isToViewSpaceIdentifications(request);
        Boolean isToViewDoorNumbers = isToViewDoorNumbers(request);

        BigDecimal scalePercentage = getScalePercentage(request);

        DateTime now = new DateTime();

        Space space = getDomainObject(request, "spaceId");

        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=blueprint.jpeg");
        final ServletOutputStream writer = response.getOutputStream();

        SpaceBlueprintsDWGProcessor.writeBlueprint(space, now, isToViewOriginalSpaceBlueprint, viewBlueprintNumbers,
                isToViewIdentifications, isToViewDoorNumbers, scalePercentage, writer);

        return null;
    }

    // Private Methods

    private Space getSpaceFromParameter(final HttpServletRequest request) {
        final DomainObject obj = getDomainObject(request, "spaceID");
        return obj instanceof Space ? (Space) obj : null;
    }

    private Space getSuroundingSpaceMostRecentBlueprint(Space space) {
        Optional<BlueprintFile> blueprintFile = space.getBlueprintFile();
        if (blueprintFile.isPresent()) {
            return space;
        }

        if (space.getParent() != null) {
            return getSuroundingSpaceMostRecentBlueprint(space.getParent());
        }

        return null;
    }

    private void setBlueprintTextRectangles(HttpServletRequest request, Space space) throws IOException {
        DateTime now = new DateTime();

        Space spaceWithBlueprint = getSuroundingSpaceMostRecentBlueprint(space);

        BlueprintFile mostRecentBlueprint = spaceWithBlueprint.getBlueprintFile().get();

        if (mostRecentBlueprint != null) {

            final byte[] blueprintBytes = mostRecentBlueprint.getContent();
            final InputStream inputStream = new ByteArrayInputStream(blueprintBytes);
            BlueprintTextRectangles blueprintTextRectangles =
                    SpaceBlueprintsDWGProcessor.getBlueprintTextRectangles(inputStream, spaceWithBlueprint, now, false, false,
                            true, false, null);

            request.setAttribute("mostRecentBlueprint", mostRecentBlueprint);
            request.setAttribute("blueprintTextRectangles", blueprintTextRectangles);
        }
    }
}
