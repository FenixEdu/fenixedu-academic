<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<!-- Import new CSS for this section: #navlateral  -->
<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>

<logic:present role="SCIENTIFIC_COUNCIL">
	<ul>
<%-- OLD FEATURE 
		<li>
			<html:link page="/curricularCourseManagement.do">
				<bean:message key="link.curricularCourseManagement" />
			</html:link>
		</li>
--%>
		<li>
			<html:link page="/curricularPlans/curricularPlansManagement.faces">
				<bean:message key="curricularPlansManagement"/>
			</html:link>
		</li>
		
		<li>
			<html:link page="/curricularPlans/chooseCurricularPlan.faces">
				Gerir Grupos
			</html:link>
		</li>		
	</ul>
</logic:present>
