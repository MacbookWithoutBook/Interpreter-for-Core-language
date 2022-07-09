class Decl implements Stmt {
	DeclInt declInt;
	DeclRef declRef;
	
	public void parse() {
		if (Parser.scanner.currentToken() == Core.INT) {
			declInt = new DeclInt();
			declInt.parse();
		} else {
			declRef = new DeclRef();
			declRef.parse();
		}
	}
	
	public void print(int indent) {
		if (declInt != null) {
			declInt.print(indent);
		} else {
			declRef.print(indent);
		}
	}
	
	public void execute() {
		if (declInt != null) {
			declInt.execute();
		} else {
			declRef.execute();
		}
	}
}