package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadLessonsExamsAndPunctualRoomsOccupationsInWeekAndRoom;
import net.sourceforge.fenixedu.applicationTier.Servico.space.DeleteSpace;
import net.sourceforge.fenixedu.applicationTier.Servico.space.DeleteSpaceInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.space.MergeRooms;
import net.sourceforge.fenixedu.applicationTier.Servico.space.MoveSpace;
import net.sourceforge.fenixedu.applicationTier.Servico.space.SpaceAccessGroupsManagement;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.AccessGroupPersonBean;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.MoveSpaceBean;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.ViewEventSpaceOccupationsBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.resource.ResourceResponsibility;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.Blueprint.BlueprintTextRectangles;
import net.sourceforge.fenixedu.domain.space.BlueprintFile;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Floor;
import net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.Space.SpaceAccessGroupType;
import net.sourceforge.fenixedu.domain.space.SpaceComparator;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;
import net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.spaceBlueprints.SpaceBlueprintsDWGProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Mapping(module = "SpaceManager", path = "/manageSpaces", attribute = "spaceContextForm", formBean = "spaceContextForm",
        scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "ShowCreateSubSpaceForm", path = "/spaceManager/createSubSpace.jsp", tileProperties = @Tile(
                title = "private.spacemanagement.searchspaces")),
        @Forward(name = "CreateSpaceInformation", path = "/spaceManager/createSpaceInformation.jsp", tileProperties = @Tile(
                title = "private.spacemanagement.searchspaces")),
        @Forward(name = "CompareRoomsBeforeMerge", path = "/spaceManager/compareRoomsBeforeMerge.jsp"),
        @Forward(name = "ManageSpace", path = "/spaceManager/manageSpace.jsp", tileProperties = @Tile(
                title = "private.spacemanagement.searchspaces")),
        @Forward(name = "PrepareMergeSpace", path = "/spaceManager/mergeRoom.jsp"),
        @Forward(name = "PrepareMoveSpace", path = "/spaceManager/moveSpace.jsp", tileProperties = @Tile(
                title = "private.spacemanagement.searchspaces")),
        @Forward(name = "ViewSpaceInformation", path = "/spaceManager/viewSpaceInformation.jsp"),
        @Forward(name = "ShowSpaces", path = "/spaceManager/index.jsp"),
        @Forward(name = "ViewEventSpaceOccupations", path = "/spaceManager/viewEventSpaceOccupations.jsp"),
        @Forward(name = "ManageSpaceAccessGroups", path = "/spaceManager/manageAccessGroups.jsp", tileProperties = @Tile(
                title = "private.spacemanagement.searchspaces")),
        @Forward(name = "EditSpace", path = "/spaceManager/editSpace.jsp", tileProperties = @Tile(
                title = "private.spacemanagement.searchspaces")) })
public class ManageSpacesDA extends FenixDispatchAction {

    public ActionForward viewSpaces(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final SortedSet<Space> spaces = new TreeSet<Space>(SpaceComparator.SPACE_COMPARATOR_BY_CLASS);
        for (final Resource resource : rootDomainObject.getResources()) {
            if (resource.isSpace() && !((Space) resource).hasSuroundingSpace()) {
                spaces.add((Space) resource);
            }
        }
        request.setAttribute("spaces", spaces);
        return mapping.findForward("ShowSpaces");
    }

    protected SpaceInformation executeSpaceFactoryMethod(final HttpServletRequest request) throws FenixServiceException,
            DomainException {
        Object serviceResult = executeFactoryMethod();
        if (serviceResult instanceof Space) {
            return ((Space) serviceResult).getSuroundingSpace() != null ? ((Space) serviceResult).getSuroundingSpace()
                    .getSpaceInformation() : ((Space) serviceResult).getSpaceInformation();
        } else if (serviceResult instanceof SpaceInformation) {
            return ((SpaceInformation) serviceResult);
        } else {
            return null;
        }
    }

    protected ActionForward manageSpace(final ActionMapping mapping, final HttpServletRequest request,
            final SpaceInformation spaceInformation) throws IOException {

        return returnToManageSpacePage(mapping, request, spaceInformation);
    }

