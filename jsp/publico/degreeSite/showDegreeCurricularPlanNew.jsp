<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeCurricularPlan">
	<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> >
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>ensino/index.shtml</bean:define>
&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="label.education" /></a>
		<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />	
		&nbsp;&gt;&nbsp;		
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
			<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
		</html:link>
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString()%>" >
<%-- &amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") --%>
		<bean:message key="label.curricularPlan"/>
	</html:link>
	&nbsp;&gt;&nbsp;<bean:message key="label.curriculum"/>		
</div>	

	<!-- COURSE NAME -->
	<h1>
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />
		<bean:message key="label.in" />
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" />
	</h1>

	<!-- CURRICULAR PLAN -->
	<h2 class="greytxt">
		<bean:message key="label.curricularPlan"/>
		<bean:message key="label.of" />
		<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />		
		<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
		<logic:notEmpty name="infoDegreeCurricularPlan" property="endDate">
			<bean:define id="endDate" name="infoDegreeCurricularPlan" property="endDate" />	
			-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
		</logic:notEmpty>
	</h2>

	<!-- EXECUTIVE AND CURRICULAR YEAR SELECTION -->
	<html:form action="/prepareConsultCurricularPlanNew.do">
		<html:hidden property="<%SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
		<html:hidden property="degreeID" value="<%= ""+request.getAttribute("degreeID")%>" />
		<html:hidden property="degreeCurricularPlanID" value="<%= pageContext.findAttribute("degreeCurricularPlanID").toString() %>" />	
		<html:hidden property="index" value="<%= ""+ request.getAttribute("index")%>"/>
		<html:hidden property="page" value="1"/>
		<html:hidden property="method" value="select"/>
		<html:hidden property="<%SessionConstants.EXECUTION_DEGREE%>" value="<%= "" + request.getAttribute(SessionConstants.EXECUTION_DEGREE)%>"/>

		<table border="0" cellspacing="2" cellpadding="0">
			<tr>
				<td><bean:message key="label.executionYear"/>:</td>
				<td>
					<html:select property="indice" size="1" onchange='this.form.submit();'>
						<html:options property="value" labelProperty="label" collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td><bean:message key="label.curricularYear"/>:</td>
				<td>
					<html:select property="curYear" size="1" onchange='this.form.submit();'>
						<html:options collection="curricularYearList" property="value" labelProperty="label"/>
					</html:select>				
				</td>
			</tr>
		</table>
	</html:form> 
	
	<!-- CURRICULAR PLAN BY COURSES -->
	<logic:present name="allActiveCurricularCourseScopes">
	<logic:notEmpty name="allActiveCurricularCourseScopes">

	<logic:iterate id="curricularCourseScopeElemList" name="allActiveCurricularCourseScopes" length="1">
		<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="DataBeans.InfoCurricularCourseScope" length="1">
			<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
		</logic:iterate>
	</logic:iterate>
	
	<table class="tab_lay" cellspacing="0" cellpadding="5">
		<tr>
			<th colspan="11" scope="col">
				<bean:write name="currentYear"/><bean:message key="label.ordinal.year" />
			</th>
		</tr>
		<tr>						
			<td colspan="7" class="subheader">&nbsp;</td>
			<td colspan="4" class="subheader"><bean:message key="label.hoursPerWeek" /></td>
		</tr>	
		</tr>
			<td class="subheader"><bean:message key="label.semester.abbr"/></td>
			<td class="subheader"><bean:message key="label.curricularCourse"/></td>
			<td class="subheader"><bean:message key="label.type"/></td>
			<td class="subheader"><bean:message key="label.branch"/></td>			
			<td class="subheader"><bean:message key="label.credits"/></td>
			<td class="subheader"><bean:message key="label.ects"/></td>
			<td class="subheader"><bean:message key="label.weight"/></td>
			<td class="subheader"><bean:message key="label.theoretical.abbr"/></td>
			<td class="subheader"><bean:message key="label.pratical.abbr"/></td>
			<td class="subheader"><bean:message key="label.theoPrat.abbr"/></td>
			<td class="subheader"><bean:message key="label.laboratorial.abbr"/></td>
		</tr>
		
		<% int count=0; %>
		<logic:iterate id="curricularCourseScopeElemList" name="allActiveCurricularCourseScopes" indexId="row">
			<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="DataBeans.InfoCurricularCourseScope" length="1">
				<logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year" value="<%= pageContext.findAttribute("currentYear").toString()%>">
					<% if (row.intValue() % 2 !=0) count=1; else count=0; %>
					<tr>
						<th colspan="11" scope="col">
							<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>&ordm;&nbsp;<bean:message key="label.year"/>
						</th>
					</tr>
					<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
					<tr>
						<td colspan="7" class="subheader">&nbsp;</td>
						<td colspan="4" class="subheader"><bean:message key="label.hoursPerWeek" /></td>
					</tr>
					<tr>						
						<td class="subheader"><bean:message key="label.semester.abbr"/></td>
						<td class="subheader"><bean:message key="label.curricularCourse"/></td>
						<td class="subheader"><bean:message key="label.type"/></td>
						<td class="subheader"><bean:message key="label.branch"/></td>			
						<td class="subheader"><bean:message key="label.credits"/></td>
						<td class="subheader"><bean:message key="label.ects"/></td>
						<td class="subheader"><bean:message key="label.weight"/></td>							
						<td class="subheader"><bean:message key="label.theoretical.abbr"/></td>
						<td class="subheader"><bean:message key="label.pratical.abbr"/></td>
						<td class="subheader"><bean:message key="label.theoPrat.abbr"/></td>
						<td class="subheader"><bean:message key="label.laboratorial.abbr"/></td>
					</tr>
				</logic:notEqual>
			</logic:iterate>
			<bean:size id="numberOfScopes" name="curricularCourseScopeElemList"/>
			<% String rowColor = (row.intValue()+count) % 2 == 0 ? "white" : "bluecell" ; %>						
			<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="DataBeans.InfoCurricularCourseScope" length="1">
				<tr>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/></td>
					<td class="<%= rowColor %>">
						<bean:define id="curricularCourseID" name="curricularCourseScopeElem" property="infoCurricularCourse.idInternal"/>
						<html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")%>" >
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name" />
						</html:link>
					</td>
					<td class="<%= rowColor %>"><bean:message name="curricularCourseScopeElem" property="infoCurricularCourse.type.keyName"/></td>
					<td class="<%= rowColor %>">					
						<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>&nbsp;
						<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="DataBeans.InfoCurricularCourseScope" offset="1">
							<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>&nbsp;				
						</logic:iterate>
					</td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.credits"/></td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.ectsCredits"/></td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.weigth"/></td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.theoreticalHours"/></td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.praticalHours"/></td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.theoPratHours"/></td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.labHours"/></td>					
				</tr>	
			</logic:iterate>
		</logic:iterate>
	</table>
</logic:notEmpty>
</logic:present>

<logic:notPresent name="allActiveCurricularCourseScopes">
	<p><span class="error"><bean:message key="error.impossibleCurricularPlan" /></span></p>
</logic:notPresent>
<logic:empty name="allActiveCurricularCourseScopes">
	<p><span class="error"><bean:message key="error.impossibleCurricularPlan" /></span></p>
</logic:empty>

</logic:present>
