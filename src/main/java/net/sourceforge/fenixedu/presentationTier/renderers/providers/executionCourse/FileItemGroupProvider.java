/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileContentCreationBean;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemPermissionBean;

import org.fenixedu.bennu.core.groups.AnyoneGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.LoggedGroup;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class FileItemGroupProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        Site site = null;

        if (source instanceof FileContent) {
            FileContent fileContent = (FileContent) source;
            site = fileContent.getSite();

            if (site == null) {
                site = fileContent.getAnnouncementBoard().getSite();
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

    private List<Group> getDefaultPermissions() {
        List<Group> groups = new ArrayList<Group>();
        groups.add(AnyoneGroup.get());
        groups.add(LoggedGroup.get());
        return groups;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
