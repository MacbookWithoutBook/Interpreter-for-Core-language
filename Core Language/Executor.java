import java.util.*;

class CoreVar {
	Core type;
	Integer value;
	
	public CoreVar(Core varType) {
		type = varType;
		if (type == Core.INT) {
			value = 0;
		} else {
			value = null;
		}
	}
}

class Executor {
	
	static HashMap<String, CoreVar> globalSpace;
	static ArrayList<Integer> heapSpace;
	static ArrayList<Integer> referenceCount;
	static Scanner dataFile;
	
	// stackSpace is now our call stack
	static Stack<Stack<HashMap<String, CoreVar>>> stackSpace;
	
	// This will store all FuncDecls so we can look up the function being called
	static HashMap<String, FuncDecl> funcDefinitions;
	
	/*
	Overriding some methods from the super class to handle the call stack
	*/
	
	static void initialize(String dataFileName) {
		globalSpace = new HashMap<String, CoreVar>();
		heapSpace = new ArrayList<Integer>();
		referenceCount = new ArrayList<Integer>();
		dataFile = new Scanner(dataFileName);
		stackSpace = new Stack<Stack<HashMap<String, CoreVar>>>();
		funcDefinitions = new HashMap<String, FuncDecl>();
	}
	
	static void pushLocalScope() {
		stackSpace.peek().push(new HashMap<String, CoreVar>());
	}
	
	static void popLocalScope() {
		// Get the list of values of poped vairables
		ArrayList<CoreVar> popedVariables = new ArrayList<CoreVar>(stackSpace.peek().pop().values());
		updateRCByPopedVariables(popedVariables);
	}
	
	static int getNextData() {
		int data = 0;
		if (dataFile.currentToken() == Core.EOS) {
			System.out.println("ERROR: data file is out of values!");
			System.exit(0);
		} else {
			data = dataFile.getCONST();
			dataFile.nextToken();
		}
		return data;
	}
	
	static void allocate(String identifier, Core varType) {
		CoreVar record = new CoreVar(varType);
		// If we are in the DeclSeq, no frames will have been created yet
		if (stackSpace.size()==0) {
			globalSpace.put(identifier, record);
		} else {
			stackSpace.peek().peek().put(identifier, record);
		}
	}
	
	static CoreVar getStackOrStatic(String identifier) {
		CoreVar record = null;
		for (int i=stackSpace.peek().size() - 1; i>=0; i--) {
			if (stackSpace.peek().get(i).containsKey(identifier)) {
				record = stackSpace.peek().get(i).get(identifier);
				break;
			}
		}
		if (record == null) {
			record = globalSpace.get(identifier);
		}
		return record;
	}
	
	static void heapAllocate(String identifier) {
		CoreVar x = getStackOrStatic(identifier);
		if (x.type != Core.REF) {
			System.out.println("ERROR: " + identifier + " is not of type ref, cannot perform \"new\"-assign!");
			System.exit(0);
		}
		x.value = heapSpace.size();
		heapSpace.add(null);
		
		// add new position to refernceCount, which keeps track of the pointers of corresponding position in heapSpace
		referenceCount.add(null);
		referenceCount.set(heapSpace.size() - 1, 1);
		// print gc:n each time a new heap space is allocated
		//System.out.println("[print in alllocate]gc:" + numOfReachableValues());
	}
	
	static Core getType(String identifier) {
		CoreVar x = getStackOrStatic(identifier);
		return x.type;
	}
	
	static Integer getValue(String identifier) {
		CoreVar x = getStackOrStatic(identifier);
		Integer value = x.value;
		if (x.type == Core.REF) {
			try {
				value = heapSpace.get(value);
			} catch (Exception e) {
				System.out.println("ERROR: invalid heap read attempted!");
				System.exit(0);
			}
		}
		return value;
	}
	
	static void storeValue(String identifier, int value) {
		CoreVar x = getStackOrStatic(identifier);
		if (x.type == Core.REF) {
			try {
				heapSpace.set(x.value, value);
			} catch (Exception e) {
				System.out.println("ERROR: invalid heap write attempted!");
				System.exit(0);
			}
		} else {
			x.value = value;
		}
	}
	
