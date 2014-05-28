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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState" %>

<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %></bean:define>
<div class="breadcumbs mvert0">
	<a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
	<logic:present name="degree">
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>"><bean:write name="degree" property="sigla"/></html:link>
	</logic:present>
	&nbsp;&gt;&nbsp;
	<bean:message key="public.degree.information.label.curricularPlan"  bundle="PUBLIC_DEGREE_INFORMATION" />
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

<em><span class="error0"><!-- error messages --><html:errors/></span></em>

<logic:present name="infoDegreeCurricularPlan">

	<!-- CURRICULAR PLAN -->
	<h2 class="greytxt">
		<bean:message key="public.degree.information.label.curricularPlan"  bundle="PUBLIC_DEGREE_INFORMATION" />
		<bean:write name="infoDegreeCurricularPlan" property="name"/>
		<logic:notEmpty name="infoDegreeCurricularPlan" property="initialDate">
			<logic:empty name="infoDegreeCurricularPlan" property="endDate">
				(<bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.since" />
				<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />
				<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>)
			</logic:empty>
			<logic:notEmpty name="infoDegreeCurricularPlan" property="endDate">
				(<bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.of" />
				<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />
				<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
				<bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.to" />
				<bean:define id="endDate" name="infoDegreeCurricularPlan" property="endDate" />	
				<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>)
			</logic:notEmpty>
		</logic:notEmpty>	
	</h2>

	<div class="col_right">
		<table class="box" cellspacing="0">
			<tr>
				<td class="box_header">
					<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="externalId" />
					<bean:define id="degreeInitials" name="infoDegreeCurricularPlan" property="infoDegree.sigla"/>
					<bean:define id="nameDegreeCurricularPlan" name="infoDegreeCurricularPlan" property="name"/>
					<logic:equal name="infoDegreeCurricularPlan" property="degreeCurricularPlan.boxStructure" value="false">
						<html:link page="<%= "/prepareConsultCurricularPlanNew.do?method=prepare&amp;degreeID="
						+ request.getAttribute("degreeID")
						+ "&amp;degreeCurricularPlanID="
						+ pageContext.findAttribute("degreeCurricularPlanID").toString()
						+ "&amp;degreeInitials="
						+ pageContext.findAttribute("degreeInitials")
						+ "&amp;executionPeriodOID="
						+ request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" >
							<strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curriculum" /></strong>
						</html:link>
					</logic:equal>
					<logic:equal name="infoDegreeCurricularPlan" property="degreeCurricularPlan.boxStructure" value="true">
						<html:link page="<%= "/degreeSite/showDegreeCurricularPlanBolonha.faces?degreeID=" + request.getAttribute("degreeID")
						+ "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() 
						+ "&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)
						+ "&amp;organizeBy=groups&amp;showRules=false&amp;hideCourses=false"%>"  >
							<strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curriculum" /></strong>
						</html:link>
					</logic:equal>
				</td>
			</tr>		
			<tr>
				<td class="box_cell">
					<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.coursesText" />
				</td>
			</tr>

			<tr>
				<td class="box_header">
					<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="externalId" />
					<html:hidden alt="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" property="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" />	
					<html:link page="<%= "/chooseExamsMapContextDANew.do?degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;page=1&amp;method=prepare&amp;selectAllCurricularYears=on" %>">
						<strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.exams" /></strong>
					</html:link>
				</td>
			</tr>
			<tr>
				<td class="box_cell">
					<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.examsText" /> 
				</td>
			</tr>
			<tr>
				<td class="box_header">
					<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="externalId" />
					<html:link page="<%= "/chooseContextDANew.do?method=prepare&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;degreeID=" + request.getAttribute("degreeID")+ "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString()%>" >
						<strong><bean:message key="public.degree.information.label.classes"  bundle="PUBLIC_DEGREE_INFORMATION" /></strong>
					</html:link>
				</td>
			</tr>
			<tr>
				<td class="box_cell">
					<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.classesText" />
				</td>
			</tr>


			<logic:present name="infoDegreeCurricularPlanList">
				<logic:notEmpty name="infoDegreeCurricularPlanList">
					<bean:size id="listSize" name="infoDegreeCurricularPlanList" />
					<logic:greaterThan name="listSize" value="1">
						<logic:iterate id="infoDegreeCurricularPlanElem" name="infoDegreeCurricularPlanList" indexId="index"> 
							<logic:notEqual name="index" value="0">
								<logic:equal name="infoDegreeCurricularPlanElem" property="state" value="<%= DegreeCurricularPlanState.ACTIVE.toString() %>" >
									<bean:define id="plansActives" value="true" />											
								</logic:equal>
							</logic:notEqual>
						</logic:iterate>
			
						<logic:present name="plansActives">		
						<tr>
							<td class="box_header">
								<strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularPlans" /></strong>
							</td>
						</tr>
						<tr>
							<td class="box_cell">
							<ul>		
								<logic:iterate id="infoDegreeCurricularPlanElem" name="infoDegreeCurricularPlanList" indexId="index"> 
									<bean:define id="otherDegreeCurricularPlanID" name="infoDegreeCurricularPlanElem" property="externalId" />						
									<logic:equal name="infoDegreeCurricularPlanElem" property="state" value="<%= DegreeCurricularPlanState.ACTIVE.toString() %>" > <!-- If is active -->
										<li><html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("otherDegreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) %>" >
											<logic:notEmpty name="infoDegreeCurricularPlan" property="initialDate">
											<bean:define id="initialDate" name="infoDegreeCurricularPlanElem" property="initialDate" />		
											<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")) %>
											<logic:notEmpty name="infoDegreeCurricularPlanElem" property="endDate">
												<bean:define id="endDate" name="infoDegreeCurricularPlanElem" property="endDate" />	
												-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
											</logic:notEmpty>
											</logic:notEmpty>
										</html:link></li>
									</logic:equal>																				
								</logic:iterate>
							</ul>
							</td>
						</tr>
						</logic:present>
					</logic:greaterThan>	
				</logic:notEmpty>  
			</logic:present>		
		 </table> 
	</div>
		
	<h2 class="arrow_bullet">
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularPlanDescription" />
	</h2>
	<logic:empty name="infoDegreeCurricularPlan" property="description">
		<p><i><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="not.available" /></i></p>
	</logic:empty>
	<logic:notEmpty name="infoDegreeCurricularPlan" property="description">
		<p><bean:write name="infoDegreeCurricularPlan" property="description" filter="false" /></p>
	</logic:notEmpty>

</logic:present>

<p style="margin-top: 2em;">
	<span class="px10">
		<i>
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.responsability.information.degree" />
		</i>
	</span>
</p>
