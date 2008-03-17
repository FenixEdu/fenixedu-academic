package pt.utl.ist.propreaders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.MenuMessage;
import pt.linkare.ant.propreaders.AbstractPropertyReader;

public class RoleTypePropertyReader extends AbstractPropertyReader{

	public RoleTypePropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {
		
		return getRoleTypesString();
	}
	
	public String getRoleTypesString() throws InvalidPropertySpecException, UnsupportedEncodingException
	{
		MenuMessage menuOptionsLang=new MenuMessage();
		menuOptionsLang.setMessage(buildDefaultMessage(false));
		List<String>[] optionsAndValuesLang=null;
		optionsAndValuesLang=buildRoleTypesOptions();
		menuOptionsLang.setOptions(optionsAndValuesLang[0]);
		menuOptionsLang.setOptionValues(optionsAndValuesLang[1]);
		if(getProperty().getPropertyDefaultValue()!=null)
			return getInput().readMultipleOptionOrDefault(menuOptionsLang, getProperty().getPropertyDefaultValue());
		else
			return getInput().readMultipleOption(menuOptionsLang);
	}

	@SuppressWarnings("unchecked")
	private List<String>[] buildRoleTypesOptions() throws InvalidPropertySpecException {
		List<String>[] optionsValuesRetVal=(List<String>[])new List[]{new ArrayList<String>(RoleType.values().length),new ArrayList<String>(RoleType.values().length)};
		for(RoleType roleType:RoleType.values())
		{
			optionsValuesRetVal[0].add(roleType.getDefaultLabel());
			optionsValuesRetVal[1].add(roleType.name());
		}
		return optionsValuesRetVal;
		
	}
	
}
