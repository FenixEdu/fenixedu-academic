<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/>
<br/>
<br/>
<br/>

<logic:present name="selectedSpace">
	<bean:write name="selectedSpace" property="idInternal"/>
</logic:present>
<br/>
<br/>

<html:link page="/manageSpaces.do?method=showCreateSpaceForm&page=0">
	<bean:message bundle="SPACE_RESOURCES" key="link.create.subspace"/>
</html:link>
<br/>
<br/>

<logic:present name="spaces">
	<logic:iterate id="space" name="spaces">
		<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceID" paramName="space" paramProperty="idInternal">
			<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
				<bean:write name="space" property="spaceInformation.name"/>
			</logic:equal>
			<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
				<bean:write name="space" property="spaceInformation.name"/>
			</logic:equal>
		</html:link>
		<html:link page="/manageSpaces.do?method=deleteSpace&page=0" paramId="spaceID" paramName="space" paramProperty="idInternal">
			<bean:message bundle="SPACE_RESOURCES" key="link.delete.space"/>
		</html:link>
		<br/>
	</logic:iterate>
</logic:present>