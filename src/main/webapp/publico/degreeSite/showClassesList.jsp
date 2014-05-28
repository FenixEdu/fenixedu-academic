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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.domain.Degree" %>

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
	<bean:message key="public.degree.information.label.classes"  bundle="PUBLIC_DEGREE_INFORMATION" />
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

<h2 class="greytxt">
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.classes"/>
</h2>

<em><span class="error0"><!-- Error messages go here --><html:errors/></span></em>

<!-- BOOLEAN RENDER VALUES -->
<bean:define id="numberRowsCurrent" name="classViewsTableCurrent" property="numberRows" />
<bean:define id="renderCurrentExecutionPeriod">
<%=(((Integer)numberRowsCurrent).intValue() != 0)%>
</bean:define>

<bean:define id="numberRowsNext" name="classViewsTableNext" property="numberRows" />
<bean:define id="renderNextExecutionPeriod">
<%=(((Integer)numberRowsNext).intValue() != 0)%>
</bean:define>

<!-- INFO MESSAGES -->
<logic:equal value="false" name="renderCurrentExecutionPeriod">
	<p><em><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="no.classes.for.current.execution.period"/></em></p>
</logic:equal>
<logic:equal value="false" name="renderNextExecutionPeriod">
	<p><em><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="no.classes.for.next.execution.period"/></em></p>
</logic:equal>

<!-- TABLE (IF INFO TO DISPLAY) -->

<bean:define id="currentSemester" name="execution_period" property="semester"/>
<bean:define id="degree" name="degree" type="net.sourceforge.fenixedu.domain.Degree"/>

	<logic:equal value="true" name="renderCurrentExecutionPeriod">
<table class="tab_lay" cellspacing="0">
		<caption>
			<bean:write name="execution_period" property="infoExecutionYear.year"/>,
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester"/>
			<bean:write name="execution_period" property="semester"/>
		</caption>
	
		<tr>
		<%  java.util.Iterator iter = degree.buildFullCurricularYearList().iterator();
			while (iter.hasNext()) {
				Integer curricularYear = (Integer)iter.next(); %>
			<th class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/> <%= String.valueOf(curricularYear) %></th>
		<% } %>
		</tr>
	
	
		<% for (int rowIndex=0; rowIndex < Integer.valueOf(pageContext.findAttribute("numberRowsCurrent").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "bgwhite" : "bluecell" ; %>
		<tr>
			<logic:iterate id="classView" name="classViewsTableCurrent" property='<%= "row[" + rowIndex + "]" %>'>
				<td class="<%= rowColor %>">
					<logic:notEmpty name="classView">
						<bean:define id="classOID" name="classView" property="classOID"/>
						<bean:define id="className" name="classView" property="className"/>
						<bean:define id="degreeCurricularPlanID" name="classView" property="degreeCurricularPlanID"/>
						<bean:define id="degreeInitials" name="classView" property="degreeInitials"/>
						<bean:define id="nameDegreeCurricularPlan" name="classView" property="nameDegreeCurricularPlan"/>
	
						<html:link page="<%= "/viewClassTimeTableNew.do?classId="
												+ pageContext.findAttribute("classOID").toString()
												+ "&amp;className="
												+ pageContext.findAttribute("className").toString()
												+ "&amp;degreeInitials="
												+ pageContext.findAttribute("degreeInitials").toString()
												+ "&amp;degreeCurricularPlanID="
												+ pageContext.findAttribute("degreeCurricularPlanID").toString()
												+ "&amp;degreeID="
												+ pageContext.findAttribute("degreeID").toString()
											%>" >
							<bean:write name="classView" property="className"/>
						</html:link>
					</logic:notEmpty>
					<logic:empty name="classView">
						&nbsp;
					</logic:empty>
				</td>
			</logic:iterate>
		</tr>
		<% } %>
</table>
	</logic:equal>



	<logic:equal value="true" name="renderNextExecutionPeriod">
<table class="tab_lay" cellspacing="0">
		<caption>
				<bean:write name="nextInfoExecutionPeriod" property="infoExecutionYear.year"/>,
				<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester"/>
				<bean:write name="nextInfoExecutionPeriod" property="semester"/>
		</caption>

		<tr>
		<%  java.util.Iterator iter = degree.buildFullCurricularYearList().iterator();
			while (iter.hasNext()) {
				Integer curricularYear = (Integer)iter.next(); %>
			<th class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/> <%= String.valueOf(curricularYear) %></th>
		<% } %>
		</tr>
	
		<% for (int rowIndex=0; rowIndex < Integer.valueOf(pageContext.findAttribute("numberRowsNext").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "bgwhite" : "bluecell" ; %>
		<tr>	
			<logic:iterate id="classView" name="classViewsTableNext" property='<%= "row[" + rowIndex + "]" %>'>
				<td class="<%= rowColor %>">
					<logic:notEmpty name="classView">
						<bean:define id="classOID" name="classView" property="classOID"/>
						<bean:define id="className" name="classView" property="className"/>
						<bean:define id="degreeCurricularPlanID" name="classView" property="degreeCurricularPlanID"/>
						<bean:define id="degreeInitials" name="classView" property="degreeInitials"/>
						<bean:define id="nameDegreeCurricularPlan" name="classView" property="nameDegreeCurricularPlan"/>
	
						<html:link page="<%= "/viewClassTimeTableNew.do?classId="
												+ pageContext.findAttribute("classOID").toString()
												+ "&amp;className="
												+ pageContext.findAttribute("className").toString()
												+ "&amp;degreeInitials="
												+ pageContext.findAttribute("degreeInitials").toString()
												+ "&amp;degreeCurricularPlanID="
												+ pageContext.findAttribute("degreeCurricularPlanID").toString()
												+ "&amp;degreeID="
												+ pageContext.findAttribute("degreeID").toString()
											%>" >
							<bean:write name="classView" property="className"/>
						</html:link>
					</logic:notEmpty>
					<logic:empty name="classView">
						&nbsp;
					</logic:empty>
				</td>
			</logic:iterate>		
		</tr>
		<% } %>
</table>
	</logic:equal>
