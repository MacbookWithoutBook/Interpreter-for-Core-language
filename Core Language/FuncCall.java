
class FuncCall implements Stmt {
	Id funcName;
	Formals actualParams;
	
	public void parse() {
		Parser.scanner.nextToken();
		funcName = new Id();;
		funcName.parse();
		Parser.expectedToken(Core.LPAREN);
		Parser.scanner.nextToken();
		actualParams = new Formals();
		actualParams.parse();
		Parser.expectedToken(Core.RPAREN);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.SEMICOLON);
		Parser.scanner.nextToken();
	}
	
	public void print(int indent) {
		for (int i=0; i<indent; i++) {
			System.out.print("\t");
		}
		System.out.print("begin ");
		funcName.print(); 
		System.out.print("(");
		actualParams.print();
		System.out.println(");");
	}
	
	public void execute() {
		Formals formalParams = Executor.getFormalParams(funcName);
		StmtSeq body = Executor.getBody(funcName);
		Executor.pushFrame(formalParams, actualParams);
		body.execute();
		Executor.popFrame();		
	}
}