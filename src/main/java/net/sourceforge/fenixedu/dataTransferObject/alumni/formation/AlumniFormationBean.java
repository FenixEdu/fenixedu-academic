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
package net.sourceforge.fenixedu.dataTransferObject.alumni.formation;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Alumni;

public class AlumniFormationBean implements Serializable {

    private Alumni alumni;
    private AlumniFormation alumniFormation;

    public AlumniFormationBean(Alumni alumni) {
        setAlumni(alumni);
        alumniFormation = new AlumniFormation();
    }

    public Alumni getAlumni() {
        return this.alumni;
    }

    public void setAlumni(Alumni alumni) {
        this.alumni = alumni;
    }

    public AlumniFormation getAlumniFormation() {
        return alumniFormation;
    }

    public void setAlumniFormation(AlumniFormation alumniFormation) {
        this.alumniFormation = alumniFormation;
    }

    public int getSize() {
        return this.alumni.getFormations().size();
    }

}
