class Term {
	Factor factor;
	Term term;
	
	void parse() {
		factor = new Factor();
		factor.parse();
		if (Parser.scanner.currentToken() == Core.MULT) {
			Parser.scanner.nextToken();
			term = new Term();
			term.parse();
		}				
	}
	
	void print() {
		factor.print();
		if (term != null) {
			System.out.print("*");
			term.print();
		}
	}
	
	int execute() {
		int result = factor.execute();
		if (term != null) {
			result = result * term.execute();
		}
		return result;
	}
}