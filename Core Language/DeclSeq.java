class DeclSeq {
	Decl decl;
	FuncDecl fdecl;
	DeclSeq ds;
	
	void parse() {
		if (Parser.scanner.currentToken() == Core.ID) {
			fdecl = new FuncDecl();
			fdecl.parse();
		} else {
			decl = new Decl();
			decl.parse();
		}
		if (Parser.scanner.currentToken() != Core.BEGIN) {
			ds = new DeclSeq();
			ds.parse();
		}
	}
	
	void print(int indent) {
		if (fdecl != null) {
			fdecl.print(indent);
		} else {
			decl.print(indent);
		}
		if (ds != null) {
			ds.print(indent);
		}
	}
	
	void execute() {
		if (fdecl != null) {
			fdecl.execute();
		} else {
			decl.execute();
		}
		if (ds != null) {
			ds.execute();
		}
	}
}