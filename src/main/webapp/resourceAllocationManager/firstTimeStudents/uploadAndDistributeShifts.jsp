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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="title.firstTimeStudents.shiftDistribution" bundle="SOP_RESOURCES" /></h2>

<logic:present name="success">
	<p><span class="success0"><bean:message key="label.firstTimeStudents.distributionSuccess" bundle="SOP_RESOURCES"/></span></p>
</logic:present>

<fr:form action="/shiftDistributionFirstYear.do?method=uploadAndSimulateFileDistribution" encoding="multipart/form-data">
	<fr:edit id="fileBeanDistribution" name="fileBeanDistribution">
		<fr:schema bundle="SOP_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ShiftDistributionFileBean">
			<fr:slot name="inputStream" key="label.file" required="true" bundle="APPLICATION_RESOURCES">
				<fr:property name="fileNameSlot" value="filename"/>
			</fr:slot>
			<fr:slot name="firstPhase" key="label.firstTimeStudents.phase" required="true" layout="radio">
				<fr:property name="trueLabel" value="label.firstTimeStudents.firstPhase" />
				<fr:property name="falseLabel" value="label.firstTimeStudents.secondPhase" />
				<fr:property name="bundle" value="SOP_RESOURCES" />
				<fr:property name="classes" value="dinline liinline nobullet"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
		    <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
		    <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		    <fr:property name="title" value=",,"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit>
		<bean:message key="label.firstTimeStudents.submitAndSimulate" bundle="SOP_RESOURCES" />
	</html:submit>
</fr:form>

<logic:present name="errorLog">
	<p>Lista de Erros</p>
	<ul>
		<logic:iterate id="errorLine" name="errorLog">
			<li><bean:write name="errorLine"/></li>
		</logic:iterate>
	</ul>
</logic:present>
<logic:notEmpty name="warningLog">
	<p>Lista de Avisos</p>
	<ul>
		<logic:iterate id="warningLine" name="warningLog">
			<li><bean:write name="warningLine"/></li>
		</logic:iterate>
	</ul>
</logic:notEmpty>

<logic:present name="allowToWriteDistribution">
	<logic:equal name="allowToWriteDistribution" value="true">
		<fr:form action="/shiftDistributionFirstYear.do?method=writeDistribution" encoding="multipart/form-data">
			<fr:edit name="fileBeanDistribution" visible="false"/>		
			<html:submit>
				<bean:message key="label.firstTimeStudents.writeDistribution" bundle="SOP_RESOURCES" />
			</html:submit>
		</fr:form>		
	</logic:equal>
</logic:present>

<logic:present name="allowToGetStatistics">
	<logic:equal name="allowToGetStatistics" value="true">
		<fr:form action="/shiftDistributionFirstYear.do?method=exportStatistics" encoding="multipart/form-data">
			<input type="hidden" name="method" value="writeDistribution" />
			<fr:edit name="fileBeanDistribution" visible="false"/>
			<html:submit>
				<bean:message key="label.firstTimeStudents.exportStatistics" bundle="SOP_RESOURCES" />
			</html:submit>			
		</fr:form>
	</logic:equal>
</logic:present>