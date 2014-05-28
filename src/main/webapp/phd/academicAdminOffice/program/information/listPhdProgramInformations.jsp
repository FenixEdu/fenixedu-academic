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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<html:xhtml />

<%-- ### Title #### --%>
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
			<fr:link 	label="link.net.sourceforge.fenixedu.domain.phd.PhdProgramInformation.edit,PHD_RESOURCES" 
						name="edit"
						link="<%= "/phdProgramInformation.do?method=prepareEditPhdProgramInformation&amp;phdProgramInformationId=${externalId}&amp;phdProgramId=" + phdProgramId %>"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<p>
	<html:link action="/phdProgramInformation.do?method=prepareCreatePhdInformation" paramId="phdProgramId" paramName="phdProgramId" >
		<bean:message key="link.phdProgramInformation.create" bundle="PHD_RESOURCES" />
	</html:link>
</p>
