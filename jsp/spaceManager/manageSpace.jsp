<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:present name="selectedSpaceInformation">
	<bean:define id="selectedSpaceInformationIDString" type="java.lang.String"><bean:write name="selectedSpaceInformation" property="idInternal"/></bean:define>

	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<jsp:include page="spaceCrumbs.jsp"/>
	<br/>
	<br/>

	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.type"/>
			</td>
			<td class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.Space"/>
			</td>
			<td class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.number.subspaces"/>
			</td>
			<td class="listClasses-header">
			</td>
		</tr>
		<tr>
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
			<td class="listClasses">
				<html:link page="/manageSpaces.do?method=prepareEditSpace&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
					<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
						<bean:write name="selectedSpaceInformation" property="name"/>
					</logic:equal>
					<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Building">
						<bean:write name="selectedSpaceInformation" property="name"/>
					</logic:equal>
					<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
						<bean:write name="selectedSpaceInformation" property="level"/>
					</logic:equal>
					<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Room">
						<bean:write name="selectedSpaceInformation" property="name"/>
					</logic:equal>
				</html:link>
			</td>
			<td class="listClasses">
				<bean:write name="selectedSpaceInformation" property="space.containedSpacesCount"/>
			</td>
			<td class="listClasses">
				<html:link page="/manageSpaces.do?method=deleteSpace&page=0" paramId="spaceID" paramName="selectedSpaceInformation" paramProperty="space.idInternal">
					<bean:message bundle="SPACE_RESOURCES" key="link.delete.space"/>
				</html:link>
			</td>
		</tr>
	</table>
	<br/>
	<br/>

	<table>
		<tr>
			<logic:iterate id="spaceInformation" name="selectedSpaceInformation" property="space.orderedSpaceInformations">
				<td>
					<logic:equal name="spaceInformation" property="idInternal" value="<%= selectedSpaceInformationIDString %>">
						[<bean:write name="spaceInformation" property="validFrom"/>, <bean:write name="spaceInformation" property="validUntil"/>[
					</logic:equal>
					<logic:notEqual name="spaceInformation" property="idInternal" value="<%= selectedSpaceInformationIDString %>">
						<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="spaceInformation" paramProperty="idInternal">
							[<bean:write name="spaceInformation" property="validFrom"/>, <bean:write name="spaceInformation" property="validUntil"/>[
						</html:link>
					</logic:notEqual>
				</td>
			</logic:iterate>
		</tr>
	</table>
	<br/>
	<br/>

	<html:link page="/manageSpaces.do?method=showCreateSubSpaceForm&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="space.idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.create.subspace"/>
	</html:link>

</logic:present>
<br/>
<br/>

<logic:present name="spaces">
	<table>
		<tr>
			<td colspan="4">
				<center>
					<bean:message bundle="SPACE_RESOURCES" key="title.space.number.subspaces"/>
				</center>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.type"/>
			</td>
			<td class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.Space"/>
			</td>
			<td class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.number.subspaces"/>
			</td>
			<td class="listClasses-header">
			</td>
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
						<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
							<bean:write name="space" property="spaceInformation.name"/>
						</logic:equal>
						<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
							<bean:write name="space" property="spaceInformation.name"/>
						</logic:equal>
						<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
							<bean:write name="space" property="spaceInformation.level"/>
						</logic:equal>
						<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Room">
							<bean:write name="space" property="spaceInformation.name"/>
						</logic:equal>
					</html:link>
				</td>
				<td class="listClasses">
					<bean:write name="space" property="containedSpacesCount"/>
				</td>
				<td class="listClasses">
					<html:link page="/manageSpaces.do?method=deleteSpace&page=0" paramId="spaceID" paramName="space" paramProperty="idInternal">
						<bean:message bundle="SPACE_RESOURCES" key="link.delete.space"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>