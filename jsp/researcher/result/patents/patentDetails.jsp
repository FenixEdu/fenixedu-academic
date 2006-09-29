<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<span class="error0"><!-- Error messages go here --><html:errors /></span>

<html:messages id="message" message="true" bundle="RESEARCHER_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message"/>
	</span>
</html:messages>

<logic:present role="RESEARCHER">
	<em>Patentes</em> <!-- tobundle -->
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.details.useCase.title"/></h2>

	<ul class="mvert2 list5">
		<li>
			<html:link page="/resultPatents/management.do">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
			</html:link>
		</li>
	</ul>
	
	<jsp:include page="patentInfo.jsp"/>

</logic:present>
