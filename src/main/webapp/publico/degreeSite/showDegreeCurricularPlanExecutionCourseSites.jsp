<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %></bean:define>
<div class="breadcumbs mvert0">
	<a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
	<logic:present name="infoDegree">
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>"><bean:write name="infoDegree" property="sigla"/></html:link>
	</logic:present>
	&nbsp;&gt;&nbsp;
	<bean:message key="public.degree.information.label.courseSites"  bundle="PUBLIC_DEGREE_INFORMATION" />
</div>

<!-- COURSE NAME -->
<h1>
	<logic:notEmpty name="degree" property="phdProgram">
		<bean:write name="degree" property="phdProgram.presentationName"/>
	</logic:notEmpty>
	<logic:empty name="degree" property="phdProgram">
		<bean:write name="degree" property="presentationName"/>
	</logic:empty>
</h1>

<em><span class="error0"><!-- Error messages go here --><html:errors/></span></em>

<h2 class="greytxt">
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.courseSites"/>
</h2>

<!-- BOOLEAN RENDER VALUES -->
<bean:define id="numberRowsCurrent1_2" name="executionCourseViewsTableCurrent1_2" property="numberRows" />	
<bean:define id="numberRowsCurrent3_4" name="executionCourseViewsTableCurrent3_4" property="numberRows" />
<bean:define id="numberRowsCurrent5" name="executionCourseViewsTableCurrent5" property="numberRows" />
<bean:define id="renderCurrentExecutionPeriod">
<%=(((Integer)numberRowsCurrent1_2).intValue() != 0) || (((Integer)numberRowsCurrent3_4).intValue() != 0) || (((Integer)numberRowsCurrent5).intValue() != 0)%>
</bean:define>

<bean:define id="numberRowsPrevious1_2" name="executionCourseViewsTablePrevious1_2" property="numberRows" />
<bean:define id="numberRowsPrevious3_4" name="executionCourseViewsTablePrevious3_4" property="numberRows" />
<bean:define id="numberRowsPrevious5" name="executionCourseViewsTablePrevious5" property="numberRows" />
<bean:define id="renderPreviousExecutionPeriod">
<%=(((Integer)numberRowsPrevious1_2).intValue() != 0) || (((Integer)numberRowsPrevious3_4).intValue() != 0) || (((Integer)numberRowsPrevious5).intValue() != 0)%>
</bean:define>

<!-- INFO MESSAGES -->
<logic:equal value="false" name="renderCurrentExecutionPeriod">
	<p><em><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="no.execution.courses.for.current.execution.period"/></em></p>
</logic:equal>
<logic:equal value="false" name="renderPreviousExecutionPeriod">
	<p><em><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="no.execution.courses.for.previous.execution.period"/></em></p>
</logic:equal>


<ul>
	<!-- TOP LINKS -->
	<logic:equal value="true" name="renderCurrentExecutionPeriod">
		<logic:equal value="true" name="renderPreviousExecutionPeriod">
			<li><!-- NO_CHECKSUM --><a href="#currentSem">
					<bean:write name="execution_period" property="infoExecutionYear.year" />,
					<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr" />
					<bean:write name="execution_period" property="semester"/>
				</a>
			</li>
			<li><!-- NO_CHECKSUM --><a href="#otherSem">
					<bean:write name="previousInfoExecutionPeriod" property="infoExecutionYear.year" />,
					<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr" />
					<bean:write name="previousInfoExecutionPeriod" property="semester"/>
				</a>
			</li>
		</logic:equal>
	</logic:equal>
</ul>

<!-- CURRENT EXECUTION PERIOD -->
<logic:equal value="true" name="renderCurrentExecutionPeriod">
	<table class="tab_lay" cellspacing="0" width="90%">
		<caption id="currentSem">
			<bean:write name="execution_period" property="infoExecutionYear.year" />,
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester" />
			<bean:write name="execution_period" property="semester"/>
		</caption>
	
		<logic:greaterThan name="numberRowsCurrent1_2" value="0">
			<tr>
				<th class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.first.year"/></th>
				<th class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.second.year"/></th>
			</tr>
		
			<% for (int rowIndex=0; rowIndex < Integer.valueOf(pageContext.findAttribute("numberRowsCurrent1_2").toString()).intValue(); rowIndex++) { %>
			<% String rowColor = rowIndex % 2 == 0 ? "bgwhite" : "bluecell" ; %>
			<tr>
				<logic:iterate id="executionCourseView" name="executionCourseViewsTableCurrent1_2" property='<%= "row[" + rowIndex + "]" %>'>	
				<td class="<%= rowColor %>" width="50%">
					<logic:notEmpty name="executionCourseView">										
						<a href="${executionCourseView.executionCourse.site.fullPath}">
							<bean:write name="executionCourseView" property="executionCourseName"/>
						</a>				
					</logic:notEmpty>
					<logic:empty name="executionCourseView">&nbsp;</logic:empty>
				</td>
				
				</logic:iterate>
			</tr>
			<% } %>
		</logic:greaterThan>
	
		<logic:greaterThan name="numberRowsCurrent3_4" value="0">
			<tr>
				<th class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.third.year"/></th>
				<th class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.fourth.year"/></th>
			</tr>
	
			<% for (int rowIndex=0; rowIndex < Integer.valueOf(pageContext.findAttribute("numberRowsCurrent3_4").toString()).intValue(); rowIndex++) { %>
			<% String rowColor = rowIndex % 2 == 0 ? "bgwhite" : "bluecell" ; %>
			<tr>
				<logic:iterate id="executionCourseView" name="executionCourseViewsTableCurrent3_4" property='<%= "row[" + rowIndex + "]" %>'>	
				<td class="<%= rowColor %>" width="50%">
					<logic:notEmpty name="executionCourseView">
						<a href="${executionCourseView.executionCourse.site.fullPath}">
							<bean:write name="executionCourseView" property="executionCourseName"/>
						</a>
					</logic:notEmpty>
					<logic:empty name="executionCourseView">&nbsp;</logic:empty>
				</td>
				</logic:iterate>
			</tr>
			<% } %>
		</logic:greaterThan>
		
		<logic:greaterThan name="numberRowsCurrent5" value="0">
			<tr>
				<th colspan="2" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.fiveth.year" /></th>
			</tr>
		
			<% for (int rowIndex=0; rowIndex < Integer.valueOf(pageContext.findAttribute("numberRowsCurrent5").toString()).intValue(); rowIndex++) { %>
			<% String rowColor = rowIndex % 2 == 0 ? "bgwhite" : "bluecell" ; %>
			<tr>
				<logic:iterate id="executionCourseView" name="executionCourseViewsTableCurrent5" property='<%= "row[" + rowIndex + "]" %>'>	
				<td class="<%= rowColor %>" colspan="2">
					<logic:notEmpty name="executionCourseView">
						<a href="${executionCourseView.executionCourse.site.fullPath}">
							<bean:write name="executionCourseView" property="executionCourseName"/>
						</a>
					</logic:notEmpty>
					<logic:empty name="executionCourseView">&nbsp;</logic:empty>
				</td>
				</logic:iterate>
			</tr>
			<% } %>
		</logic:greaterThan>
	</table>
	<br/>
