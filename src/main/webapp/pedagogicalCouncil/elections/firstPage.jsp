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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<div class="row text-center">
    <h1><bean:message key="link.createEditVotingPeriods" bundle="PEDAGOGICAL_COUNCIL" /></h1>
</div>

<div class="row">
    <div class="col-md-3">
    <h2><bean:message key="link.createEditCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL"/></h2>
    <p><bean:message key="label.createEditCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL" /></p>
    <p><html:link styleClass="btn btn-default" page="/electionsPeriodsManagement.do?method=prepare&forwardTo=createEditCandidacyPeriods"><bean:message key="label.open"/> &raquo;</html:link></p>
    </div>
    <div class="col-md-3">
    <h2><bean:message key="link.showCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL"/></h2>
    <p><bean:message key="label.showCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL" /></p>
    <p><html:link styleClass="btn btn-default" page="/electionsPeriodsManagement.do?method=prepare&forwardTo=showCandidacyPeriods"><bean:message key="label.open"/> &raquo;</html:link></p>
    </div>
    <div class="col-md-3">
    <h2><bean:message key="link.createEditVotingPeriods" bundle="PEDAGOGICAL_COUNCIL"/></h2>
    <p><bean:message key="label.createEditVotingPeriods" bundle="PEDAGOGICAL_COUNCIL" /></p>
    <p><html:link styleClass="btn btn-default" page="/electionsPeriodsManagement.do?method=prepare&forwardTo=createEditVotingPeriods"><bean:message key="label.open"/> &raquo;</html:link></p>
    </div>
    <div class="col-md-3">
    <h2><bean:message key="link.showVotingPeriods" bundle="PEDAGOGICAL_COUNCIL"/></h2>
    <p><bean:message key="label.showVotingPeriods" bundle="PEDAGOGICAL_COUNCIL" /></p>
    <p><html:link styleClass="btn btn-default" page="/electionsPeriodsManagement.do?method=prepare&forwardTo=showVotingPeriods"><bean:message key="label.open"/> &raquo;</html:link></p>
    </div>
</div>
