<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="degreeType" type="java.lang.String" toScope="request">
	<bean:write name="degreeType"/>
</bean:define>

<bean:define id="renderBolonha" type="java.lang.String" toScope="request">
	<bean:write name="renderBolonha"/>
</bean:define>

&nbsp;&nbsp;<h3><bean:message key="<%= degreeType.toString() %>" bundle="ENUMERATION_RESOURCES"/> :</h3>
<br/>

<%
	boolean hasDegree = false;
%>

	<logic:iterate id="degree" name="degrees" type="net.sourceforge.fenixedu.domain.Degree">
		<logic:equal name="degree" property="degreeType" value="<%= degreeType.toString() %>">
			<%if ((renderBolonha.equals("false") && !degree.isBolonhaDegree())|| (renderBolonha.equals("true") && degree.isBolonhaDegree())) {
				hasDegree = true;
			} %>
		</logic:equal>
	</logic:iterate>

<%
	if (hasDegree) {
%>

<table>
	<logic:iterate id="degree" name="degrees" type="net.sourceforge.fenixedu.domain.Degree">
		<logic:equal name="degree" property="degreeType" value="<%= degreeType.toString() %>">
			<%if ((renderBolonha.equals("false") && !degree.isBolonhaDegree())|| (renderBolonha.equals("true") && degree.isBolonhaDegree())) { %>
				<bean:define id="degreeCode" name="degree" property="sigla"/>
				<bean:define id="degreeName" name="degree" property="presentationName"/>
				<tr>
					<td>
						<bean:define id="degreeSitePath" name="degree" property="site.reversePath"/>
						<html:link href="<%= request.getContextPath() + degreeSitePath.toString() %>" title="<%= degreeName.toString() %>"><bean:write name="degree" property="sigla"/></html:link>
					</td>
					<td>
						<html:link href="<%= degreeCode.toString() %>" title="<%= degreeName.toString() %>"><bean:write name="degree" property="sigla"/></html:link>
					</td>
					<td>
						<html:link href="<%= degreeCode.toString() + "/disciplinas"%>">
							<bean:message key="courseSite.url.subpattern" bundle="GLOBAL_RESOURCES"/>
						</html:link>
					</td>
					<td>
						<html:link href="<%= degreeCode.toString() + "/horarios"%>">
							<bean:message key="schedules.url.subpattern" bundle="GLOBAL_RESOURCES"/>
						</html:link>
					</td>
					<td>
						<html:link href="<%= degreeCode.toString() + "/exames"%>">
							<bean:message key="exams.url.subpattern" bundle="GLOBAL_RESOURCES"/>
						</html:link>
					</td>
				</tr>
			<% } %>
		</logic:equal>
	</logic:iterate>
</table>

<%
	}
%>
