<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>

<html:xhtml/>

<table class="tstyle2 tdcenter mtop05">	
	<tr>
		<th>
			<bean:message key="label.name"/>
		</th>
		<th>
			<bean:message key="label.board.unit" bundle="MESSAGING_RESOURCES"/>
		</th>
		<th>
			<bean:message key="label.type" bundle="MESSAGING_RESOURCES"/>
		</th>
		<th style="width: 100px;">
			<bean:message key="label.favorites" bundle="MESSAGING_RESOURCES"/>
		</th>
		<th>
			<bean:message key="label.permissions" bundle="MESSAGING_RESOURCES"/>
		</th>
		<th>
			<bean:message key="label.rss" bundle="MESSAGING_RESOURCES"/>
		</th>
	</tr>
	<logic:iterate id="board" name="boards" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard">
		<bean:define id="qualifiedName" name="board" property="qualifiedName" type="java.lang.String"/>
		
		<tr>
			<td style="text-align: left;">
				<logic:equal name="board" property="reader" value="true">
					<html:link title="<%= qualifiedName %>"
							page="/announcements/announcementsStartPageHandler.do?method=viewAnnouncements"
							paramId="announcementBoardId" paramName="board" paramProperty="externalId">
						<bean:write name="board" property="name"/>
					</html:link>
				</logic:equal>
				<logic:notEqual name="board" property="reader" value="true">
					<bean:write name="board" property="name"/>
				</logic:notEqual>
			</td>
			<td class="smalltxt2 lowlight1" style="text-align: left;">
				<bean:write name="board" property="fullName"/>
			</td>
			<td class="acenter">
				<logic:empty name="board" property="readers">
					<bean:message key="label.public" bundle="MESSAGING_RESOURCES"/>
				</logic:empty>
				<logic:notEmpty name="board" property="readers">
					<bean:message key="label.private" bundle="MESSAGING_RESOURCES"/>
				</logic:notEmpty>					
			</td>
			<td class="acenter">
				<logic:equal name="board" property="bookmarkOwner" value="true">
					<bean:message key="label.yes" bundle="MESSAGING_RESOURCES"/>
					(<html:link title="<%= qualifiedName %>"
							page="/announcements/announcementsStartPageHandler.do?method=removeBookmark"
							paramId="announcementBoardId" paramName="board" paramProperty="externalId">
						<bean:message key="label.remove" bundle="MESSAGING_RESOURCES"/>
					</html:link>)
				</logic:equal>
				<logic:notEqual name="board" property="bookmarkOwner" value="true">
					<bean:message key="label.no" bundle="MESSAGING_RESOURCES"/>
					(<html:link title="<%= qualifiedName %>"
							page="/announcements/announcementsStartPageHandler.do?method=addBookmark"
							paramId="announcementBoardId" paramName="board" paramProperty="externalId">
						<bean:message key="label.add" bundle="MESSAGING_RESOURCES"/>
					</html:link>)
				</logic:notEqual>
			</td>
			<td class="acenter">
				<logic:equal name="board" property="manager" value="true">
					<html:link title="<%= qualifiedName %>"
							page="<%= "/announcements/manage" + board.getClass().getSimpleName() + ".do?method=prepareEditAnnouncementBoard&amp;tabularVersion=true" %>"
							paramId="announcementBoardId" paramName="board" paramProperty="externalId">
						<bean:message key="label.manage" bundle="MESSAGING_RESOURCES"/>
					</html:link>
				</logic:equal>
			</td>
			<td class="acenter">
				<logic:empty name="board" property="readers">
					<html:link module="" page="/external/announcementsRSS.do?method=simple" paramId="announcementBoardId" paramName="board" paramProperty="externalId" styleClass="tdnone">
						<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>
					</html:link>				
				</logic:empty>
			</td>
		</tr>
	</logic:iterate>
</table>
