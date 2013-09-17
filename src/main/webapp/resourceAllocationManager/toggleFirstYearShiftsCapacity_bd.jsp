<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message key="title.resourceAllocationManager.management" /></em>
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