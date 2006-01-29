<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:message bundle="SPACE_RESOURCES" key="link.create.space"/>
<br/>
<br/>
<br/>

<html:form action="/createSpace">
	<html:hidden property="page" value="0"/>
	<html:hidden property="method" value="showCreateSpaceForm"/>

	<html:select property="classname" onchange="this.form.submit()">
		<html:option value=""/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.campus" value="net.sourceforge.fenixedu.domain.space.Campus"/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.building" value="net.sourceforge.fenixedu.domain.space.Building"/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.floor" value="net.sourceforge.fenixedu.domain.space.Floor"/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.room" value="net.sourceforge.fenixedu.domain.space.Room"/>
	</html:select>
</html:form>
<br/>

<html:form action="/createSpace">
	<html:hidden property="page" value="1"/>

	<logic:equal name="createSpaceForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Campus">
		<html:hidden property="method" value="createCampus"/>

		<html:text property="spaceName"/>
	</logic:equal>
	<logic:equal name="createSpaceForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Building">
		<html:hidden property="method" value="createBuilding"/>

		<html:text property="spaceName"/>
	</logic:equal>
	<logic:equal name="createSpaceForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Floor">
		<html:hidden property="method" value="createFloor"/>
	</logic:equal>
	<logic:equal name="createSpaceForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Room">
		<html:hidden property="method" value="createRoom"/>
	</logic:equal>
	<br/>

	<html:submit styleClass="inputbutton">
		<bean:message bundle="SPACE_RESOURCES" key="label.button.create.space"/>
	</html:submit>
</html:form>