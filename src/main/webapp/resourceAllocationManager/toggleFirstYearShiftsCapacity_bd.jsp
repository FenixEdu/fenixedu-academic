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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="title.manage.firstYearShiftCapacity" /></h2>


<bean:message bundle="SOP_RESOURCES" key="label.firstYearShiftsCapacity.description"/>
<bean:define id="executionYear" name="executionYear" type="net.sourceforge.fenixedu.domain.ExecutionYear"/>
<br/>
<br/>

<h4><bean:message bundle="SOP_RESOURCES" key="label.change.firstYearShiftsCapacity"/> <bean:write name="executionYear" property="name"/></h4>

<span style="float:left;">
	<fr:form id="firstYearShiftCapacityForm" action="<%="/chooseExecutionPeriod.do?method=blockFirstYearShiftsCapacity&executionYearId=" + executionYear.getOID()%>">
		<html:submit altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="label.block" />
		</html:submit>	
	</fr:form>
</span>

<span style="float:left;">
	<fr:form id="firstYearShiftCapacityForm" action="<%="/chooseExecutionPeriod.do?method=unblockFirstYearShiftsCapacity&executionYearId=" + executionYear.getOID()%>">
		<html:submit altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="label.unblock" />
		</html:submit>
	</fr:form>
</span>

<br/>
<br/>

<logic:present name="affectedDegrees">
	<h4><bean:message key="label.affectedDegrees"/>:</h4>
	<p/>
	<logic:empty name="affectedDegrees">
		<p><span class="error">
			<bean:message key="message.error.firstYearShiftsCapacity" bundle="SOP_RESOURCES"/>
		</span></p>
	</logic:empty>
	<logic:notEmpty name="affectedDegrees">
		<logic:iterate id="affectedDegree" name="affectedDegrees">
			<bean:write name="affectedDegree"/>
			<br/>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>