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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present role="role(STUDENT)">

<h2><bean:message key="label.enrollment.courses.instructions"/></h2>

<h3 class="mtop15 separator2"><bean:message key="label.introduction"/></h3>

<p><bean:message key="message.instructions.introduction"/></p>

<h3 class="mtop15 separator2"><bean:message key="label.instructions.proceed"/></h3>

<p><b><bean:message key="label.attention.nonCaps"/>:</b> <bean:message key="message.instructions.proceed"/></p>

<ul class="list4">
<li><bean:message key="label.instructions.proceed.register"/></li>
<li><bean:message key="label.instructions.proceed.unsubscribe"/></li>
<li><bean:message key="label.instructions.proceed.chooseGroup"/></li>
<li><bean:message key="label.instructions.proceed.chooseDisciplines"/></li>
<li><bean:message key="label.instructions.proceed.endProcess"/></li>
</ul>


<h3 class="mtop15 separator2"><bean:message key="label.instructions.enrollmentType"/></h3>

<ul class="list4">
	<li><strong><bean:message key="label.instructions.enrollmentType.confirmation"/>:</strong> <span class="se_enrolled"><bean:message key="label.greenLines"/></span></li>
	<li><strong><bean:message key="label.instructions.enrollmentType.provisional"/>:</strong> <span class="se_temporary"><bean:message key="label.yellowLines"/></span><br/><bean:message key="message.instructions.enrollmentType.provisional"/>
		<ul>
			<li><bean:message key="message.instructions.enrollmentType.precedence"/></li>
			<li><bean:message key="message.instructions.enrollmentType.exclusivity"/></li>
		</ul>
	</li>
	<li><strong><bean:message key="label.instructions.enrollmentType.impossible"/>:</strong> <span class="se_impossible"><bean:message key="label.redLines"/></span><br/><bean:message key="message.instructions.enrollmentType.impossible"/></li>
</ul>


<h3 class="mtop15 separator2"><bean:message key="label.instructions.registrations"/></h3>

<p class="mbottom05"><bean:message key="message.instructions.registrations"/></p>
<p class="mbottom05"><bean:message key="message.instructions.registrations.change"/></p>

</logic:present>

