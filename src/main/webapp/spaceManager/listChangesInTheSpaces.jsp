<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message bundle="SPACE_RESOURCES" key="space.list.changes.in.the.spaces.title"/></h2>

<logic:messagesPresent message="true">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<logic:notEmpty name="domainObjectActionLogs">			
	
	<div class="mtop15">
		<bean:message key="label.page" bundle="SPACE_RESOURCES"/>:
		<cp:collectionPages url="/SpaceManager/listChangesInTheSpaces.do?method=changesList" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>	
	</div>

	<fr:view name="domainObjectActionLogs" schema="ListChangesInTheSpacesSchema">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
			<fr:property name="columnClasses" value="nowrap smalltxt,smalltxt,nowrap smalltxt,nowrap smalltxt,aleft smalltxt,aleft smalltxt"/>
		</fr:layout>
	</fr:view>		
	
	<logic:notEqual name="numberOfPages" value="1">
		<bean:message key="label.page" bundle="SPACE_RESOURCES"/>:
		<cp:collectionPages url="/SpaceManager/listChangesInTheSpaces.do?method=changesList" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>	
	</logic:notEqual>	
</logic:notEmpty>

<logic:empty name="domainObjectActionLogs">			
	<p class="mtop05"><em><bean:message key="label.empty.log" bundle="SPACE_RESOURCES"/></em></p>		
</logic:empty>
