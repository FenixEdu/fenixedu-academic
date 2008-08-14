package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileContentCreationBean;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemPermissionBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class FileItemGroupProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	Site site = null;

	if (source instanceof FileContent) {
	    FileContent fileContent = (FileContent) source;
	    site = fileContent.getSite();

	    if (site == null) {
		UnitAnnouncementBoard board = fileContent.getAttachment().getParent(UnitAnnouncementBoard.class);
		if (board != null) {
		    site = board.getUnit().getSite();
		} else {
		    ExecutionCourseAnnouncementBoard executionBoard = fileContent.getAttachment().getParent(
			    ExecutionCourseAnnouncementBoard.class);
		    if (executionBoard != null) {
			site = executionBoard.getExecutionCourse().getSite();
		    }
		}
	    }
	} else if (source instanceof FileContentCreationBean) {
	    FileContentCreationBean bean = (FileContentCreationBean) source;
	    site = bean.getSite();

	    if (site == null) {
		if (bean.getFileHolder() instanceof UnitAnnouncementBoard) {
		    UnitAnnouncementBoard board = (UnitAnnouncementBoard) bean.getFileHolder();
		    site = board != null ? board.getUnit().getSite() : null;
		} else if (bean.getFileHolder() instanceof ExecutionCourseAnnouncementBoard) {
		    ExecutionCourseAnnouncementBoard board = (ExecutionCourseAnnouncementBoard) bean.getFileHolder();
		    site = board != null ? board.getExecutionCourse().getSite() : null;
		}
	    }
	} else if (source instanceof FileItemPermissionBean) {
	    FileItemPermissionBean bean = (FileItemPermissionBean) source;
	    site = bean.getFileItem().getSite();
	}

	return site != null ? site.getContextualPermissionGroups() : getDefaultPermissions();
    }

    private List<IGroup> getDefaultPermissions() {
	List<IGroup> groups = new ArrayList<IGroup>();
	groups.add(new EveryoneGroup());
	groups.add(new InternalPersonGroup());
	return groups;
    }

    public Converter getConverter() {
	return null;
    }

}
