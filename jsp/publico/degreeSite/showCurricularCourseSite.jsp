<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<span class="error"><html:errors/></span>

<logic:present name="infoCurriculum" >

<bean:define id="infoCurricularCourse" name="infoCurriculum" property="infoCurricularCourse" />
<bean:define id="infoDegree" name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree" />
<bean:define id="degreeCurricularPlanId" name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.idInternal" />

<div  class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a> > <a href="http://www.ist.utl.pt/html/ensino/ensino.shtml">Ensino</a> &gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOId=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeId=" + request.getAttribute("degreeId").toString() %>">
		<bean:write name="infoDegree" property="tipoCurso" />&nbsp<bean:write name="infoDegree" property="nome" />
	</html:link>&gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;executionPeriodOId=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeId=" +  request.getAttribute("degreeId") %>" >
	<bean:message key="label.curricularPlan"/>
	</html:link>&gt;&nbsp;
	<html:link page="<%= "/showDegreeCurricularPlan.do?method=showCurricularPlan&amp;degreeId=" + request.getAttribute("degreeId") + "&amp;degreeCurricularPlanId=" + pageContext.findAttribute("degreeCurricularPlanId").toString() + "&amp;executionPeriodOId=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >
	<bean:message key="label.curriculum"/>
	</html:link>&gt;&nbsp;
	<bean:write name="infoCurricularCourse" property="name" />
	
</div>	

<!-- PÁGINA EM INGLÊS -->
<div class="version"><span class="px10"><a href="http://www.ist.utl.pt/html/en/teaching.shtml">english version</a> <img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="Icon: English version!" width="16" height="12" /></span></div>
<div class="clear"></div> 

<br />
<br />
<div class="clear"></div> 
<h1><bean:write name="infoDegree" property="tipoCurso" />&nbsp<bean:write name="infoDegree" property="nome" /></h1>

<!-- ANO LECTIVO -->
<logic:notEmpty name="schoolYear">
  <h2><span class="redbox"><bean:write name="schoolYear" /></span>
	<span class="greytxt">&nbsp;<bean:write name="infoCurricularCourse" property="name" /></span></h2>
</logic:notEmpty>  
<br />


<!-- EXECUTION COURSES LINK  -->
<div class="col_right">
  <table class="box" cellspacing="0">
		<tr>
			<td class="box_header"><strong><bean:message key="label.courses" /></strong></td>
		</tr>
		<tr>
			<td class="box_cell">
				<ul>
					<logic:iterate id="infoExecutionCourse" name="infoCurricularCourse" property="infoAssociatedExecutionCourses">
						<bean:define id="executionCourseId" name="infoExecutionCourse" property="idInternal" />
						<p><html:link page="<%= "/showCourseSite.do?method=showExecutionCourseSite&amp;executionCourseId=" +  pageContext.findAttribute("executionCourseId")+ "&amp;executionPeriodOId=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeId=" +  request.getAttribute("degreeId") %>" >
						<bean:write name="infoExecutionCourse" property="nome" />
						</html:link>
						<br />
						</p>
					</logic:iterate>
				</ul>
			</td>
		</tr>  
  </table>
</div>

<!-- 	CURRICULAR COURSE SCOPES  -->
<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" /><bean:message key="label.scope" />	</h2>
<p>
	<ul>
		<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourse" property="infoScopes">
			<li>
				<logic:notEmpty name="infoCurricularCourseScope" property="infoBranch.name">
					<logic:notEqual name="infoCurricularCourseScope" property="infoBranch.name" value="">	
	 					<bean:message key="property.curricularCourse.branch"/>&nbsp;<bean:message key="label.the"/>&nbsp;<bean:write name="infoCurricularCourseScope" property="infoBranch.name"/>&nbsp;
	 				</logic:notEqual>
				</logic:notEmpty>
				
				<logic:notEmpty name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year">
					<logic:notEqual name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" value="">					
						<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>º&nbsp;<bean:message key="property.curricularCourse.curricularYear"/>&nbsp;
		 			</logic:notEqual>
				</logic:notEmpty>
				
				<logic:notEmpty name="infoCurricularCourseScope" property="infoCurricularSemester.semester">
					<logic:notEqual name="infoCurricularCourseScope" property="infoCurricularSemester.semester" value="">					
						<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>º&nbsp;<bean:message key="property.curricularCourse.semester"/>&nbsp;
	 				</logic:notEqual>
				</logic:notEmpty>
			</li>
		</logic:iterate>		
	</ul>
</p>

<!-- CURRICULAR COURSE INFO -->
<logic:notEmpty name="infoCurriculum" property="generalObjectives">
		<logic:notEqual name="infoCurriculum" property="generalObjectives" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" /><bean:message key="label.generalObjectives" />	</h2>
		<p>
			<bean:write name="infoCurriculum" property="generalObjectives" filter="false"/>
		</p>
		</logic:notEqual>
</logic:notEmpty>
<logic:notEmpty name="infoCurriculum" property="operacionalObjectives">
		<logic:notEqual name="infoCurriculum" property="operacionalObjectives" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" /><bean:message key="label.operacionalObjectives" /></h2>
		<p>
			<bean:write name="infoCurriculum" property="operacionalObjectives" filter="false"/>
		</p>
		</logic:notEqual>
</logic:notEmpty> 
<br/>
<br/>
<logic:notEmpty name="infoCurriculum" property="program">
	<logic:notEqual name="infoCurriculum" property="program" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" /><bean:message key="label.program" /></h2>	
		<p>
			<bean:write name="infoCurriculum" property="program" filter="false" />
		</p>	
	</logic:notEqual>
</logic:notEmpty>
<br/>
<br/>

</logic:present>