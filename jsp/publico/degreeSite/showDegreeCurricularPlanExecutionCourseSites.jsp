<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegree">
<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> >
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>ensino/index.shtml</bean:define>
&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="label.education" /></a>
		<bean:define id="degreeType" name="infoDegree" property="tipoCurso" />	
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
			<bean:write name="infoDegree" property="sigla" />
		</html:link>
		&nbsp;&gt;&nbsp;<bean:message key="label.courseSites"/>		
	</div>

	<h1>
		<bean:write name="infoDegree" property="tipoCurso" />
		<bean:message key="label.in" />
		<bean:write name="infoDegree" property="nome" />
	</h1>
</logic:present>

<h2 class="greytxt">
	<bean:message key="label.courseSites"/>		
</h2>

<ul>
	<li><a href="#currentSem">
			<bean:write name="execution_period" property="semester"/><bean:message key="label.ordinal.semester.abbr" />
			<bean:write name="execution_period" property="infoExecutionYear.year" />
		</a>
	</li>
	<li><a href="#otherSem">
			<bean:write name="previousInfoExecutionPeriod" property="semester"/><bean:message key="label.ordinal.semester.abbr" />
			<bean:write name="previousInfoExecutionPeriod" property="infoExecutionYear.year" />
		</a>
	</li>
</ul>

<p id="currentSem">
<table class="tab_lay" cellspacing="0" width="90%">
	<tr>
		<th colspan="2" scope="col">
			<bean:write name="execution_period" property="semester"/><bean:message key="label.ordinal.semester.abbr" />
			<bean:write name="execution_period" property="infoExecutionYear.year" />
		</th>
	</tr>

	<bean:define id="numberRowsCurrent1_2" name="executionCourseViewsTableCurrent1_2" property="numberRows" />	
	<logic:greaterThan name="numberRowsCurrent1_2" value="0">
		<tr>
			<td class="subheader"><bean:message key="label.first.year"/></td>
			<td class="subheader"><bean:message key="label.second.year"/></td>
		</tr>
	
		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsCurrent1_2").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTableCurrent1_2" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" width="50%">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>

	<bean:define id="numberRowsCurrent3_4" name="executionCourseViewsTableCurrent3_4" property="numberRows" />
	<logic:greaterThan name="numberRowsCurrent3_4" value="0">
		<tr>
			<td class="subheader"><bean:message key="label.third.year"/></td>
			<td class="subheader"><bean:message key="label.fourth.year"/></td>
		</tr>

		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsCurrent3_4").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTableCurrent3_4" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" width="50%">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>
	
	<bean:define id="numberRowsCurrent5" name="executionCourseViewsTableCurrent5" property="numberRows" />
	<logic:greaterThan name="numberRowsCurrent5" value="0">
		<tr>
			<td colspan="2" class="subheader"><bean:message key="label.fiveth.year" /></td>
		</tr>
	
		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsCurrent5").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTableCurrent5" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" colspan="2">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>
</table>
</p>

<p id="otherSem">
<table class="tab_lay" cellspacing="0" width="90%">
	<tr>
		<th colspan="2" scope="col">
			<bean:write name="previousInfoExecutionPeriod" property="semester"/><bean:message key="label.ordinal.semester.abbr" />
			<bean:write name="previousInfoExecutionPeriod" property="infoExecutionYear.year" />
		</th>
	</tr>
	
	<bean:define id="numberRowsPrevious1_2" name="executionCourseViewsTablePrevious1_2" property="numberRows" />
	<logic:greaterThan name="numberRowsPrevious1_2" value="0">
		<tr>
			<td class="subheader"><bean:message key="label.first.year"/></td>
			<td class="subheader"><bean:message key="label.second.year"/></td>
		</tr>	
	
		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsPrevious1_2").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTablePrevious1_2" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" width="50%">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>
	
	<bean:define id="numberRowsPrevious3_4" name="executionCourseViewsTablePrevious3_4" property="numberRows" />
	<logic:greaterThan name="numberRowsPrevious3_4" value="0">
		<tr>
			<td class="subheader"><bean:message key="label.third.year"/></td>
			<td class="subheader"><bean:message key="label.fourth.year"/></td>
		</tr>
	
		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsPrevious3_4").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTablePrevious3_4" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" width="50%">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>
	
	<bean:define id="numberRowsPrevious5" name="executionCourseViewsTablePrevious5" property="numberRows" />
	<logic:greaterThan name="numberRowsPrevious5" value="0">
		<tr>
			<td colspan="2" class="subheader"><bean:message key="label.fiveth.year" /></td>
		</tr>	
	
		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsPrevious5").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTablePrevious5" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" colspan="2">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>
</table>
</p>