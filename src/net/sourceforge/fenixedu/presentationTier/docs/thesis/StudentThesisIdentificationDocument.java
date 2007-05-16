package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.util.JasperPrintProcessor;

import org.joda.time.DateTime;

public class StudentThesisIdentificationDocument extends ThesisDocument {

    /**
     * Serialization id. 
     */
    private static final long serialVersionUID = 1L;
    
    public StudentThesisIdentificationDocument(Thesis thesis) {
        	super(thesis);
    }
    
    @Override
    protected void fillThesisInfo() {
        Thesis thesis = getThesis();

        ThesisFile file = thesis.getDissertation();
        if (file != null) {
            parameters.put("thesisTitle", file.getTitle());
            parameters.put("thesisSubtitle", neverNull(file.getSubTitle()));
            parameters.put("thesisLanguage", getLanguage(file));
        }
        else {
            parameters.put("thesisTitle", "");
            parameters.put("thesisSubtitle", "");
            parameters.put("thesisLanguage", "");
        }
        
        String date = null;

        DateTime discussion = thesis.getDiscussed();
        if (discussion != null) {
            date = String.format(new Locale("pt"), "%1$td/%1$tm/%1$tY", discussion.toDate());
        }
        
        parameters.put("discussion", neverNull(date));

        int index = 0;
        for (String keyword : splitKeywords(thesis.getKeywordsPt())) {
            parameters.put("keywordPt" + index++, keyword);
        }

        while (index < 6) {
            parameters.put("keywordPt" + index++, "");
        }
        
        index = 0;
        for (String keyword : splitKeywords(thesis.getKeywordsEn())) {
            parameters.put("keywordEn" + index++, keyword);
        }

        while (index < 6) {
            parameters.put("keywordEn" + index++, "");
        }
        
        parameters.put("abstractPt", neverNull(thesis.getThesisAbstractPt()));
        parameters.put("abstractEn", neverNull(thesis.getThesisAbstractEn()));
    }

    private String getLanguage(ThesisFile file) {
        Language language = file.getLanguage();
        
        if (language == null) {
            return "";
        }
        
        ResourceBundle enumResourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", new Locale("pt"));
        return enumResourceBundle.getString(language.name());
    }

    private List<String> splitKeywords(String keywords) {
        List<String> result = new ArrayList<String>();
        
        if (keywords == null) {
            return result;
        }
        
        for (String part : keywords.split(",")) {
            String trimmed = part.trim();
            
            if (trimmed.length() > 0) {
                result.add(trimmed);
            }
        }
        
        return result;
    }

    @Override
    public String getReportFileName() {
        Thesis thesis = getThesis();
        return "identificacao-aluno-" + thesis.getStudent().getNumber();
    }

    @Override
    public JasperPrintProcessor getPreProcessor() {
    	return LineProcessor.instance;
    }

    private static class LineProcessor implements JasperPrintProcessor {

    	private static LineProcessor instance = new LineProcessor();
    	
        private static String[][] FIELD_LINE_MAP = {
        	{ "textField-title", "13", "line-title-2" },
        	{ "textField-subtitle", "13", "line-subtitle-2" }
        };
        
        private static float LINE_DISTANCE = 11.5f;
        
        public JasperPrint process(JasperPrint jasperPrint) {
			Map<String, JRPrintElement> map = getElementsMap(jasperPrint);
			
			for (int i = 0; i < FIELD_LINE_MAP.length; i++) {
				String elementKey = FIELD_LINE_MAP[i][0];
				Integer height = new Integer(FIELD_LINE_MAP[i][1]); 
				
				JRPrintElement element = map.get(elementKey);
				
				if (element == null) {
					continue;
				}
				
				if (element.getHeight() > height.intValue()) {
					// height increased
					for (int j = 2; j < FIELD_LINE_MAP[i].length; j++) {
						JRPrintElement line = map.get(FIELD_LINE_MAP[i][j]);
						
						if (line != null) {
							line.setY(element.getY() + ((int) (j * LINE_DISTANCE)));
						}
					}
				}
				else {
					// height is the same
					for (int j = 2; j < FIELD_LINE_MAP[i].length; j++) {
						JRPrintElement line = map.get(FIELD_LINE_MAP[i][j]);
					
						if (line != null) {
							removeElement(jasperPrint, line);
						}
					}
				}
			}
			
			return jasperPrint;
		}

		private Map<String, JRPrintElement> getElementsMap(JasperPrint jasperPrint) {
			Map<String, JRPrintElement> map = new HashMap<String, JRPrintElement>();
			
			Iterator pages = jasperPrint.getPages().iterator();
			while (pages.hasNext()) {
				JRPrintPage page = (JRPrintPage) pages.next();
				
				Iterator elements = page.getElements().iterator();
				while (elements.hasNext()) {
					JRPrintElement element = (JRPrintElement) elements.next();

					map.put(element.getKey(), element);
				}
			}

			return map;
		}
		
		private void removeElement(JasperPrint jasperPrint, JRPrintElement target) {
			Iterator pages = jasperPrint.getPages().iterator();
			while (pages.hasNext()) {
				JRPrintPage page = (JRPrintPage) pages.next();
				
				Iterator elements = page.getElements().iterator();
				while (elements.hasNext()) {
					JRPrintElement element = (JRPrintElement) elements.next();

					if (element == target) {
						elements.remove();
					}
				}
			}
		}

    }
}
