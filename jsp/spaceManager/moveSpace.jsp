<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="move.space.title" bundle="SPACE_RESOURCES"/></h2>

<logic:present name="moveSpaceBean">
	<logic:notEmpty name="moveSpaceBean" property="space">
	
	<bean:define id="space" name="moveSpaceBean" property="space" toScope="request"/>	
	<bean:define id="selectedSpace" name="moveSpaceBean" property="space" toScope="request"/>	
	
	<div class="mbottom2">
		<jsp:include page="spaceCrumbs.jsp"/>
	</div>	
	
	<bean:define id="backLink">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="moveSpaceBean" property="space.spaceInformation.idInternal"/></bean:define>		
	<ul class="mvert15 list5">
		<li>
			<html:link page="<%= backLink %>">
				<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<b><bean:message key="label.space.move.up" bundle="SPACE_RESOURCES"/></b>
	<fr:form action="/manageSpaces.do?method=moveSpace">
		<fr:edit id="moveSpaceBeanWithParentSpace" name="moveSpaceBean" schema="SpaceMoveUp">		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 vamiddle thlight" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>					
		</fr:edit>		
		<html:submit><bean:message key="link.submit" bundle="SPACE_RESOURCES"/></html:submit>
	</fr:form>

	<br/>
	
	<b><bean:message key="label.space.move.down" bundle="SPACE_RESOURCES"/></b>
	<fr:form action="/manageSpaces.do?method=moveSpace">
		<fr:edit id="moveSpaceBeanWithChildSpace" name="moveSpaceBean" schema="SpaceMoveDown">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 vamiddle thlight" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>			
		</fr:edit>			
		<html:submit><bean:message key="link.submit" bundle="SPACE_RESOURCES"/></html:submit>
	</fr:form>	
	
	</logic:notEmpty>
</logic:present>
	

