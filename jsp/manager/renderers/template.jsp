<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>

<!-- Defines a page attribute with the value of the property "nome" of the shown object -->
<ft:define id="personName" property="name"/>

<table style="border-bottom: 1px solid gray">
    <thead>
        <tr>
            <th colspan="5" style="border-bottom: 1px solid gray">(1) <%= personName %></td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>(2) <ft:label property="gender"/></td>
            <td>(3) <ft:view property="gender"/></td>
            
            <!-- separator -->
            <td width="100px" rowspan="2"></td>
            
            <td>(4) <ft:label property="dateOfBirthYearMonthDay"/></td>
            <td>(5)
                <ft:view property="dateOfBirthYearMonthDay">
                    <ft:layout>
                        <ft:property name="format" value="dd MMMM yyyy"/>
                    </ft:layout>
                </ft:view>
            </td>
        </tr>
        <tr>
            <td>(6) <ft:label property="username"/></td>
            <td>(7) <ft:view property="username"/></td>

            <td>(8) <ft:label property="nationality.nationality"/></td>
            <td>(9) <ft:view property="nationality.nationality"/> </td>
        </tr>
    </tbody>
</table>
