<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries.student" bundle="INQUIRIES_RESOURCES"/></h2>

<logic:present name="executionSemester" property="inquiryResponsePeriod">
	<logic:notEmpty name="executionSemester" property="inquiryResponsePeriod.introduction">	
		<div>
			<bean:write name="executionSemester" property="inquiryResponsePeriod.introduction" filter="false"/>
		</div>
	</logic:notEmpty>
</logic:present>
<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.introduction" bundle="INQUIRIES_RESOURCES"/></span></h3>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<logic:equal name="student" property="weeklySpentHoursSubmittedForOpenInquiry" value="true">

	<p>
		<bean:message key="label.weeklySpentHours" bundle="INQUIRIES_RESOURCES"/>: 
		<b><bean:write name="student" property="openStudentInquiryExecutionPeriod.weeklyHoursSpentInClassesSeason"/></b> 
		<bean:message key="label.hoursPerWeek.a" bundle="INQUIRIES_RESOURCES"/>
	</p>
	<div class="inquiries-registry" style="width: 1000px;">
	<fr:view name="courses" schema="curricularCourseInquiriesRegistryDTO.submitHoursAndDays" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight tdcenter tdwith90px"/>
			<fr:property name="columnClasses" value="col1,col2,col3,col4,col5,col6,col7,col8"/>
			<fr:property name="headerClasses" value="col1,col2,col3,col4,col5,col6,col7,col8"/>
			<fr:property name="suffixes" value=",%,dias,h,%,, ,"/>
			<fr:property name="linkGroupSeparator" value=" | "/>
			
			<fr:property name="linkFormat(answerNow)" value="/studentInquiry.do?method=showCurricularInquiry&amp;inquiryRegistryID=${inquiryRegistry.externalId}" />
			<fr:property name="key(answerNow)" value="link.inquiries.answerNow"/>
			<fr:property name="bundle(answerNow)" value="INQUIRIES_RESOURCES"/>
			<fr:property name="contextRelative(answerNow)" value="true"/>      
			<fr:property name="order(answerNow)" value="2"/>
			<fr:property name="visibleIf(answerNow)" value="inquiryRegistry.toAnswerLater"/>
			
			<fr:property name="linkFormat(dontRespond)" value="/studentInquiry.do?method=showJustifyNotAnswered&amp;inquiryRegistryID=${inquiryRegistry.externalId}" />
			<fr:property name="key(dontRespond)" value="link.inquiries.dontRespond"/>
			<fr:property name="bundle(dontRespond)" value="INQUIRIES_RESOURCES"/>
			<fr:property name="contextRelative(dontRespond)" value="true"/>      
			<fr:property name="order(dontRespond)" value="1"/>
			<fr:property name="visibleIf(dontRespond)" value="inquiryRegistry.toAnswerLater"/>
			
			<fr:property name="visibleIf(notAnswered)" value="inquiryRegistry.notAnswered"/>
			<fr:property name="customLink(notAnswered)" >
				<em><bean:message key="label.notAnswered" bundle="INQUIRIES_RESOURCES" /></em>
			</fr:property>
			
			<fr:property name="visibleIf(answered)" value="inquiryRegistry.answered"/>
			<fr:property name="customLink(answered)" >
				<span class="success0"><bean:message key="label.answered" bundle="INQUIRIES_RESOURCES" /></span>
			</fr:property>
			
			<fr:property name="visibleIfNot(notAvailableToInquiries)" value="inquiryRegistry.availableToInquiries"/>
			<fr:property name="customLink(notAvailableToInquiries)" >
				<em><bean:message key="label.notAvailableToInquiries" bundle="INQUIRIES_RESOURCES" /></em>
			</fr:property>			
			
		</fr:layout>
	</fr:view>
	</div>
</logic:equal>

<logic:equal name="student" property="weeklySpentHoursSubmittedForOpenInquiry" value="false">

<p><bean:message key="message.inquiries.nhta.definition" bundle="INQUIRIES_RESOURCES"/></p>

<p><b><bean:message key="message.inquiries.nhta.total" bundle="INQUIRIES_RESOURCES"/></b></p>
	
	<fr:form action="/studentInquiry.do">
		<html:hidden property="method" value="submitWeeklySpentHours"/>
		<p>
			<bean:message key="label.weeklySpentHours" bundle="INQUIRIES_RESOURCES"/>: 
			<fr:edit id="weeklySpentHours" name="weeklySpentHours" schema="inquiriesStudentExecutionPeriod.submitWeeklySpentHours" type="net.sourceforge.fenixedu.dataTransferObject.VariantBean" >
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
					<fr:property name="validatorClasses" value="error0"/>
				</fr:layout>
			</fr:edit>			
			<bean:message key="label.hoursPerWeek" bundle="INQUIRIES_RESOURCES"/>
		</p>

		<div class="inquiries-registry" style="width: 900px;">
			<fr:edit id="hoursAndDaysByCourse" name="courses" schema="curricularCourseInquiriesRegistryDTO.submitHoursAndDays.edit" >
				<fr:layout name="tabular-editable" >
					<fr:property name="classes" value="tstyle1 thlight tdcenter"/>
					<fr:property name="columnClasses" value="col1,col2,col3,col4,col5,col6"/>
					<fr:property name="headerClasses" value="col1,col2,col3,col4,col5,col6"/>
					<fr:property name="suffixes" value=",h,,%,dias,h"/>
					<fr:property name="validatorClasses" value="error0"/>
					<fr:property name="hideValidators" value="false"/>
					<fr:property name="allValidatorsInline" value="true" />
				</fr:layout>
			</fr:edit>
		</div>		
		<p>
			<html:submit onclick="this.form.method.value='simulateAWH';"><bean:message key="button.simulateAWH" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</p>	
		
		<p class="mvert1"><bean:message key="message.inquiries.estimatedECTS" bundle="INQUIRIES_RESOURCES"/></p>
		
		<p class="mtop15"><html:submit><bean:message key="button.submit" bundle="INQUIRIES_RESOURCES"/></html:submit></p>	
	</fr:form>
</logic:equal>

<style>

span.error {
white-space: nowrap
}

th.empty, td.empty {
background: none;
border-top: none;
border-bottom: none;
width: 10px !important;
padding: 0 !important;
}

th.col1 { }
th.col2 { border-left: 6px solid #ccc;}
th.col3 {  }
th.col4 { border-left: 6px solid #ccc; }
th.col5 { }
th.col6 { border-left: 6px solid #ccc; }
th.col7 { }
th.col8 { border-left: 6px solid #ccc; }

td.col1 { white-space: nowrap; text-align: left; }
td.col2 { border-left: 6px solid #ccc; }
td.col3 {  }
td.col4 { border-left: 6px solid #ccc; }
td.col5 { }
td.col6 { border-left: 6px solid #ccc; }
td.col7 { }
td.col8 { border-left: 6px solid #ccc; white-space: nowrap; }

div.inquiries-registry {
background: #ccc;	
padding: 4px;
margin: 15px 0;
}

div.inquiries-registry table {
margin: 0;
}

div.inquiries-registry table td {
width: 90px;
}

</style>	