	// id = share id;
	static void referenceCopy(String var1, String var2) {
		//System.out.println("Enter reference copy");
		CoreVar x = getStackOrStatic(var1); // copyTo
		CoreVar y = getStackOrStatic(var2); // copyFrom

		// remove what x previously points to
		if (x.value != null) {
			referenceCount.set(x.value, referenceCount.get(x.value) - 1);
		}
		x.value = y.value;

		// add what x now points to
		if (y.value != null) {
			referenceCount.set(y.value, referenceCount.get(y.value) + 1);
		}
		//System.out.println("[print in share]gc:" + numOfReachableValues());
	}
	
	static void storeFuncDef(Id name, FuncDecl definition) {
		funcDefinitions.put(name.getString(), definition);
	}
	
	static Formals getFormalParams(Id name) {
		if (!funcDefinitions.containsKey(name.getString())) {
			System.out.println("ERROR: Function call " + name.getString() + " has no target!");
			System.exit(0);
		}
		return funcDefinitions.get(name.getString()).getFormalParams();
	}
	
	static StmtSeq getBody(Id name) {
		return funcDefinitions.get(name.getString()).getBody();
	}
	
	static void pushFrame() {
		stackSpace.push(new Stack<HashMap<String, CoreVar>>());
		pushLocalScope();
	}
	
	static void pushFrame(Formals formalParams, Formals actualParams) {
		List<String> formals = formalParams.execute();
		List<String> actuals = actualParams.execute();
		
		Stack<HashMap<String, CoreVar>> newFrame = new Stack<HashMap<String, CoreVar>>();
		newFrame.push(new HashMap<String, CoreVar>());
		
		for (int i=0; i<formals.size(); i++) {
			CoreVar temp = new CoreVar(Core.REF);
			temp.value = getStackOrStatic(actuals.get(i)).value;

			// update referenceCount[i] when more ref variables pointing to it
			referenceCount.set(temp.value, referenceCount.get(temp.value) + 1);
			newFrame.peek().put(formals.get(i), temp);
		}
		
		stackSpace.push(newFrame);
		pushLocalScope();
	}
	
	static void popFrame() {
		Stack<HashMap<String, CoreVar>> popedFrame = stackSpace.pop();
		while (!popedFrame.isEmpty()) {
			// get all keys of tempMap, convert it to an array
			ArrayList<CoreVar> popedVariables = new ArrayList<CoreVar>(popedFrame.pop().values());
			updateRCByPopedVariables(popedVariables);
		}
	}

	// print current number of reachable variables on heap
	static int numOfReachableValues () {
		int num = 0;
		for (int i = 0; i < referenceCount.size(); i++) {
			// referenceCount[i] = 0 means heapSpace[i] is unreachable
			if (referenceCount.get(i) != 0) {
				num++;
			}
		}
		return num;
	}

	// This function updates referenceCount after the globalSpace is cleared
	static void dealGolbalSpace() {
		boolean containsREF = false;
		ArrayList<CoreVar> popedVariables = new ArrayList<CoreVar>(globalSpace.values());

		// check whether globalSpace contains REF variables
		for (int i = 0; i < popedVariables.size(); i++) {
			if (popedVariables.get(i) != null && popedVariables.get(i).type == Core.REF) {
				containsREF = true;
			}
		}

		// if globalSpace contains REF variables, update referenceCount
		if (containsREF) {
			updateRCByPopedVariables(popedVariables);
		}
	}

	// This function update referenceCount by REF variables in popedVariables
	static void updateRCByPopedVariables (ArrayList<CoreVar> popedVariables) {
		for (int i = 0; i < popedVariables.size(); i++) {
			// if a CoreVar has type REF and it is poped, decrement corresponding value in referenceCount by 1
			if (popedVariables.get(i).value != null && popedVariables.get(i).type == Core.REF) {
				referenceCount.set(popedVariables.get(i).value, referenceCount.get(popedVariables.get(i).value) - 1);
			}
			// report gc:n each time heapSpace[i] becomes 0
			if (popedVariables.get(i).value != null && referenceCount.get(popedVariables.get(i).value) == 0) {
				System.out.println("gc:" + numOfReachableValues());
			}
		}
	}

}
