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
package org.fenixedu.academic.domain.teacher;

import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class TeacherServiceComment extends TeacherServiceComment_Base {

    public TeacherServiceComment(TeacherService teacherService, String content) {
        super();
        super.setContent(content);
        setLastModifiedDate(new DateTime());
        setTeacherService(teacherService);
        setCreationDate(new DateTime());
        setCreatedBy(Authenticate.getUser().getPerson());
        new TeacherServiceLog(getTeacherService(), BundleUtil.getString(Bundle.TEACHER_CREDITS,
                "label.teacher.teacherServiceComment.create", content, getCreationDate().toString("yyyy-MM-dd HH:mm")));
    }

    @Override
    public void setContent(String content) {
        super.setContent(content);
        setLastModifiedDate(new DateTime());
        new TeacherServiceLog(getTeacherService(), BundleUtil.getString(Bundle.TEACHER_CREDITS,
                "label.teacher.teacherServiceComment.edit", content, getCreationDate().toString("yyyy-MM-dd HH:mm"),
                getLastModifiedDate().toString("yyyy-MM-dd HH:mm")));
    }

    public boolean getCanEdit() {
        return Authenticate.getUser().getPerson().equals(getCreatedBy());
    }

    @Atomic
    @Override
    public void delete() {
        new TeacherServiceLog(getTeacherService(), BundleUtil.getString(Bundle.TEACHER_CREDITS,
                "label.teacher.teacherServiceComment.delete", getContent(), getCreationDate().toString(), getLastModifiedDate()
                        .toString("yyyy-MM-dd HH:mm")));
        setCreatedBy(null);
        super.delete();
    }

}
