<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.domain.Language"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="sections_temp" name="sections"/>
<logic:iterate id="section" name="sections_temp" type="net.sourceforge.fenixedu.domain.Section">


	<logic:notPresent name="section" property="superiorSection">
		<li class="sub1">
	</logic:notPresent>

	<logic:present name="section" property="superiorSection">
		<li class="sub2">
	</logic:present>

		<bean:define id="url">/manageExecutionCourse.do?method=section&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
		<html:link page="<%= url %>"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<%= section.getName().getContent(Language.pt) %>
		</html:link>

	<logic:notPresent name="section" property="superiorSection">
		</li>
	</logic:notPresent>

	<logic:present name="section" property="superiorSection">
		</li>
	</logic:present>
	
	
	<logic:present name="selectedSections">
		<% if (((java.util.Set) request.getAttribute("selectedSections")).contains(pageContext.findAttribute("section"))) { %>
			<bean:define id="sections" toScope="request" name="section" property="orderedSubSections"/>
			<logic:notEmpty name="sections">
				<li>
					<jsp:include page="sections.jsp"/>
				</li>
			</logic:notEmpty>
		<% } %>
	</logic:present>
</logic:iterate>
