<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoCurricularCourseScope" %>
<%@ page import="Util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeCurricularPlan">

<div  class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a> > <a href="http://www.ist.utl.pt/html/ensino/ensino.shtml">Ensino</a> &gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />&nbsp<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" />
	</html:link>&gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") %>" >
	<bean:message key="label.curricularPlan"/>
	</html:link>&gt;&nbsp;
	<bean:message key="label.curriculum"/>	
	
</div>	

<br />
<br />
<div class="clear"></div> 
<h1><bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />&nbsp<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" /></h1>

<!-- ANO LECTIVO -->
<logic:notEmpty name="schoolYear">
  <h2><span class="redbox"><bean:write name="schoolYear" /></span>
	<span class="greytxt">&nbsp;<bean:message key="label.curricularPlan"/></span></h2>
</logic:notEmpty>  
<br />
  		
<logic:present name="allActiveCurricularCourseScopes">
	<logic:iterate id="curricularCourseScopeElem" name="allActiveCurricularCourseScopes" type="DataBeans.InfoCurricularCourseScope" length="1">
			<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
	</logic:iterate>
	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear"/></td>
			<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester"/></td>
			<td class="listClasses-header"><bean:message key="label.curricularCourse"/></td>
			<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch"/></td>
		</tr>
			<logic:iterate id="curricularCourseScopeElem" name="allActiveCurricularCourseScopes" type="DataBeans.InfoCurricularCourseScope">
				<logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.semester" value="<%= pageContext.findAttribute("currentSemester").toString()%>">
					<tr>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear"/></td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester"/></td>
						<td class="listClasses-header"><bean:message key="label.curricularCourse"/></td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch"/></td>
					</tr>
					<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
				</logic:notEqual>
				<tr>
					<td class="listClasses"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/></td>
					<td class="listClasses"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/></td>
					<td class="listClasses" style="text-align:left">
						<bean:define id="curricularCourseID" name="curricularCourseScopeElem" property="infoCurricularCourse.idInternal"/>
						<html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")%>" >
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name" />
						</html:link>
					</td>
					<td class="listClasses">
						<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>
					</td>
				</tr>
			</logic:iterate>
		</logic:iterate>
	</table>
</logic:present>

<logic:notPresent name="allActiveCurricularCourseScopes">
	<h1><p><bean:message key="error.impossibleDegreeSite" /></p></h1>
</logic:notPresent>

</logic:present>
