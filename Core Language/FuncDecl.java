class FuncDecl {
	Id name;
	Formals formalParams;
	StmtSeq body;
	
	void parse() {
		name = new Id();
		name.parse();
		Parser.expectedToken(Core.LPAREN);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.REF);
		Parser.scanner.nextToken();
		formalParams = new Formals();
		formalParams.parse();
		Parser.expectedToken(Core.RPAREN);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.LBRACE);
		Parser.scanner.nextToken();
		body = new StmtSeq();
		body.parse();
		Parser.expectedToken(Core.RBRACE);
		Parser.scanner.nextToken();
	}
	
	void print(int indent) {
		for (int i=0; i<indent; i++) {
			System.out.print("\t");
		}
		name.print();
		System.out.print("(ref ");
		formalParams.print();
		System.out.println(") {");
		body.print(indent+1);
		for (int i=0; i<indent; i++) {
			System.out.print("\t");
		}
		System.out.println("}");
	}
	
	void execute() {
		Executor.storeFuncDef(name, this);
	}

	Formals getFormalParams() {
		return formalParams;
	}
	
	StmtSeq getBody() {
		return body;
	}
}