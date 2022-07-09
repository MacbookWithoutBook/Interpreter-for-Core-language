class Cond {
	Cmpr cmpr;
	Cond cond;
	
	void parse() {
		if (Parser.scanner.currentToken() == Core.NEGATION){
			Parser.scanner.nextToken();
			Parser.expectedToken(Core.LPAREN);
			Parser.scanner.nextToken();
			cond = new Cond();
			cond.parse();
			Parser.expectedToken(Core.RPAREN);
			Parser.scanner.nextToken();
		} else {
			cmpr = new Cmpr();
			cmpr.parse();
			if (Parser.scanner.currentToken() == Core.OR) {
				Parser.scanner.nextToken();
				cond = new Cond();
				cond.parse();
			}
		}
	}
	
	void print() {
		if (cmpr == null) {
			System.out.print("!(");
			cond.print();
			System.out.print(")");
		} else {
			cmpr.print();
			if (cond != null) {
				System.out.print(" or ");
				cond.print();
			}
		}
	}
	
	boolean execute() {
		boolean result = false;
		if (cmpr == null) {
			result = !cond.execute();
		} else {
			result = cmpr.execute();
			if (cond != null) {
				result = result || cond.execute();
			}
		}
		return result;
	}
}