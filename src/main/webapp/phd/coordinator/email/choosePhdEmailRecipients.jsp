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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>


<html:xhtml/>


<%@page import="net.sourceforge.fenixedu.util.BundleUtil"%>
<%@page import="pt.ist.fenixframework.DomainObject"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess" %>

<logic:present role="role(COORDINATOR)">


<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manage.emails" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdIndividualProgramProcess.do?method=managePhdEmails">
	« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<p><jsp:include page="createEmailStepsBreadcrumb.jsp?step=1"></jsp:include></p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" >
</script>


<fr:form action="/phdIndividualProgramProcess.do?method=prepareSendPhdEmail">

<p><html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /></html:submit></p>

<fr:edit id="phdEmailBean" name="phdEmailBean" visible="false" />

<h3 class="separator2 mtop15"><bean:message key="label.phd.candidacy" bundle="PHD_RESOURCES"/></h3>



<logic:iterate id="containerEnum" name="candidacyCategory" indexId="i">
	<div id='<%= "checkbox-1-" + i %>' class="hide-theader">
	
	<phd:filterProcessesForEmail id="processList" predicateContainer="containerEnum" bean="phdEmailBean"/>
	<bean:size id="size" name="processList" />		
	<p class="mbottom05">
		<strong><%= BundleUtil.getEnumName((Enum<?>) containerEnum, "Phd") %> (<%= size %>)</strong>
		
		<logic:notEqual name="size" value="0">
			 |	
			<html:link href="#" onclick="<%= "$('div#checkbox-1-" + i + " input[type=checkbox]').attr('checked','true')" %>">
				Seleccionar todos
			</html:link>
		</logic:notEqual>
	</p>
	
	<logic:equal name="size" value="0">
		<p class="mbottom2"><em><bean:message key="message.no.processes.in.state" bundle="PHD_RESOURCES"/></em></p>
	</logic:equal>
	
	<logic:notEqual name="size" value="0">
	
		<fr:view name="processList">
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdIndividualProgramProcess.class.getName() %>">
				<fr:slot name="phdIndividualProcessNumber.number"/>
				<fr:slot name="person.name" />
				<fr:slot name="executionYear.year"/>
				<fr:slot name="phdProgram.acronym" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft"/>
				<fr:property name="checkable" value="true"/>
				<fr:property name="checkboxName" value="selectedProcesses"/>
				<fr:property name="checkboxValue" value="externalId"/>
			</fr:layout>
		</fr:view>
	
	</logic:notEqual>
	
	</div>
</logic:iterate>



<h3 class="separator2"><bean:message key="label.phd.publicPresentationSeminar" bundle="PHD_RESOURCES"/></h3>

<logic:iterate id="containerEnum" name="seminarCategory" indexId="i">
	<div id='<%= "checkbox-2-" + i %>' class="hide-theader">
	
	<phd:filterProcessesForEmail id="processList" predicateContainer="containerEnum" bean="phdEmailBean" />
	<bean:size id="size" name="processList" />
	<p class="mbottom05">
		<strong><%= BundleUtil.getEnumName((Enum<?>) containerEnum, "Phd") %> (<%= size %>)</strong>
		
		<logic:notEqual name="size" value="0">
			 |	
			<html:link href="#" onclick="<%= "$('div#checkbox-2-" + i + " input[type=checkbox]').attr('checked','true')" %>">
				Seleccionar todos
			</html:link>
		</logic:notEqual>
	</p>
	
	<logic:equal name="size" value="0">
		<p class="mbottom2"><em><bean:message key="message.no.processes.in.state" bundle="PHD_RESOURCES"/></em></p>
	</logic:equal>
	
	<logic:notEqual name="size" value="0">
		
		<fr:view name="processList">
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdIndividualProgramProcess.class.getName() %>">
				<fr:slot name="phdIndividualProcessNumber.number"/>
				<fr:slot name="person.name" />
				<fr:slot name="executionYear.year"/>
				<fr:slot name="phdProgram.acronym" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft"/>
				<fr:property name="checkable" value="true"/>
				<fr:property name="checkboxName" value="selectedProcesses"/>
				<fr:property name="checkboxValue" value="externalId"/>
			</fr:layout>
			
		</fr:view>
		
	</logic:notEqual>
	
		</div>
