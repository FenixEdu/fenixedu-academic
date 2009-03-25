package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.ResearcherDTO;

public interface ISearchResearcher {
    public ResearcherDTO[] searchByKeyword(String name);
    public ResearcherDTO[] seacherByName(String keywords);
    
}
