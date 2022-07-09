class Main {
	public static void main(String[] args) {
		// Initialize the scanner with the input file
		Scanner S = new Scanner(args[0]);
		Parser.scanner = S;
		
		Program prog = new Program();
		
		prog.parse();
		
		//prog.print();
		
		prog.execute(args[1]);
	}
}