<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
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
	
	<p class="infoop2"><bean:message key="label.search.spaces.info" bundle="DEFAULT"/></p>
	
	<fr:form id="searchform" action="/findSpaces.do">		
		
		<html:hidden name="findSpacesForm" property="method" value="search"/>				
		
		<fr:edit id="beanWithLabelToSearchID" name="bean" schema="<%= schemaName %>">
			<fr:destination name="postBack" path="/findSpaces.do?method=prepareSearchSpacesPostBack"/>	
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mbottom0" />
				<fr:property name="columnClasses" value="width100px,width400px,tdclear tderror1" />			
			</fr:layout>			
		</fr:edit>

		<table class="tstyle5 mtop0">
			<tr>
				<td class="width100px"></td>
				<td class="width400px">
					<html:submit><bean:message key="link.search" bundle="DEFAULT"/></html:submit> &nbsp;
					<logic:equal name="bean" property="extraOptions" value="false">
						<a href="#" onclick="document.getElementById('searchform').method.value='searchWithExtraOptions';document.getElementById('searchform').submit();"><bean:message key="link.search.with.extra.options" bundle="DEFAULT"/></a>
					</logic:equal>
					<logic:equal name="bean" property="extraOptions" value="true">
						<a href="#" onclick="document.getElementById('searchform').method.value='searchWithoutExtraOptions';document.getElementById('searchform').submit();"><bean:message key="link.search.without.extra.options" bundle="DEFAULT"/></a>
					</logic:equal>
				</td>
			</tr>
		</table>
	
	</fr:form>
	
	<logic:notEmpty name="bean" property="labelToSearch">
		<logic:empty name="foundSpaces">
			<p class="mtop15"><em><bean:message key="label.not.found.spaces" bundle="DEFAULT"/></em></p>
		</logic:empty>
	</logic:notEmpty>
			
	<logic:notEmpty name="foundSpaces">	
	
		<bean:size id="foundSpacesSize" name="foundSpaces"/>
		<p class="mtop2"><i><bean:message key="label.number.of.found.spaces" bundle="DEFAULT"/> <b><bean:write name="foundSpacesSize"/></b> <bean:message key="label.find.spaces.space" bundle="DEFAULT"/>.</i></p>
				
		<p class="mtop15">						
			<fr:view name="foundSpaces" schema="PublicFoundSpaceInfo">			
				<fr:layout name="tabular">
					
					<fr:property name="classes" value="tstyle2 mtop05" />
					<fr:property name="columnClasses" value=",acenter,acenter" />					
					
					<fr:property name="link(viewSchedule)" value="/viewRoom.do?method=roomViewer" />					
					<fr:property name="param(viewSchedule)" value="space.identification/roomName" />
					<fr:property name="key(viewSchedule)" value="link.view.schedule" />
					<fr:property name="bundle(viewSchedule)" value="DEFAULT" />
					<fr:property name="order(viewSchedule)" value="0" />
					<fr:property name="visibleIf(viewSchedule)" value="withSchedule"/>
					
					<fr:property name="link(viewWrittenEvaluation)" value="/spaces/writtenEvaluationsByRoom.faces" />					
					<fr:property name="param(viewWrittenEvaluation)" value="space.idInternal/selectedRoomID,executionPeriod.idInternal/executionPeriodOID" />
					<fr:property name="key(viewWrittenEvaluation)" value="link.view.written.evaluations" />
					<fr:property name="bundle(viewWrittenEvaluation)" value="DEFAULT" />
					<fr:property name="order(viewWrittenEvaluation)" value="1" />
					<fr:property name="visibleIf(viewWrittenEvaluation)" value="withWrittenEvaluations"/>
											
				</fr:layout>
			</fr:view>
		</p>		
	</logic:notEmpty>

</logic:notEmpty>
		
