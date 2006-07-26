<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<H2><bean:message bundle="SPACE_RESOURCES" key="link.create.space"/></H2>
<br/>

<H3><bean:message key="title.space.type" bundle="SPACE_RESOURCES"/></H3>
<html:form action="/showCreateSpaceForm">
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.classname" property="classname" onchange="this.form.submit()">
		<html:option value=""/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.campus" value="net.sourceforge.fenixedu.domain.space.Campus"/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.building" value="net.sourceforge.fenixedu.domain.space.Building"/>
	</html:select>
	<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit"/>
	</html:submit>
</html:form>
<br/>


<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Campus">
	<H3><bean:message key="label.space.details" bundle="SPACE_RESOURCES"/></H3>
	<fr:create type="net.sourceforge.fenixedu.domain.space.Campus$CampusFactoryCreator"
			schema="CampusFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
	</fr:create>
</logic:equal>
<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Building">
    <H3><bean:message key="label.space.details" bundle="SPACE_RESOURCES"/></H3>
	<fr:create type="net.sourceforge.fenixedu.domain.space.Building$BuildingFactoryCreator"
			schema="BuildingFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
	</fr:create>
</logic:equal>
