<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="sections_temp" name="sections"/>
<logic:iterate id="section" name="sections_temp">
	<logic:notPresent name="section" property="superiorSection">
		<li>
	</logic:notPresent>
	<logic:present name="section" property="superiorSection">
		<dd style="padding: 0pt 0pt 0pt 20px;">
	</logic:present>
		<bean:define id="url">/executionCourse.do?method=section&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
		<html:link page="<%= url %>"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<fr:view name="section" property="name"/>
		</html:link>
	<logic:notPresent name="section" property="superiorSection">
		</li>
	</logic:notPresent>
	<logic:present name="section" property="superiorSection">
		</dd>
	</logic:present>
	<logic:present name="selectedSections">
		<% if (((java.util.Set) request.getAttribute("selectedSections")).contains(pageContext.findAttribute("section"))) { %>
			<bean:define id="sections" toScope="request" name="section" property="orderedSubSections"/>
			<logic:notEmpty name="sections">
				<li>
					<dl>
						<jsp:include page="sections.jsp"/>
					</dl>
				</li>
			</logic:notEmpty>
		<% } %>
	</logic:present>
</logic:iterate>
