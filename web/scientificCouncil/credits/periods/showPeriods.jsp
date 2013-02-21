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
		
		<fr:form action="/defineCreditsPeriods.do">
			<fr:edit name="teacherCreditsBean" schema="teacher.credits.list.execution.periods" id="teacherCreditsBeanID">
				<fr:destination name="postBack" path="/defineCreditsPeriods.do?method=showPeriods"/>		
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
					<fr:property name="columnClasses" value=",,tdclear"/>			
				</fr:layout>			
			</fr:edit>
			<logic:present role="MANAGER">
				<bean:define id="executionYearOid" name="teacherCreditsBean" property="executionPeriod.executionYear.externalId"/>
				<logic:equal name="teacherCreditsBean" property="executionPeriod.executionYear.annualCreditsState.isCreditsClosed" value="false">
					<logic:equal name="teacherCreditsBean" property="executionPeriod.executionYear.annualCreditsState.isFinalCreditsCalculated" value="true">
						<p><html:link page='<%="/annualTeachingCreditsDocument.do?method=getAnnualTeachingCreditsPdf&executionYearOid=" + executionYearOid %>'>
							<bean:message key="link.teacherCredits.close"/>
						</html:link></p>
						<bean:message key="label.teacherCredits.close.message"/>
					</logic:equal>
				</logic:equal>
			</logic:present>
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
		
		<h3 class="mtop15 mbottom05">Outras datas</h3>
		<fr:edit name="teacherCreditsBean" property="annualCreditsState">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.AnnualCreditsState">
				<fr:slot name="orientationsCalculationDate" layout="null-as-label"/>
				<fr:slot name="finalCalculationDate" layout="null-as-label"/>
				<fr:slot name="closeCreditsDate" layout="null-as-label"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
			
</logic:present>

<script type="text/javascript">
function check(e,v){
	if (e.className == "dnone")
  	{
	  e.className = "dblock";
	  v.value = "-";
	}
	else {
	  e.className = "dnone";
  	  v.value = "+";
	}
}
</script>