<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoCurriculum" >

<bean:define id="infoCurricularCourse" name="infoCurriculum" property="infoCurricularCourse" />
<bean:define id="infoDegree" name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree" />
<bean:define id="degreeCurricularPlanID" name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.idInternal" />

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> 
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="label.education" bundle="PUBLIC_DEGREE_INFORMATION" /></a>
	<bean:define id="degreeType" name="infoDegree" property="tipoCurso" />	
	&nbsp;&gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString()  %>">
	<!-- &amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "-->
		<bean:write name="infoDegree" property="sigla" />
	</html:link>
	&nbsp;&gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() %>" >
	<!-- &amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + " -->
		<bean:message key="label.curricularPlan"  bundle="PUBLIC_DEGREE_INFORMATION" />
	</html:link>
	&nbsp;&gt;&nbsp;
	<html:link page="<%= "/showDegreeCurricularPlanNew.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="label.curriculum"/>
	</html:link>
	&nbsp;&gt;&nbsp;<bean:write name="infoCurricularCourse" property="name" />	
</div>	

<h1>
	<bean:write name="infoDegree" property="tipoCurso" />
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="label.in" />
	<bean:write name="infoDegree" property="nome" />
</h1>

<h2><span class="greytxt"><bean:write name="infoCurricularCourse" property="name" /></span></h2>

<!-- EXECUTION COURSES LINK  -->
<logic:present name="infoCurricularCourse" property="infoAssociatedExecutionCourses" />
<bean:size id="listSize" name="infoCurricularCourse" property="infoAssociatedExecutionCourses" />

<logic:greaterThan name="listSize" value="0">
<div class="col_right">
  <table class="box" cellspacing="0">
		<tr>
			<td class="box_header"><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="label.courses" /></strong></td>
		</tr>
		<tr>
			<td class="box_cell">
				<logic:iterate id="infoExecutionCourse" name="infoCurricularCourse" property="infoAssociatedExecutionCourses">
					<logic:notEqual name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.state.stateCode" value="NO">
						<bean:define id="executionCourseID" name="infoExecutionCourse" property="idInternal" />
						<p>
						<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&amp;objectCode=" + pageContext.findAttribute("executionCourseID").toString() + "&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)  %>"  >
							<bean:write name="infoExecutionCourse" property="nome" />&nbsp;(<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/>&nbsp;-&nbsp;<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>)
						</html:link>
						</p>
					</logic:notEqual>
				</logic:iterate>
			</td>
		</tr>  
  </table>
</div>
</logic:greaterThan>
</logic:present>

<!-- 	CURRICULAR COURSE SCOPES  -->
<logic:present name="infoCurricularCourse" property="infoScopes">
	<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="label.scope" />	</h2>
	<ul>
		<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourse" property="infoScopes">
		<li>
			<logic:notEmpty name="infoCurricularCourseScope" property="infoBranch.name">
				<logic:notEqual name="infoCurricularCourseScope" property="infoBranch.name" value="">	
					<bean:write name="infoCurricularCourseScope" property="infoBranch.name"/>&nbsp;
	 			</logic:notEqual>
			</logic:notEmpty>
				
			<logic:notEmpty name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year">
				<logic:notEqual name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" value="">					
					<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>º&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="label.year"/>&nbsp;
					</logic:notEqual>
			</logic:notEmpty>
				
			<logic:notEmpty name="infoCurricularCourseScope" property="infoCurricularSemester.semester">
				<logic:notEqual name="infoCurricularCourseScope" property="infoCurricularSemester.semester" value="">					
					<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>º&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="label.semester"/>&nbsp;
	 			</logic:notEqual>
			</logic:notEmpty>
		</li>
		</logic:iterate>		
	</ul>
</logic:present>

<!-- CURRICULAR COURSE INFO -->
<logic:present name="infoCurriculum" property="generalObjectives">
<logic:notEmpty name="infoCurriculum" property="generalObjectives">
	<logic:notEqual name="infoCurriculum" property="generalObjectives" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="label.generalObjectives" />	</h2>
		<p><bean:write name="infoCurriculum" property="generalObjectives" filter="false"/></p>
	</logic:notEqual>
</logic:notEmpty>
</logic:present>

<logic:present name="infoCurriculum" property="operacionalObjectives">
<logic:notEmpty name="infoCurriculum" property="operacionalObjectives">
	<logic:notEqual name="infoCurriculum" property="operacionalObjectives" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="label.operacionalObjectives" /></h2>
		<p><bean:write name="infoCurriculum" property="operacionalObjectives" filter="false"/></p>
	</logic:notEqual>
</logic:notEmpty> 
</logic:present>

<logic:present name="infoCurriculum" property="program">
<logic:notEmpty name="infoCurriculum" property="program">
	<logic:notEqual name="infoCurriculum" property="program" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="label.program" /></h2>	
		<p><bean:write name="infoCurriculum" property="program" filter="false" /></p>	
	</logic:notEqual>
</logic:notEmpty>
</logic:present>


</logic:present>

<logic:notPresent name="infoCurriculum" >
	<p><span class="error"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="error.impossibleCurricularCourseInfo" /></span></p>
</logic:notPresent>