</logic:equal>

<!-- PREVIOUS EXECUTION PERIOD -->
<logic:equal value="true" name="renderPreviousExecutionPeriod">
	<table class="tab_lay" cellspacing="0" width="90%">
		<caption id="otherSem">
			<bean:write name="previousInfoExecutionPeriod" property="infoExecutionYear.year" />,
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester" />
			<bean:write name="previousInfoExecutionPeriod" property="semester"/>
		</caption>
		
		<logic:greaterThan name="numberRowsPrevious1_2" value="0">
			<tr>
				<th class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.first.year"/></th>
				<th class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.second.year"/></th>
			</tr>	
		
			<% for (int rowIndex=0; rowIndex < Integer.valueOf(pageContext.findAttribute("numberRowsPrevious1_2").toString()).intValue(); rowIndex++) { %>
			<% String rowColor = rowIndex % 2 == 0 ? "bgwhite" : "bluecell" ; %>
			<tr>
				<logic:iterate id="executionCourseView" name="executionCourseViewsTablePrevious1_2" property='<%= "row[" + rowIndex + "]" %>'>	
				<td class="<%= rowColor %>" width="50%">
					<logic:notEmpty name="executionCourseView">
						<a href="${executionCourseView.executionCourse.site.fullPath}">
							<bean:write name="executionCourseView" property="executionCourseName"/>
						</a>
					</logic:notEmpty>
					<logic:empty name="executionCourseView">&nbsp;</logic:empty>
				</td>
				</logic:iterate>
			</tr>
			<% } %>
		</logic:greaterThan>
		
		<logic:greaterThan name="numberRowsPrevious3_4" value="0">
			<tr>
				<th class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.third.year"/></th>
				<th class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.fourth.year"/></th>
			</tr>
		
			<% for (int rowIndex=0; rowIndex < Integer.valueOf(pageContext.findAttribute("numberRowsPrevious3_4").toString()).intValue(); rowIndex++) { %>
			<% String rowColor = rowIndex % 2 == 0 ? "bgwhite" : "bluecell" ; %>
			<tr>
				<logic:iterate id="executionCourseView" name="executionCourseViewsTablePrevious3_4" property='<%= "row[" + rowIndex + "]" %>'>	
				<td class="<%= rowColor %>" width="50%">
					<logic:notEmpty name="executionCourseView">
						<a href="${executionCourseView.executionCourse.site.fullPath}">
							<bean:write name="executionCourseView" property="executionCourseName"/>
						</a>
					</logic:notEmpty>
					<logic:empty name="executionCourseView">&nbsp;</logic:empty>
				</td>
				</logic:iterate>
			</tr>
			<% } %>
		</logic:greaterThan>
		
		<logic:greaterThan name="numberRowsPrevious5" value="0">
			<tr>
				<th colspan="2" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.fiveth.year" /></th>
			</tr>	
		
			<% for (int rowIndex=0; rowIndex < Integer.valueOf(pageContext.findAttribute("numberRowsPrevious5").toString()).intValue(); rowIndex++) { %>
			<% String rowColor = rowIndex % 2 == 0 ? "bgwhite" : "bluecell" ; %>
			<tr>
				<logic:iterate id="executionCourseView" name="executionCourseViewsTablePrevious5" property='<%= "row[" + rowIndex + "]" %>'>	
				<td class="<%= rowColor %>" colspan="2">
					<logic:notEmpty name="executionCourseView">
						<a href="${executionCourseView.executionCourse.site.fullPath}">
							<bean:write name="executionCourseView" property="executionCourseName"/>
						</a>
					</logic:notEmpty>
					<logic:empty name="executionCourseView">&nbsp;</logic:empty>
				</td>
				</logic:iterate>
			</tr>
			<% } %>
		</logic:greaterThan>
	</table>
	<br/>
</logic:equal>
