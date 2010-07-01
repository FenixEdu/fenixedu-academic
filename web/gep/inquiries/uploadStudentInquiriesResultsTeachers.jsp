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

<h2><bean:message key="title.inquiries.studentInquiry.uploadTeachingResults" bundle="INQUIRIES_RESOURCES"/></h2>

<fr:edit id="uploadTeachingFileBean" name="uploadTeachingFileBean" schema="studentInquiry.uploadTeachingResults" action="/uploadStudentInquiriesResults.do?method=submitTeachingFile" >
	<fr:layout name="tabular">
	    <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
	    <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
	<fr:destination name="cancel" path="/uploadStudentInquiriesResults.do?method=prepare"/>
</fr:edit>

<br/>
<h3><bean:message key="title.inquiries.studentInquiry.deleteTeachingResults" bundle="INQUIRIES_RESOURCES"/></h3>
<fr:form id="deleteFormTeaching" action="/uploadStudentInquiriesResults.do?method=deleteTeachingData">
	<fr:edit id="deleteTeachingDataBean" name="uploadTeachingFileBean" schema="studentInquiry.deleteTeachingResults">
		<fr:layout name="tabular">
		    <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
		    <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
		<fr:destination name="cancel" path="/uploadStudentInquiriesResults.do?method=prepare"/>
	</fr:edit>
	
	<div id="deleteTeaching">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"
					onclick="document.getElementById('deleteConfirmTeaching').style.display='block';
							document.getElementById('deleteTeaching').style.display='none';return false;">
			<bean:message key="button.delete"/>
		</html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton">
			<bean:message key="button.cancel"/>
		</html:cancel>
	</div>
	
	<div id="deleteConfirmTeaching" class="infoop2 switchInline">	
		<p class="mvert05"><bean:message key="message.studentInquiry.delete.confirm" bundle="INQUIRIES_RESOURCES"/></p>
		<p class="mvert05">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.delete"/>
			</html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"
						onclick="document.getElementById('deleteConfirmTeaching').style.display='none'; 
								document.getElementById('deleteTeaching').style.display='block';return false;">
				<bean:message key="button.cancel"/>
			</html:cancel>
		</p>
	</div>
</fr:form>