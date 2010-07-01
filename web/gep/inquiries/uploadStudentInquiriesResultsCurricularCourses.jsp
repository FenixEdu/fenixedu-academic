<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<span class="error"><html:errors bundle="INQUIRIES_RESOURCES" /></span>
<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
    <p><span class="error"><bean:write name="message" /></span></p>
</html:messages>

<h2><bean:message key="title.inquiries.studentInquiry.uploadCourseResults" bundle="INQUIRIES_RESOURCES"/></h2>

<fr:edit id="uploadCourseFileBean" name="uploadCourseFileBean" schema="studentInquiry.uploadCourseResults" action="/uploadStudentInquiriesResults.do?method=submitCourseFile" >
	<fr:layout name="tabular">
	    <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
	    <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
	<fr:destination name="cancel" path="/uploadStudentInquiriesResults.do?method=prepare"/>
</fr:edit>

<br/>
<h3><bean:message key="title.inquiries.studentInquiry.deleteCourseResults" bundle="INQUIRIES_RESOURCES"/></h3>
<fr:form id="deleteFormTeaching" action="/uploadStudentInquiriesResults.do?method=deleteCurricularCoursesData">
	<fr:edit id="deleteCourseDataBean" name="uploadCourseFileBean" schema="studentInquiry.deleteCourseResults">
		<fr:layout name="tabular">
		    <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
		    <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
		<fr:destination name="cancel" path="/uploadStudentInquiriesResults.do?method=prepare"/>
	</fr:edit>

	<div id="deleteCourse">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"
					onclick="document.getElementById('deleteConfirmCourse').style.display='block';
							document.getElementById('deleteCourse').style.display='none';return false;">
			<bean:message key="button.delete"/>
		</html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton">
			<bean:message key="button.cancel"/>
		</html:cancel>
	</div>
	
	<div id="deleteConfirmCourse" class="infoop2 switchInline">	
		<p class="mvert05"><bean:message key="message.studentInquiry.delete.confirm" bundle="INQUIRIES_RESOURCES"/></p>
		<p class="mvert05">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.delete"/>
			</html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"
						onclick="document.getElementById('deleteConfirmCourse').style.display='none'; 
								document.getElementById('deleteCourse').style.display='block';return false;">
				<bean:message key="button.cancel"/>
			</html:cancel>
		</p>
	</div>
</fr:form>