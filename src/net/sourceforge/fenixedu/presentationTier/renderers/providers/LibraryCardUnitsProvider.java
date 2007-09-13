package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class LibraryCardUnitsProvider implements DataProvider {
    
    public Object provide(Object source, Object currentValue) {
        List<String> unitsNames = new ArrayList<String>();
        unitsNames.add(new String("1 - DECivil"));
        unitsNames.add(new String("2 - DECivil - Sec. Híd. Rec. Híd. Ambientais"));
        unitsNames.add(new String("3 - DECivil - Sec. Mec. Estrutural Estruturas"));
        unitsNames.add(new String("4 - DECivil - Secção Construção"));
        unitsNames.add(new String("5 - DECivil - Sec. Urb. Trans. Vias Sistemas"));
        unitsNames
                .add(new String("6 - DECivil - Secção Geotecnia"));
        unitsNames.add(new String("7 - DECivil - Sec. Sist. Apoio Projecto"));
        unitsNames.add(new String("8 - DECivil - Secção Arquitectura"));
        unitsNames.add(new String("9 - DEEC"));
        unitsNames.add(new String("10 - DEEC - Área Científica Energia"));
        unitsNames.add(new String("11 - DEEC - Área Científica Computadores"));
        unitsNames.add(new String("12 - DEEC - Área Científica Electrónica"));
        unitsNames.add(new String("13 - DEEC - Área Científica Dec. e Control"));
        unitsNames.add(new String("14 - DEEC - Área Científica Telecomunicações"));
        unitsNames.add(new String("15 - DEM"));
        unitsNames.add(new String("16 - DEM - Secção Projecto Mecânico"));
        unitsNames.add(new String("17 - DEM - Secção Termofluídos e Energia"));
        unitsNames.add(new String("18 - DEM - Secção Tecnologia Mecânica"));
        unitsNames.add(new String("19 - DEM - Secção Sistemas"));
        unitsNames.add(new String("20 - DEM - Secção Ambiente e Energia"));
        unitsNames.add(new String("21 - DEM - Secção Mecânica Aeroespacial"));
        unitsNames.add(new String("22 - DM"));
        unitsNames.add(new String("23 - DM - Secção Estatísticas e Aplicações"));
        unitsNames.add(new String("24 - DM - Secção Mat. Apli. e Aná. Numérica"));
        unitsNames.add(new String("25 - DM - Secção Lógica e Computação"));
        unitsNames.add(new String("26 - DM - Secção Álgebra e Análise"));
        unitsNames.add(new String("27 - DEMG"));
        unitsNames.add(new String("28 - DEMG - Lab. Mineralogia e Petrologia"));
        unitsNames.add(new String("29 - DEMG - Secção Exploração Minas"));
        unitsNames.add(new String("30 - DEMG - Lab. Mine. e Pla. Mineiro"));
        unitsNames.add(new String("31 - DEQB"));
        unitsNames.add(new String("32 - DF"));
        unitsNames.add(new String("33 - DEI"));
        unitsNames.add(new String("34 - DEG"));
        unitsNames.add(new String("35 - SAEN"));
        unitsNames.add(new String("36 - DEMAT"));

        return unitsNames;
    }

    public Converter getConverter() {
        return null;
    }

}
