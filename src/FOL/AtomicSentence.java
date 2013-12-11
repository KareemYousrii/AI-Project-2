package FOL;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface AtomicSentence extends Sentence {
        List<Term> getArgs();

        AtomicSentence copy();
}
