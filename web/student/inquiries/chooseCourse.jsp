<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style>
table.tdwith50px td { width: 50px; }
table.tdwith90px td { width: 90px; }
.aleft { text-align: left !important; }
.acenter { text-align: center !important; }
.bold { font-weight: bold !important; }
</style>
<style>
a.help {
position: relative;
text-decoration: none;
/*color: black !important;*/
border: none !important;
width: 20px;
}
a.help span {
display: none;
}
a.help:hover {
/*background: none;*/ /* IE hack */
z-index: 100;
}
a.help:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 10px;
left: 30px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
/*cursor: help;*/
}
</style>

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries" bundle="INQUIRIES_RESOURCES"/></h2>

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

<c:if test="${student.weeklySpentHoursSubmittedForCurrentPeriod}">

	<bean:message key="label.weeklySpentHours" bundle="INQUIRIES_RESOURCES"/>: <c:out value="${student.currentInquiriesStudentExecutionPeriod.weeklyHoursSpentInClassesSeason}" /> <bean:message key="label.hoursPerWeek" bundle="INQUIRIES_RESOURCES"/>

	<fr:view name="courses" schema="curricularCourseInquiriesRegistryDTO.submitHoursAndDays" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight tdcenter tdwith90px"/>
			<fr:property name="columnClasses" value="nowrap aleft,,,,,"/>
			<fr:property name="suffixes" value=",ECTS,h,%,dias,ECTS,"/>
			
			<fr:property name="linkFormat(answerNow)" value="/studentInquiry.do?method=showInquiries1stPage&amp;inquiriesRegistryID=${inquiriesRegistry.idInternal}" />
			<fr:property name="key(answerNow)" value="link.inquiries.answerNow"/>
			<fr:property name="bundle(answerNow)" value="INQUIRIES_RESOURCES"/>
			<fr:property name="contextRelative(answerNow)" value="true"/>      
			<fr:property name="order(answerNow)" value="1"/>
			<fr:property name="visibleIfNot(answerNow)" value="inquiriesRegistry.answered"/>
		</fr:layout>
	</fr:view>

</c:if>

<c:if test="${!student.weeklySpentHoursSubmittedForCurrentPeriod}">
	<fr:form action="/studentInquiry.do">
		<html:hidden property="method" value="submitWeeklySpentHours"/>

		<bean:message key="label.weeklySpentHours" bundle="INQUIRIES_RESOURCES"/>: 
		<fr:create id="weeklySpentHours" schema="inquiriesStudentExecutionPeriod.submitWeeklySpentHours" type="net.sourceforge.fenixedu.dataTransferObject.VariantBean" >
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
				<fr:property name="validatorClasses" value="error0"/>
			</fr:layout>			
		</fr:create>
		 <bean:message key="label.hoursPerWeek" bundle="INQUIRIES_RESOURCES"/>

		<fr:edit id="hoursAndDaysByCourse" name="courses" schema="curricularCourseInquiriesRegistryDTO.submitHoursAndDays.edit" >
			<fr:layout name="tabular-editable" >
				<fr:property name="classes" value="tstyle1 thlight tdcenter tdwith90px"/>
				<fr:property name="columnClasses" value="nowrap aleft,,,,,"/>
				<fr:property name="suffixes" value=",h,%,dias,,,"/>
				<fr:property name="validatorClasses" value="error0"/>
				<fr:property name="hideValidators" value="false"/>
			</fr:layout>
		</fr:edit>

		<html:submit><bean:message key="button.submit" bundle="INQUIRIES_RESOURCES"/></html:submit>	
	</fr:form>
</c:if>
