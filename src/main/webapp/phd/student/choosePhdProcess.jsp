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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<logic:present role="role(STUDENT)">


<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manageProcesses" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>



<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<logic:notEmpty name="processes">
	<fr:view schema="PhdIndividualProgramProcess.view.resume.for.student" name="processes">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
				<fr:property name="linkFormat(view)" value="/phdIndividualProgramProcess.do?method=viewProcess&processId=${externalId}"/>
				<fr:property name="key(view)" value="label.view"/>
				<fr:property name="bundle(view)" value="PHD_RESOURCES"/>
				<fr:property name="order(view)" value="0"/>				
				<fr:property name="sortBy" value="whenCreated=desc" />
		</fr:layout>
	</fr:view>	
</logic:notEmpty>
<logic:empty name="processes">
	<bean:message  key="label.phd.no.processes" bundle="PHD_RESOURCES"/>
</logic:empty>


<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</logic:present>