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
	<bean:define id="spaceID" type="java.lang.Integer" name="selectedSpace" property="idInternal"/>
	<html:hidden property="spaceID" value="<%= spaceID.toString() %>"/>

	<logic:equal name="selectedSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
		<html:hidden property="method" value="editCampus"/>

		<html:text property="spaceName"/>
	</logic:equal>
	<logic:equal name="selectedSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
		<html:hidden property="method" value="editBuilding"/>

		<html:text property="spaceName"/>
	</logic:equal>
	<logic:equal name="selectedSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
		<html:hidden property="method" value="editFloor"/>
	</logic:equal>
	<logic:equal name="selectedSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Room">
		<html:hidden property="method" value="editRoom"/>
	</logic:equal>
	<br/>

	<html:submit styleClass="inputbutton">
		<bean:message bundle="SPACE_RESOURCES" key="label.button.submit.changes"/>
	</html:submit>
</html:form>