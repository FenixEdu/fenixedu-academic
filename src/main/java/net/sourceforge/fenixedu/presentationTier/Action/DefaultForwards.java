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
package net.sourceforge.fenixedu.presentationTier.Action;

import org.apache.struts.actions.ForwardAction;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * Contains General forwards, previously in struts-default.xml
 * 
 * Use Struts's {@link ForwardAction}, which takes the {@link Mapping}s parameter as the destination location.
 */
public final class DefaultForwards {

    @Mapping(path = "/showErrorPageRegistered", parameter = "/exception/errorRegistered.jsp")
    public static class ShowErrorPageRegistered extends ForwardAction {
    }

    @Mapping(path = "/nonExistingObject", parameter = "/nonExistingError.jsp")
    public static class NonExistingObject extends ForwardAction {
    }

    @Mapping(path = "/publicNotAuthorized", parameter = "/exception/publicNotAuthorized.jsp")
    public static class PublicNotAuthorized extends ForwardAction {
    }

    @Mapping(path = "/alumniReminder", parameter = "/showAlumniDataReminder.jsp")
    public static class AlumniReminder extends ForwardAction {
    }

    @Mapping(path = "/notFound", module = "publico", parameter = "/notFound.jsp")
    public static class NotFoundAction extends ForwardAction {
    }

    @Mapping(path = "/fenixEduIndex", parameter = "/fenixEduIndex.jsp")
    public static class FenixEduIndexAction extends ForwardAction {
    }

}
