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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<h3 class="mtop15 mbottom05"><bean:message key="label.queueJobs.latest" bundle="MANAGER_RESOURCES" /></h3>

<fr:view name="queueJobList" schema="manager.list.queueJobs.undone">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight mtop05" />
		<fr:property name="link(sendJob)" value="/manager/undoneQueueJobs.do?method=resendQueuedJob"/>
		<fr:property name="param(sendJob)" value="externalId/id"/>
		<fr:property name="key(sendJob)" value="label.queueJobs.list.sendJob"/>
		<fr:property name="bundle(sendJob)" value="MANAGER_RESOURCES"/>
		<fr:property name="visibleIf(sendJob)" value="isNotDoneAndCancelled"/>
		<fr:property name="module(sendJob)" value=""/>

		<fr:property name="link(Cancel)" value="/manager/undoneQueueJobs.do?method=cancelQueuedJob"/>
		<fr:property name="param(Cancel)" value="externalId/id"/>
		<fr:property name="key(Cancel)" value="label.queueJobs.list.cancelJob"/>
		<fr:property name="bundle(Cancel)" value="MANAGER_RESOURCES"/>
		<fr:property name="visibleIf(Cancel)" value="isNotDoneAndNotCancelled"/>
		<fr:property name="module(Cancel)" value=""/>
	</fr:layout>
</fr:view>


