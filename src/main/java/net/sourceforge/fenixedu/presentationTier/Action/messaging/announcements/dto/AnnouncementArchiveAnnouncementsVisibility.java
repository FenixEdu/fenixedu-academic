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
 * Author : Goncalo Luiz
 * Creation Date: Jul 28, 2006,12:26:20 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jul 28, 2006,12:26:20 PM
 * 
 */
public enum AnnouncementArchiveAnnouncementsVisibility {
    ALL, ACTIVE, /* annoumcements for whose the today date is between the date of begin and end of publication AND they are visible*/
    VISIBLE; /* all visible announcements independently of the publication begin and end dates*/
    ;
}
