class Factor {
	Id id;
	int constant;
	Expr expr;
	
	void parse() {
		if (Parser.scanner.currentToken() == Core.ID) {
			id = new Id();
			id.parse();
		} else if (Parser.scanner.currentToken() == Core.CONST) {
			constant = Parser.scanner.getCONST();
			Parser.scanner.nextToken();
		} else if (Parser.scanner.currentToken() == Core.LPAREN) {
			Parser.scanner.nextToken();
			expr = new Expr();
			expr.parse();
			Parser.expectedToken(Core.RPAREN);
			Parser.scanner.nextToken();
		} else {
			System.out.println("ERROR: Expected ID, CONST, or LPAREN, recieved " + Parser.scanner.currentToken());
			System.exit(0);
		}
	}
	
	void print() {
		if (id != null) {
			id.print();
		} else if (expr != null) {
			System.out.print("(");
			expr.print();
			System.out.print(")");
		} else {
			System.out.print(constant);
		}
	}
	
	int execute() {
		int result = constant;
		if (id != null) {
			try {
				result = (int) id.getValue();
			} catch (Exception e) {
				System.out.println("ERROR: " + id.getString() + " is null");
				System.exit(0);
			}
		} else if (expr != null) {
			result = expr.execute();
		}
		return result;
	}
}