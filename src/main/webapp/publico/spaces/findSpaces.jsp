<%@ page language="java" %>
<%@ page isELIgnored="true" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h1><bean:message key="title.search.spaces" bundle="DEFAULT"/></h1>

<logic:notEmpty name="bean">

	<logic:messagesPresent message="true">
		<p class="mtop15">
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true">
					<bean:write name="message" filter="true"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>

	<bean:define id="schemaName" value="" />	
	<logic:equal name="bean" property="extraOptions" value="false">
		<bean:define id="schemaName" type="java.lang.String" value="PublicFindSpaces" />
	</logic:equal>
	<logic:equal name="bean" property="extraOptions" value="true">
		<bean:define id="schemaName" type="java.lang.String" value="PublicFindSpacesWithExtraOptions" />		
	</logic:equal>
	
	<p><bean:message key="label.search.spaces.info" bundle="DEFAULT"/></p>
	
<style>
.form1 table ul li * {
display: inline !important;
}
</style>


	<fr:form id="searchform" action="/findSpaces.do">
		<div class="form1">
			<html:hidden name="findSpacesForm" property="method" value="search"/>				
			<fieldset>
				<legend><bean:message key="label.find" bundle="DEFAULT"/></legend>
				
				<fr:edit id="beanWithLabelToSearchID" name="bean" schema="<%= schemaName %>">
					<fr:destination name="postBack" path="/findSpaces.do?method=prepareSearchSpacesPostBack"/>	
					<fr:layout name="tabular" >
						<fr:property name="classes" value="thleft ulnomargin" />
						<fr:property name="columnClasses" value="" />			
					</fr:layout>			
				</fr:edit>
				
				<p style="margin-top: 1em; margin-bottom: 0;">
					<html:submit styleClass="dinline"><bean:message key="link.search" bundle="DEFAULT"/></html:submit> &nbsp;
					<logic:equal name="bean" property="extraOptions" value="false">
						<a href="#" onclick="document.getElementById('searchform').method.value='searchWithExtraOptions';document.getElementById('searchform').submit();"><bean:message key="link.search.with.extra.options" bundle="DEFAULT"/></a>
					</logic:equal>
					<logic:equal name="bean" property="extraOptions" value="true">
						<a href="#" onclick="document.getElementById('searchform').method.value='searchWithoutExtraOptions';document.getElementById('searchform').submit();"><bean:message key="link.search.without.extra.options" bundle="DEFAULT"/></a>
					</logic:equal>
				</p>
			</fieldset>
		</div>
	</fr:form>
	
	<logic:notEmpty name="bean" property="labelToSearch">
		<logic:empty name="foundSpaces">
			<p class="mtop15"><em><bean:message key="label.not.found.spaces" bundle="DEFAULT"/></em></p>
		</logic:empty>
	</logic:notEmpty>
			
	<logic:notEmpty name="foundSpaces">	
	
		<bean:size id="foundSpacesSize" name="foundSpaces"/>
		<p class="mtop2"><i><bean:message key="label.number.of.found.spaces" bundle="DEFAULT"/> <b><bean:write name="foundSpacesSize"/></b> <bean:message key="label.find.spaces.space" bundle="DEFAULT"/>.</i></p>
				
		<fr:view name="foundSpaces">
			<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.FindSpacesBean" bundle="DEFAULT">
				<fr:slot name="spacePath" key="label.find.spaces.identification" layout="list-link">
					<fr:property name="linkFormat" value="/findSpaces.do?method=${method}&amp;spaceID=${id}"/>
					<fr:property name="moduleRelative" value="true"/>
					<fr:property name="contextRelative" value="true"/>
				</fr:slot>
				<fr:slot name="space.classification.name.content" layout="null-as-label" key="label.space.classification"/>
			</fr:schema>			
			<fr:layout name="tabular">
				
				<fr:property name="classes" value="tab_lay mtop05" />
				<fr:property name="columnClasses" value=",,acenter" />					
				<fr:property name="rowClasses" value=",bluecell" />
				
				<fr:property name="link(viewSchedule)" value="/viewRoom.do?method=roomViewer" />
				<fr:property name="param(viewSchedule)" value="space.name/roomName" />
				<fr:property name="key(viewSchedule)" value="link.view.schedule" />
				<fr:property name="bundle(viewSchedule)" value="DEFAULT" />
				<fr:property name="order(viewSchedule)" value="0" />
				<fr:property name="visibleIf(viewSchedule)" value="withSchedule"/>
				
				<fr:property name="link(viewWrittenEvaluation)" value="/spaces/writtenEvaluationsByRoom.faces" />					
				<fr:property name="param(viewWrittenEvaluation)" value="space.externalId/selectedRoomID,academicInterval.resumedRepresentationInStringFormat/academicInterval" />
				<fr:property name="key(viewWrittenEvaluation)" value="link.view.written.evaluations" />
				<fr:property name="bundle(viewWrittenEvaluation)" value="DEFAULT" />
				<fr:property name="order(viewWrittenEvaluation)" value="1" />
				<fr:property name="visibleIf(viewWrittenEvaluation)" value="withWrittenEvaluations"/>
											
			</fr:layout>
		</fr:view>
		
	</logic:notEmpty>

</logic:notEmpty>
		
