<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manageProcesses" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>



<%--  ###  Return Links  ### --%>

<%--  ### Return Links  ### --%>




<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>



<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<jsp:include page="/phd/alertMessagesNotifier.jsp?global=true" />

<%--  ### End Of Context Information  ### --%>



<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<%--  ### Search Criteria  ### --%>

<bean:define id="searchProcessBean" name="searchProcessBean" type="net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean" />

<fr:form id="search" action="/phdIndividualProgramProcess.do">
	<input type="hidden" name="sortBy" value="" />
	<input type="hidden" name="method" id="method" value="manageProcesses" />

	<fr:edit id="searchProcessBean"
		name="searchProcessBean">
		<fr:schema type="net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean" bundle="PHD_RESOURCES">
			<fr:slot name="executionYear" layout="menu-select">	
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.ExecutionYearsProvider" />
				<fr:property name="format" value="${year}" />
		        <fr:property name="saveOptions" value="true"/>
			</fr:slot>
			<fr:slot name="phdProgram" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProgramsProvider" />
				<fr:property name="format" value="${name}" />
			</fr:slot>
			<fr:slot name="processState" layout="menu-select-postback">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.PhdIndividualProgramProcessStateProvider" />
				<fr:property name="format" value="${localizedName}" />
				<fr:property name="destination" value="process-state-postback" />
			</fr:slot>
			
			<% if(net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState.CANDIDACY.equals(searchProcessBean.getProcessState())) { %>
			<fr:slot name="candidacyProcessState" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.PhdProgramCandidacyProcessStateProvider" />
				<fr:property name="format" value="${localizedName}" />
			</fr:slot>
			<% } %>			
			
			<% if(net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState.THESIS_DISCUSSION.equals(searchProcessBean.getProcessState())) { %>
			<fr:slot name="thesisProcessState" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.PhdThesisProcessStateTypeProvider" />
				<fr:property name="format" value="${localizedName}" />
			</fr:slot>
			<%  } %>
			<fr:slot name="processNumber" />
			<fr:slot name="studentNumber" />
			<fr:slot name="phdStudentNumber" />
			<fr:slot name="name" />
			<fr:slot name="onlineApplicationFilter" layout="menu-select-postback">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.SearchPhdIndividualProgramProcessBeanOnlineApplicationFilterProvider" />
				<fr:property name="destination" value="online-application-filter-postback" />
			</fr:slot>
			
			<% if(net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean.OnlineApplicationFilter.ONLY_ONLINE.equals(searchProcessBean.getOnlineApplicationFilter())) { %>
			<fr:slot name="phdCandidacyPeriod" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.providers.PhdCandidacyPeriodsProvider" />
				<fr:property name="format" value="${presentationName}" />
			</fr:slot>
			<% } %>
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="process-state-postback" path="/phdIndividualProgramProcess.do?method=manageProcesses"/>
		<fr:destination name="online-application-filter-postback" path="/phdIndividualProgramProcess.do?method=manageProcesses" />
	</fr:edit>

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='manageProcesses';return true;">
			<bean:message bundle="PHD_RESOURCES" key="label.search"/>
		</html:submit>
	</p>
	
	<p>
		<html:link href="#" onclick="jQuery('#method').attr('value', 'generateReport'); jQuery('#search').submit();">
			<bean:message bundle="PHD_RESOURCES" key="link.export.in.excel" />
		</html:link>
	</p>

<%--  ### End of Search Criteria  ### --%>

<br/>
<br/>

<%--  ### Results  ### --%>
<html:link action="/phdProgramCandidacyProcess.do?method=prepareSearchPerson">
	<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.academicAdminOffice.createCandidacy"/>
</html:link> | 

<html:link action="/phdIndividualProgramProcess.do?method=viewMigratedProcesses">
	<bean:message bundle="PHD_RESOURCES" key="label.phd.viewMigrationProcesses" />
</html:link> |

<html:link action="/phdProgram.do?method=listPhdProgramForPeriods">
	<bean:message bundle="PHD_RESOURCES" key="link.phdProgram.periods.list" />
</html:link> |

<html:link action="/phdProgramInformation.do?method=listPhdPrograms">
	<bean:message bundle="PHD_RESOURCES" key="link.phdProgramInformation.list" />
</html:link>
<br/><br/>


<bean:size id="processesCount" name="processes"/>
<bean:message  key="label.phd.process.count" bundle="PHD_RESOURCES" arg0="<%= processesCount.toString() %>"/>
<logic:notEmpty name="processes">
	<fr:view schema="PhdIndividualProgramProcess.view.resume" name="processes">
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			<fr:property name="linkFormat(view)" value="/phdIndividualProgramProcess.do?method=viewProcess&processId=${externalId}"/>
			<fr:property name="key(view)" value="label.view"/>
			<fr:property name="bundle(view)" value="PHD_RESOURCES"/>

			<fr:property name="sortFormId" value="search"/>
			<fr:property name="sortActionLink" value="true"/>
			<fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") != null ? request.getParameter("sortBy"): "phdIndividualProcessNumber"  %>"/>
			<fr:property name="ascendingImage" value="/images/upArrow.gif"/>
        	<fr:property name="descendingImage" value="/images/downArrow.gif"/>
			<fr:property name="sortableSlots" value="phdIndividualProcessNumber,activeState,person.name" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="processes">
	<br/>
	<em><bean:message key="label.phd.no.processes" bundle="PHD_RESOURCES" /></em>
	<br/>
</logic:empty>

<%--  ### End of Results  ### --%>

<%--  ### End of Operation Area  ### --%>


<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>

</fr:form>

</logic:present>


