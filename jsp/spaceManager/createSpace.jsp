<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message bundle="SPACE_RESOURCES" key="link.create.space"/></h2>


<p class="mbottom05"><strong><bean:message key="title.space.type" bundle="SPACE_RESOURCES"/></strong></p>

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



<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Campus">
	<p class="mtop2 mbottom05"><strong><bean:message key="label.space.details" bundle="SPACE_RESOURCES"/></strong></p>
	<fr:create type="net.sourceforge.fenixedu.domain.space.Campus$CampusFactoryCreator"
			schema="CampusFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
		</fr:layout>
	</fr:create>
</logic:equal>
<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Building">
    <p class="mtop2 mbottom05"><strong><bean:message key="label.space.details" bundle="SPACE_RESOURCES"/></strong></p>
	<fr:create type="net.sourceforge.fenixedu.domain.space.Building$BuildingFactoryCreator"
			schema="BuildingFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
		</fr:layout>
	</fr:create>
</logic:equal>
