package CNF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
	
	public void standardizeApart(Sentence s) {
		Stack <String> stack = new Stack <String> ();
		stack.push("t");
		stack.push("u");
		stack.push("v");
		stack.push("w");
		stack.push("x");
		stack.push("y");
		stack.push("z");
		standardizeApart(s, stack);
	}

	
	public void standardizeApart(Sentence s, Stack <String> stack) {
		if(s instanceof QuantifiedSentence) {
			for(Variable v: ((QuantifiedSentence) s).getVariables())
				v.setValue(stack.pop() + "");
			
			standardizeApart(((QuantifiedSentence) s).getQuantified(), stack);
		}
		
		else if(s instanceof ConnectedSentence) {
			standardizeApart(((ConnectedSentence) s).getFirst(), stack);
			standardizeApart(((ConnectedSentence) s).getSecond(), stack);
		}
	}
	
	public void skolemize(Sentence s) {
		
		Stack <Constant> stack = new Stack <Constant> ();
		stack.push(new Constant("a"));
		stack.push(new Constant("b"));
		stack.push(new Constant("c"));
		stack.push(new Constant("d"));
		stack.push(new Constant("e"));
		
		List <Variable> universal = new ArrayList <Variable> ();
		Map <Variable, Term> existential = new LinkedHashMap<Variable, Term>();
		skolemize(s, universal, existential);
	}
	
	
	public void skolemize(Sentence s, List <Variable> universal, Map <Variable, Term> existential, stack) {
		if(s instanceof QuantifiedSentence) {
			if(((QuantifiedSentence) s).getQuantifier().equals("ForAll")) {
				
				universal.add(((QuantifiedSentence) s).getVariables().get(0));
				
				skolemize(((QuantifiedSentence) s).getQuantified(), universal, existential);
			}
			
			else {
				existential.put(((QuantifiedSentence) s).getVariables().get(0), 
			}
		}
		
		else {
			for(Sentence s1: (List<Sentence>)s.getArgs()) {
				if (s1 instanceof Variable) {
					((Variable) s1).setValue();
				}
					
			}
		}
	}

	public Sentence removeUniversals(Sentence s) {
		if(s instanceof QuantifiedSentence) {
			s = removeUniversals(((QuantifiedSentence) s).getQuantified());
		}
		
		if(s instanceof ConnectedSentence)
		{
			((ConnectedSentence) s).setFirst(removeUniversals(((ConnectedSentence) s).getFirst()));
			((ConnectedSentence) s).setSecond(removeUniversals(((ConnectedSentence) s).getSecond()));
		}
		return s;
	}
	
	public Sentence disToConj(Sentence s) {
		if(s instanceof ConnectedSentence) {
			if(((ConnectedSentence) s).getConnector().equals(Connectors.OR)) {
				if(((ConnectedSentence) s).getSecond() instanceof ConnectedSentence 
						&& ((ConnectedSentence) s).getFirst() instanceof Predicate) {
					ConnectedSentence second = (ConnectedSentence) (((ConnectedSentence) s)
							.getSecond());
					if(second.getConnector().equals(Connectors.AND)) {
						Predicate first = (Predicate)((ConnectedSentence) s).getFirst();
						if(second.getFirst() instanceof Predicate 
								&& second.getSecond() instanceof Predicate) {
							ConnectedSentence firstdisj = 
									new ConnectedSentence(Connectors.OR, first, second.getFirst());
							ConnectedSentence seconddisj = 
									new ConnectedSentence(Connectors.OR, first, second.getSecond());
							s = new ConnectedSentence(Connectors.AND, firstdisj, seconddisj);
						}
					}
				}
			}
		}
	}
	
	public List <Sentence> disjToList(Sentence s)
	{
		List <Sentence> disj = new ArrayList <Sentence> ();
		disjToList(disj, s);
	}
	
	public List <Sentence> disjToList(List<Sentence> disj, Sentence s)
	{
		if(s instanceof ConnectedSentence)
		{
			ConnectedSentence temp = (ConnectedSentence) s;
			if(temp.getConnector().equals(Connectors.AND))
			{
				disj.add(temp.getFirst());
			}
			return disjToList(disj, temp.getSecond());
		}
		return disj;
	}
	
	public List<List<Sentence>> conjToList(List<Sentence> conj)
	{
		List<ArrayList<Sentence>> ret = new ArrayList< ArrayList <Sentence> >();
		for(int i = 0; i < conj.size(); i++)
		{
			if(conj.get(i) instanceof ConnectedSentence)
			{
				ConnectedSentence temp = (ConnectedSentence) conj.get(i);
				ArrayList<Sentence> sentences = new ArrayList<Sentence>();
				sentences.add(temp.getFirst());
				while(temp.getSecond() instanceof ConnectedSentence)
				{
					temp = (ConnectedSentence) temp.getSecond();
					sentences.add(temp.getFirst());
				}
			}
			ret.add(sentences);
		}
	}
	
	
	
}

