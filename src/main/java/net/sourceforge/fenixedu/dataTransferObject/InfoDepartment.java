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
/*
 * Created on 4/Jul/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Department;

/**
 * @author jpvl
 */
public class InfoDepartment extends InfoObject {
    private String name;
    private String realName;
    private String code;
    private Boolean active;

    public InfoDepartment() {
    }

    /**
     * @return
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InfoDepartment) {
            InfoDepartment infoDepartment = (InfoDepartment) obj;
            return ((this.getCode().equals(infoDepartment.getCode())) || (this.getName().equals(infoDepartment.getName())));
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */
    public void copyFromDomain(Department department) {
        super.copyFromDomain(department);
        if (department != null) {
            setCode(department.getCode());
            setName(department.getName());
            setRealName(department.getRealName());
            setActive(department.getActive());
        }
    }

    public static InfoDepartment newInfoFromDomain(Department department) {
        InfoDepartment infoDepartment = null;
        if (department != null) {
            infoDepartment = new InfoDepartment();
            infoDepartment.copyFromDomain(department);
        }
        return infoDepartment;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}