<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<h2><bean:message key="manager.announcements.title.label" bundle="MANAGER_RESOURCES"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<logic:present name="unit">

	<p class="mtop2 mbottom0"><bean:message key="label.channel" bundle="MESSAGING_RESOURCES"/>: <span class="emphasis1"><bean:write name="unit" property="name"/></span></p>

	
	<%--
	<bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.create.creatingForUnit.label"/>
	<bean:write name="unit" property="name"/>
	--%>
	
	<bean:define id="boards" name="unit" property="boards"/>
	<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>
	
	<jsp:include page="/messaging/announcements/listAnnouncementBoards.jsp" flush="true"/>		


<p class="mbottom0"><strong><bean:message key="label.createChannel" bundle="MESSAGING_RESOURCES"/>:</strong></p>

<%--
<p><span>(associada a <bean:write name="unit" property="name"/>)</span></p>
--%>

	<html:form action="/announcements/manageUnitAnnouncementBoard" method="get">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createBoard"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.keyUnit" property="keyUnit"/>
		<table class="tstyle5 thlight thright">
			<tr>
				<th>
					Nome:
				</th>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="50"/>
				</td>				
			</tr>
			<tr>
				<th>
					<bean:message key="label.mandatory" bundle="MESSAGING_RESOURCES"/>
				</th>
				<td>
					<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.mandatory" property="mandatory" value="true"/>
				</td>				
			</tr>
			<tr>
				<th>
					Quem pode ler:
				</th>
				<e:labelValues id="values" bundle="ENUMERATION_RESOURCES"  enumeration="net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType" />				
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
			<bean:message key="label.create" bundle="MANAGER_RESOURCES"/>
		</html:submit>
	</html:form>
		
</logic:present>