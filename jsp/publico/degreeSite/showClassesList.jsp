<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegree">
	<div class="breadcumbs"><a href="http://www.ist.utl.pt/"><bean:message key="label.school" /></a> 
		<bean:define id="degreeType" name="infoDegree" property="tipoCurso" />	
		&nbsp;&gt;&nbsp;<a href="http://www.ist.utl.pt/html/ensino/"><bean:message key="label.education" /></a>	
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
			<bean:write name="infoDegree" property="sigla" />
		</html:link>
		&nbsp;&gt;&nbsp;<bean:message key="label.classes"/>		
	</div>

	<h1>
		<bean:write name="infoDegree" property="tipoCurso" />
		<bean:message key="label.in" />
		<bean:write name="infoDegree" property="nome" />
	</h1>

	<h2><span class="greytxt"><bean:message key="label.classes" /></span></h2>
</logic:present>


<bean:define id="nextSemester" name="previousInfoExecutionPeriod" property="semester"/>
<bean:define id="currentSemester" name="execution_period" property="semester"/>

<table class="tab_lay" cellspacing="0" width="75%">
	<tr>
		<th colspan="5" scope="col">
			<bean:write name="execution_period" property="semester"/><bean:message key="label.ordinal.semester.abbr"/>
			<bean:write name="execution_period" property="infoExecutionYear.year"/>
		</th>	
	</tr>

	<tr>
	<% for (int year = 1; year <= 5; year++) { %>
		<td class="subheader" width="75px"><%= String.valueOf(year) %><bean:message key="label.ordinal.year"/></td>
	<% } %>
	</tr>

	<bean:define id="numberRowsCurrent" name="classViewsTableCurrent" property="numberRows" />
	<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsCurrent").toString()).intValue(); rowIndex++) { %>
	<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
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
											+ "&amp;nameDegreeCurricularPlan="
											+ pageContext.findAttribute("nameDegreeCurricularPlan").toString()
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

	<tr>
		<th colspan="5" scope="col">
			<bean:write name="previousInfoExecutionPeriod" property="semester"/><bean:message key="label.ordinal.semester.abbr"/>
			<bean:write name="previousInfoExecutionPeriod" property="infoExecutionYear.year"/>
		</th>	
	</tr>

	<tr>
	<% for (int year = 1; year <= 5; year++) { %>
		<td class="subheader"><%= String.valueOf(year) %><bean:message key="label.ordinal.year"/></td>
	<% } %>
	</tr>

	<bean:define id="numberRowsNext" name="classViewsTablePrevious" property="numberRows" />
	<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsNext").toString()).intValue(); rowIndex++) { %>
	<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
	<tr>	
		<logic:iterate id="classView" name="classViewsTablePrevious" property='<%= "row[" + rowIndex + "]" %>'>
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
											+ "&amp;nameDegreeCurricularPlan="
											+ pageContext.findAttribute("nameDegreeCurricularPlan").toString()
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

