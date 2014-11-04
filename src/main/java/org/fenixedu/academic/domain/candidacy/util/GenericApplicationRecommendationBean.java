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
package net.sourceforge.fenixedu.domain.candidacy.util;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.candidacy.GenericApplication;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplicationRecomentation;
import pt.ist.fenixframework.Atomic;

public class GenericApplicationRecommendationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String title;
    protected String name;
    protected String institution;
    protected String email;

    public GenericApplicationRecommendationBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Atomic
    public void requestRecommendation(final GenericApplication application) {
        new GenericApplicationRecomentation(application, title, name, institution, email);
    }

}
