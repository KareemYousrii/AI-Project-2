package CNF;

import java.util.ArrayList;
import java.util.List;

import FOL.*;
public class CNF {
	
	public Sentence removeEquiv(Sentence s) {
		if(s instanceof ConnectedSentence)
		{
			Sentence first = removeEquiv(((ConnectedSentence) s).getFirst());
			Sentence second = removeEquiv(((ConnectedSentence) s).getSecond());
			
			String conn = ((ConnectedSentence) s).getConnector();
			
			if (Connectors.isBICOND(conn)){
				ConnectedSentence fir = new ConnectedSentence(Connectors.IMPLIES, first, second);
				ConnectedSentence sec = new ConnectedSentence(Connectors.IMPLIES, second, first);
				
				s = new ConnectedSentence(Connectors.AND, fir, sec);
			}
		}
		return s;
	}
	
	public Sentence removeImpl(Sentence s) {
		
		if(s instanceof ConnectedSentence) {
			Sentence first = removeImpl(((ConnectedSentence) s).getFirst());
			Sentence second = removeImpl(((ConnectedSentence) s).getSecond());
			
			String conn = ((ConnectedSentence) s).getConnector();
			
			if(Connectors.isIMPLIES(conn))
			{
				s = new ConnectedSentence(Connectors.OR, new NotSentence(first), second);
			}
		}
		return s;
	}
	
	public Sentence pushNot(Sentence s) {
		/* If s is of the form S /\ S or S \/ S */
		if(s instanceof ConnectedSentence) {
			Sentence first = pushNot(((ConnectedSentence) s).getFirst());
			Sentence second = pushNot(((ConnectedSentence) s).getSecond());
		}
		
		else if(s instanceof NotSentence) {
			Sentence negated = ((NotSentence) s).getNegated();
			
			if(negated instanceof ConnectedSentence) {
				Sentence first = ((ConnectedSentence) negated).getFirst();
				Sentence second = ((ConnectedSentence) negated).getSecond();
				
				String connector = ((ConnectedSentence) negated).getConnector();
				if(Connectors.isAND(connector)) {
					s = pushNot(new ConnectedSentence(Connectors.OR, new NotSentence(first),
							new NotSentence(second)));	
				}
				
				else if (Connectors.isOR(connector)) {
					s = pushNot(new ConnectedSentence(Connectors.AND, new NotSentence(first),
							new NotSentence(second)));	
				}
			}
			
			else if(negated instanceof NotSentence) {
				s = pushNot(((NotSentence) negated).getNegated());
			}
			
			else if(negated instanceof QuantifiedSentence) {
				if(((QuantifiedSentence) negated).getQuantifier().equals("ForAll")){
					s = pushNot(new QuantifiedSentence("Exists", 
							((QuantifiedSentence) negated).getVariables(),
							((QuantifiedSentence) negated).getQuantified()));
				}
				else {
					s = pushNot(new QuantifiedSentence("ForAll", 
							((QuantifiedSentence) negated).getVariables(),
							((QuantifiedSentence) negated).getQuantified()));
				}
			}
		}
		return s;
	}
	
	public void StandardizeApart(Sentence s, int var) {
		if(s instanceof QuantifiedSentence) {
			for(Variable v: ((QuantifiedSentence) s).getVariables()) {
				v.setValue(var + "");
				var++;
			}
			
			StandardizeApart(((QuantifiedSentence) s).getQuantified(), var);
		}
		
		else if(s instanceof ConnectedSentence) {
			StandardizeApart(((ConnectedSentence) s).getFirst(), var);
			StandardizeApart(((ConnectedSentence) s).getSecond(), var);
		}
	}
}
