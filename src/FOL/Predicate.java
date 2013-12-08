package FOL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */

public class Predicate implements FOLNode {
        private String predicateName;
        private List<Term> terms = new ArrayList<Term>();
        private String stringRep = null;

        public Predicate(String predicateName, List<Term> terms) {
                this.predicateName = predicateName;
                this.terms.addAll(terms);
        }

        public String getPredicateName() {
                return predicateName;
        }

        public List<Term> getTerms() {
                return Collections.unmodifiableList(terms);
        }
        
        public String getOp(){
        	return this.getPredicateName();
        }
        
        public String getSymbolicName() {
                return getPredicateName();
        }

        public boolean isCompound() {
                return true;
        }

        public List<Term> getArgs() {
                return getTerms();
        }

        @Override
        public boolean equals(Object o) {

                if (this == o) {
                        return true;
                }
                if (!(o instanceof Predicate)) {
                        return false;
                }
                Predicate p = (Predicate) o;
                return p.getPredicateName().equals(getPredicateName())
                                && p.getTerms().equals(getTerms());
        }

        @Override
        public String toString() {
                if (null == stringRep) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(predicateName);
                        sb.append("(");

                        boolean first = true;
                        for (Term t : terms) {
                                if (first) {
                                        first = false;
                                } else {
                                        sb.append(",");
                                }
                                sb.append(t.toString());
                        }

                        sb.append(")");
                        stringRep = sb.toString();
                }

                return stringRep;
        }

}