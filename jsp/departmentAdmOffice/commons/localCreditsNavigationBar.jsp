<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">
	<logic:present role="DEPARTMENT_CREDITS_MANAGER">
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