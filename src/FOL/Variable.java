package FOL;

import java.util.List;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class Variable implements Term {
        private String value;

        public Variable(String s) {
                value = s.trim();
        }

        public String getValue() {
                return value;
        }

        public String getSymbolicName() {
                return getValue();
        }

        public boolean isCompound() {
                return false;
        }

        public List<Term> getArgs() {
                // Is not Compound, therefore should
                // return null for its arguments
                return null;
        }

        @Override
        public String toString() {
                return value;
        }

		@Override
		public String getOp() {
			// TODO Auto-generated method stub
			return null;
		}
}