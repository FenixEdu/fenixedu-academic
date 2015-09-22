<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="label.operator.photo.title" bundle="MANAGER_RESOURCES" /> <small><bean:message key="label.operator.photo.subtitle" bundle="MANAGER_RESOURCES" /></small></h2>

<div class="alert alert-info" role="alert"><bean:message key="label.operator.photo.instructions" bundle="MANAGER_RESOURCES" /></div>

<logic:empty name="pending">
    <p><em><bean:message key="label.operator.photo.no.pending" bundle="MANAGER_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="pending">
	<fr:edit id="result" name="pending" schema="operator.photo.pending.list"
		action="/pendingPhotos.do?method=accept" layout="tabular-editable">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle1 thlight mtop05 ulnomargin tdcenter" />
		</fr:layout>
	</fr:edit>
</logic:notEmpty>

<style>
	.btn-tag {
	    font-size: 12px;
		padding: 2px 4px;
	    border-radius: 4px;
	    cursor: default;
	    margin: auto 2px;
	}
	
	.keyb-tag {
	    font-size: 12px;
	    font-family: Monospace;
	    background-color: #787878;
	    padding: 2px 4px;
	    border-radius: 4px;
	    color: #ffffff;
	    font-weight: 100;
	}
</style>