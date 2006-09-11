package pt.utl.ist.analysis;

import java.util.List;

import dml.DmlCompiler;
import dml.DomainModel;

public class DomainModelInfo {
    private DomainModel domainModel = null;

    public DomainModelInfo(List<String> dmlFiles) {
        this(dmlFiles.toArray(new String[dmlFiles.size()]));
    }

    public DomainModelInfo(String[] dmlFiles) {
        try {
            this.domainModel = DmlCompiler.getFenixDomainModel(dmlFiles);
        } catch (antlr.ANTLRException ae) {
            System.err.println("Error parsing the DML files, leaving the domain empty");
        }
    }

    public boolean isDomainBaseClass(String name) {
        return ((domainModel != null) 
                && name.endsWith("_Base") 
                && (domainModel.findClass(name.substring(0, name.length() - 5)) != null));
    }

    public boolean isDomainNonBaseClass(String name) {
        return ((domainModel != null) 
                && (domainModel.findClass(name) != null));
    }

    public boolean isDomainClass(String name){
        return isDomainNonBaseClass(name) || isDomainBaseClass(name);
    }

    public boolean isInterface(String name) {
        if (domainModel != null) {
            // The pos of the first char is one plus the pos of the last dot
            // if no dot exists, then it is 0, which is (-1 + 1)
            int posFirstChar = name.lastIndexOf('.') + 1;
            if (name.charAt(posFirstChar) == 'I') {
                String className = name.substring(0, posFirstChar) + name.substring(posFirstChar + 1);
                return (domainModel.findClass(className) != null);
            }
        }
        return false;
    }

    public boolean belongsToDomainModel(String name) {
        return isDomainNonBaseClass(name) || isDomainBaseClass(name) || isInterface(name);
    }
}
