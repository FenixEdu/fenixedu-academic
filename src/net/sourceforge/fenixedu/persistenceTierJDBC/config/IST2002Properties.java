package net.sourceforge.fenixedu.persistenceTierJDBC.config;

import java.io.InputStream;
import java.util.Properties;

public class IST2002Properties extends Properties {

    public IST2002Properties(String filename) {
        try {
            InputStream file = IST2002Properties.class.getResourceAsStream(filename);
            load(file);
        } catch (Exception e) {
            System.out.println("IST2002Properties: Erro na leitura do ficheiro de configuração:"
                    + e.toString());
        }
    }
}