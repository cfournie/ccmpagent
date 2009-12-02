package learning;

public class DTAttribute {
	public String name, type;
	
	public DTAttribute() {}
	public DTAttribute(String name, String type)
	{
		this.name = name;
		this.type = type;
	}
	
	public void setName(String rhs){
		name = rhs;
	}
	
	public void setType(String rhs){
		type = rhs;
	}
}
