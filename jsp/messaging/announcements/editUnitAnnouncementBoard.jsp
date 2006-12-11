<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<html:xhtml/>
<em><bean:message key="label.communicationPortal.header" bundle="MESSAGING_RESOURCES"/></em>
<h2><bean:message key="label.manageChannel" bundle="MESSAGING_RESOURCES"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<p class="mbottom0"><b>Unidade</b>: <span class="emphasis1"><bean:write name="unit" property="name"/></span></p>

<logic:present name="announcementBoard">
<bean:define id="contextPrefix" name="contextPrefix" />
<bean:define id="extraParameters" name="extraParameters" />
<bean:define id="announcementBoardId" name="announcementBoard" property="idInternal"/>

<html:form action="/announcements/manageUnitAnnouncementBoard" method="get">
		<html:hidden property="method" value="editAnnouncementBoard"/>
		<html:hidden property="keyUnit"/>
		<html:hidden property="announcementBoardId" value="<%=request.getParameter("announcementBoardId")%>"/>		
		<html:hidden property="returnAction"/>
		<html:hidden property="returnMethod"/>
		<html:hidden property="tabularVersion"/>
		<table class="tstyle5 thlight thright">
			<tr>
				<th>
					Nome:
				</th>
				<td>
					<html:text property="name"/>
				</td>				
			</tr>
			<tr>
				<th>
					<bean:message key="label.mandatory" bundle="MESSAGING_RESOURCES"/>
				</th>
				<td>
					<html:checkbox property="mandatory" value="true"/>
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
			Editar
		</html:submit>
	</html:form>

<p>Apagar este canal? 
	<html:link action="<%= contextPrefix + "method=deleteAnnouncementBoard&amp;announcementBoardId="+announcementBoardId+ "&amp;"+extraParameters +"&amp;returnAction="+request.getParameter("returnAction") + "&amp;returnMethod="+request.getParameter("returnMethod")%>">
		Apagar
	</html:link>
</p>


	<logic:present name="announcements">
		<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
		<bean:define id="extraParameters" name="extraParameters" />
		<bean:define id="announcementBoard" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
		<bean:define id="announcementBoardId" name="announcementBoard" property="idInternal"/>		
		<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>
	
		<ul>
				<%
					if (announcementBoard.hasWriter(person))
					{
				%>
				<li>
					<html:link action="<%= contextPrefix + "method=addAnnouncement&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters%>">
						<bean:message key="label.createAnnouncement" bundle="MESSAGING_RESOURCES"/>
					</html:link>
				</li>				
				<%
					}
				%>
		</ul>
	
		<jsp:include page="/messaging/announcements/listAnnouncements.jsp" flush="true"/>
	
	</logic:present>

</logic:present>
