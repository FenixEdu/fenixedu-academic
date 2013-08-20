<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.departmentAdmOffice" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.teacherExpectationDefinitionPeriodManagement.title" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></h2>

<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">

	<logic:messagesPresent message="true">
		<p class="mtop2">
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>

	<logic:notEmpty name="bean">
		
		<fr:form action="/teacherPersonalExpectationsDefinitionPeriod.do?method=showPeriodWithSelectedYear">
			<div class="mtop2 mbottom1">
			<bean:message key="label.common.executionYear"/>:
			<fr:edit id="executionYear" name="bean" slot="executionYear"> 
				<fr:layout name="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsToViewTeacherPersonalExpectationsProvider"/>
					<fr:property name="format" value="${year}"/>
					<fr:destination name="postback" path="/teacherPersonalExpectationsDefinitionPeriod.do?method=showPeriodWithSelectedYear"/>
				</fr:layout>
			</fr:edit>
			<html:submit styleClass="switchNone">
				<bean:message key="label.next"/>
			</html:submit>
			</div>
		</fr:form>
	
		<logic:notEmpty name="period">
			<bean:define id="executionYearId" name="bean" property="executionYear.externalId"/>
			<fr:view name="period" schema="editTeacherPersonalExpectationsDefinitionPeriod">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright"/>
				</fr:layout>
			</fr:view>
			<ul class="list5">
				<li>
					<html:link page="<%= "/teacherPersonalExpectationsDefinitionPeriod.do?method=editPeriod&executionYearId=" + executionYearId.toString() %>">
					  <bean:message key="link.edit"/>
					</html:link>
				</li>
			</ul>
		</logic:notEmpty>

		<logic:notEmpty name="bean" property="executionYear">
			<bean:define id="executionYearId" name="bean" property="executionYear.externalId"/>
			<logic:empty name="period">
				<p class="mtop15 mbottom1"><em><bean:message key="label.noTeacherPersonalExpectationsDefinitionPeriodNotAvailable"/></em></p>
				<ul class="list5">
					<li>
						<html:link page="<%= "/teacherPersonalExpectationsDefinitionPeriod.do?method=createPeriod&executionYearId=" + executionYearId.toString() %>">
							<bean:message key="label.teacher-institution-working-time.create"/>
						</html:link>
					</li>
				</ul>
			</logic:empty>
		</logic:notEmpty>
	
		<script type="text/javascript" language="javascript">
			switchGlobal();
		</script>

	</logic:notEmpty>
	
</logic:present>