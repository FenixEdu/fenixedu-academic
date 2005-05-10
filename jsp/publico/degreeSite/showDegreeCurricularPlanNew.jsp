<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeCurricularPlan">
	<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a>
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="public.degree.information.label.education" bundle="PUBLIC_DEGREE_INFORMATION" /></a>
		<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />	
		&nbsp;&gt;&nbsp;		
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
			<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
		</html:link>
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString()%>" >
<%-- &amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") --%>
		<bean:message key="public.degree.information.label.curricularPlan"  bundle="PUBLIC_DEGREE_INFORMATION" />
	</html:link>
	&nbsp;&gt;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.curriculum"/>		
</div>	

<style>@import "<%= request.getContextPath() %>/CSS/table-adjustment.css";</style>

	<!-- COURSE NAME -->
	<h1>
	   <bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso.name"/>
	
	    <logic:equal name="degreeType" value="DEGREE" >
		    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.degreeType" />
		</logic:equal>    
		<logic:equal name="degreeType" value="MASTER_DEGREE" >
		    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.masterDegreeType" />
		</logic:equal>   
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in" />
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" />
	</h1>

	<!-- CURRICULAR PLAN -->
	<h2 class="greytxt">
		<bean:message key="public.degree.information.label.curricularPlan"  bundle="PUBLIC_DEGREE_INFORMATION" />
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.of" />
		<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />		
		<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
		<logic:notEmpty name="infoDegreeCurricularPlan" property="endDate">
			<bean:define id="endDate" name="infoDegreeCurricularPlan" property="endDate" />	
			-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
		</logic:notEmpty>
	</h2>

	<!-- EXECUTIVE AND CURRICULAR YEAR SELECTION -->
	<html:form action="/prepareConsultCurricularPlanNew.do" method="GET" >
		<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
		<html:hidden property="degreeID" value="<%= ""+request.getAttribute("degreeID")%>" />
		<html:hidden property="degreeCurricularPlanID" value="<%= pageContext.findAttribute("degreeCurricularPlanID").toString() %>" />	
		<html:hidden property="index" value="<%= ""+ request.getAttribute("index")%>"/>
		<html:hidden property="page" value="1"/>
		<html:hidden property="method" value="select"/>
		<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE%>" value="<%= "" + request.getAttribute(SessionConstants.EXECUTION_DEGREE)%>"/>		
		
		<table border="0" cellspacing="2" cellpadding="0">			
			<tr>
				<td><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.executionYear"/>:</td>
				<td>
					<html:select property="indice" size="1" onchange="this.form.submit();">
						<html:options property="value" labelProperty="label" collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>"/>
						<bean:define id="indiceID" name="chooseContextDegreeForm" property="indice"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularYear"/>:</td>
				<td>
					<html:select property="curYear" size="1" onchange="this.form.submit();">
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
		<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1">
			<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
		</logic:iterate>
		<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1">
			<bean:define id="currentCode" name="curricularCourseScopeElem" property="infoBranch.code"/>
		</logic:iterate>
	</logic:iterate>

	<table class="tab_lay" cellspacing="0" cellpadding="5">
		<tr>
			<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>
				<th colspan="11" scope="col">
				<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">
					<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>&nbsp;<bean:write name="currentYear"/>
				</logic:equal>
				<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
					<bean:write name="currentYear"/>&ordm;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>
				</logic:equal>
				</th>
			<%}else{ %>
				<th colspan="10" scope="col">
				<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">
					<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>&nbsp;<bean:write name="currentYear"/>
				</logic:equal>
				<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
					<bean:write name="currentYear"/>&ordm;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>
				</logic:equal>
				</th>
			<%}%>
		</tr>
		<tr>
			<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>		
				<th colspan="11" scope="col">
					 <center><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
				</th>
			<%}else{ %>
				<th colspan="10" scope="col">
					 <center><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
				</th>
			<%}%>
		</tr>
		<tr>						
			<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>				
				<td colspan="7" class="subheader">&nbsp;</td>
				<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
			<%}else{ %>
				<td colspan="6" class="subheader">&nbsp;</td>
				<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
			<%}%>
		</tr>	
		</tr>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularCourse"/></td>
			<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>
				<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.anotation"/></td>
			<%}%>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.type"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.credits"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.ects"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.weight"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoretical.abbr"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.pratical.abbr"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoPrat.abbr"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.laboratorial.abbr"/></td>
		</tr>
		
		<% int count=0; %>
		<logic:iterate id="curricularCourseScopeElemList" name="allActiveCurricularCourseScopes" indexId="row">
			<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1">
				<logic:notEqual name="curricularCourseScopeElem" property="infoBranch.code" value="<%= pageContext.findAttribute("currentCode").toString()%>">
				  <logic:equal name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year" value="<%= pageContext.findAttribute("currentYear").toString()%>">					 
						<tr>
							<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>		
								<th colspan="11" scope="col">
									 <center><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
								</th>
							<%}else{ %>
								<th colspan="10" scope="col">
									 <center><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
								</th>
							<%}%>
						</tr>
						<bean:define id="currentCode" name="curricularCourseScopeElem" property="infoBranch.code"/>
						<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
						<tr>
							<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>				
								<td colspan="7" class="subheader">&nbsp;</td>
								<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
							<%}else{ %>
								<td colspan="6" class="subheader">&nbsp;</td>
								<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
							<%}%>
						</tr>
						
						<tr>						
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularCourse"/></td>
							<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>
								<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.anotation"/></td>
							<%}%>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.type"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.credits"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.ects"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.weight"/></td>							
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoretical.abbr"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.pratical.abbr"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoPrat.abbr"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.laboratorial.abbr"/></td>
						</tr>
					</logic:equal>
					</logic:notEqual>
			        <logic:equal name="curricularCourseScopeElem" property="infoBranch.code" value="<%= pageContext.findAttribute("currentCode").toString()%>">
					<logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year" value="<%= pageContext.findAttribute("currentYear").toString()%>">
					</table>
					<br/>
					 <table class="tab_lay" cellspacing="0" cellpadding="5">
					 
					<% if (row.intValue() % 2 !=0) count=1; else count=0; %>
	
					<tr>
					<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>		
							<th colspan="11" scope="col">
							<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">
								<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>&nbsp;<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
							</logic:equal>
							<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
								<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>&ordm;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>
							</logic:equal>
							</th>
					<%}else{ %>
							<th colspan="10" scope="col">
							<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">
								<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>&nbsp;<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
							</logic:equal>
							<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
								<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>&ordm;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>
							</logic:equal>
							</th>
					<%}%>
					</tr>
					<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
					<bean:define id="currentCode" name="curricularCourseScopeElem" property="infoBranch.code"/>
					<tr>
						<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>		
							<th colspan="11" scope="col">
								 <center><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
							</th>
						<%}else{ %>
							<th colspan="10" scope="col">
								 <center><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
							</th>
						<%}%>
						</tr>
					<tr>
						<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>				
							<td colspan="7" class="subheader">&nbsp;</td>
							<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
						<%}else{ %>
							<td colspan="6" class="subheader">&nbsp;</td>
							<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
						<%}%>
					</tr>
					
					<tr>						
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularCourse"/></td>
						<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.anotation"/></td>
						<%}%>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.type"/></td>
