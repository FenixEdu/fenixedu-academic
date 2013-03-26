<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

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


			
