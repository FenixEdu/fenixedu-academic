<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPhdParticipant"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%-- ### Title #### --%>
<h2><bean:message key="title.phdProgram.periods" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<p>
	<html:link action="/phdProgram.do?method=listPhdProgramForPeriods" >
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
 
<p>
	<strong>
		<bean:message key="label.php.program" bundle="PHD_RESOURCES"/>:
		<bean:write name="phdProgram" property="name"/>
	</strong>
</p>

<logic:empty name="phdProgram" property="phdProgramContextPeriods">
	<em><bean:message key="message.net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod.empty" bundle="PHD_RESOURCES" /></em>
</logic:empty>

<logic:notEmpty name="phdProgram" property="phdProgramContextPeriods">
	<bean:define id="phdProgramId" name="phdProgram" property="externalId" />
	
	<fr:view name="phdProgram" property="phdProgramContextPeriods">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod" >
			<fr:slot name="beginDate" />
			<fr:slot name="endDate" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
	
			<fr:link 	label="link.net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod.remove,PHD_RESOURCES" 
						name="remove"
						confirmation="message.net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod.remove.confirmation,PHD_RESOURCES"
						link="<%= "/phdProgram.do?method=removePhdProgramPeriod&amp;phdProgramContextPeriodId=${externalId}&amp;phdProgramId=" + phdProgramId %>"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<p>
	<html:link action="/phdProgram.do?method=prepareAddPhdProgramPeriod" paramId="phdProgramId"  paramName="phdProgram" paramProperty="externalId" >
		<bean:message key="link.net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod.add.period" bundle="PHD_RESOURCES" />
	</html:link>
</p>
