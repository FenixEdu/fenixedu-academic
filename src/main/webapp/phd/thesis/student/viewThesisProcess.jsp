<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:notEmpty name="process" property="thesisProcess">

<logic:equal name="process" property="activeState.active" value="true">

	<bean:define id="thesisProcess" name="process" property="thesisProcess" />
	
	<br/>
	<strong><bean:message  key="label.phd.thesisProcess" bundle="PHD_RESOURCES"/></strong>
	<table>
	  <tr>
		    <td>
				<fr:view schema="PhdThesisProcess.view.simple" name="process" property="thesisProcess">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight mtop10" />
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<td>
				<ul class="operations" >
					<li>
						<html:link action="/phdThesisProcess.do?method=manageThesisDocuments" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.manageThesisDocuments"/>
						</html:link>
					</li>
				</ul>
			</td>
	  	</tr>
	 </table>
	
	<br/>

</logic:equal>

</logic:notEmpty>
