<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="SCIENTIFIC_COUNCIL">

	<em><bean:message key="title.teaching"/></em>
	<h2><bean:message key="link.define.periods"/></h2>

	<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>	
	<html:messages id="message" message="true">
		<p>
			<span class="error"><!-- Error messages go here -->
				<bean:write name="message"/>
			</span>
		</p>
	</html:messages>

	<logic:notEmpty name="teacherCreditsBean">

		<fr:form action="/defineCreditsPeriods.do?method=showPeriods">
			<fr:edit name="teacherCreditsBean" schema="teacher.credits.list.execution.periods" id="teacherCreditsBeanID">
				<fr:destination name="postBack" path="/defineCreditsPeriods.do?method=showPeriods"/>		
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
					<fr:property name="columnClasses" value=",,tdclear"/>			
				</fr:layout>			
			</fr:edit>
		</fr:form>
	
		<h3 class="mtop15 mbottom05"><bean:message key="label.teacher"/></h3>
		<fr:view name="teacherCreditsBean" schema="teacher.credits.period.view" layout="tabular">	
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
			</fr:layout>
		</fr:view>
		<html:link page="/defineCreditsPeriods.do?method=prepareEditTeacherCreditsPeriod" paramName="teacherCreditsBean" paramProperty="executionPeriod.idInternal" paramId="executionPeriodId">
			<bean:message key="link.change"/>
		</html:link>
	
		
		<h3 class="mtop15 mbottom05"><bean:message key="label.department.adm.office"/></h3>
		<fr:view name="teacherCreditsBean" schema="departmentAdmOffice.credits.period.view" layout="tabular">
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
			</fr:layout>
		</fr:view>
		<html:link page="/defineCreditsPeriods.do?method=prepareEditDepartmentAdmOfficeCreditsPeriod" paramName="teacherCreditsBean" paramProperty="executionPeriod.idInternal" paramId="executionPeriodId">
			<bean:message key="link.change"/>
		</html:link>			
			
	</logic:notEmpty>
			
</logic:present>