package FOL;

import java.util.List;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface Term extends FOLNode {
        List<Term> getArgs();
}
