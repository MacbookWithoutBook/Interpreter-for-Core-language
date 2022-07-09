class Id {
	String identifier;
	
	void parse() {
		Parser.expectedToken(Core.ID);
		identifier = Parser.scanner.getID();
		Parser.scanner.nextToken();
	}
	
	void print() {
		System.out.print(identifier);
	}
	
	// Returns the string value of the Id
	String getString() {
		return identifier;
	}
	
	// Finds the stored value of the variable
	Integer getValue() {
		return Executor.getValue(identifier);
	}
	
	// Stores the passed value to the variable, used to handle regular assign
	void storeValue(int value) {
		Executor.storeValue(identifier, value);
	}
	
	// Called by assign to handle "class"-assign
	void referenceCopy(Id copyFrom) {
		Executor.referenceCopy(identifier, copyFrom.getString());
	}
	
	// Called by assign to handle "new"-assign
	void heapAllocate() {
		Executor.heapAllocate(identifier);
	}
	
	// Called when declaring an int variable
	void executeIntAllocate() {
		Executor.allocate(identifier, Core.INT);
	}
	
	// Called when declaring a class variable
	void executeRefAllocate() {
		Executor.allocate(identifier, Core.REF);
	}
}