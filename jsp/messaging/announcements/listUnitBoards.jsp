<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

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
		<tr>
			<td style="text-align: left;">
				<logic:equal name="board" property="reader" value="true">
					<html:link title="<%= board.getQualifiedName()%>"
							page="/announcements/announcementsStartPageHandler.do?method=viewAnnouncements"
							paramId="announcementBoardId" paramName="board" paramProperty="idInternal">
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
					(<html:link title="<%= board.getQualifiedName()%>"
							page="/announcements/announcementsStartPageHandler.do?method=removeBookmark"
							paramId="announcementBoardId" paramName="board" paramProperty="idInternal">
						<bean:message key="label.remove" bundle="MESSAGING_RESOURCES"/>
					</html:link>)
				</logic:equal>
				<logic:notEqual name="board" property="bookmarkOwner" value="true">
					<bean:message key="label.no" bundle="MESSAGING_RESOURCES"/>
					(<html:link title="<%= board.getQualifiedName()%>"
							page="/announcements/announcementsStartPageHandler.do?method=addBookmark"
							paramId="announcementBoardId" paramName="board" paramProperty="idInternal">
						<bean:message key="label.add" bundle="MESSAGING_RESOURCES"/>
					</html:link>)
				</logic:notEqual>
			</td>
			<td class="acenter">
				<logic:equal name="board" property="manager" value="true">
					<html:link title="<%= board.getQualifiedName()%>"
							page="<%= "/announcements/manage" + board.getClass().getSimpleName() + ".do?method=prepareEditAnnouncementBoard&amp;tabularVersion=true" %>"
							paramId="announcementBoardId" paramName="board" paramProperty="idInternal">
						<bean:message key="label.manage" bundle="MESSAGING_RESOURCES"/>
					</html:link>
				</logic:equal>
			</td>
			<td class="acenter">
				<logic:empty name="board" property="readers">
					<html:link module="" page="/external/announcementsRSS.do?method=simple"
							paramId="announcementBoardId" paramName="board" paramProperty="idInternal">
							<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>
					</html:link>				
				</logic:empty>
			</td>
		</tr>
	</logic:iterate>
</table>
