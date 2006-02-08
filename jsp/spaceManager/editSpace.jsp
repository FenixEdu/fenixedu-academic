<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:message bundle="SPACE_RESOURCES" key="link.edit.space"/>
<br/>
<br/>
<br/>

<html:form action="/editSpace">
	<html:hidden property="page" value="1"/>
	<bean:define id="spaceInformationID" type="java.lang.Integer" name="selectedSpaceInformation" property="idInternal"/>
	<html:hidden property="spaceInformationID" value="<%= spaceInformationID.toString() %>"/>
	<html:hidden property="asNewVersion"/>

	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
		<html:hidden property="method" value="editCampus"/>

		<bean:define id="name" type="java.lang.String" name="selectedSpaceInformation" property="name"/>
		<html:text property="spaceName" value="<%= name %>"/>
	</logic:equal>
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Building">
		<html:hidden property="method" value="editBuilding"/>

		<bean:define id="name" type="java.lang.String" name="selectedSpaceInformation" property="name"/>
		<html:text property="spaceName" value="<%= name %>"/>
	</logic:equal>
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
		<html:hidden property="method" value="editFloor"/>

		<bean:define id="level" type="java.lang.Integer" name="selectedSpaceInformation" property="level"/>
		<html:text property="level" value="<%= level.toString() %>"/>
	</logic:equal>
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Room">
		<html:hidden property="method" value="editRoom"/>

		<bean:define id="name" type="java.lang.String" name="selectedSpaceInformation" property="name"/>
		<html:text property="spaceName" value="<%= name %>"/>
	</logic:equal>
	<br/>

	<html:submit onclick="this.form.asNewVersion.value='false';this.form.submit();">
		<bean:message bundle="SPACE_RESOURCES" key="label.button.submit.changes"/>
	</html:submit>

	<html:submit onclick="this.form.asNewVersion.value='true';this.form.submit();">
		<bean:message bundle="SPACE_RESOURCES" key="label.button.submit.changes.new.version"/>
	</html:submit>
</html:form>