package FOL;

import java.util.List;


/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class Constant implements Term {
        private String value;

        public Constant(String s) {
                value = s;
        }

        public String getValue() {
                return value;
        }

        public boolean isCompound() {
                return false;
        }
        
        @Override
        public boolean equals(Object o) {

                if (this == o) {
                        return true;
                }
                if (!(o instanceof Constant)) {
                        return false;
                }
                Constant c = (Constant) o;
                return c.getValue().equals(getValue());

        }
        
		@Override
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