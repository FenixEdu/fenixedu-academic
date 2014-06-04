/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum ResultPersonCategory implements IPresentableEnum {

    DELEGATE(1), TEACHER(2), REGENT(3), DEGREE_COORDINATOR(4), DEPARTMENT_PRESIDENT(5);

    private int permissionOrder;

    private ResultPersonCategory(int permissionOrder) {
        setPermissionOrder(permissionOrder);
    }

    public void setPermissionOrder(int permissionOrder) {
        this.permissionOrder = permissionOrder;
    }

    public int getPermissionOrder() {
        return permissionOrder;
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, this.getClass().getName() + "." + name());
    }

}
