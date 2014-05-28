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

<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.create"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.convokes"/></h2>

<p class="mvert15 breadcumbs"><span><bean:message key="label.vigilancy.firstStep" bundle="VIGILANCY_RESOURCES"/></span> > <span class="actual"><bean:message key="label.vigilancy.secondStep" bundle="VIGILANCY_RESOURCES"/></span></p>

<fr:form id="back" action="/vigilancy/convokeManagement.do?method=revertConvokes">
	<fr:edit id="convoke" name="bean" visible="false"/>
	<ul>
		<li><a href="javascript:document.getElementById('back').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.back"/></a></li>
	</ul>
</fr:form>

<fr:view name="bean" schema="confirmConvokes">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thlight thright thtop ulnomargin"/>
		<fr:property name="columnClasses" value="inobullet"/>
	</fr:layout>
</fr:view>

<fr:form action="/vigilancy/convokeManagement.do?method=createConvokes">
	<fr:edit name="bean" schema="showEmail" id="confirmConvokes" action="/vigilancy/convokeManagement.do?method=createConvokes">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright"/>
		</fr:layout>
	</fr:edit>
	<p><html:submit><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit></p>
</fr:form>