    public ActionForward manageSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        return manageSpace(mapping, request, spaceInformation);
    }

    public ActionForward viewSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        Space space = getSpaceFromParameter(request);
        return manageSpace(mapping, request, space.getSpaceInformation());
    }

    public ActionForward executeFactoryMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        SpaceInformation spaceInformation = null;
        try {
            spaceInformation = executeSpaceFactoryMethod(request);
        } catch (DomainException e) {
            saveMessages(request, e);
            spaceInformation = getSpaceInformationFromParameter(request);
        }
        return (spaceInformation == null) ? viewSpaces(mapping, form, request, response) : manageSpace(mapping, request,
                spaceInformation);
    }

    public ActionForward deleteSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Space space = getSpaceFromParameter(request);
        if (space == null) {
            throw new DomainException("error.spaces.cannot.delete.inexistent.space");
        }

        final Space surroundingSpace = space.getSuroundingSpace();

        try {
            DeleteSpace.run(space);
        } catch (DomainException e) {
            saveMessages(request, e);
            return manageSpace(mapping, request, space.getSpaceInformation());
        }

        if (surroundingSpace == null) {
            return viewSpaces(mapping, form, request, response);
        } else {
            final SpaceInformation surroundingSpaceInformation = surroundingSpace.getSpaceInformation();
            return manageSpace(mapping, request, surroundingSpaceInformation);
        }
    }

    public ActionForward setSelectedSpaceInformationAndForward(final ActionMapping mapping, final HttpServletRequest request,
            final String forward) {
        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        return mapping.findForward(forward);
    }

    public ActionForward showCreateSubSpaceForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return setSelectedSpaceInformationAndForward(mapping, request, "ShowCreateSubSpaceForm");
    }

    public ActionForward viewSpaceInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return setSelectedSpaceInformationAndForward(mapping, request, "ViewSpaceInformation");
    }

    public ActionForward prepareEditSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return setSelectedSpaceInformationAndForward(mapping, request, "EditSpace");
    }

    public ActionForward prepareCreateSpaceInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        final FactoryExecutor factoryExecutor = spaceInformation.getSpaceFactoryEditor();
        request.setAttribute("SpaceFactoryEditor", factoryExecutor);
        return mapping.findForward("CreateSpaceInformation");
    }

    public ActionForward deleteSpaceInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {

        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        if (spaceInformation == null) {
            throw new DomainException("error.spaces.cannot.delete.inexistent.spaceInformation");
        }

        final Space space = spaceInformation.getSpace();

        try {
            DeleteSpaceInformation.run(spaceInformation);
        } catch (DomainException e) {
            saveMessages(request, e);
        }

        final SpaceInformation previousSpaceInformation = space.getSpaceInformation();
        return manageSpace(mapping, request, previousSpaceInformation);
    }

    public ActionForward manageAccessGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        final Space space = spaceInformation.getSpace();
        request.setAttribute("selectedSpace", space);
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        return mapping.findForward("ManageSpaceAccessGroups");
    }

    public ActionForward addPersonToAccessGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        final Space space = spaceInformation.getSpace();

        final IViewState viewState = RenderUtils.getViewState();
        AccessGroupPersonBean bean = (AccessGroupPersonBean) viewState.getMetaObject().getObject();
        RenderUtils.invalidateViewState();

        String groupExpression = null;
        Person person = (bean != null) ? bean.getPerson() : null;
        PersistentGroupMembers persistentGroupMembers = (bean != null) ? bean.getPersistentGroupMembers() : null;
        RoleType roleType = (bean != null) ? bean.getRoleType() : null;

        if (person != null) {
            groupExpression = new PersonGroup(person).getExpression();
        } else if (persistentGroupMembers != null) {
            groupExpression = new PersistentGroup(persistentGroupMembers).getExpression();
        } else if (roleType != null) {
            groupExpression = new RoleGroup(roleType).getExpression();
        }

        try {
            SpaceAccessGroupsManagement.run(space, (bean != null) ? bean.getAccessGroupType() : null, true,
                    (bean != null) ? bean.getMaintainElements() : false, groupExpression);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }
        return manageAccessGroups(mapping, form, request, response);
    }

    public ActionForward removePersonFromAccessGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        final Space space = spaceInformation.getSpace();
        final SpaceAccessGroupType groupType = SpaceAccessGroupType.valueOf(request.getParameter("spaceAccessGroupType"));
        final String groupExpression = getGroupExpressionFromParameter(request);

        try {
            SpaceAccessGroupsManagement.run(space, groupType, false, false, groupExpression);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }
        return manageAccessGroups(mapping, form, request, response);
    }

    public ActionForward exportSpaceInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        Space space = getSpaceFromParameter(request);

        try {
            String filename;
            Building spaceBuilding = space.getSpaceBuilding();
            if (spaceBuilding != null && !spaceBuilding.equals(space)) {
                filename =
                        spaceBuilding.getClass().getSimpleName() + "_"
                                + spaceBuilding.getSpaceInformation().getPresentationName().replace(' ', '_') + "_"
                                + space.getClass().getSimpleName() + "_"
                                + space.getSpaceInformation().getPresentationName().replace(' ', '_') + "_"
                                + new DateTime().toString("dd-MM-yyyy_hh:mm");
            } else {
                filename =
                        space.getClass().getSimpleName() + "_"
                                + space.getSpaceInformation().getPresentationName().replace(' ', '_') + "_"
                                + new DateTime().toString("dd-MM-yyyy_hh:mm");
            }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

            ServletOutputStream writer = response.getOutputStream();
            exportToXls(space, writer);

            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

    public ActionForward prepareMoveSpace(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        SpaceInformation spaceInformationFromParameter = getSpaceInformationFromParameter(request);
        Space space = spaceInformationFromParameter.getSpace();
        request.setAttribute("moveSpaceBean", new MoveSpaceBean(space));
        return mapping.findForward("PrepareMoveSpace");
    }

    public ActionForward moveSpace(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {

        final IViewState viewState = RenderUtils.getViewState();
        MoveSpaceBean bean = (MoveSpaceBean) viewState.getMetaObject().getObject();

        MoveSpace.run(bean);
        Space space = bean.getSpace();
        SpaceInformation spaceInformation = space.getSpaceInformation();

        return returnToManageSpacePage(mapping, request, spaceInformation);
    }

    public ActionForward prepareMergeRoom(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {

        SpaceInformation spaceInformationFromParameter = getSpaceInformationFromParameter(request);
        Space space = spaceInformationFromParameter.getSpace();
        request.setAttribute("moveSpaceBean", new MoveSpaceBean(space));
        return mapping.findForward("PrepareMergeSpace");
    }

    public ActionForward findDestinationRoomForProcessMerge(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, IOException {

        final IViewState viewState = RenderUtils.getViewState("findMergeDestinationRoomBean");
        MoveSpaceBean bean = (MoveSpaceBean) viewState.getMetaObject().getObject();

        String roomName = bean.getSpaceName();
        List<AllocatableSpace> foundSpaces = AllocatableSpace.readAllAllocatableSpacesByName(roomName);
        request.setAttribute("spaces", foundSpaces);
        request.setAttribute("moveSpaceBean", bean);

        return mapping.findForward("PrepareMergeSpace");
    }

    public ActionForward compareDestinationRoomWithFromRoom(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, IOException {

        AllocatableSpace fromRoom = getFromRoomFromParameter(request);
        AllocatableSpace destinationRoom = getDestinationRoomFromParameter(request);

        MoveSpaceBean bean = new MoveSpaceBean(fromRoom, destinationRoom);
        request.setAttribute("spaceBean", bean);

        return mapping.findForward("CompareRoomsBeforeMerge");
    }

    public ActionForward mergeRooms(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {

        final IViewState viewState = RenderUtils.getViewState("mergeRoomsBean");
        MoveSpaceBean bean = (MoveSpaceBean) viewState.getMetaObject().getObject();

        Space fromRoom = bean.getSpace();
        Space destinationRoom = bean.getSelectedParentSpace();

        MergeRooms.runMergeRooms((AllocatableSpace) fromRoom, (AllocatableSpace) destinationRoom);

        return returnToManageSpacePage(mapping, request, destinationRoom.getSpaceInformation());
    }

    public ActionForward viewEventSpaceOccupations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {

        Space space = null;
        YearMonthDay day = null;
        ViewEventSpaceOccupationsBean bean = getRenderedObject();

        if (bean != null) {

            space = bean.getAllocatableSpace();
            day = bean.getDay();
            Partial month = bean.getMonth();
            Partial year = bean.getYear();

            if (day != null
                    && (month == null || year == null || day.getMonthOfYear() != month.get(DateTimeFieldType.monthOfYear()) || day
                            .getYear() != year.get(DateTimeFieldType.year()))) {

                bean.setDay(null);
                day = null;
            }

            request.setAttribute("eventSpaceOccupationsBean", bean);

        } else {
            SpaceInformation spaceInformationFromParameter = getSpaceInformationFromParameter(request);
            space = spaceInformationFromParameter != null ? spaceInformationFromParameter.getSpace() : null;
            day = new YearMonthDay();
            request.setAttribute("eventSpaceOccupationsBean", new ViewEventSpaceOccupationsBean(day, (AllocatableSpace) space));
        }

        if (space != null && space.isAllocatableSpace() && day != null) {

            List<InfoObject> showOccupations =
                    ReadLessonsExamsAndPunctualRoomsOccupationsInWeekAndRoom.run((AllocatableSpace) space, day);
            request.setAttribute(PresentationConstants.LESSON_LIST_ATT, showOccupations);
        }

        return mapping.findForward("ViewEventSpaceOccupations");
    }

    // Private Methods

    private ActionForward returnToManageSpacePage(ActionMapping mapping, HttpServletRequest request,
            SpaceInformation spaceInformation) throws IOException {

        MoveSpaceBean spaceBean = getRenderedObject("subSpacesStateBeanID");
        spaceBean = (spaceBean == null) ? new MoveSpaceBean(spaceInformation.getSpace()) : spaceBean;

        Space space = spaceInformation.getSpace();
        Set<Space> spaces = space.getContainedSpacesByState(spaceBean.getSpaceState());
        setBlueprintTextRectangles(request, space);

        request.setAttribute("subSpacesStateBean", spaceBean);
        request.setAttribute("selectedSpace", space);
        request.setAttribute("spaces", spaces);
        request.setAttribute("selectedSpaceInformation", spaceInformation);

        return mapping.findForward("ManageSpace");
    }

    private void exportToXls(Space space, OutputStream outputStream) throws IOException {
        final List<Object> headers = getHeaders();
        final Spreadsheet spreadsheet = new Spreadsheet("GestãoDeEspaços", headers);
        fillSpreadSheet(space, spreadsheet);
        spreadsheet.exportToXLSSheet(outputStream);
    }

    private void fillSpreadSheet(Space space, final Spreadsheet spreadsheet) {
        for (Space subSpace : space.getContainedSpacesSet()) {

            if (subSpace.isActive() && subSpace.isRoom()) {

                Room room = (Room) subSpace;
                final Row row = spreadsheet.addRow();

                Building spaceBuilding = room.getSpaceBuilding();
                row.setCell((spaceBuilding != null) ? spaceBuilding.getSpaceInformation().getPresentationName() : "--");

                Floor spaceFloor = room.getSpaceFloorWithIntermediary();
                if (spaceFloor != null && spaceFloor.getSuroundingSpace() instanceof Floor) {
                    if (spaceFloor.getSpaceInformation().getLevel().intValue() == 1) {
                        row.setCell(spaceFloor.getSuroundingSpace().getSpaceInformation().getPresentationName() + "i");
                    } else {
                        row.setCell(spaceFloor.getSuroundingSpace().getSpaceInformation().getPresentationName());
                    }
                } else {
                    row.setCell((spaceFloor != null) ? spaceFloor.getSpaceInformation().getPresentationName() : "--");
                }

                row.setCell(room.getSpaceInformation().getDescription() != null ? room.getSpaceInformation().getDescription() : "--");
                row.setCell(room.getSpaceInformation().getIdentification() != null ? room.getSpaceInformation()
                        .getIdentification() : "--");
                row.setCell(room.getSpaceInformation().getDoorNumber() != null ? room.getSpaceInformation().getDoorNumber() : "--");
                row.setCell(room.getSpaceInformation().getBlueprintNumber() != null ? room.getSpaceInformation()
                        .getBlueprintNumber() : "--");
                row.setCell((room.getSpaceInformation().getRoomClassification() != null) ? room.getSpaceInformation()
                        .getRoomClassification().getPresentationCode() : "--");
                row.setCell((room.getSpaceInformation().getArea() != null) ? room.getSpaceInformation().getArea().toString() : "--");

                row.setCell(room.getSpaceInformation().getHeightQuality() != null ? room.getSpaceInformation().getHeightQuality()
                        .toString() : "--");
                row.setCell(room.getSpaceInformation().getIlluminationQuality() != null ? room.getSpaceInformation()
                        .getIlluminationQuality().toString() : "--");
                row.setCell(room.getSpaceInformation().getDistanceFromSanitaryInstalationsQuality() != null ? room
                        .getSpaceInformation().getDistanceFromSanitaryInstalationsQuality().toString() : "--");
                row.setCell(room.getSpaceInformation().getSecurityQuality() != null ? room.getSpaceInformation()
                        .getSecurityQuality().toString() : "--");
                row.setCell(room.getSpaceInformation().getAgeQuality() != null ? room.getSpaceInformation().getAgeQuality()
                        .toString() : "--");

                StringBuilder builder = new StringBuilder();
                for (ResourceResponsibility responsibility : room.getResourceResponsibility()) {
                    if (responsibility.isSpaceResponsibility()) {
                        Unit unit = ((SpaceResponsibility) responsibility).getUnit();
                        builder.append(unit.getPresentationName()).append("; ");
                    }
                }
                row.setCell(builder.toString());

                builder = new StringBuilder();
                for (UnitSpaceOccupation occupation : room.getUnitSpaceOccupations()) {
                    Unit unit = occupation.getUnit();
                    builder.append(unit.getPresentationName()).append("; ");
                }
                row.setCell(builder.toString());

                builder = new StringBuilder();
                for (PersonSpaceOccupation occupation : room.getPersonSpaceOccupations()) {
                    Person person = occupation.getPerson();
                    builder.append(person.getName() + " (" + person.getUsername() + "); ");
                }
                row.setCell(builder.toString());

                row.setCell(room.getSpaceInformation().getObservations());
            }

            if (subSpace.hasAnyContainedSpaces()) {
                fillSpreadSheet(subSpace, spreadsheet);
            }
        }
    }

    private List<Object> getHeaders() {
        final List<Object> headers = new ArrayList<Object>();

        headers.add("Edifício");
        headers.add("Piso");
        headers.add("Espaço");
        headers.add("Identificação do Espaço");
        headers.add("Número na Porta");
        headers.add("Número na Planta");
        headers.add("Classificação");
        headers.add("Área");

        headers.add("Qualid. em Pé Direito");
        headers.add("Qualid. em Iluminação");
        headers.add("Qualid. em Dist. às Instalações Sanitárias");
        headers.add("Qualid. em Segurança");
        headers.add("Qualid. em Vetustez");

        headers.add("Unidade(s) responsáveis");
        headers.add("Ocupantes (Unidades)");
        headers.add("Ocupantes (Pessoas)");

        headers.add("Observações");

        return headers;
    }

    private void setBlueprintTextRectangles(HttpServletRequest request, Space space) throws IOException {

        Boolean viewBlueprintNumbers = isToViewBlueprintNumbers(request);
        Boolean viewOriginalSpaceBlueprint = isToViewOriginalSpaceBlueprint(request);
        Boolean viewSpaceIdentifications = isToViewSpaceIdentifications(request);
        Boolean viewDoorNumbers = isToViewDoorNumbers(request);
        BigDecimal scalePercentage = getScalePercentage(request);

        Blueprint mostRecentBlueprint = space.getMostRecentBlueprint();
        Boolean suroundingSpaceBlueprint = mostRecentBlueprint == null;
        mostRecentBlueprint = (mostRecentBlueprint == null) ? space.getSuroundingSpaceMostRecentBlueprint() : mostRecentBlueprint;

        if (mostRecentBlueprint != null) {

            if (!viewDoorNumbers && !viewSpaceIdentifications && !viewBlueprintNumbers && !viewOriginalSpaceBlueprint) {
                viewSpaceIdentifications = Boolean.TRUE;
            }

            final BlueprintFile blueprintFile = mostRecentBlueprint.getBlueprintFile();
            final byte[] blueprintBytes = blueprintFile.getContent().getBytes();
            final InputStream inputStream = new ByteArrayInputStream(blueprintBytes);
            try {
                BlueprintTextRectangles blueprintTextRectangles =
                        SpaceBlueprintsDWGProcessor.getBlueprintTextRectangles(inputStream, mostRecentBlueprint.getSpace(),
                                viewBlueprintNumbers, viewOriginalSpaceBlueprint, viewSpaceIdentifications, viewDoorNumbers,
                                scalePercentage);
                request.setAttribute("blueprintTextRectangles", blueprintTextRectangles);
            } catch (final Error e) {
                if ("Dwg version not supported".equals(e.getMessage())) {
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("", new ActionMessage(e.getMessage()));
                    saveMessages(request, actionMessages);
                } else {
                    throw e;
                }
            }

            request.setAttribute("mostRecentBlueprint", mostRecentBlueprint);
            request.setAttribute("suroundingSpaceBlueprint", suroundingSpaceBlueprint);
        }

        request.setAttribute("scalePercentage", scalePercentage.doubleValue());
        request.setAttribute("viewDoorNumbers", viewDoorNumbers);
        request.setAttribute("viewSpaceIdentifications", viewSpaceIdentifications);
        request.setAttribute("viewBlueprintNumbers", viewBlueprintNumbers);
        request.setAttribute("viewOriginalSpaceBlueprint", viewOriginalSpaceBlueprint);
    }

    private void saveMessages(HttpServletRequest request, DomainException e) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
        saveMessages(request, actionMessages);
    }

    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
        final String spaceInformationIDString =
                request.getParameterMap().containsKey("spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
                        .getAttribute("spaceInformationID");
        return AbstractDomainObject.fromExternalId(spaceInformationIDString);
    }

    private String getGroupExpressionFromParameter(final HttpServletRequest request) {
        final String groupExpression =
                request.getParameterMap().containsKey("expression") ? request.getParameter("expression") : (String) request
                        .getAttribute("expression");
        return groupExpression;
    }

    private Boolean isToViewBlueprintNumbers(HttpServletRequest request) {
        final String viewBlueprintNumbersString =
                request.getParameterMap().containsKey("viewBlueprintNumbers") ? request.getParameter("viewBlueprintNumbers") : (String) request
                        .getAttribute("viewBlueprintNumbers");
        return viewBlueprintNumbersString != null ? Boolean.valueOf(viewBlueprintNumbersString) : Boolean.FALSE;
    }

    private Boolean isToViewDoorNumbers(HttpServletRequest request) {
        final String isToViewDoorNumbersString =
                request.getParameterMap().containsKey("viewDoorNumbers") ? request.getParameter("viewDoorNumbers") : (String) request
                        .getAttribute("viewDoorNumbers");
        return isToViewDoorNumbersString != null ? Boolean.valueOf(isToViewDoorNumbersString) : Boolean.FALSE;
    }

    private Boolean isToViewSpaceIdentifications(HttpServletRequest request) {
        final String viewSpaceIdentificationsString =
                request.getParameterMap().containsKey("viewSpaceIdentifications") ? request
                        .getParameter("viewSpaceIdentifications") : (String) request.getAttribute("viewSpaceIdentifications");
        return viewSpaceIdentificationsString != null ? Boolean.valueOf(viewSpaceIdentificationsString) : Boolean.FALSE;
    }

    private Boolean isToViewOriginalSpaceBlueprint(HttpServletRequest request) {
        final String viewOriginalSpaceBlueprintString =
                request.getParameterMap().containsKey("viewOriginalSpaceBlueprint") ? request
                        .getParameter("viewOriginalSpaceBlueprint") : (String) request.getAttribute("viewOriginalSpaceBlueprint");
        return viewOriginalSpaceBlueprintString != null ? Boolean.valueOf(viewOriginalSpaceBlueprintString) : Boolean.FALSE;
    }

    private BigDecimal getScalePercentage(HttpServletRequest request) {
        final String scalePercentageString =
                request.getParameterMap().containsKey("scalePercentage") ? request.getParameter("scalePercentage") : (String) request
                        .getAttribute("scalePercentage");
        return scalePercentageString != null ? BigDecimal.valueOf(Double.valueOf(scalePercentageString)) : BigDecimal.valueOf(50);
    }

    private Space getSpaceFromParameter(final HttpServletRequest request) {
        final String spaceIDString = request.getParameter("spaceID");
        return (Space) AbstractDomainObject.fromExternalId(spaceIDString);
    }

    private AllocatableSpace getDestinationRoomFromParameter(final HttpServletRequest request) {
        final String spaceIDString = request.getParameter("destinationRoomID");
        return (AllocatableSpace) AbstractDomainObject.fromExternalId(spaceIDString);
    }

    private AllocatableSpace getFromRoomFromParameter(final HttpServletRequest request) {
        final String spaceIDString = request.getParameter("fromRoomID");
        return (AllocatableSpace) AbstractDomainObject.fromExternalId(spaceIDString);
    }
}