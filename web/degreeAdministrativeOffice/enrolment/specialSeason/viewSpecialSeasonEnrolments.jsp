<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<h2><bean:message key="title.student.enrolment.specialSeason" bundle="DEGREE_ADM_OFFICE"/></h2>

<logic:messagesPresent message="true">
	<ul>
	<html:messages id="messages" message="true">
		<li><span class="error0"><bean:write name="messages" /></span></li>
	</html:messages>
	</ul>
	<br/>
</logic:messagesPresent>
<br/>
<br/>

<fr:form action="/specialSeasonEnrolment.do">
	<fr:edit id="showStudent" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment.SpecialSeasonEnrolmentBean" 
			 schema="specialSeason.show.student">
			 <fr:destination name="postBack" path="/specialSeasonEnrolment.do?method=changeSpecialSeasonCodePostBack"/>
			 <fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
			 </fr:layout>
	</fr:edit>		 
</fr:form>
<br/>
<br/>

<p><strong><bean:message key="message.student.enrolled.curricularCourses"/>:</strong></p>
<logic:notEmpty name="bean" property="specialSeasonAlreadyEnroled">
	<fr:form action="/specialSeasonEnrolment.do?method=deleteSpecialSeasonEnrolments" >
		<fr:edit name="bean" id="unenrol" type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment.SpecialSeasonEnrolmentBean" visible="false"/>
		<fr:edit id="specialSeasonAlreadyEnroled" name="bean" property="specialSeasonAlreadyEnroled" schema="specialSeason.show.curricular.already.enroled" >
			<fr:layout name="tabular-editable" >
				<fr:property name="sortBy" value="enrolment.executionPeriod,enrolment.curricularCourse.name"/>
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="button.unenroll"/></html:submit>
	</fr:form>
</logic:notEmpty>
<logic:empty name="bean" property="specialSeasonAlreadyEnroled">
	<p class="error">
		<bean:message key="message.no.already.specialSeason.enrollments"/>
	</p>
</logic:empty>
<br/>
<br/>

<p><strong><bean:message key="message.student.unenrolled.curricularCourses"/>:</strong></p>
<logic:notEmpty name="bean" property="specialSeasonToEnrol">
	<fr:form action="/specialSeasonEnrolment.do?method=createSpecialSeasonEnrolments" >
		<fr:edit name="bean" id="enrol" type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment.SpecialSeasonEnrolmentBean" visible="false"/>
		<fr:edit id="specialSeasonToEnrol" name="bean" property="specialSeasonToEnrol" schema="specialSeason.show.curricular.to.enrol" >
			<fr:layout name="tabular-editable" >
				<fr:property name="sortBy" value="enrolment.executionPeriod,enrolment.curricularCourse.name"/>
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="button.enroll"/></html:submit>
	</fr:form>		
</logic:notEmpty>
<logic:empty name="bean" property="specialSeasonToEnrol">
	<p class="error">
		<bean:message key="message.no.already.specialSeason.enrollments"/>
	</p>
</logic:empty>
