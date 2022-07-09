import java.util.*;

class Formals {
	Id id;
	Formals list;
	
	void parse() {
		id = new Id();
		id.parse();
		if (Parser.scanner.currentToken() == Core.COMMA) {
			Parser.scanner.nextToken();
			list = new Formals();
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
	
	List<String> execute() {
		List<String> strings;
		if (list == null) {
			strings = new ArrayList<String>();
		} else {
			strings = list.execute();
		}
		strings.add(id.getString());
		return strings;
	}
}