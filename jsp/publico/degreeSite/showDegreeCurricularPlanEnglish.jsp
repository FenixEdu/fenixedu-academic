<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoCurricularCourseScope" %>
<%@ page import="Util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeCurricularPlan">
<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
<div  class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a> > 
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
<!-- PÁGINA EM PORTUGUÊS -->
<div class="version">
	<span class="px10">
		<html:link page="<%= "/showDegreeCurricularPlan.do?method=showCurricularPlan&amp;&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >portuguese version</html:link> <img src="<%= request.getContextPath() %>/images/portugal-flag.gif" alt="Icon: Poruguese version!" width="16" height="12" />
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

	<logic:iterate id="curricularCourseScopeElem" name="allActiveCurricularCourseScopes" type="DataBeans.InfoCurricularCourseScope" length="1">
			<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
	</logic:iterate>
	<table class="tab_altrow" cellspacing="0">
		<!-- cabeçalho -->
		<tr>
			<th colspan="3"><bean:write name="currentYear"/>º&nbsp;<bean:message key="label.manager.curricularCourseScope.curricularYear.en"/></th>
		</tr>
		<tr>			
			<td class="subheader"><bean:message key="label.manager.curricularCourseScope.curricularSemester.en"/></td>
			<td class="subheader"><bean:message key="label.curricularCourse.en"/></td>
			<td class="subheader"><bean:message key="label.manager.curricularCourseScope.branch.en"/></td>
		</tr>
		<logic:iterate id="curricularCourseScopeElem" name="allActiveCurricularCourseScopes" type="DataBeans.InfoCurricularCourseScope" indexId="row">
				<logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year" value="<%= pageContext.findAttribute("currentYear").toString()%>">
					<!-- cabeçalho -->
					<tr>
						<th colspan="3"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>º&nbsp;<bean:message key="label.manager.curricularCourseScope.curricularYear.en"/></th>
					</tr>
					<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
					<tr>						
						<td class="subheader"><bean:message key="label.manager.curricularCourseScope.curricularSemester.en"/></td>
						<td class="subheader"><bean:message key="label.curricularCourse.en"/></td>
						<td class="subheader"><bean:message key="label.manager.curricularCourseScope.branch.en"/></td>
					</tr>
				</logic:notEqual>
				
				<bean:define id="isEven">
					<%= String.valueOf(row.intValue() % 2) %>
				</bean:define>
				<logic:equal name="isEven" value="0"> <!-- Linhas pares com uma cor -->
				<tr>
					<td class="white"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/></td>
					<td class="white" style="text-align:left">
						<bean:define id="curricularCourseID" name="curricularCourseScopeElem" property="infoCurricularCourse.idInternal"/>
						<html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;inEnglish=true&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")%>" >
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name" />
						</html:link>
					</td>
					<td class="white">
						<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>
					</td>
				</tr>
				</logic:equal>
				
				<logic:equal name="isEven" value="1"> <!-- Linhas impares com uma cor -->
				<tr>
					<td><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/></td>
					<td style="text-align:left">
						<bean:define id="curricularCourseID" name="curricularCourseScopeElem" property="infoCurricularCourse.idInternal"/>
						<html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")%>" >
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name" />
						</html:link>
					</td>
					<td>
						<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>
					</td>
				</tr>
				</logic:equal>				
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
