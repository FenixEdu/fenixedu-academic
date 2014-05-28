<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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
	<div class="inquiries-registry2" style="max-width: 900px;">
	<fr:view name="courses" schema="curricularCourseInquiriesRegistryDTO.submitHoursAndDays" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight tdcenter"/>
			<fr:property name="style" value="width: 100%;"/>
			<fr:property name="columnClasses" value="col1,col2,col3,col4,col5,col6,col7,col8"/>
			<fr:property name="headerClasses" value="col1,col2,col3,col4,col5,col6,col7,col8"/>
			<fr:property name="suffixes" value=",h,%,%,dias,, ,"/>
			<fr:property name="linkGroupSeparator" value=" | "/>
			
			<fr:property name="linkFormat(answerNow)" value="/studentInquiry.do?method=showCurricularInquiry&amp;inquiryRegistryID=${inquiryRegistry.externalId}" />
			<fr:property name="key(answerNow)" value="link.inquiries.answerNow"/>
			<fr:property name="bundle(answerNow)" value="INQUIRIES_RESOURCES"/>
			<fr:property name="contextRelative(answerNow)" value="true"/>      
			<fr:property name="order(answerNow)" value="1"/>
			<fr:property name="visibleIf(answerNow)" value="inquiryRegistry.toAnswerLater"/>
			
			<fr:property name="linkFormat(dontRespond)" value="/studentInquiry.do?method=showJustifyNotAnswered&amp;inquiryRegistryID=${inquiryRegistry.externalId}" />
			<fr:property name="key(dontRespond)" value="link.inquiries.dontRespond"/>
			<fr:property name="bundle(dontRespond)" value="INQUIRIES_RESOURCES"/>
			<fr:property name="contextRelative(dontRespond)" value="true"/>      
			<fr:property name="order(dontRespond)" value="2"/>
			<fr:property name="visibleIf(dontRespond)" value="inquiryRegistry.toAnswerLater"/>
			
			<fr:property name="linkFormat(answerTeachers)" value="/studentInquiry.do?method=showTeachersToAnswerDirectly&amp;inquiryRegistryID=${inquiryRegistry.externalId}" />
			<fr:property name="key(answerTeachers)" value="link.inquiries.answerTeachers"/>
			<fr:property name="bundle(answerTeachers)" value="INQUIRIES_RESOURCES"/>
			<fr:property name="contextRelative(answerTeachers)" value="true"/>
			<fr:property name="visibleIf(answerTeachers)" value="inquiryRegistry.toAnswerTeachers"/>
			
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
			<bean:message key="label.weeklySpentHours.edit" bundle="INQUIRIES_RESOURCES"/>: 
			<fr:edit id="weeklySpentHours" name="weeklySpentHours" schema="inquiriesStudentExecutionPeriod.submitWeeklySpentHours" type="net.sourceforge.fenixedu.dataTransferObject.VariantBean" >
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
					<fr:property name="validatorClasses" value="error0"/>
				</fr:layout>
			</fr:edit>			
			<bean:message key="label.hoursPerWeek" bundle="INQUIRIES_RESOURCES"/>
		</p>

		<bean:define id="col5" value="col5" type="java.lang.String"/>
 		<logic:present name="estimatedWithSuccess">
			<p>
		 		<span class="success0">
		 			<bean:message key="message.inquiries.nhtaSuccessfullyEstimated" bundle="INQUIRIES_RESOURCES"/>
		 		</span>
	 		</p> 		
 			<bean:define id="col5" value="col5_estimated"/>
 		</logic:present>
 		<div class="inquiries-registry1" style="max-width: 770px; margin-bottom: 0;">
			<fr:edit id="hoursAndDaysByCourse" name="courses" schema="curricularCourseInquiriesRegistryDTO.submitHoursAndDays.edit" >
				<fr:layout name="tabular-editable" >
					<fr:property name="classes" value="tstyle1 thlight tdcenter"/>
					<fr:property name="style" value="width: 100%; margin-bottom: 0;"/>
					<fr:property name="columnClasses" value="<%= "col1,col2,col3,col4," + col5 + ",col6"%>"/>
					<fr:property name="headerClasses" value="col1,col2,col3,col4,col5,col6"/>
					<fr:property name="suffixes" value=",h,,%,h,dias"/>
					<fr:property name="validatorClasses" value="error0"/>
					<fr:property name="hideValidators" value="false"/>
					<fr:property name="allValidatorsInline" value="true" />
				</fr:layout>
			</fr:edit>
		</div>
		
		<div class="inquiries-registry-empty" style="max-width: 770px;"> 
			<table class="tstyle1 tempty thlight tdcenter" style="width: 100%; margin: 0;"> 
				<tr> 
					<td class="col1"></td>							
					<td class="col2"></td> 
					<td class="col3"></td> 
					<td class="col4"><b><bean:message key="label.inquiries.columnTotal.100" bundle="INQUIRIES_RESOURCES"/></b></td> 
					<td class="col5"></td> 
					<td class="col6"></td> 
				</tr> 
			</table> 
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
 
