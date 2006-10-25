<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message bundle="SPACE_RESOURCES" key="title.create.subspace"/></h2>

<bean:define id="selectedSpace" name="selectedSpaceInformation" property="space"/>
<bean:define id="suroundingSpaceID" type="java.lang.Integer" name="selectedSpace" property="idInternal"/>
<bean:define id="suroundingSpaceInformationID" type="java.lang.Integer" name="selectedSpace" property="spaceInformation.idInternal"/>

<logic:messagesPresent message="true">
<p>
	<em><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</em>
</p>
</logic:messagesPresent>	

<p class="mvert15"><span class="warning0"><bean:message key="label.space.createIn" bundle="SPACE_RESOURCES"/>: <strong><bean:write name="selectedSpace" property="spaceInformation.presentationName"/></strong></span></p>

<p class="mtop15 mbottom05"><strong><bean:message key="title.space.type" bundle="SPACE_RESOURCES"/></strong></p>
<html:form action="/manageSpaces">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showCreateSubSpaceForm"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.spaceInformationID" property="spaceInformationID" value="<%= suroundingSpaceInformationID.toString() %>"/>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.classname" property="classname" onchange="this.form.submit()">
		<html:option value=""/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.building" value="net.sourceforge.fenixedu.domain.space.Building"/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.floor" value="net.sourceforge.fenixedu.domain.space.Floor"/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.room" value="net.sourceforge.fenixedu.domain.space.Room"/>
	</html:select>
	<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit"/>
	</html:submit>
</html:form>

<bean:define id="cancelPath">
	/manageSpaces.do?method=manageSpace&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>
</bean:define>	

<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Building">
    <p class="mtop15 mbottom05"><strong><bean:message key="label.space.details" bundle="SPACE_RESOURCES"/></strong></p>
	<fr:create type="net.sourceforge.fenixedu.domain.space.Building$BuildingFactoryCreator"
			schema="BuildingFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:hidden slot="surroundingSpace" name="selectedSpace"/>
		<fr:destination name="cancel" path="<%= cancelPath %>"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom1"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>		
		</fr:layout>	
	</fr:create>
</logic:equal>


<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Floor">
	<p class="mtop15 mbottom05"><strong><bean:message key="label.space.details" bundle="SPACE_RESOURCES"/></strong></p>
	<fr:create type="net.sourceforge.fenixedu.domain.space.Floor$FloorFactoryCreator"
			schema="FloorFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:hidden slot="surroundingSpace" name="selectedSpace"/>
		<fr:destination name="cancel" path="<%= cancelPath %>"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom1"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>		
	</fr:create>
</logic:equal>

<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Room">
    <p class="mtop15 mbottom05"><strong><bean:message key="label.space.details" bundle="SPACE_RESOURCES"/></strong></p>
	<fr:create type="net.sourceforge.fenixedu.domain.space.Room$RoomFactoryCreator"
			schema="RoomFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:hidden slot="surroundingSpace" name="selectedSpace"/>
		<fr:destination name="cancel" path="<%= cancelPath %>"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thmiddle mtop0 mbottom1"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>	
	</fr:create>
</logic:equal>
