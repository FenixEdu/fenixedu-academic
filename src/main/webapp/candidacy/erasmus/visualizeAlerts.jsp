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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusAlert" %>

<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>


<html:xhtml/>


<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.erasmus.visualize.alerts.title" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />

<p>
	<html:link action='<%= f("/caseHandlingMobilityIndividualApplicationProcess.do?method=listProcessAllowedActivities&amp;processId=%s", processId.toString()) %>'>
		« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p><span class="error0"><bean:write name="message" /></span></p>
</html:messages>


<fr:hasMessages for="individualCandidacyProcessBean.precedentDegreeInformation" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>



<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" ></script>


<logic:empty name="individualCandidacyProcessBean" property="individualCandidacyProcess.alert">
	<p class="mbottom05"><em><bean:message key="label.erasmus.visualize.alerts.empty" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:iterate name="individualCandidacyProcessBean" property="individualCandidacyProcess.alert" id="alert" type="ErasmusAlert">
	<logic:equal name="alert" property="toFire" value="true">
		<logic:present role="role(INTERNATIONAL_RELATION_OFFICE)" >
			<html:link action='<%= f("/caseHandlingMobilityIndividualApplicationProcess.do?method=markAlertAsViewed&amp;erasmusAlertId=%s&amp;processId=%s", alert.getExternalId(), processId.toString()) %>'>
				<bean:message key="label.eramus.alert.mark.as.viewed" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</html:link>
		</logic:present>
	</logic:equal>
	<fr:view name="alert" schema="ErasmusAlert.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
		</fr:layout>		
	</fr:view>
</logic:iterate>


