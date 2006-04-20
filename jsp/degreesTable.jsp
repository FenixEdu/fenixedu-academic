<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="degreeType" type="java.lang.String" toScope="request"><bean:write name="degreeType"/></bean:define>
&nbsp;&nbsp;<bean:message key="<%= degreeType.toString() %>" bundle="ENUMERATION_RESOURCES"/> :
<BR/>
<TABLE>
	<logic:iterate id="degree" name="oldDegrees">
		<logic:equal name="degree" property="tipoCurso" value="<%= degreeType.toString() %>">
			<bean:define id="degreeCode" name="degree" property="sigla"/>
			<bean:define id="degreeName" name="degree" property="nome"/>
			<TR>
				<TD>
					<html:link href="<%= degreeCode.toString() %>" title="<%= degreeName.toString() %>"><bean:write name="degree" property="sigla"/></html:link>
				</TD>
				<TD>
					<html:link href="<%= degreeCode.toString() + "/disciplinas"%>">
						<bean:message key="courseSite.url.subpattern" bundle="GLOBAL_RESOURCES"/>
					</html:link>
				</TD>
				<TD>
					<html:link href="<%= degreeCode.toString() + "/horarios"%>">
						<bean:message key="schedules.url.subpattern" bundle="GLOBAL_RESOURCES"/>
					</html:link>
				</TD>
				<TD>
					<html:link href="<%= degreeCode.toString() + "/exames"%>">
						<bean:message key="exams.url.subpattern" bundle="GLOBAL_RESOURCES"/>
					</html:link>
				</TD>
			</TR>
		</logic:equal>
	</logic:iterate>

	<logic:iterate id="bolonhaDegree" name="bolonhaDegrees">
		<logic:equal name="bolonhaDegree" property="tipoCurso" value="<%= degreeType.toString() %>">
			<bean:define id="degreeAcronym" name="bolonhaDegree" property="acronym"/>
			<bean:define id="degreeName" name="bolonhaDegree" property="name"/>
			<TR>
				<TD>
					<html:link href="<%= degreeAcronym.toString() %>" title="<%= degreeName.toString() %>"><bean:write name="bolonhaDegree" property="acronym"/></html:link>
				</TD>
				<TD>
					<html:link href="<%= degreeAcronym.toString() + "/disciplinas"%>">
						<bean:message key="courseSite.url.subpattern" bundle="GLOBAL_RESOURCES"/>
					</html:link>
				</TD>
				<TD>
					<html:link href="<%= degreeAcronym.toString() + "/horarios"%>">
						<bean:message key="schedules.url.subpattern" bundle="GLOBAL_RESOURCES"/>
					</html:link>
				</TD>
				<TD>
					<html:link href="<%= degreeAcronym.toString() + "/exames"%>">
						<bean:message key="exams.url.subpattern" bundle="GLOBAL_RESOURCES"/>
					</html:link>
				</TD>
			</TR>
		</logic:equal>
	</logic:iterate>
</TABLE>