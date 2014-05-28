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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

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