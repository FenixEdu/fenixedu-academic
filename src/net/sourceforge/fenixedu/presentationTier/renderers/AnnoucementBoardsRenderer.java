package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableHeader;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class AnnoucementBoardsRenderer extends OutputRenderer {

    private String sortBy;
    private String classes;
    private String boardUrl;
    private String managerUrl;
    private String rssUrl;
    private String rssImage;
    private String removeFavouriteUrl;
    private String addFavouriteUrl;
    
    public String getRemoveFavouriteUrl() {
        return removeFavouriteUrl;
    }

    public void setRemoveFavouriteUrl(String removeFavouriteUrl) {
        this.removeFavouriteUrl = removeFavouriteUrl;
    }

    public String getAddFavouriteUrl() {
        return addFavouriteUrl;
    }

    public void setAddFavouriteUrl(String addFavouriteUrl) {
        this.addFavouriteUrl = addFavouriteUrl;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public String getRssImage() {
        return rssImage;
    }

    public void setRssImage(String rssImage) {
        this.rssImage = rssImage;
    }

    public String getManagerUrl() {
	return managerUrl;
    }

    public void setManagerUrl(String managerUrl) {
	this.managerUrl = managerUrl;
    }

    public String getBoardUrl() {
	return boardUrl;
    }

    public void setBoardUrl(String boardUrl) {
	this.boardUrl = boardUrl;
    }

    public String getClasses() {
	return classes;
    }

    public void setClasses(String classes) {
	this.classes = classes;
    }

    public String getSortBy() {
	return sortBy;
    }

    public void setSortBy(String sortBy) {
	this.sortBy = sortBy;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {

	List<AnnouncementBoard> boards = getSortBy() != null ? RenderUtils.sortCollectionWithCriteria(
		(Collection<AnnouncementBoard>) object, getSortBy()) : new ArrayList<AnnouncementBoard>((Collection) object);
	return new AnnouncementBoardsRendererLayout(boards);

    }

    private class AnnouncementBoardsRendererLayout extends Layout {

	private List<AnnouncementBoard> boards;
	private boolean canManageAtLeastOneBoard;
	private boolean canReadAtLeastOneBoard;

	public AnnouncementBoardsRendererLayout(List<AnnouncementBoard> boards) {
	    this.boards = boards;
	    this.canManageAtLeastOneBoard = canManageAtLeastOneBoard();
	    this.canReadAtLeastOneBoard = canReadAtLeastOneBoard();
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    HtmlTable table = new HtmlTable();
	    table.setClasses(getClasses());
	    generateHeader(table);
	    for (AnnouncementBoard board : boards) {
		generateDataRow(table, board);
	    }
	    return table;
	}

	private void generateDataRow(HtmlTable table, AnnouncementBoard board) {
	    HtmlTableRow dataRow = table.createRow();
	    HtmlTableCell nameCell = dataRow.createCell();
	    HtmlText boardName = new HtmlText(board.getName().getContent());
	    if (board.isCurrentUserReader()) {
		HtmlLink link = generateLinkForBoard(board);
		link.setBody(boardName);
		nameCell.setBody(link);
	    } else {
		nameCell.setBody(boardName);
	    }

	    dataRow.createCell(board.getFullName()).setClasses("smalltxt2 lowlight1");
	    dataRow.createCell(board.getReaders() == null ? RenderUtils.getResourceString("MESSAGING_RESOURCES", "label.public")
		    : RenderUtils.getResourceString("MESSAGING_RESOURCES", "label.private"));
	    HtmlInlineContainer container = new HtmlInlineContainer();
	    dataRow.createCell().setBody(container);
	    
	    if(board.getBookmarkOwner().contains(AccessControl.getPerson())) {
		container.addChild(new HtmlText(RenderUtils.getResourceString("MESSAGING_RESOURCES", "label.yes")));
		container.addChild(new HtmlText("("));
		HtmlLink link = generateLinkToRemoveFavourite(board);
		link.setBody(new HtmlText(RenderUtils.getResourceString("MESSAGING_RESOURCES","label.remove")));
		container.addChild(link);
		container.addChild(new HtmlText(")"));
	    }
	    else {
		container.addChild(new HtmlText(RenderUtils.getResourceString("MESSAGING_RESOURCES", "label.no")));
		container.addChild(new HtmlText("("));
		HtmlLink link = generateLinkToAddFavourite(board);
		link.setBody(new HtmlText(RenderUtils.getResourceString("MESSAGING_RESOURCES","label.add")));
		container.addChild(link);
		container.addChild(new HtmlText(")"));
	    }

	    if (canManageAtLeastOneBoard) {
		HtmlComponent component = null;
		if (board.isCurrentUserManager()) {
		    HtmlLink link = generateLinkForManageBoard(board);
		    link.setBody(new HtmlText(RenderUtils.getResourceString("MESSAGING_RESOURCES", "label.board.manage")));
		    component = link;
		} else {
		    component = new HtmlText("-");
		}
		dataRow.createCell().setBody(component);
	    }
	    if(canReadAtLeastOneBoard) {
		HtmlComponent component = null;
		if(board.isCurrentUserReader()) {
		    HtmlLink link = generateLinkForRss(board);
		    HtmlImage image = new HtmlImage();
		    image.setSource(getContext().getViewState().getRequest().getContextPath() + getRssImage());
		    link.setBody(image);
		    component = link;
		}
		else {
		    component = new HtmlText("-");
		}
		dataRow.createCell().setBody(component);
	    }
	}

	private HtmlLink generateLinkToAddFavourite(AnnouncementBoard board) {
	    HtmlLink link = new HtmlLink();
	    link.setUrl(RenderUtils.getFormattedProperties(getAddFavouriteUrl(), board));
	    link.setModuleRelative(true);
	    link.setContextRelative(true);
	    return link;
	}

	private HtmlLink generateLinkToRemoveFavourite(AnnouncementBoard board) {
	    HtmlLink link = new HtmlLink();
	    link.setUrl(RenderUtils.getFormattedProperties(getRemoveFavouriteUrl(), board));
	    link.setModuleRelative(true);
	    link.setContextRelative(true);
	    return link;
	}

	private HtmlLink generateLinkForRss(AnnouncementBoard board) {
	    HtmlLink link = new HtmlLink();
	    link.setUrl(RenderUtils.getFormattedProperties(getRssUrl(), board));
	    link.setModuleRelative(false);
	    link.setContextRelative(true);
	    return link;
	}

	private HtmlLink generateLinkForManageBoard(AnnouncementBoard board) {
	    HtmlLink link = new HtmlLink();
	    link.setUrl(RenderUtils.getFormattedProperties(getManagerUrl(), board));
	    link.setModuleRelative(true);
	    link.setContextRelative(true);
	    return link;
	}

	private HtmlLink generateLinkForBoard(AnnouncementBoard board) {
	    HtmlLink link = new HtmlLink();
	    link.setUrl(RenderUtils.getFormattedProperties(getBoardUrl(), board));
	    link.setModuleRelative(true);
	    link.setContextRelative(true);
	    return link;
	}

	private void generateHeader(HtmlTable table) {
	    HtmlTableHeader tableHeader = table.createHeader();
	    HtmlTableRow row = tableHeader.createRow();
	    row.createCell(RenderUtils.getResourceString("MESSAGING_RESOURCES", "label.board.name"));
	    row.createCell(RenderUtils.getResourceString("MESSAGING_RESOURCES", "label.board.unit"));
	    row.createCell(RenderUtils.getResourceString("MESSAGING_RESOURCES", "label.board.type"));
	    row.createCell(RenderUtils.getResourceString("MESSAGING_RESOURCES", "label.board.favourites"));
	    if (canManageAtLeastOneBoard) {
		row.createCell(RenderUtils.getResourceString("MESSAGING_RESOURCES", "label.board.permissions"));
	    }
	    if (canReadAtLeastOneBoard) {
		row.createCell(RenderUtils.getResourceString("MESSAGING_RESOURCES", "label.board.rss"));
	    }

	}

	private boolean canReadAtLeastOneBoard() {
	    for (AnnouncementBoard announcementBoard : boards) {
		if (announcementBoard.getReaders() == null) {
		    return true;
		}
	    }
	    return false;
	}

	private boolean canManageAtLeastOneBoard() {
	    for (AnnouncementBoard announcementBoard : boards) {
		if (announcementBoard.isCurrentUserManager()) {
		    return true;
		}
	    }
	    return false;
	}

    }
}
