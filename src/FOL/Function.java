package FOL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class Function implements Term {
        private String functionName;
        private List<Term> terms = new ArrayList<Term>();
        private String stringRep = null;

        public Function(String functionName, List<Term> terms) {
                this.functionName = functionName;
                this.terms.addAll(terms);
        }

        public String getFunctionName() {
                return functionName;
        }
        
        public String getOp(){
        	return this.getFunctionName();
        }
        
        public List<Term> getArgs() {
                return Collections.unmodifiableList(terms);
        }

        public boolean isCompound() {
                return true;
        }

        @Override
        public boolean equals(Object o) {

                if (this == o) {
                        return true;
                }
                if (!(o instanceof Function)) {
                        return false;
                }

                Function f = (Function) o;

                return f.getFunctionName().equals(getFunctionName())
                                && f.getArgs().equals(getArgs());
        }

        @Override
        public String toString() {
                if (null == stringRep) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(functionName);
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