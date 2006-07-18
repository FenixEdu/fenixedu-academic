<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="selectedSpaceInformation">

	<bean:define id="selectedSpaceInformationIDString" type="java.lang.String"><bean:write name="selectedSpaceInformation" property="idInternal"/></bean:define>	
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<jsp:include page="spaceCrumbs.jsp"/>
	<br/><br/>
			
	<logic:messagesPresent message="true">
		<span class="error">
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<br/>
	</logic:messagesPresent>
	
	<bean:message bundle="SPACE_RESOURCES" key="label.versions"/>: 
	<logic:iterate id="spaceInformation" name="selectedSpaceInformation" property="space.orderedSpaceInformations">
		<bean:define id="spaceInformation" name="spaceInformation" toScope="request"/>
			<logic:equal name="spaceInformation" property="idInternal" value="<%= selectedSpaceInformationIDString %>">
				<bean:define id="spaceInformation2" name="spaceInformation"/>
				<jsp:include page="spaceInformationVersion.jsp"/>
			</logic:equal>
			<logic:notEqual name="spaceInformation" property="idInternal" value="<%= selectedSpaceInformationIDString %>">
				<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="spaceInformation" paramProperty="idInternal">
					<jsp:include page="spaceInformationVersion.jsp"/>
				</html:link>
			</logic:notEqual>
	</logic:iterate>
	<br/><br/>

	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.type"/>
			</th>
			<td class="listClasses">
				<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
					<bean:message bundle="SPACE_RESOURCES" key="select.item.campus"/>
				</logic:equal>
				<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Building">
					<bean:message bundle="SPACE_RESOURCES" key="select.item.building"/>
				</logic:equal>
				<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
					<bean:message bundle="SPACE_RESOURCES" key="select.item.floor"/>
				</logic:equal>
				<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Room">
					<bean:message bundle="SPACE_RESOURCES" key="select.item.room"/>
				</logic:equal>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.Space"/>
			</th>
			<td class="listClasses">
				<bean:write name="selectedSpaceInformation" property="presentationName"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.last.version.date"/>
			</th>
			<td class="listClasses">
				<bean:define id="spaceInformation" name="spaceInformation2" toScope="request"/>
				<jsp:include page="spaceInformationVersion.jsp"/>
			</td>
		</tr>
		<tr>	
			<th class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.number.subspaces"/>
			</th>						
			<td class="listClasses">
				<bean:write name="selectedSpaceInformation" property="space.containedSpacesCount"/>
			</td>
		</tr>
	</table>
	<br/>
	<html:link page="/manageSpaces.do?method=prepareEditSpace&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.edit.space.information"/>
	</html:link>,&nbsp;
	<html:link page="/manageSpaces.do?method=deleteSpaceInformation&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.delete.space.information"/>
	</html:link>,&nbsp;
	<html:link page="/manageSpaces.do?method=prepareCreateSpaceInformation&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.create.space.information"/>
	</html:link>,&nbsp;
	<html:link page="/manageSpaces.do?method=deleteSpace&page=0" paramId="spaceID" paramName="selectedSpaceInformation" paramProperty="space.idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.delete.space"/>
	</html:link>
	<br/><br/>
	
	<html:img align="middle" src="<%= request.getContextPath() +"/images/clip_image002.jpg"%>" altKey="clip_image002" bundle="IMAGE_RESOURCES" />" />
	<br/>

	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Room">
		<p><b><bean:message bundle="SPACE_RESOURCES" key="label.space.details"/></b></p>
		<fr:view name="selectedSpaceInformation" schema="RoomInformation" layout="tabular"/>
		<br/><br/>
	</logic:equal>		    

	<logic:present name="spaces">
		<p><b><bean:message bundle="SPACE_RESOURCES" key="title.subspaces"/></b></p>
		<bean:size id="spacesSize" name="spaces"/>
		<logic:greaterEqual name="spacesSize" value="1">
			<table>				
				<tr>
					<th class="listClasses-header">
						<bean:message bundle="SPACE_RESOURCES" key="title.space.type"/>
					</th>
					<th class="listClasses-header">
						<bean:message bundle="SPACE_RESOURCES" key="title.space.Space"/>
					</th>
					<th class="listClasses-header">
						<bean:message bundle="SPACE_RESOURCES" key="title.space.number.subspaces"/>
					</th>
					<th class="listClasses-header">
					</th>
				</tr>
				<logic:iterate id="space" name="spaces">
					<tr>
						<td class="listClasses">
							<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
								<bean:message bundle="SPACE_RESOURCES" key="select.item.campus"/>
							</logic:equal>
							<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
								<bean:message bundle="SPACE_RESOURCES" key="select.item.building"/>
							</logic:equal>
							<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
								<bean:message bundle="SPACE_RESOURCES" key="select.item.floor"/>
							</logic:equal>
							<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Room">
								<bean:message bundle="SPACE_RESOURCES" key="select.item.room"/>
							</logic:equal>
						</td>
						<td class="listClasses">
							<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="space" paramProperty="spaceInformation.idInternal">
								<bean:write name="space" property="spaceInformation.presentationName"/>
							</html:link>
						</td>
						<td class="listClasses">
							<bean:write name="space" property="containedSpacesCount"/>
						</td>
						<td class="listClasses">
							<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="space" paramProperty="spaceInformation.idInternal">
								<bean:message bundle="SPACE_RESOURCES" key="label.view"/>
							</html:link>,&nbsp; 
							<html:link page="/manageSpaces.do?method=deleteSpace&page=0" paramId="spaceID" paramName="space" paramProperty="idInternal">
								<bean:message bundle="SPACE_RESOURCES" key="link.delete.space"/>
							</html:link>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:greaterEqual>
	</logic:present>

	<p><html:link page="/manageSpaces.do?method=showCreateSubSpaceForm&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.create.subspace"/>
	</html:link></p>
	<br/>
	
	<p><b><bean:message bundle="SPACE_RESOURCES" key="label.reponsible.unit"/></b></p>
	<fr:view schema="ViewSpaceResponsibleUnits" name="selectedSpaceInformation" property="space.spaceResponsability" />
	
	<p><html:link page="/manageSpaceResponsability.do?method=showSpaceResponsability&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.manage.occupations"/>
	</html:link></p>	
	
	<br/>	
	<p><b><bean:message bundle="SPACE_RESOURCES" key="label.active.occupations"/></b></p>
	<fr:view schema="PersonSpaceOccupationsWithUsername" name="selectedSpaceInformation" property="space.activePersonSpaceOccupations" />
	
	<p><html:link page="/managePersonSpaceOccupations.do?method=showSpaceOccupations&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.manage.occupations"/>
	</html:link></p>

</logic:present>	