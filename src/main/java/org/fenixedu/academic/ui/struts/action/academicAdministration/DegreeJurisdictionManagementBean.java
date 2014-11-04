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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;

import org.fenixedu.bennu.core.domain.Bennu;

public class DegreeJurisdictionManagementBean implements Serializable {
    private static final long serialVersionUID = -3115602761325408245L;

    private static final Comparator<AcademicProgram> COMPARATOR_BY_SIGLA = new Comparator<AcademicProgram>() {

        @Override
        public int compare(AcademicProgram ap1, AcademicProgram ap2) {
            if (ap1 instanceof Degree && ap2 instanceof Degree) {
                Degree d1 = (Degree) ap1;
                Degree d2 = (Degree) ap2;
                return d1.getSigla().compareTo(d2.getSigla());
            }
            if (ap1 instanceof PhdProgram && ap2 instanceof PhdProgram) {
                PhdProgram p1 = (PhdProgram) ap1;
                PhdProgram p2 = (PhdProgram) ap2;
                return p1.getAcronym().compareTo(p2.getAcronym());
            }
            return 0;
        }

    };

    private List<Degree> preBologna;

    private List<Degree> bachelors;

    private List<Degree> masters;

    private List<Degree> integratedMasters;

    private List<Degree> dfas;

    private List<Degree> deas;

    private List<PhdProgram> phds;

    private List<Degree> specs;

    public DegreeJurisdictionManagementBean() {
        super();
        init();
    }

    public List<Degree> getPreBologna() {
        return preBologna;
    }

    public void setPreBologna(List<Degree> preBologna) {
        Collections.sort(preBologna, COMPARATOR_BY_SIGLA);
        this.preBologna = preBologna;
    }

    public List<Degree> getBachelors() {
        return bachelors;
    }

    public void setBachelors(List<Degree> bachelors) {
        Collections.sort(bachelors, COMPARATOR_BY_SIGLA);
        this.bachelors = bachelors;
    }

    public List<Degree> getMasters() {
        return masters;
    }

    public void setMasters(List<Degree> masters) {
        Collections.sort(masters, COMPARATOR_BY_SIGLA);
        this.masters = masters;
    }

    public List<Degree> getIntegratedMasters() {
        return integratedMasters;
    }

    public void setIntegratedMasters(List<Degree> integratedMasters) {
        Collections.sort(integratedMasters, COMPARATOR_BY_SIGLA);
        this.integratedMasters = integratedMasters;
    }

    public List<Degree> getDfas() {
        return dfas;
    }

    public void setDfas(List<Degree> dfas) {
        Collections.sort(dfas, COMPARATOR_BY_SIGLA);
        this.dfas = dfas;
    }

    public List<Degree> getDeas() {
        return deas;
    }

    public void setDeas(List<Degree> deas) {
        Collections.sort(deas, COMPARATOR_BY_SIGLA);
        this.deas = deas;
    }

    public List<PhdProgram> getPhds() {
        return phds;
    }

    public void setPhds(List<PhdProgram> phds) {
        Collections.sort(phds, COMPARATOR_BY_SIGLA);
        this.phds = phds;
    }

    public List<Degree> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Degree> specs) {
        Collections.sort(specs, COMPARATOR_BY_SIGLA);
        this.specs = specs;
    }

    private void init() {
        List<Degree> preBologna = new ArrayList<Degree>();
        List<Degree> bachelors = new ArrayList<Degree>();
        List<Degree> masters = new ArrayList<Degree>();
        List<Degree> integratedMasters = new ArrayList<Degree>();

        List<Degree> dfas = new ArrayList<Degree>();
        List<Degree> deas = new ArrayList<Degree>();
        List<PhdProgram> phds = new ArrayList<PhdProgram>();
        List<Degree> specs = new ArrayList<Degree>();

        for (Degree degree : Bennu.getInstance().getDegreesSet()) {
            switch (degree.getDegreeType()) {
            case DEGREE:
                preBologna.add(degree);
                break;
            case MASTER_DEGREE:
                preBologna.add(degree);
                break;

            case BOLONHA_DEGREE:
                bachelors.add(degree);
                break;
            case BOLONHA_MASTER_DEGREE:
                masters.add(degree);
                break;
            case BOLONHA_INTEGRATED_MASTER_DEGREE:
                integratedMasters.add(degree);
                break;

            case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
                dfas.add(degree);
                break;
            case BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA:
                deas.add(degree);
                break;
            case BOLONHA_SPECIALIZATION_DEGREE:
                specs.add(degree);
                break;
            case EMPTY:
                specs.add(degree);
                break;
            }
        }

        for (PhdProgram phd : Bennu.getInstance().getPhdProgramsSet()) {
            if (!deas.contains(phd.getDegree())) {
                phds.add(phd);
            }
        }

        setPreBologna(preBologna);
        setBachelors(bachelors);
        setMasters(masters);
        setIntegratedMasters(integratedMasters);
        setDfas(dfas);
        setDeas(deas);
        setPhds(phds);
        setSpecs(specs);
    }

}
