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
 * Created on 27/Out/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Coordinator;

/**
 * fenix-head Dominio
 * 
 * @author João Mota 27/Out/2003
 * 
 */
public class InfoCoordinator extends InfoObject {

    private final Coordinator coordinator;

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public InfoCoordinator(final Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public InfoExecutionDegree getInfoExecutionDegree() {
        return InfoExecutionDegree.newInfoFromDomain(getCoordinator().getExecutionDegree());
    }

    public InfoTeacher getInfoTeacher() {
        return InfoTeacher.newInfoFromDomain(getCoordinator().getPerson().getTeacher());
    }

    public InfoPerson getInfoPerson() {
        return InfoPerson.newInfoFromDomain(getCoordinator().getPerson());
    }

    public Boolean getResponsible() {
        return getCoordinator().getResponsible();
    }

    public static InfoCoordinator newInfoFromDomain(final Coordinator coordinator) {
        return coordinator == null ? null : new InfoCoordinator(coordinator);
    }

    @Override
    public String getExternalId() {
        return getCoordinator().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

}
