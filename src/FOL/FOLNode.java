package FOL;

import java.util.List;

public interface FOLNode {
	String getOp();
	
	boolean isCompound();
	
	List<Term> getArgs();
}
