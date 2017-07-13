<%--

    Copyright © 2017 Instituto Superior Técnico

    This file is part of FenixEdu Spaces.

    FenixEdu Spaces is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Spaces is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Spaces.  If not, see <http://www.gnu.org/licenses/>.

--%>
<!DOCTYPE html> 
${portal.toolkit()}
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="page-header">
    <h2>
        <spring:message code="title.manage.countries"/>
        <small><c:out value="${country.localizedName.content}"/></small>
    </h2>
</div>

<c:if test="${not empty success}">
    <div class="alert alert-success" role="alert">
        <strong><c:out value="${success}"/></strong>
    </div>
</c:if>

<spring:url var="editCountryUrl" value="/country-management/${country.externalId}/edit" />
<spring:url var="backUrl" value="/country-management" />
<form id="bean" role="form" class="accept" action="${editCountryUrl}" method="POST">
    ${csrf.field()}
    <div class="container-fluid">
        <div class="form-group">
        	<div class="row">
                <label class="col-xs-2">
                	<spring:message code="label.country.localized.name"/>
                </label>
                <div class="col-xs-4">
                    <input bennu-localized-string type="text" name="localizedName" id="localizedName" value='<c:out value="${country.localizedName.toLocalizedString().json()}"/>' required/>
                </div>
            </div>
            <div class="row">
                <label class="col-xs-2 control-label" for="countryNationality" >
                	<spring:message code="label.country.nationality"/>
                </label>
                <div class="col-xs-4">
                    <input bennu-localized-string type="text" name="countryNationality" value='<c:out value="${country.countryNationality.json()}"/>' required/>
                </div>
            </div>
        </div>
        <div class="form-group">
           <div class="row">
                <div class="col-xs-2">
                    <button type="submit" class="btn btn-primary btn-default"><spring:message code="label.save" /></button>
                    <a class="btn btn-default" href="${backUrl}"><spring:message code="label.back" /></a>
                </div>
            </div>  
        </div>
    </div> 
</form>
