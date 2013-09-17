<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>

<html:xhtml/>
<em><bean:message key="label.communicationPortal.header" bundle="MESSAGING_RESOURCES"/></em>
<h2><bean:message key="label.manageChannel" bundle="MESSAGING_RESOURCES"/></h2>


<jsp:include flush="true" page="/messaging/context.jsp"/>

<h3 class="mbottom0 fwnormal">Unidade: <span class="emphasis1"><bean:write name="unit" property="name"/></span></h3>

<logic:present name="announcementBoard">
<bean:define id="contextPrefix" name="contextPrefix" />
<bean:define id="extraParameters" name="extraParameters" />
<bean:define id="announcementBoardId" name="announcementBoard" property="externalId"/>

<html:form action="/announcements/manageUnitAnnouncementBoard" >
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editAnnouncementBoard"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.keyUnit" property="keyUnit"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.announcementBoardId" property="announcementBoardId" value="<%=request.getParameter("announcementBoardId")%>"/>		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.returnAction" property="returnAction"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.returnMethod" property="returnMethod"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tabularVersion" property="tabularVersion"/>
		<table class="tstyle5 thlight thright thmiddle">
			<tr>
				<th>
					Nome:
				</th>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="40"/>
				</td>				
			</tr>
			<tr>
				<th>
					<bean:message key="label.mandatory" bundle="MESSAGING_RESOURCES"/>:
				</th>
				<td>
					<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.mandatory" property="mandatory" value="true"/>
				</td>				
			</tr>
			<tr>
				<th>
					Quem pode ler:
				</th>
				<e:labelValues id="values" bundle="ENUMERATION_RESOURCES" enumeration="net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType" />				
				<td>
					<html:select property="unitBoardReadPermittedGroupType">
						<html:options collection="values" property="value" labelProperty="label" />
					</html:select>
				</td>				
			</tr>
			<tr>
				<th>
					Quem pode escrever:
				</th>
				<td>
					<html:select property="unitBoardWritePermittedGroupType">
						<html:options collection="values" property="value" labelProperty="label" />
					</html:select>
				</td>				
			</tr>
			<tr>
				<th>
					Quem pode gerir:
				</th>
				<td>
					<html:select property="unitBoardManagementPermittedGroupType">
						<html:options collection="values" property="value" labelProperty="label" />
					</html:select>
				</td>				
			</tr>										
		</table>
		<html:submit>
			Guardar Alterações
		</html:submit>
	</html:form>


<p class="mvert1">
	Apagar este canal? 
	<html:link action="<%= contextPrefix + "method=deleteAnnouncementBoard&amp;announcementBoardId="+announcementBoardId+ "&amp;"+extraParameters +"&amp;returnAction="+request.getParameter("returnAction") + "&amp;returnMethod="+request.getParameter("returnMethod")%>">
		Apagar
	</html:link>
</p>


	<logic:present name="announcements">
		<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
		<bean:define id="extraParameters" name="extraParameters" />
		<bean:define id="announcementBoard" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
		<bean:define id="announcementBoardId" name="announcementBoard" property="externalId"/>		
	
		<ul>
				
				<logic:equal name="announcementBoard" property="currentUserWriter" value="true">
					<li>
						<html:link action="<%= contextPrefix + "method=addAnnouncement&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters%>">
							<bean:message key="label.createAnnouncement" bundle="MESSAGING_RESOURCES"/>
						</html:link>
					</li>
				</logic:equal>
				
				<li>
					<html:link action="<%= "announcements/announcementsStartPageHandler.do?method=viewAnnouncements&amp;announcementBoardId=" + announcementBoardId %>">
						<bean:message key="label.board.page" bundle="MESSAGING_RESOURCES"/>
					</html:link>
				</li>
		</ul>
	
		<jsp:include page="/messaging/announcements/listAnnouncements.jsp" flush="true"/>
	</logic:present>

</logic:present>
