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
		<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />	
		<logic:equal name="degreeType" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
			 <html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino Mestrados</html:link>
		</logic:equal>
		<logic:equal name="degreeType" value="<%= TipoCurso.LICENCIATURA_OBJ.toString() %>">
			<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino Licenciaturas</html:link>		
		</logic:equal>
		&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;inEnglish=true&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString()  + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID")%>">
			<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
		</html:link>&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;inEnglish=true&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >
			<bean:message key="label.curricularPlan.en"/>
		</html:link>&gt;&nbsp;
		<bean:message key="label.curriculum.en"/>	
</div>	
<!-- P�GINA EM PORTUGU�S -->
<div class="version">
	<span class="px10">
		<html:link page="<%= "/showDegreeCurricularPlan.do?method=showCurricularPlan&amp;&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >
			<bean:message key="label.version.portuguese" />
		</html:link>
		<img src="<%= request.getContextPath() %>/images/portugal-flag.gif" alt="Icon: Poruguese version!" width="16" height="12" />
	</span>	
</div>
<div class="clear"></div> 
<h1><bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />&nbsp;<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" /></h1>

<h2>
<span class="greytxt">
	<bean:message key="label.curricularPlan.en"/>
	of
	<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />		
	<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
	<logic:notEmpty name="infoDegreeCurricularPlan" property="endDate">
		<bean:define id="endDate" name="infoDegreeCurricularPlan" property="endDate" />	
		-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
	</logic:notEmpty>
</span>
</h2>
<br />
		
<logic:present name="allActiveCurricularCourseScopes">
<logic:notEmpty name="allActiveCurricularCourseScopes">

	<logic:iterate id="curricularCourseScopeElemList" name="allActiveCurricularCourseScopes" length="1" >
		<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="DataBeans.InfoCurricularCourseScope" length="1">
			<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
		</logic:iterate>
	</logic:iterate>
	<table class="tab_altrow" cellspacing="0" cellpadding="5">
		<!-- cabe�alho -->
		<tr>
			<th colspan="4"><bean:write name="currentYear"/>�&nbsp;<bean:message key="label.curricular.year.en"/></th>
		</tr>
		<tr>						
			<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.semester.en"/></td>
			<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.curricularCourse.en"/></td>
			<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.curricularCourseType.en"/></td>
			<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.branch.en"/></td>			
			<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.DCPsite.curricular.course.credits.en"/></td>
			<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.DCPsite.curricular.course.ectsCredits.en"/></td>
			<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.DCPsite.curricular.course.weight.en"/></td>
			<td colspan="4" class="subheader" style="text-align:center"><bean:message key="label.DCPsite.hours.en" /></td>					
		</tr>
		<tr>
			<td class="subheader" style="text-align:center"><bean:message key="label.DCPsite.theoreticalHours.en"/></td>
			<td class="subheader" style="text-align:center"><bean:message key="label.DCPsite.praticalHours.en"/></td>
			<td class="subheader" style="text-align:center"><bean:message key="label.DCPsite.theoPratHours.en"/></td>
			<td class="subheader" style="text-align:center"><bean:message key="label.DCPsite.labHours.en"/></td>
		</tr>
		<logic:iterate id="curricularCourseScopeElemList" name="allActiveCurricularCourseScopes"  indexId="row">
			<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="DataBeans.InfoCurricularCourseScope" length="1" >
				<logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year" value="<%= pageContext.findAttribute("currentYear").toString()%>">
					<!-- cabe�alho -->
					<tr>
						<th colspan="4"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>�&nbsp;<bean:message key="label.curricular.year.en"/></th>
					</tr>
					<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
					<tr>						
						<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.semester.en"/></td>
						<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.curricularCourse.en"/></td>
						<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.curricularCourseType.en"/></td>
						<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.branch.en"/></td>			
						<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.DCPsite.curricular.course.credits.en"/></td>
						<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.DCPsite.curricular.course.ectsCredits.en"/></td>
						<td rowspan="2" class="subheader" style="text-align:center"><bean:message key="label.DCPsite.curricular.course.weight.en"/></td>
						<td colspan="4" class="subheader" style="text-align:center"><bean:message key="label.DCPsite.hours.en" /></td>					
					</tr>
					<tr>
						<td class="subheader" style="text-align:center"><bean:message key="label.DCPsite.theoreticalHours.en"/></td>
						<td class="subheader" style="text-align:center"><bean:message key="label.DCPsite.praticalHours.en"/></td>
						<td class="subheader" style="text-align:center"><bean:message key="label.DCPsite.theoPratHours.en"/></td>
						<td class="subheader" style="text-align:center"><bean:message key="label.DCPsite.labHours.en"/></td>
					</tr>
				</logic:notEqual>
			</logic:iterate>
			<bean:size id="numberOfScopes" name="curricularCourseScopeElemList"/>
			<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="DataBeans.InfoCurricularCourseScope" length="1">
				<bean:define id="keyNameCCType" name="curricularCourseScopeElem" property="infoCurricularCourse.type.keyName" />
				<bean:define id="keyNameCCTypeEnglish" value="<%= keyNameCCType.toString().concat(".en") %>"/>
				<tr>
					<td class="white"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/></td>
					<td class="white" style="text-align:left">
						<bean:define id="curricularCourseID" name="curricularCourseScopeElem" property="infoCurricularCourse.idInternal"/>
						
							<html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;inEnglish=true&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")%>" >
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name" />
							</html:link>
						
						
					</td>
					<td class="white"><bean:message name="keyNameCCTypeEnglish"/></td>
					<td class="white">
					
						<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>&nbsp;
					
				</logic:iterate>		
				
				<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="DataBeans.InfoCurricularCourseScope" offset="1">			
						<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>&nbsp;					
				</logic:iterate>
				</td>
				<td class="white" style="text-align:center"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.credits"/></td>
				<td class="white" style="text-align:center"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.ectsCredits"/></td>
				<td class="white" style="text-align:center"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.weigth"/></td>
				<td class="white" style="text-align:center"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.theoreticalHours"/></td>
				<td class="white" style="text-align:center"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.praticalHours"/></td>
				<td class="white" style="text-align:center"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.theoPratHours"/></td>
				<td class="white" style="text-align:center"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.labHours"/></td>									
			</tr>					
			</logic:iterate>
		</logic:iterate>
	</table>
</logic:notEmpty>
</logic:present>

<logic:notPresent name="allActiveCurricularCourseScopes">
	<p><span class="error"><bean:message key="error.impossibleCurricularPlan.en" /></span></p>
</logic:notPresent>
<logic:empty name="allActiveCurricularCourseScopes">
	<p><span class="error"><bean:message key="error.impossibleCurricularPlan.en" /></span></p>
</logic:empty>

</logic:present>
