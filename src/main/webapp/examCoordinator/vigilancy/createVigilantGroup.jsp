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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.newVigilantGroup.title"/></h2>

<fr:form action="/vigilancy/vigilantGroupManagement.do?method=createVigilantGroup">

<fr:edit id="createVigilantGroup" name="bean" visible="false"/>

<fr:edit 
		   id="createVigilantGroup.block1"
		   type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean" layout="tabular"
		   name="bean"
           schema="vigilantGroup.block1" >
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thright thlight mtop1"/>		
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"/>
</fr:edit>

<p class="mtop1 mbottom025"><strong><bean:message key="label.vigilancy.firstUnavailablePeriod" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
<fr:edit 
		   id="createVigilantGroup.block2"
		   type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean" layout="tabular"
		   name="bean"
           schema="vigilantGroup.block2" 
>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop025"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<p class="mtop15 mbottom025"><strong><bean:message key="label.vigilancy.secondUnavailablePeriod" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
<fr:edit 
		   id="createVigilantGroup.block3"
		   type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean" layout="tabular"
		   name="bean"
           schema="vigilantGroup.block3" 
>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop025"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<p class="mtop15"><html:submit><bean:message key="label.create" bundle="VIGILANCY_RESOURCES"/></html:submit>
<html:cancel>
	<bean:message key="label.cancel" bundle="VIGILANCY_RESOURCES"/>
</html:cancel>
</p>

</fr:form>
           
