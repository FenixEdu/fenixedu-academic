/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.spring.controller.teacher.authorization;

import org.fenixedu.academic.domain.TeacherCategory;
import org.fenixedu.bennu.core.domain.User;

public class FormBean extends SearchBean {

    private User user;
    private TeacherCategory category;
    private Double lessonHours;
    private Boolean contracted;
    private Boolean revoked;

    public Boolean getContracted() {
        return contracted;
    }

    public void setContracted(Boolean contracted) {
        this.contracted = contracted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TeacherCategory getCategory() {
        return category;
    }

    public void setCategory(TeacherCategory category) {
        this.category = category;
    }

    public Double getLessonHours() {
        return lessonHours;
    }

    public void setLessonHours(Double lessonHours) {
        this.lessonHours = lessonHours;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

}
