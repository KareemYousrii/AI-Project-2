package FOL;

import java.util.List;
/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface FOLNode {
        String getSymbolicName();

        boolean isCompound();

        List<? extends FOLNode> getArgs();

        FOLNode copy();
}