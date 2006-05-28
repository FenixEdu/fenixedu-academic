<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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
	</html:select>
</html:form>
<br/>


<logic:equal name="createSpaceForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Campus">
	<fr:create type="net.sourceforge.fenixedu.domain.space.Campus$CampusFactoryCreator"
			schema="CampusFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
	</fr:create>
</logic:equal>
<logic:equal name="createSpaceForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Building">
	<fr:create type="net.sourceforge.fenixedu.domain.space.Building$BuildingFactoryCreator"
			schema="BuildingFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
	</fr:create>
</logic:equal>
