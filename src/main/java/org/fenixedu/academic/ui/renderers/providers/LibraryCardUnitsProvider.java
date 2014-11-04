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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class LibraryCardUnitsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        List<String> unitsNames = new ArrayList<String>();
        unitsNames.add("1 - DECivil");
        unitsNames.add("2 - DECivil - Sec. Híd. Rec. Híd. Ambientais");
        unitsNames.add("3 - DECivil - Sec. Mec. Estrutural Estruturas");
        unitsNames.add("4 - DECivil - Secção Construção");
        unitsNames.add("5 - DECivil - Sec. Urb. Trans. Vias Sistemas");
        unitsNames.add("6 - DECivil - Secção Geotecnia");
        unitsNames.add("7 - DECivil - Sec. Sist. Apoio Projecto");
        unitsNames.add("8 - DECivil - Secção Arquitectura");
        unitsNames.add("9 - DEEC");
        unitsNames.add("10 - DEEC - Área Científica Energia");
        unitsNames.add("11 - DEEC - Área Científica Computadores");
        unitsNames.add("12 - DEEC - Área Científica Electrónica");
        unitsNames.add("13 - DEEC - Área Científica Dec. e Controlo");
        unitsNames.add("14 - DEEC - Área Científica Telecomunicações");
        unitsNames.add("15 - DEM");
        unitsNames.add("16 - DEM - Secção Projecto Mecânico");
        unitsNames.add("17 - DEM - Secção Termofluídos e Energia");
        unitsNames.add("18 - DEM - Secção Tecnologia Mecânica");
        unitsNames.add("19 - DEM - Secção Sistemas");
        unitsNames.add("20 - DEM - Secção Ambiente e Energia");
        unitsNames.add("21 - DEM - Secção Mecânica Aeroespacial");
        unitsNames.add("22 - DM");
        unitsNames.add("23 - DM - Secção Estatísticas e Aplicações");
        unitsNames.add("24 - DM - Secção Mat. Apli. e Aná. Numérica");
        unitsNames.add("25 - DM - Secção Lógica e Computação");
        unitsNames.add("26 - DM - Secção Álgebra e Análise");
        unitsNames.add("27 - DEMG");
        unitsNames.add("28 - DEMG - Lab. Mineralogia e Petrologia");
        unitsNames.add("29 - DEMG - Secção Exploração Minas");
        unitsNames.add("30 - DEMG - Lab. Mine. e Pla. Mineiro");
        unitsNames.add("31 - DEQB");
        unitsNames.add("32 - DF");
        unitsNames.add("33 - DEI");
        unitsNames.add("34 - DEG");
        unitsNames.add("35 - SAEN");
        unitsNames.add("36 - DEMAT");

        return unitsNames;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