<!--						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.branch"/></td>	-->
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.credits"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.ects"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.weight"/></td>							
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoretical.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.pratical.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoPrat.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.laboratorial.abbr"/></td>
					</tr>
					</logic:notEqual>
				</logic:equal>
				<logic:notEqual name="curricularCourseScopeElem" property="infoBranch.code" value="<%= pageContext.findAttribute("currentCode").toString()%>">
					 <logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year" value="<%= pageContext.findAttribute("currentYear").toString()%>">
					
					</table>
					<p/>
					 <table class="tab_lay" cellspacing="0" cellpadding="5">
					 <% if (row.intValue() % 2 !=0) count=1; else count=0; %>

					<tr>
					<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>		
							<th colspan="11" scope="col">
								<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">
								<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>&nbsp;<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
							</logic:equal>
							<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
								<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>&ordm;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>
							</logic:equal>
							</th>
					<%}else{ %>
							<th colspan="10" scope="col">
								<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">
								<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>&nbsp;<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
							</logic:equal>
							<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
								<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>&ordm;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>
							</logic:equal>
							</th>
					<%}%>
					</tr>
					<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
					<bean:define id="currentCode" name="curricularCourseScopeElem" property="infoBranch.code"/>
					<tr>
							<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>		
								<th colspan="11" scope="col">
									 <center><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
								</th>
							<%}else{ %>
								<th colspan="10" scope="col">
									 <center><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
								</th>
							<%}%>
						</tr>
					<tr>
						<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>				
							<td colspan="7" class="subheader">&nbsp;</td>
							<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
						<%}else{ %>
							<td colspan="6" class="subheader">&nbsp;</td>
							<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
						<%}%>
					</tr>
					
					<tr>						
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularCourse"/></td>
						<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.anotation"/></td>
						<%}%>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.type"/></td>
<!--						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.branch"/></td>	-->
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.credits"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.ects"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.weight"/></td>							
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoretical.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.pratical.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoPrat.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.laboratorial.abbr"/></td>
					</tr>
				</logic:notEqual>
				</logic:notEqual>
			</logic:iterate>
			
			<bean:size id="numberOfScopes" name="curricularCourseScopeElemList"/>
			<% String rowColor = (row.intValue()+count) % 2 == 0 ? "white" : "bluecell" ; %>						
			<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1">
				<tr>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/></td>
					<td class="<%= rowColor %>">
						<bean:define id="curricularCourseID" name="curricularCourseScopeElem" property="infoCurricularCourse.idInternal"/>
						<html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")%>" >
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name" />
						</html:link>
					</td>
					<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>
						<logic:notEmpty name="curricularCourseScopeElem" property="anotation" >
							<bean:define id="anotationID" value="0"/>
							<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="anotation"/></td>
						</logic:notEmpty>
						<logic:empty name="curricularCourseScopeElem" property="anotation" >
							<td class="<%= rowColor %>"></td>
						</logic:empty>
					<%}%>
					<td class="<%= rowColor %>"><bean:message name="curricularCourseScopeElem" property="infoCurricularCourse.type.keyName"/></td>
					<!--<td class="<%= rowColor %>">					
						<bean:write name="curricularCourseScopeElem" property="infoBranch.name"/>&nbsp;
						<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" offset="1">
							<bean:write name="curricularCourseScopeElem" property="infoBranch.name"/>&nbsp;				
						</logic:iterate>
					</td>-->
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
	<logic:equal name="anotationID" value="0">

    <% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>	
		<logic:equal name="anotationID" value="0">
			<pre><bean:write name="infoDegreeCurricularPlan" property="anotation" /> </pre>
		</logic:equal>
	<%}%>
</logic:equal>

</logic:notEmpty>
</logic:present>

<logic:notPresent name="allActiveCurricularCourseScopes">
	<p><span class="error"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.error.impossibleCurricularPlan" /></span></p>
</logic:notPresent>
<logic:empty name="allActiveCurricularCourseScopes">
	<p><span class="error"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.error.impossibleCurricularPlan" /></span></p>
</logic:empty>
	
</logic:present>