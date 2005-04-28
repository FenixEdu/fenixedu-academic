/*
 * Created on 28/Jul/2003, 15:05:18
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;


/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Jul/2003, 15:05:18
 *  
 */
public class CaseStudy extends CaseStudy_Base {
    
    public CaseStudy() {
    }

    public CaseStudy(String name, String description, String code) {
        this.setName(name);
        this.setDescription(description);
        this.setCode(code);
    }

}