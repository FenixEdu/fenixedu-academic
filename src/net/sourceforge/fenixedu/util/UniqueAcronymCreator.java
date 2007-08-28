package net.sourceforge.fenixedu.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class UniqueAcronymCreator<T extends DomainObject> {

    private String slotName;
    private String acronymSlot;
    private Set<T> objects;
    private static boolean toLowerCase;
    private final static Logger logger = Logger.getLogger(UniqueAcronymCreator.class);
    
    public UniqueAcronymCreator(String slotName, String acronymSlot, Set<T> objects, boolean toLowerCase) throws Exception {
        this.slotName = slotName;
        this.acronymSlot = acronymSlot;
        this.objects = new TreeSet<T>(new BeanComparator(this.slotName));
        this.objects.addAll(objects);
        this.toLowerCase = toLowerCase;
        this.logger.setLevel(Level.OFF);
    }

    private Map<String, T> existingAcronyms = new HashMap<String, T>();

    private void initialize() throws Exception {
        existingAcronyms.clear();
        colisions.clear();

        for (final T object : objects) {
            String objectAcronym = (String) PropertyUtils.getProperty(object, acronymSlot);
            
            if (objectAcronym != null) {
                if (existingAcronyms.containsKey(objectAcronym)) {
                    throw new Exception("given object list doesn't have unique acronyms!");
                }
                
                existingAcronyms.put(objectAcronym, object);    
            }
        }
    }
    
    private T object;
    private static String[] splitsName;
    private Set<T> colisions = new HashSet<T>();
    
    public GenericPair<String, Set<T>> create(T object) throws Exception {
        initialize();
        
        final String slotValue = (String) PropertyUtils.getProperty(object, slotName);
        final String slotValueWithNoAccents = noAccent(slotValue);

        if (LogLevel.INFO) {
            logger.info("slotValue -> " + slotValue);
            logger.info("slotValueWithNoAccents -> " + slotValueWithNoAccents);
        }

        this.object = object;
        splitsName = slotValueWithNoAccents.split(" ");
        
        String acronym = constructBasicAcronym(new StringBuilder());
        
        if (canAccept(acronym)) {
            return new GenericPair<String, Set<T>>(acronym, colisions);
        } else {
            acronym = constructExtendedAcronym(acronym);
            
            if (canAccept(acronym)) {
                return new GenericPair<String, Set<T>>(acronym, colisions);
            } else {
                int index = 3;

                StringBuilder acronymAux = new StringBuilder(acronym.toString());
                while (!canAccept(acronym.toString()) && (index <= slotValueWithNoAccents.length())) {
                    acronymAux = appendLastChar(index, acronymAux);
                    acronym = acronymAux.toString();
                    
                    if (canAccept(acronym)) {
                        return new GenericPair<String, Set<T>>(acronym, colisions);
                    }
                    index++;
                }
            }
        }
        
        throw new Exception("unable to create acronym!");
    }

    private boolean canAccept(String acronym) {
        if (!existingAcronyms.containsKey(acronym)) {
            if (LogLevel.INFO) {
                logger.info("canAccept, true -> " + acronym);
            }
            return true;
        } else {

            if (existingAcronyms.get(acronym).equals(object)) {
                if (LogLevel.INFO) {
                    logger.info("canAccept, true -> " + acronym);
                }
                return true;
            } else {
                colisions.add(existingAcronyms.get(acronym));

                if (LogLevel.INFO) {
                    logger.info("canAccept, false -> " + acronym);
                }
                return false;
            }
        }
    }

    private static String constructBasicAcronym(StringBuilder acronym) {
        for (int i = 0; i < splitsName.length; i++) {
            if (splitsName[i].indexOf("(") == 0) {
                int closingBracketsSplit = i;
                for (; closingBracketsSplit < splitsName.length && !splitsName[closingBracketsSplit].contains(")"); closingBracketsSplit++);
                
                if (closingBracketsSplit == i) {
                    // Ex: Xpto (And) --> X-And
                    String toAppend = splitsName[i].substring(0 + 1, splitsName[i].indexOf(")"));
                    acronym.append("-").append(toAppend);
                    if (LogLevel.INFO) {
                        logger.info("constructBasicAcronym, found a '(...)', appendding " + toAppend);
                    }
                } else {
                    // Ex: Xpto (And <anything> More) --> X-And<anything>More
                    
                    // adding 'And'
                    String toAppend = splitsName[i].substring(1, splitsName[i].length());
                    
                    // adding anything in between, if not rejectfull
                    for (int iter = i + 1; iter < closingBracketsSplit; iter++) {
                        if ((!isValidRejection(splitsName[iter]) && splitsName[iter].length() >= 3) || hasNumber(splitsName[iter])) {
                            toAppend += splitsName[iter];
                        }
                    }
                    
                    // adding 'More'
                    toAppend += splitsName[closingBracketsSplit].substring(0, splitsName[closingBracketsSplit].length() - 1);
                    
                    // skipping until this split in next iteration
                    i = closingBracketsSplit + 1;
                    
                    acronym.append("-").append(toAppend);
                    if (LogLevel.INFO) {
                        logger.info("constructBasicAcronym, found a '(... ...)', appendding " + toAppend);
                    }
                }
            } else if (splitsName[i].indexOf("-") == 0) {
                // Ex: Xpto - and --> X-AND
                
                if ((i + 1 ) <= splitsName.length - 1) {
                    if (!isValidRejection(splitsName[i + 1])) {
                        // adding 'And', but limiting it to 4 chars
                        String toAppend = (splitsName[i + 1].length() < 4) ? splitsName[i + 1].toUpperCase() : String.valueOf(splitsName[i + 1].charAt(0)).toUpperCase();
                        
                        // skipping until this split in next iteration
                        i = i + 1;
                        
                        if (toAppend.length() == 1) {
                            acronym.append(toAppend);
                            if (LogLevel.INFO) {
                                logger.info("constructBasicAcronym, found a '- ...', appendding letter " + toAppend);
                            }
                        } else {
                            acronym.append("-").append(toAppend);
                            if (LogLevel.INFO) {
                                logger.info("constructBasicAcronym, found a '- ...', appendding " + toAppend);
                            }
                        }
                    } else {
                        acronym.append("-");
                        if (LogLevel.INFO) {
                            logger.info("constructBasicAcronym, found a '- ...', only appendding '-'");
                        }
                    }
                } else {
                    if (!splitsName[i].equals("-")) {
                        String toAppend = splitsName[i].substring(1, splitsName[i].length() - 1);
                        toAppend = (toAppend.length() < 4) ? toAppend.toUpperCase() : String.valueOf(toAppend.charAt(0)).toUpperCase();
                        
                        acronym.append("-").append(toAppend);
                        if (LogLevel.INFO) {
                            logger.info("constructBasicAcronym, found a '-...' at the end, appendding " + toAppend);
                        }
                    }
                }
            } else if (isValidNumeration(splitsName[i]) || hasNumber(splitsName[i])) {
                // Ex: Xpto I --> X-I
                
                acronym.append("-").append(splitsName[i].toUpperCase());
                if (LogLevel.INFO) {
                    logger.info("constructBasicAcronym, found a numeration, appendding " + splitsName[i].toUpperCase());
                }
            } else if (!isValidRejection(splitsName[i]) && splitsName[i].length() >= 3) {
                if (splitsName[i].contains("-")) {
                    // Ex: Xpto And-More --> XAM
                    
                    int index = splitsName[i].indexOf("-");
                    acronym.append(splitsName[i].charAt(0)).append(splitsName[i].charAt(index+1));
                    if (LogLevel.INFO) {
                        logger.info("constructBasicAcronym, found a '-', appendding " + splitsName[i].charAt(0) + splitsName[i].charAt(index+1));
                    }
                } else {
                    // Ex: Xpto And More --> XAM
                    
                    acronym.append(splitsName[i].charAt(0));
                    if (LogLevel.INFO) {
                        logger.info("constructBasicAcronym, appendding " + splitsName[i].charAt(0));
                    }
                }
            }
        }

        if (LogLevel.INFO) {
            logger.info("constructBasicAcronym, returning " + acronym.toString());
        }
        return acronym.toString();
    }

    private static boolean isValidRejection(String string) {
        if (rejectionSet.contains(StringUtils.lowerCase(string))) {
            if (LogLevel.INFO) {
                logger.info("isValidRejection, true -> " + string);
            }
            return true;
        }

        if (LogLevel.INFO) {
            logger.info("isValidRejection, false -> " + string);
        }
        return false;
    }

    private static boolean isValidNumeration(String string) {
        if (numerationSet.contains(StringUtils.upperCase(string))) {
            if (LogLevel.INFO) {
                logger.info("isValidNumeration, true -> " + string);
            }
            return true;
        }

        if (LogLevel.INFO) {
            logger.info("isValidNumeration, false -> " + string);
        }
        return false;
    }

    private static boolean hasNumber(String string) {
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);

            if (numberSet.contains(String.valueOf(c))) {
                if (LogLevel.INFO) {
                    logger.info("hasNumber, true -> " + string);
                }
                return true;
            }
        }

        if (LogLevel.INFO) {
            logger.info("hasNumber, false -> " + string);
        }
        return false;
    }
    
    private static void addAcception(String acronym) {
        if (LogLevel.INFO) {
            logger.info("addAcception, called with " + acronym);
        }
        
        StringBuilder temp = new StringBuilder(acronym);
        
        for (int i = 0; i < splitsName.length; i++) {
            if (isValidAcception(splitsName[i])) {
                temp.append(splitsName[i]);
                if (LogLevel.INFO) {
                    logger.info("addAcception, appending " + splitsName[i]);
                }
            }
        }
    }
    
    private static boolean isValidAcception(String string) {
        if (acceptSet.contains(StringUtils.upperCase(string))) {
            if (LogLevel.INFO) {
                logger.info("isValidAcception, true -> " + string);
            }
            return true;
        }
        if (LogLevel.INFO) {
            logger.info("isValidAcception, false -> " + string);
        }
        return false;
    }

    private static String constructExtendedAcronym(String basicAcronym) {
        StringBuilder extendedAcronym = new StringBuilder();
        
        int length = splitsName.length;
        if (LogLevel.INFO) {
            logger.info("constructExtendedAcronym, sliptsName length " + length);
        }
        if (length == 1) {
            extendedAcronym.append(splitsName[0].charAt(0));
            
            String toAppend = splitsName[0].substring(1, 3);
            if (LogLevel.INFO) {
                logger.info("constructExtendedAcronym, length 1, appending " + toAppend);
            }
            extendedAcronym.append(((toLowerCase) ? toAppend.toLowerCase() : toAppend.toUpperCase()));
            
            return extendedAcronym.toString();
        }

        //constructBasicAcronym();
        appendLast(extendedAcronym.append(basicAcronym));
        if (LogLevel.INFO) {
            logger.info("constructExtendedAcronym, returning " + extendedAcronym.toString());
        }
        return extendedAcronym.toString();
    }

    private static void appendLast(StringBuilder acronym) {
        if (LogLevel.INFO) {
            logger.info("appendLast, called with " + acronym);
        }
        
        for (int i = splitsName.length - 1; i > -1; i--) {
            if (!isValidAcception(splitsName[i]) && splitsName[i].length() >= 3 && !isValidNumeration(splitsName[i])) {
                String toAppend = splitsName[i].substring(1, 3);
                toAppend = (toLowerCase) ? toAppend.toLowerCase() : toAppend.toUpperCase();
                
                if (acronym.toString().contains("-")) {
                    int hiffen = acronym.toString().indexOf("-");
                    if ((hiffen+1 < acronym.toString().length()) && (isValidNumeration(String.valueOf(acronym.charAt(hiffen + 1)))
                            || hasNumber(String.valueOf(acronym.charAt(hiffen + 1))))) {
                        acronym.insert(hiffen, toAppend);
                        if (LogLevel.INFO) {
                            logger.info("appendLast, found a '-', appending before hiffen " + toAppend);
                        } 
                    } else {
                        acronym.append(toAppend);
                        if (LogLevel.INFO) {
                            logger.info("appendLast, found a '-', appending in end " + toAppend);
                        }
                    }
                    
                } else {
                    if (LogLevel.INFO) {
                        logger.info("appendLast, appending " + toAppend);
                    }
                    acronym.append(toAppend);
                }

                break;
            }
        }
    }

    private static StringBuilder appendLastChar(int index, StringBuilder acronym) {
        if (LogLevel.INFO) {
            logger.info("appendLastChar, called with index " + index + " and " +  acronym);
        }
        
        for (int i = splitsName.length - 1; i > -1; i--) {
            if (!(isValidAcception(splitsName[i])) && splitsName[i].length() > index) {
                String toAppend = (splitsName[i].substring(index, index + 1));
                toAppend = (toLowerCase) ? toAppend.toLowerCase() : toAppend.toUpperCase();
                
                if (acronym.toString().contains("-")) {
                    int hiffen = acronym.toString().indexOf("-");
                    if (isValidNumeration(String.valueOf(acronym.charAt(hiffen + 1)))
                            || hasNumber(String.valueOf(acronym.charAt(hiffen + 1)))) {
                        acronym.insert(hiffen, toAppend);
                        if (LogLevel.INFO) {
                            logger.info("appendLastChar, found a '-', appending before hiffen " + toAppend);
                        }
                    } else {
                        acronym.append(toAppend);
                        if (LogLevel.INFO) {
                            logger.info("appendLastChar, found a '-', appending in end " + toAppend);
                        }
                    }
                } else {
                    if (LogLevel.INFO) {
                        logger.info("appendLastChar, appending " + toAppend);
                    }
                    acronym.append(toAppend);
                }

                break;
            }
        }

        return acronym;
    }

    private static Set<String> rejectionSet = new HashSet<String>();
    private static Set<String> acceptSet = new HashSet<String>();
    private static Set<String> numberSet = new HashSet<String>();
    private static Set<String> numerationSet = new HashSet<String>();
    static {
        rejectionSet.add("às");
        rejectionSet.add("à");
        rejectionSet.add("com");
        rejectionSet.add("sobre");
        rejectionSet.add("de");
        rejectionSet.add("e");
        rejectionSet.add("para");
        rejectionSet.add("em");
        rejectionSet.add("do");
        rejectionSet.add("dos");
        rejectionSet.add("da");
        rejectionSet.add("das");
        rejectionSet.add("na");
        rejectionSet.add("no");
        rejectionSet.add("nas");
        rejectionSet.add("nos");
        rejectionSet.add("por");
        rejectionSet.add("aos");
        rejectionSet.add("ao");
        rejectionSet.add("a)");
        rejectionSet.add("b)");
        rejectionSet.add("c)");
        rejectionSet.add("d)");
        rejectionSet.add("e)");
        rejectionSet.add("(m)");
        rejectionSet.add("(m/l)");
        rejectionSet.add("(d/m)");
        rejectionSet.add("(m/d)");
        rejectionSet.add("(a)");
        rejectionSet.add("(p)");
        rejectionSet.add("(md)");
        rejectionSet.add("(sie)");
        rejectionSet.add("(sm)");
        rejectionSet.add("(taguspark)");
        rejectionSet.add("");

        acceptSet.add("-");
        acceptSet.add("B");
        acceptSet.add("C");
        acceptSet.add("A)");
        acceptSet.add("B)");
        acceptSet.add("C)");
        acceptSet.add("D)");
        acceptSet.add("E)");
        acceptSet.add("(M)");
        acceptSet.add("(M/L)");
        acceptSet.add("(D/M)");
        acceptSet.add("(M/D)");
        acceptSet.add("(A)");
        acceptSet.add("(P)");
        acceptSet.add("(MD)");
        acceptSet.add("(SM)");
        acceptSet.add("(SIE)");
        acceptSet.add("(TAGUSPARK)");
        
        numberSet.add("1");
        numberSet.add("2");
        numberSet.add("3");
        numberSet.add("4");
        numberSet.add("5");
        numberSet.add("6");
        numberSet.add("7");
        numberSet.add("8");
        numberSet.add("9");
        numberSet.add("10");
        
        numerationSet.add("I");
        numerationSet.add("II");
        numerationSet.add("III");
        numerationSet.add("IV");
        numerationSet.add("V");
        numerationSet.add("VI");
        numerationSet.add("VII");
        numerationSet.add("VIII");
        numerationSet.add("IX");
        numerationSet.add("X");
        numerationSet.add("XX");
        numerationSet.add("XXI");
    }

    private static StringBuilder sbna = null;
    private static String noAccent(String ptxt) {
        if (sbna == null) {
            sbna = new StringBuilder();
        } else {
            sbna.setLength(0);
        }

        if (ptxt == null) {
            return null;
        }

        for (int i = 0; i < ptxt.length(); ++i) {
            char c = ptxt.charAt(i);

            switch (c) {
            case 'Ã':
                sbna.append('A');
                break;
            case 'À':
                sbna.append('A');
                break;
            case 'Á':
                sbna.append('A');
                break;
            case 'Â':
                sbna.append('A');
                break;
            case 'Ä':
                sbna.append('A');
                break;
            case 'Å':
                sbna.append('A');
                break;
            case 'à':
                sbna.append('a');
                break;
            case 'á':
                sbna.append('a');
                break;
            case 'â':
                sbna.append('a');
                break;
            case 'ã':
                sbna.append('a');
                break;
            case 'ä':
                sbna.append('a');
                break;
            case 'å':
                sbna.append('a');
                break;
            case 'Ç':
                sbna.append('C');
                break;
            case 'ç':
                sbna.append('c');
                break;
            case 'È':
                sbna.append('E');
                break;
            case 'É':
                sbna.append('E');
                break;
            case 'Ê':
                sbna.append('E');
                break;
            case 'Ë':
                sbna.append('E');
                break;
            case 'è':
                sbna.append('e');
                break;
            case 'é':
                sbna.append('e');
                break;
            case 'ê':
                sbna.append('e');
                break;
            case 'ë':
                sbna.append('e');
                break;
            case 'Ì':
                sbna.append('I');
                break;
            case 'Í':
                sbna.append('I');
                break;
            case 'Î':
                sbna.append('I');
                break;
            case 'Ï':
                sbna.append('I');
                break;
            case 'ì':
                sbna.append('i');
                break;
            case 'í':
                sbna.append('i');
                break;
            case 'î':
                sbna.append('i');
                break;
            case 'ï':
                sbna.append('i');
                break;
            case 'Ñ':
                sbna.append('N');
                break;
            case 'ñ':
                sbna.append('n');
                break;
            case 'Ò':
                sbna.append('O');
                break;
            case 'Ó':
                sbna.append('O');
                break;
            case 'Ô':
                sbna.append('O');
                break;
            case 'Õ':
                sbna.append('O');
                break;
            case 'Ö':
                sbna.append('O');
                break;
            case 'ò':
                sbna.append('o');
                break;
            case 'ó':
                sbna.append('o');
                break;
            case 'ô':
                sbna.append('o');
                break;
            case 'õ':
                sbna.append('o');
                break;
            case 'ö':
                sbna.append('o');
                break;
            case 'Ù':
                sbna.append('U');
                break;
            case 'Ú':
                sbna.append('U');
                break;
            case 'Û':
                sbna.append('U');
                break;
            case 'Ü':
                sbna.append('U');
                break;
            case 'ù':
                sbna.append('u');
                break;
            case 'ú':
                sbna.append('u');
                break;
            case 'û':
                sbna.append('u');
                break;
            case 'ü':
                sbna.append('u');
                break;
            case 'Ý':
                sbna.append('Y');
                break;
            case 'ý':
                sbna.append('Y');
                break;
            case 'ÿ':
                sbna.append('y');
                break;

            case ',':
                sbna.append(' ');
                break;

            case ':':
                sbna.append(" - ");
                break;

            case '-':
                sbna.append(" -");
                break;

            case 'º':
                sbna.append(' ');
                break;

            case 'ª':
                sbna.append(' ');
                break;

            case '/':
                sbna.append(' ');
                break;

            case '.':
                sbna.append(' ');
                break;

            case '(':
                sbna.append(" (");
                break;

            default:
                sbna.append(c);
            }
        }

        return sbna.toString();
    }

}
