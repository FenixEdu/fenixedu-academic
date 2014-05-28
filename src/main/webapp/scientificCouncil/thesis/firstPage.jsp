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
<%@page import="net.sourceforge.fenixedu.domain.thesis.Thesis"%>
<%@page import="java.util.Set"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis.ManageSecondCycleThesisSearchBean.Counter"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis.ManageSecondCycleThesisSearchBean.ThesisPresentationStateCountMap"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis.ManageSecondCycleThesisSearchBean"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<jsp:include page="styles.jsp"/>

<h2><bean:message key="title.scientificCouncil.thesis" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>


<div class="cf"> 
	<div class="grey-box first-box">
		<jsp:include page="filterSearchForm.jsp"/>
	</div>
	<div class="grey-box">
		<jsp:include page="searchPersonForm.jsp"/>
	</div>

	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>

	<%
		final ManageSecondCycleThesisSearchBean bean = (ManageSecondCycleThesisSearchBean) request.getAttribute("manageSecondCycleThesisSearchBean");
		final ThesisPresentationStateCountMap map = bean.getThesisPresentationStateCountMap();
		for (final Entry<ThesisPresentationState, Counter> entry : map.entrySet()) {
		    final ThesisPresentationState thesisPresentationState = entry.getKey();
		    final Counter counter = entry.getValue();
		    final String style = thesisPresentationState == bean.getPresentationState() ? "font-weight: bold; font-size: 1.0em;" : "color: graytext;";
	%>
			<span style="<%= style %>">
				<%= thesisPresentationState.getLabel() %>
			</span>
			 :
			 <%= counter.getCount() %>
			<br/>
	<%
		}
	%>
	<br/>
	<table class="tstyle4 thlight mtop05" style="margin-left: 35px; width: 90%;">
		<tr>
			<th>
				<bean:message key="label.student.number.short" bundle="APPLICATION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="student" bundle="APPLICATION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.degree" bundle="APPLICATION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.thesis.state" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.scientificCouncil.title" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
			</th>
		</tr>
		<logic:iterate id="enrolment" name="enrolments" type="net.sourceforge.fenixedu.domain.Enrolment">
			<%
				final Set<Thesis> theses = enrolment.getThesesSet();
				if (theses.isEmpty()) {
			%>
					<tr>
						<td>
							<%= enrolment.getStudent().getNumber() %>
						</td>
						<td>
							<html:link action="<%= "/manageSecondCycleThesis.do?method=showPersonThesisDetails&amp;personOid=" + enrolment.getStudent().getPerson().getExternalId() %>">
								<%= enrolment.getStudent().getPerson().getName() %>
							</html:link>
						</td>
							<td>
								<%= enrolment.getDegreeCurricularPlanOfDegreeModule().getDegree().getSigla() %>
							</td>
						<td>
							<%= ThesisPresentationState.getThesisPresentationState(enrolment).getLabel() %>
						</td>
						<td>
						</td>
					</tr>
			<%
				} else {
				    for (final Thesis thesis : theses) {
			%>
						<tr>
							<td>
								<%= enrolment.getStudent().getNumber() %>
							</td>
							<td>
								<html:link action="<%= "/manageSecondCycleThesis.do?method=showPersonThesisDetails&amp;personOid=" + enrolment.getStudent().getPerson().getExternalId() %>">
									<%= enrolment.getStudent().getPerson().getName() %>
								</html:link>
							</td>
							<td>
								<%= enrolment.getDegreeCurricularPlanOfDegreeModule().getDegree().getSigla() %>
							</td>
							<td>
								<%= ThesisPresentationState.getThesisPresentationState(enrolment).getLabel() %>
							</td>
							<td>
								<html:link action="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>">
									<%= thesis.getTitle() == null ? "" : thesis.getTitle().getContent() %>
								</html:link>
							</td>
						</tr>
			<%
				    }
				}
			%>
		</logic:iterate>
	</table>
</div> 

