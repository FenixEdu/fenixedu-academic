<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<html:xhtml />

<logic:present role="MANAGER">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="title.phdProgramInformation" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="phdProgramId" name="phdProgram" property="externalId" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<p>
	<html:link action="/phdProgramInformation.do?method=listPhdPrograms">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
 
<p><strong><bean:message key="title.phdProgramInformation" bundle="PHD_RESOURCES"/></strong></p>

<logic:empty name="phdProgram" property="phdProgramInformations">
	<em><bean:message key="message.phdProgramInformations.empty" bundle="PHD_RESOURCES" /></em>
</logic:empty>

<logic:notEmpty name="phdProgram" property="phdProgramInformations">
	<fr:view name="phdProgram" property="phdProgramInformations" >
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdProgramInformation" >
			<fr:slot name="beginDate" />
			<fr:slot name="minThesisEctsCredits" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramInformation.table.minThesisEctsCredits" />
			<fr:slot name="maxThesisEctsCredits" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramInformation.table.maxThesisEctsCredits" />
			<fr:slot name="minStudyPlanEctsCredits" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramInformation.table.minStudyPlanEctsCredits"/>
			<fr:slot name="maxStudyPlanEctsCredits" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramInformation.table.maxStudyPlanEctsCredits" />
			<fr:slot name="numberOfYears" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramInformation.table.numberOfYears" />
			<fr:slot name="numberOfSemesters" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramInformation.table.numberOfSemesters" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

</logic:present>
