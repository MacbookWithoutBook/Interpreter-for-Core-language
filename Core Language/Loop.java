

class Loop implements Stmt {
	Cond cond;
	StmtSeq ss;
	
	public void parse() {
		Parser.scanner.nextToken();
		cond = new Cond();;
		cond.parse();
		Parser.expectedToken(Core.LBRACE);
		Parser.scanner.nextToken();
		ss = new StmtSeq();
		ss.parse();
		Parser.expectedToken(Core.RBRACE);
		Parser.scanner.nextToken();
	}
	
	public void print(int indent) {
		for (int i=0; i<indent; i++) {
			System.out.print("\t");
		}
		System.out.print("while ");
		cond.print();
		System.out.println(" {");
		ss.print(indent+1);
		for (int i=0; i<indent; i++) {
			System.out.print("\t");
		}
		System.out.println("}");
	}
	
	public void execute() {
		boolean condition = cond.execute();
		Executor.pushLocalScope();
		while (condition) {
			Executor.pushLocalScope();
			ss.execute();
			Executor.popLocalScope();
			condition = cond.execute();
		}
		
	}
}