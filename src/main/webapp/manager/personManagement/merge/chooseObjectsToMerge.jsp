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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.personManagement.merge" /></h2>

<p class="breadcumbs">
	<span class="actual"><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 1</strong>: <bean:message key="label.personManagement.merge.choose.persons" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 2</strong>: <bean:message key="label.personManagement.merge.transfer.personal.data" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 3</strong>: <bean:message key="label.personManagement.merge.transfer.events.and.accounts" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 4</strong>: <bean:message key="label.personManagement.merge.transfer.student.data" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 5</strong>: <bean:message key="label.personManagement.merge.transfer.registrations" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 6</strong>: <bean:message key="label.personManagement.merge.delete.student" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 7</strong>: <bean:message key="label.personManagement.merge.delete.person" bundle="MANAGER_RESOURCES" /> </span>
</p>


<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>



<fr:form action="/mergePersons.do?method=chooseObjects">
	
	<bean:message key="documentIdNumber" bundle="MANAGER_RESOURCES"/>:&nbsp;	
	<b><bean:write name="mergePersonsBean" property="idPosOneIndex" />�</b>
	<fr:edit 	id="mergePersonBean.idPosOneValue"
				name="mergePersonsBean"
				slot="idPosOneValue">
		<fr:layout>
			<fr:property name="size" value="1" />
			<fr:property name="maxLength" value="1" />
		</fr:layout>
	</fr:edit> 

	<b><bean:write name="mergePersonsBean" property="idPosTwoIndex" />�</b>
	<fr:edit 	id="mergePersonBean.idPosTwoValue"
			name="mergePersonsBean"
			slot="idPosTwoValue" >
		<fr:layout>
			<fr:property name="size" value="1" />
			<fr:property name="maxLength" value="1" />
		</fr:layout>
	</fr:edit> 
 
	<b><bean:write name="mergePersonsBean" property="idPosThreeIndex" />�</b>
	<fr:edit 	id="mergePersonBean.idPosThreeValue"
			name="mergePersonsBean"
			slot="idPosThreeValue" >
		<fr:layout>
			<fr:property name="size" value="1" />
			<fr:property name="maxLength" value="1" />
		</fr:layout>
	</fr:edit> 

	<fr:edit id="mergePersonsBean" name="mergePersonsBean" visible="false"/>
	
	<fr:edit id="chooseObjects" 
			name="mergePersonsBean"
			schema="personManagement.merge.chooseObjects"/>
		
	<html:submit><bean:message key="label.continue" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form>

