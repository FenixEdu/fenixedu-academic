<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message bundle="SPACE_RESOURCES" key="title.create.subspace"/></h2>

<bean:define id="selectedSpace" name="selectedSpaceInformation" property="space"/>
<bean:define id="suroundingSpaceID" type="java.lang.String" name="selectedSpace" property="externalId"/>
<bean:define id="suroundingSpaceInformationID" type="java.lang.String" name="selectedSpace" property="spaceInformation.externalId"/>

<logic:messagesPresent message="true">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>	

<bean:define id="backLink">/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/></bean:define>		
<ul class="mvert15 list5">
	<li>
		<html:link page="<%= backLink %>">
			<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
		</html:link>
	</li>
</ul>

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
		<html:option bundle="SPACE_RESOURCES" key="select.item.roomSubdivision" value="net.sourceforge.fenixedu.domain.space.RoomSubdivision"/>
	</html:select>
	<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit"/>
	</html:submit>
</html:form>

<bean:define id="cancelPath">/manageSpaces.do?method=manageSpace&spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/></bean:define>	
<bean:define id="invalidLink">/manageSpaces.do?method=showCreateSubSpaceForm&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/></bean:define>	

<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Building">
    <p class="mtop15 mbottom05"><strong><bean:message key="label.space.details" bundle="SPACE_RESOURCES"/></strong></p>
	<fr:create type="net.sourceforge.fenixedu.domain.space.Building$BuildingFactoryCreator"
			schema="BuildingFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:hidden slot="surroundingSpace" name="selectedSpace"/>
		<fr:destination name="cancel" path="<%= cancelPath %>"/>
		<fr:destination name="invalid" path="<%= invalidLink %>"/>		
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
		<fr:destination name="invalid" path="<%= invalidLink %>"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom1"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>		
	</fr:create>
</logic:equal>

<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Room">
    <p class="mtop15 mbottom05"><strong><bean:message key="label.space.details" bundle="SPACE_RESOURCES"/></strong></p>
	
	<bean:define id="person" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person" type="net.sourceforge.fenixedu.domain.Person"/>
	<%
		if(net.sourceforge.fenixedu.domain.space.Space.personIsSpacesAdministrator(person)){
	%>
	<fr:create type="net.sourceforge.fenixedu.domain.space.Room$RoomFactoryCreator"
			schema="RoomFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:hidden slot="surroundingSpace" name="selectedSpace"/>
		<fr:destination name="cancel" path="<%= cancelPath %>"/>
		<fr:destination name="invalid" path="<%= invalidLink %>"/>		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom1"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>	
	</fr:create>
	<%
		} else {
	%>
	<fr:create type="net.sourceforge.fenixedu.domain.space.Room$RoomFactoryCreator"
			schema="LimitedRoomFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:hidden slot="surroundingSpace" name="selectedSpace"/>
		<fr:destination name="cancel" path="<%= cancelPath %>"/>
		<fr:destination name="invalid" path="<%= invalidLink %>"/>		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom1"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>	
	</fr:create>		
	<%
		}
	%>	
</logic:equal>

<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.RoomSubdivision">
    <p class="mtop15 mbottom05"><strong><bean:message key="label.space.details" bundle="SPACE_RESOURCES"/></strong></p>	
	<fr:create type="net.sourceforge.fenixedu.domain.space.RoomSubdivision$RoomSubdivisionFactoryCreator"
			schema="RoomSubdivisionFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:hidden slot="surroundingSpace" name="selectedSpace"/>
		<fr:destination name="cancel" path="<%= cancelPath %>"/>
		<fr:destination name="invalid" path="<%= invalidLink %>"/>		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom1"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>	
	</fr:create>
</logic:equal>
