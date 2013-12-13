package FOL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class ConnectedSentence implements Sentence {
        private String connector;
        private Sentence first, second;
        private List<Sentence> args = new ArrayList<Sentence>();
        private String stringRep = null;
        private int hashCode = 0;
        
        public ConnectedSentence(String connector) {
            this.connector = connector;
    }
        
        public ConnectedSentence(String connector, Sentence first, Sentence second) {
                this.connector = connector;
                this.first = first;
                this.second = second;
                args.add(0, first);
                args.add(1,second);
        }

        public String getConnector() {
                return connector;
        }

        public Sentence getFirst() {
        		return first;
        }

        public Sentence getSecond() {
                return second;
        }
        
        public void setConnector(String connector) {
        	this.connector = connector;
        }
        
        public void setFirst(Sentence s) {
        	args.add(0, s);
        	this.first = s;
        }
        
        public void setSecond(Sentence s) {
        	args.add(1, s);
        	this.second = s;
        }

        //
        // START-Sentence
        public String getSymbolicName() {
                return getConnector();
        }

        public boolean isCompound() {
                return true;
        }

        public List<Sentence> getArgs() {
                return args;
        }

        public ConnectedSentence copy() {
                return new ConnectedSentence(connector, first.copy(), second.copy());
        }

        // END-Sentence
        //

        @Override
        public boolean equals(Object o) {

                if (this == o) {
                        return true;
                }
                if ((o == null) || (this.getClass() != o.getClass())) {
                        return false;
                }
                ConnectedSentence cs = (ConnectedSentence) o;
                return cs.getConnector().equals(getConnector())
                                && cs.getFirst().equals(getFirst())
                                && cs.getSecond().equals(getSecond());
        }

        @Override
        public int hashCode() {
                if (0 == hashCode) {
                        hashCode = 17;
                        hashCode = 37 * hashCode + getConnector().hashCode();
                        hashCode = 37 * hashCode + getFirst().hashCode();
                        hashCode = 37 * hashCode + getSecond().hashCode();
                }
                return hashCode;
        }

        @Override
        public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("(");
                sb.append(first.toString());
                sb.append(" ");
                sb.append(connector);
                sb.append(" ");
                sb.append(second.toString());
                sb.append(")");
                stringRep = sb.toString();
                return stringRep;
        }
}