</logic:iterate>

<h3 class="separator2"><bean:message key="label.phd.thesis" bundle="PHD_RESOURCES"/></h3>

<logic:iterate id="containerEnum" name="thesisCategory" indexId="i">
	<div id='<%= "checkbox-3-" + i %>' class="hide-theader">
	
	<phd:filterProcessesForEmail id="processList" predicateContainer="containerEnum" bean="phdEmailBean" />
	<bean:size id="size" name="processList" />
	<p class="mbottom05">
		<strong><%= BundleUtil.getEnumName((Enum<?>) containerEnum, "Phd") %> (<%= size %>)</strong>
		
		<logic:notEqual name="size" value="0">
			 |	
			<html:link href="#" onclick="<%= "$('div#checkbox-3-" + i + " input[type=checkbox]').attr('checked','true')" %>">
				Seleccionar todos
			</html:link>
		</logic:notEqual>
	</p>
	
	<logic:equal name="size" value="0">
		<p class="mbottom2"><em><bean:message key="message.no.processes.in.state" bundle="PHD_RESOURCES"/></em></p>
	</logic:equal>
	
	<logic:notEqual name="size" value="0">
	
		<fr:view name="processList">
		
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdIndividualProgramProcess.class.getName() %>">
				<fr:slot name="phdIndividualProcessNumber.number"/>
				<fr:slot name="person.name" />
				<fr:slot name="executionYear.year"/>
				<fr:slot name="phdProgram.acronym" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft"/>
				<fr:property name="checkable" value="true"/>
				<fr:property name="checkboxName" value="selectedProcesses"/>
				<fr:property name="checkboxValue" value="externalId"/>
			</fr:layout>
			
		</fr:view>
		
	</logic:notEqual>
	
	</div>
</logic:iterate>

<h3 class="separator2"><bean:message key="label.phd.concluded" bundle="PHD_RESOURCES"/></h3>

<bean:define id="concludedThisYearContainer" name="concludedThisYearContainer"/>
	<div id="checkbox-4" class="hide-theader">
	
	<phd:filterProcessesForEmail id="processList" predicateContainer="concludedThisYearContainer" bean="phdEmailBean" />
	<bean:size id="size" name="processList" />
	
	<p class="mbottom05">
		<strong><%= BundleUtil.getEnumName((Enum<?>) concludedThisYearContainer, "Phd") %> (<%= size %>)</strong>
		
		<logic:notEqual name="size" value="0">
			 |	
			<html:link href="#" onclick="<%= "$('div#checkbox-4 input[type=checkbox]').attr('checked','true')" %>">
				Seleccionar todos
			</html:link>
		</logic:notEqual>
	</p>
	
	<logic:equal name="size" value="0">
		<p class="mbottom2"><em><bean:message key="message.no.processes.in.state" bundle="PHD_RESOURCES"/></em></p>
	</logic:equal>
	
	<logic:notEqual name="size" value="0">
	
		<fr:view name="processList">
		
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdIndividualProgramProcess.class.getName() %>">
				<fr:slot name="phdIndividualProcessNumber.number"/>
				<fr:slot name="person.name" />
				<fr:slot name="executionYear.year"/>
				<fr:slot name="phdProgram.acronym" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft"/>
				<fr:property name="checkable" value="true"/>
				<fr:property name="checkboxName" value="selectedProcesses"/>
				<fr:property name="checkboxValue" value="externalId"/>
			</fr:layout>
			
		</fr:view>
		
	</logic:notEqual>
	
	</div>

<p><html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /></html:submit></p>

</fr:form>

</logic:present>