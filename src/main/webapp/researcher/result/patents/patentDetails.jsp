<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<span class="error0"><!-- Error messages go here --><html:errors /></span>

<html:messages id="message" message="true" bundle="RESEARCHER_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message"/>
	</span>
</html:messages>

<em>Patentes</em> <!-- tobundle -->
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.details.useCase.title"/></h2>

<ul class="mvert2 list5">
	<li>
		<html:link page="/resultPatents/management.do">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
		</html:link>
	</li>
</ul>

<jsp:include page="patentInfo.jsp"/>
