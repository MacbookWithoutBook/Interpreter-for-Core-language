

class If implements Stmt {
	Cond cond;
	StmtSeq ss1;
	StmtSeq ss2;
	
	public void parse() {
		Parser.scanner.nextToken();
		cond = new Cond();;
		cond.parse();
		Parser.expectedToken(Core.THEN);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.LBRACE);
		Parser.scanner.nextToken();
		ss1 = new StmtSeq();
		ss1.parse();
		Parser.expectedToken(Core.RBRACE);
		Parser.scanner.nextToken();
		if (Parser.scanner.currentToken() == Core.ELSE) {
			Parser.scanner.nextToken();
			Parser.expectedToken(Core.LBRACE);
			Parser.scanner.nextToken();
			ss2 = new StmtSeq();
			ss2.parse();
			Parser.expectedToken(Core.RBRACE);
			Parser.scanner.nextToken();
		}
	}
	
	public void print(int indent) {
		for (int i=0; i<indent; i++) {
			System.out.print("\t");
		}
		System.out.print("if ");
		cond.print();
		System.out.println(" then {");
		ss1.print(indent+1);
		if (ss2 != null) {
			for (int i=0; i<indent; i++) {
				System.out.print("	");
			}
			System.out.println("} else {");
			ss2.print(indent+1);
		}
		for (int i=0; i<indent; i++) {
			System.out.print("\t");
		}
		System.out.println("}");
	}
	
	public void execute() {
		boolean condition = cond.execute();
		Executor.pushLocalScope();
		if (condition) {
			ss1.execute();
		} else if (ss2 != null) {
			ss2.execute();
		}
		Executor.popLocalScope();
	}
}