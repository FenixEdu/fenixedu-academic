<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.departmentMember" bundle="DEPARTMENT_MEMBER_RESOURCES"/></em>
<h2><bean:message key="label.autoEvaluation"/></h2>

<logic:present role="DEPARTMENT_MEMBER">

	<fr:form action="/teacherExpectationAutoAvaliation.do?method=show">
		<b><bean:message key="label.common.executionYear"/>:</b> 
		<fr:edit id="executionYear" name="bean" slot="executionYear"> 
			<fr:layout name="menu-select-postback">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsToViewTeacherPersonalExpectationsProvider"/>
				<fr:property name="format" value="${year}"/>
				<fr:destination name="postback" path="/teacherExpectationAutoAvaliation.do?method=show"/>
			</fr:layout>
		</fr:edit>	
	</fr:form>

	<logic:notEmpty name="expectation">						
		<p>				   	
			<logic:notEmpty name="expectation" property="autoEvaluation">				
				<p><fr:view name="expectation" property="autoEvaluation" layout="html"/></p>
			</logic:notEmpty> 

		   	<bean:define id="executionYearId" name="bean" property="executionYear.idInternal"/>
		   				
			<logic:empty name="expectation" property="autoEvaluation">
			   	<bean:message key="label.noAutoEvaluationsForYear" />			   	
				<p><logic:equal name="expectation" property="allowedToEditAutoEvaluation" value="true">
					<bean:define id="executionYearId" name="bean" property="executionYear.idInternal"/>
					<html:link page="<%= "/teacherExpectationAutoAvaliation.do?method=prepareEdit&amp;executionYearId=" + executionYearId%>">
						<bean:message key="button.add" />
					</html:link>
				</logic:equal></p>			   	
			</logic:empty>
			
			<logic:notEmpty name="expectation" property="autoEvaluation">
				<p><logic:equal name="expectation" property="allowedToEditAutoEvaluation" value="true">
					<html:link page="<%= "/teacherExpectationAutoAvaliation.do?method=prepareEdit&amp;executionYearId=" + executionYearId%>">
						<bean:message key="label.edit" />
					</html:link>
				</logic:equal></p>
			</logic:notEmpty>			
		</p>		
	</logic:notEmpty>
	
	<logic:empty name="expectation">
		<bean:message key="label.personalExpectationsManagement.noExpectationsDefined" />		   	
	</logic:empty>
	
</logic:present>