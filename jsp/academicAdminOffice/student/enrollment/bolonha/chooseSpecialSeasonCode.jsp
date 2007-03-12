<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="org.apache.struts.action.ActionMessages" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.academicAdminOffice" /></em>
	<h2>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.student.enrollment.courses" />
	</h2>
	<br/>
	<br/>
	
	<fr:form action="/specialSeasonBolonhaStudentEnrollment.do">
		<html:hidden property="method" value=""/>
		<fr:edit id="chooseCode" name="specialSeasonCodeBean" type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment.SpecialSeasonEnrolmentBean" 
			 schema="specialSeason.choose.special.season.code">
			 <fr:destination name="postBack" path="/specialSeasonBolonhaStudentEnrollment.do?method=changeSpecialSeasonCodePostBack"/>
			 <fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
			 </fr:layout>
		</fr:edit>
		<p class="mtop05 mbottom1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='prepare';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.continue"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" onclick="this.form.method.value='backFromChooseSpeciaSeasonCode';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.back"/></html:submit>
		</p>
		
	</fr:form>
	

</logic:present>