div.inquiries-registry1 th.col1 { }
div.inquiries-registry1 th.col2 { border-left: 5px solid #ccc; }
div.inquiries-registry1 th.col3 { }
div.inquiries-registry1 th.col4 { border-left: 5px solid #ccc; }
div.inquiries-registry1 th.col5 { }
div.inquiries-registry1 th.col6 { }

div.inquiries-registry1 td.col1 { text-align: left; }
div.inquiries-registry1 td.col2 { width: 80px !important; border-left: 5px solid #ccc; background: #f5f5f5; }
div.inquiries-registry1 td.col3 { width: 80px !important; background: #f5f5f5; }
div.inquiries-registry1 td.col4 { width: 80px !important; border-left: 5px solid #ccc; }
div.inquiries-registry1 td.col5, div.inquiries-registry1 td.col5_estimated { width: 75px !important; }
div.inquiries-registry1 td.col6 { width: 100px !important; }

div.inquiries-registry1 td.col5_estimated span { font-weight: bold; /*background: #f0f0b5; padding: 1px 3px;*/  }
 
div.inquiries-registry-empty td.col1 { text-align: left; }
div.inquiries-registry-empty td.col2 { width: 80px !important; border-left: 5px solid #ccc; background: #f5f5f5; }
div.inquiries-registry-empty td.col3 { width: 80px !important; background: #f5f5f5; }
div.inquiries-registry-empty td.col4 { width: 80px !important; border-left: 5px solid #ccc; }
div.inquiries-registry-empty td.col5 { width: 75px !important; }
div.inquiries-registry-empty td.col6 { width: 100px !important; }

div.inquiries-registry2 th.col1 { }
div.inquiries-registry2 th.col2 { border-left: 5px solid #ccc; }
div.inquiries-registry2 th.col3 { }
div.inquiries-registry2 th.col4 { border-left: 5px solid #ccc; }
div.inquiries-registry2 th.col5 { }
div.inquiries-registry2 th.col6 { border-left: 5px solid #ccc; }
div.inquiries-registry2 th.col7 { }
div.inquiries-registry2 th.col8 { border-left: 5px solid #ccc; }
 
div.inquiries-registry2 td.col1 { text-align: left; }
div.inquiries-registry2 td.col2 { width: 70px !important; border-left: 5px solid #ccc; background: #f5f5f5; }
div.inquiries-registry2 td.col3 { width: 70px !important; background: #f5f5f5; }
div.inquiries-registry2 td.col4 { width: 70px !important; border-left: 5px solid #ccc; }
div.inquiries-registry2 td.col5 { width: 70px !important; }
div.inquiries-registry2 td.col6 { width: 50px !important; border-left: 5px solid #ccc; }
div.inquiries-registry2 td.col7 { width: 50px !important; }
div.inquiries-registry2 td.col8 { width: 80px !important; border-left: 5px solid #ccc; white-space: nowrap; }
 
div.inquiries-registry1, div.inquiries-registry2 {
background: #ccc;    
padding: 3px;
margin: 15px 0;
}
 
div.inquiries-registry1 table, div.inquiries-registry2 table {
margin: 0;
}
 
div.inquiries-registry-empty {
background: #f5f5f5;
padding: 0 3px 3px 3px;
margin: 0 0 15px 0 !important;
}
div.inquiries-registry-emtpy table {
background: none;
}
table.tempty th, table.tempty td {
border-color: #f5f5f5 !important;
background: #f5f5f5;
}

</style>	