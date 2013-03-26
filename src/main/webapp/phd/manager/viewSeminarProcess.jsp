<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess"%>
<%@page import="net.sourceforge.fenixedu.domain.accessControl.PermissionType" %>

<logic:notEmpty name="process" property="seminarProcess">
<logic:equal name="process" property="activeState.active" value="true">
	<bean:define id="seminarProcess" name="process" property="seminarProcess" />

	<br/>
	<strong><bean:message  key="label.phd.publicPresentationSeminarProcess" bundle="PHD_RESOURCES"/></strong>
	<table>
		<tr>
		    <td>
				<fr:view schema="PublicPresentationSeminarProcess.view" name="process" property="seminarProcess">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight mtop10 thleft" />
					</fr:layout>
				</fr:view>
			</td>
			<td>
				<ul class="operations" >
					<li >
						<html:link action="/publicPresentationSeminarProcess.do?method=manageStates" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.states"/>
						</html:link>
					</li>
				</ul>
			</td>
		</tr>
	</table>
	
	<ul class="operations">
		<logic:notEmpty name="seminarProcess" property="comissionDocument">				
		<li style="display: inline;">
			<bean:define id="comissionDocumentUrl" name="seminarProcess" property="comissionDocument.downloadUrl" />
			<a href="<%= comissionDocumentUrl.toString() %>"><bean:message  key="label.phd.public.presentation.seminar.comission.document" bundle="PHD_RESOURCES"/></a>
		</li>
		</logic:notEmpty>
		
		<logic:notEmpty name="seminarProcess" property="reportDocument">
		<li style="display: inline;">
			<bean:define id="reportDocumentUrl" name="seminarProcess" property="reportDocument.downloadUrl" />
			<a href="<%= reportDocumentUrl.toString() %>"><bean:message  key="label.phd.public.presentation.seminar.report.document" bundle="PHD_RESOURCES"/></a>
		</li>
		</logic:notEmpty>

	</ul>
</logic:equal>
<br/>
</logic:notEmpty>
