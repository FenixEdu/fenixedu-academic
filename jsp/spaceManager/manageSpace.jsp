<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="selectedSpaceInformation">
	
	<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
	<h2><bean:message bundle="SPACE_RESOURCES" key="title.manage.space"/></h2>
	
	<bean:define id="selectedSpaceInformationIDString" type="java.lang.String"><bean:write name="selectedSpaceInformation" property="idInternal"/></bean:define>	
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<div class="mbottom2">
		<jsp:include page="spaceCrumbs.jsp"/>
	</div>
	<bean:define id="space" name="selectedSpaceInformation" property="space"/>
				
	<logic:messagesPresent message="true">
		<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		</p>
	</logic:messagesPresent>
	
	<div class="mtop1 mbottom0">
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
	</div>

	<table class="tstyle4 thleft thlight">
		<tr>
			<th>
				<bean:message bundle="SPACE_RESOURCES" key="title.type"/>:
			</th>
			<td>
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
			<th>
				<bean:message bundle="SPACE_RESOURCES" key="title.space.Space"/>:
			</th>
			<td style="background: #fafaca;">
				<bean:write name="selectedSpaceInformation" property="presentationName"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message bundle="SPACE_RESOURCES" key="title.space.last.version.date"/>:
			</th>
			<td>
				<bean:define id="spaceInformation" name="spaceInformation2" toScope="request"/>
				<jsp:include page="spaceInformationVersion.jsp"/>
			</td>
		</tr>
		<tr>	
			<th>
				<bean:message bundle="SPACE_RESOURCES" key="title.space.number.subspaces"/>:
			</th>						
			<td>
				<bean:write name="selectedSpaceInformation" property="space.containedSpacesCount"/>
			</td>
		</tr>
	</table>
	
	<p class="mtop0 mbottom2">
		<html:link page="/manageSpaces.do?method=prepareEditSpace&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
			<bean:message bundle="SPACE_RESOURCES" key="link.edit.space.information"/>
		</html:link>&nbsp;,
		<html:link page="/manageSpaces.do?method=deleteSpaceInformation&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
			<bean:message bundle="SPACE_RESOURCES" key="link.delete.space.information"/>
		</html:link>&nbsp;,
		<html:link page="/manageSpaces.do?method=prepareCreateSpaceInformation&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
			<bean:message bundle="SPACE_RESOURCES" key="link.create.space.information"/>
		</html:link>&nbsp;,
		<html:link page="/manageSpaces.do?method=deleteSpace&page=0" paramId="spaceID" paramName="selectedSpaceInformation" paramProperty="space.idInternal" onclick="return confirm('Tem a certeza que deseja apagar o espaço e todos os seus subespaços?')">
			<bean:message bundle="SPACE_RESOURCES" key="link.delete.space"/>
		</html:link>		
	</p>

	<%-- BluePrints --%>
	<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="label.recent.bluePrint"/></h3>			
	<logic:notEmpty name="selectedSpaceInformation" property="space.mostRecentBlueprint">		
		<bean:define id="blueprint" name="selectedSpaceInformation" property="space.mostRecentBlueprint"/>		
		<bean:define id="url"><%= request.getContextPath() %>/SpaceManager/manageBlueprints.do?method=view&blueprintId=<bean:write name="blueprint" property="idInternal"/></bean:define>
		<p>
		<html:img src="<%= url %>" altKey="clip_image002" bundle="IMAGE_RESOURCES" />
		</p>
	</logic:notEmpty>	
	<p class="mtop05">
		<html:link page="/manageBlueprints.do?method=showBlueprintVersions&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
			<bean:message bundle="SPACE_RESOURCES" key="link.manage.blueprints"/>
		</html:link>
	</p>
	
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Room">
		<h3 class="mtop2 mbottom0"><bean:message bundle="SPACE_RESOURCES" key="label.space.details"/></h3>
		<fr:view name="selectedSpaceInformation" schema="RoomInformation" layout="tabular"/>
	</logic:equal>		    

	<%-- Subspaces --%>
	<logic:present name="spaces">
		<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="title.subspaces"/></h3>
		<bean:size id="spacesSize" name="spaces"/>
		<logic:greaterEqual name="spacesSize" value="1">
			<table class="tstyle4 thlight mbottom05">				
				<tr>
					<th>
						<bean:message bundle="SPACE_RESOURCES" key="title.type"/>
					</th>
					<th>
						<bean:message bundle="SPACE_RESOURCES" key="title.space.Space"/>
					</th>
					<th>
						<bean:message bundle="SPACE_RESOURCES" key="title.space.number.subspaces"/>
					</th>
					<th>
					</th>
				</tr>
				<logic:iterate id="space" name="spaces">
					<tr>
						<td>
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
						<td>
							<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="space" paramProperty="spaceInformation.idInternal">
								<bean:write name="space" property="spaceInformation.presentationName"/>
							</html:link>
						</td>
						<td class="acenter">
							<bean:write name="space" property="containedSpacesCount"/>
						</td>
						<td>
							<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="space" paramProperty="spaceInformation.idInternal">
								<bean:message bundle="SPACE_RESOURCES" key="label.view"/>
							</html:link>,&nbsp; 
							<html:link page="/manageSpaces.do?method=deleteSpace&page=0" paramId="spaceID" paramName="space" paramProperty="idInternal" onclick="return confirm('Tem a certeza que deseja apagar o espaço e todos os seus subespaços?')">
								<bean:message bundle="SPACE_RESOURCES" key="link.delete.space"/>
							</html:link>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:greaterEqual>
	</logic:present>
	<p class="mtop05">
		<html:link page="/manageSpaces.do?method=showCreateSubSpaceForm&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
			<bean:message bundle="SPACE_RESOURCES" key="link.create.subspace"/>
		</html:link>
	</p>

	<%-- Responsability --%>	
	<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="label.active.responsible.units"/></h3>
	<logic:notEmpty name="selectedSpaceInformation" property="space.activeSpaceResponsibility">
		<fr:view schema="ViewSpaceResponsibleUnits" name="selectedSpaceInformation" property="space.activeSpaceResponsibility">
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mvert0"/>
	   		</fr:layout>	
		</fr:view>
	</logic:notEmpty>
	<p class="mtop05">
		<html:link page="/manageSpaceResponsibility.do?method=showSpaceResponsibility&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
			<bean:message bundle="SPACE_RESOURCES" key="link.manage.space.responsibility"/>
		</html:link>
	</p>
	
	<%-- Person Occupations --%>
	<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="label.active.person.occupations"/></h3>
	<logic:notEmpty name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
		<fr:view schema="PersonSpaceOccupationsWithUsername" name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mvert0"/>
	   		</fr:layout>	
		</fr:view>	
	</logic:notEmpty>
	<p class="mtop05"><html:link page="/managePersonSpaceOccupations.do?method=showSpaceOccupations&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.manage.person.occupations"/>
	</html:link></p>

	<%-- Material --%>
	<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="label.active.material.occupations"/></h3>
	<logic:notEmpty name="selectedSpaceInformation" property="space.activeSpaceMaterial">
		<fr:view schema="ViewSpaceMaterial" name="selectedSpaceInformation" property="space.activeSpaceMaterial">
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mvert0"/>  			
	   		</fr:layout>	
		</fr:view>			
	</logic:notEmpty>
	<p class="mtop05"><html:link page="/manageMaterialSpaceOccupations.do?method=showMaterialSpaceOccupations&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.manage.material.occupations"/>
	</html:link></p>
	
	<%-- Access Groups --%>
	<h3 class="mtop2 mbottom0"><bean:message bundle="SPACE_RESOURCES" key="label.access.groups"/></h3>
	<p class="mtop05"><html:link page="/manageSpaces.do?method=manageAccessGroups&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.manage.access.groups"/>
	</html:link></p>
	
</logic:present>	