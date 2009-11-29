package trust.model.primitives;

/**
 * Named abstract class.
 * @author cfournie
 */
public abstract class Named {
	/** Unique name (intended to be unique per subclass) */
	protected String name = "";
	
	/**
	 * Constructor
	 * @param name
	 */
	public Named(String name) {
		this.name = name;
	}
	
	/**
	 * Get unique identifier name
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Equality operator that relies upon comparison of unique name
	 */
	public boolean equals(Object o) {
		boolean equal = true;
		
		if (o != this) {
			if (o instanceof Named) {
				Named n = (Named)o;
				equal = n.name.equals(this.name);
			} else {
				equal = false;
			}
		}
		
		return equal;
	}
}
