<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="SCIENTIFIC_COUNCIL">

	<em><bean:message key="title.teaching"/></em>
	<h2><bean:message key="label.edit.credits.period"/></h2>

	<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
	<html:messages id="message" message="true">
		<p>
			<span class="error"><!-- Error messages go here -->
				<bean:write name="message"/>
			</span>
		</p>
	</html:messages>
	
	<logic:notEmpty name="teacherCreditsBean">
		
		<bean:define id="schemaName" value="" />
		<logic:equal name="teacherCreditsBean" property="teacher" value="true">
			<h3 class="mtop15 mbottom05"><bean:message key="label.teacher"/></h3>
			<bean:define id="schemaName" value="teacher.credits.period.view" />
		</logic:equal>
		<logic:equal name="teacherCreditsBean" property="teacher" value="false">
			<h3 class="mtop15 mbottom05"><bean:message key="label.department.adm.office"/></h3>
			<bean:define id="schemaName" value="departmentAdmOffice.credits.period.view" />
		</logic:equal>
		
		<bean:define id="URL">/defineCreditsPeriods.do?executionPeriodId=<bean:write name="teacherCreditsBean" property="executionPeriod.idInternal"/></bean:define>
		
		<fr:form action="<%= URL %>">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" name="creditsPeriodForm" value="editPeriod"/>	
		
			<fr:edit id="teacherCreditsBeanID" name="teacherCreditsBean" schema="<%= schemaName %>"	>				
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>				
			</fr:edit>
			<html:submit><bean:message key="submit"/></html:submit>
			<html:cancel onclick="this.form.method.value='showPeriods';this.form.submit();"><bean:message key="button.cancel"/></html:cancel>
		</fr:form>
			
	</logic:notEmpty>	
</logic:present>