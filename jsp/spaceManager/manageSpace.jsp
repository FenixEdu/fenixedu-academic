<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:present name="selectedSpace">

	<bean:define id="space" name="selectedSpace" toScope="request"/>
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
				<logic:equal name="selectedSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
					<bean:message bundle="SPACE_RESOURCES" key="select.item.campus"/>
				</logic:equal>
				<logic:equal name="selectedSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
					<bean:message bundle="SPACE_RESOURCES" key="select.item.building"/>
				</logic:equal>
			</td>
			<td class="listClasses">
				<html:link page="/manageSpaces.do?method=prepareEditSpace&page=0" paramId="spaceID" paramName="selectedSpace" paramProperty="idInternal">
					<logic:equal name="selectedSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
						<bean:write name="selectedSpace" property="spaceInformation.name"/>
					</logic:equal>
					<logic:equal name="selectedSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
						<bean:write name="selectedSpace" property="spaceInformation.name"/>
					</logic:equal>
				</html:link>
			</td>
			<td class="listClasses">
				<bean:write name="selectedSpace" property="containedSpacesCount"/>
			</td>
			<td class="listClasses">
				<html:link page="/manageSpaces.do?method=deleteSpace&page=0" paramId="spaceID" paramName="selectedSpace" paramProperty="idInternal">
					<bean:message bundle="SPACE_RESOURCES" key="link.delete.space"/>
				</html:link>
			</td>
		</tr>
	</table>
</logic:present>
<br/>
<br/>

<html:link page="/manageSpaces.do?method=showCreateSubSpaceForm&page=0" paramId="spaceID" paramName="selectedSpace" paramProperty="idInternal">
	<bean:message bundle="SPACE_RESOURCES" key="link.create.subspace"/>
</html:link>
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
				</td>
				<td class="listClasses">
					<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceID" paramName="space" paramProperty="idInternal">
						<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
							<bean:write name="space" property="spaceInformation.name"/>
						</logic:equal>
						<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
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