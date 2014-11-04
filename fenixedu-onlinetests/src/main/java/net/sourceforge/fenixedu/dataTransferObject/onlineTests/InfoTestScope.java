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
/*
 * Created on 5/Fev/2004
 *
 */
package org.fenixedu.academic.dto.onlineTests;

import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoObject;
import org.fenixedu.academic.domain.onlineTests.TestScope;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class InfoTestScope extends InfoObject {
    private InfoExecutionCourse infoObject;

    public InfoTestScope() {
    }

    public InfoExecutionCourse getInfoObject() {
        return infoObject;
    }

    private void setInfoExecutionCourse(InfoExecutionCourse object) {
        infoObject = object;
    }

    public void copyFromDomain(TestScope testScope) {
        super.copyFromDomain(testScope);
        if (testScope != null) {
            setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(testScope.getExecutionCourse()));
        }
    }

    public static InfoTestScope newInfoFromDomain(TestScope testScope) {
        InfoTestScope infoTestScope = null;
        if (testScope != null) {
            infoTestScope = new InfoTestScope();
            infoTestScope.copyFromDomain(testScope);
        }
        return infoTestScope;
    }

}