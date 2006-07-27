<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="SCIENTIFIC_COUNCIL">

	<span class="error"><html:errors/></span>
	<logic:messagesPresent>
		<span class="error"><html:errors/></span>
	</logic:messagesPresent>

	<p><h2><bean:message key="link.define.periods"/></h2></p>
	
	<html:form action="/defineCreditsPeriods.do?method=showPeriods">
		<logic:notEmpty name="executionPeriods">
			<p>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodId" property="executionPeriodId" onchange="this.form.submit()">
					<html:option key="choose.execution.period" value=""/>
					<html:options collection="executionPeriods" property="value" labelProperty="label"/>
				</html:select>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			</p>		
		</logic:notEmpty>
	</html:form>
	
	
	<h3 class="mtop2 mbottom05"><bean:message key="label.teacher"/></h3>
	<fr:view name="executionPeriod" schema="teacher.credits.period.view" layout="tabular">	
		<fr:layout>
			<fr:property name="classes" value="mtop0 thlight"/>
		</fr:layout>
	</fr:view>
	<html:link page="/defineCreditsPeriods.do?method=editTeacherCreditsPeriods" paramName="executionPeriod" paramProperty="idInternal" paramId="executionPeriodId">
		<bean:message key="link.change"/>
	</html:link>

	
	<h3 class="mtop2 mbottom05"><bean:message key="label.department.adm.office"/></h3>
	<fr:view name="executionPeriod" schema="departmentAdmOffice.credits.period.view" layout="tabular">
		<fr:layout>
			<fr:property name="classes" value="mtop0 thlight"/>
		</fr:layout>
	</fr:view>
	<html:link page="/defineCreditsPeriods.do?method=editDepartmentAdmOfficeCreditsPeriods" paramName="executionPeriod" paramProperty="idInternal" paramId="executionPeriodId">
		<bean:message key="link.change"/>
	</html:link>			
			
</logic:present>