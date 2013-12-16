<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<logic:present role="role(DEPARTMENT_ADMINISTRATIVE_OFFICE)">
	<logic:present role="role(DEPARTMENT_CREDITS_MANAGER)">
		<li class="navheader">
			<strong><bean:message key="link.group.creditsManagement"/></strong>
		</li>
		<ul>					
			<li>
			  	<html:link page="/showAllTeacherCreditsResume.do?method=prepareTeacherSearch">
			  		<bean:message key="link.teacher.sheet"/>
			  	</html:link>  
			</li>				
			<li>
				<html:link page="/prepareListDepartmentTeachersCredits.do">
					<bean:message key="link.list-department-teachers"/>
				</html:link>
			</li>			
		</ul>
		<br/>
	</logic:present>
</logic:present>