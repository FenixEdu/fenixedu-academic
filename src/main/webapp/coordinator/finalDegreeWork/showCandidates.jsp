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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>

<jsp:include page="/coordinator/context.jsp" />

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
<bean:define id="executionDegreeOID" name="executionDegree" property="externalId" type="String"/>
<h2>
	<bean:message key="title.finalDegreeWorkCandidates"/>
</h2>	

<h3>
	<bean:message key="message.final.degree.work.administration"/>
	<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
</h3>	


<p><html:link page="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfo&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID%>"><bean:message key="label.return"/></html:link></p>

<logic:present name="executionDegree" property="scheduling">
<logic:notEqual name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName" value="1">
	<div class="infoop2">
		<p>
			<strong><bean:message key="message.final.degree.work.other.execution.degrees"/></strong>
		</p>
			<logic:iterate id="currentExecutionDegree" name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName">
				<logic:notEqual name="currentExecutionDegree" property="externalId" value="<%= executionDegreeOID %>">
					<p class="mvvert05">
						<bean:write name="currentExecutionDegree" property="degreeCurricularPlan.presentationName"/>
					</p>
				</logic:notEqual>
			</logic:iterate>
	</div>
</logic:notEqual>
</logic:present>


 <logic:present name="summary">
 <p class="mbottom05"><bean:message key="label.visualization.options"/></p>
	<fr:view name="summary" schema="thesis.bean.candidacies.links">
		<fr:layout name="values">
			<fr:property name="classes" value="mvert1"/>
			<fr:property name="eachClasses" value="mvert025"/>
			<fr:property name="eachInline" value="false"/>
		</fr:layout>
	</fr:view>
</logic:present>
 
<jsp:include page="showCandidates_table.jsp"></jsp:include>

<p class="mtop2 mbottom05"><em><bean:message key="label.legend"/></em></p>
<ul class="list7 italic mtop05">
	<li>
		<span class="active">
			<bean:message key="CandidacyAttributionType.ATTRIBUTED_BY_CORDINATOR" bundle="ENUMERATION_RESOURCES"/>
		</span>
		- <bean:message key="CandidacyAttributionType.ATTRIBUTED_BY_CORDINATOR.description"/>
	</li>
	<li>
		<span class="active">
			<bean:message key="CandidacyAttributionType.ATTRIBUTED" bundle="ENUMERATION_RESOURCES"/>
		</span>
		- <bean:message key="CandidacyAttributionType.ATTRIBUTED.description"/>
	</li>
	<li>
		<span class="active">
			<bean:message key="CandidacyAttributionType.ATTRIBUTED_NOT_CONFIRMED" bundle="ENUMERATION_RESOURCES"/>
		</span>
		- <bean:message key="CandidacyAttributionType.ATTRIBUTED_NOT_CONFIRMED.description"/>
	</li>
	<li>
		<span class="active">
			<bean:message key="CandidacyAttributionType.NOT_ATTRIBUTED" bundle="ENUMERATION_RESOURCES"/>
		</span>
		- <bean:message key="CandidacyAttributionType.NOT_ATTRIBUTED.description"/>
	</li>
</ul>


			
