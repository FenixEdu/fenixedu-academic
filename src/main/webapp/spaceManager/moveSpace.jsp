<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="move.space.title" bundle="SPACE_RESOURCES"/></h2>

<logic:present name="moveSpaceBean">
	<logic:notEmpty name="moveSpaceBean" property="space">
	
	<bean:define id="space" name="moveSpaceBean" property="space" toScope="request"/>	
	<bean:define id="selectedSpace" name="moveSpaceBean" property="space" toScope="request"/>	
	
	<div class="mbottom2">
		<jsp:include page="spaceCrumbs.jsp"/>
	</div>	
	
	<bean:define id="backLink">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="moveSpaceBean" property="space.spaceInformation.externalId"/></bean:define>		
	<ul class="mvert15 list5">
		<li>
			<html:link page="<%= backLink %>">
				<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<p class="mtop15 mbottom05"><b><bean:message key="label.space.move.up" bundle="SPACE_RESOURCES"/></b></p>
	<fr:form action="/manageSpaces.do?method=moveSpace">
		<fr:edit id="moveSpaceBeanWithParentSpace" name="moveSpaceBean" schema="SpaceMoveUp">		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 vamiddle thlight mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>					
		</fr:edit>		
		<html:submit><bean:message key="link.move" bundle="SPACE_RESOURCES"/></html:submit>
	</fr:form>

	
	
	<p class="mtop2 mbottom05"><b><bean:message key="label.space.move.down" bundle="SPACE_RESOURCES"/></b></p>
	<fr:form action="/manageSpaces.do?method=moveSpace">
		<fr:edit id="moveSpaceBeanWithChildSpace" name="moveSpaceBean" schema="SpaceMoveDown">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 vamiddle thlight mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>			
		</fr:edit>			
		<html:submit><bean:message key="link.move" bundle="SPACE_RESOURCES"/></html:submit>
	</fr:form>	
	
	</logic:notEmpty>
</logic:present>
	

