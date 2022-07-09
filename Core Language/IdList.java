class IdList {
	Id id;
	IdList list;
	
	void parse() {
		id = new Id();
		id.parse();
		if (Parser.scanner.currentToken() == Core.COMMA) {
			Parser.scanner.nextToken();
			list = new IdList();
			list.parse();
		} 
	}
	
	void print() {
		id.print();
		if (list != null) {
			System.out.print(",");
			list.print();
		}
	}
	
	void executeIntIdList() {
		id.executeIntAllocate();
		if (list != null) {
			list.executeIntIdList();
		}
	}
	
	void executeRefIdList() {
		id.executeRefAllocate();
		if (list != null) {
			list.executeRefIdList();
		}
	}
}