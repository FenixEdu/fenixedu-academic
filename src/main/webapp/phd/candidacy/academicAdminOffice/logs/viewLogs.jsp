<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="title.phd.logs" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="phdIndividualProgramProcessId" name="process" property="individualProgramProcess.externalId" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
	<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + phdIndividualProgramProcessId.toString() %>">
		<bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
	
	<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<bean:define id="logs" name="process" property="logEntries" />

<logic:notEmpty name="logs">

		<fr:view name="logs">
	
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.log.PhdLogEntry" bundle="PHD_RESOURCES">
				<fr:slot name="whenOccured" />
				<fr:slot name="responsibleName" />
				<fr:slot name="state" />
				<fr:slot name="message" />
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight" />
			</fr:layout>
			
		</fr:view>

</logic:notEmpty>
