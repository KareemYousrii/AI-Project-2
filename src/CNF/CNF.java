package CNF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import FOL.*;
public class CNF {
	
	@SuppressWarnings("unchecked")
	public static void removeEquiv(Sentence s) {
		if(s instanceof ConnectedSentence)
		{
			for(Sentence s1: (List<Sentence>)s.getArgs()) {
				removeEquiv(s1);
			}
			
			String conn = ((ConnectedSentence) s).getConnector();
			Sentence first = ((ConnectedSentence) s).getFirst();
			Sentence second = ((ConnectedSentence) s).getSecond();
			
			if (Connectors.isBICOND(conn)){
				ConnectedSentence fir = new ConnectedSentence(Connectors.IMPLIES, first, second);
				ConnectedSentence sec = new ConnectedSentence(Connectors.IMPLIES, second, first);
				
				((ConnectedSentence) s).setFirst(fir);
				((ConnectedSentence) s).setSecond(sec);
				((ConnectedSentence) s).setConnector(Connectors.AND);
			}
		}
		
		else if (s instanceof NotSentence || s instanceof QuantifiedSentence) {
			for(Sentence s1: (List<Sentence>)s.getArgs()) {
				removeEquiv(s1);
			}
		}
	}
	
	public static void removeImpl(Sentence s) {
		if(s instanceof ConnectedSentence) {
			for(Sentence s1: (List<Sentence>)s.getArgs()) {
				removeImpl(s1);
			}
			
			String conn = ((ConnectedSentence) s).getConnector();
			Sentence first = ((ConnectedSentence) s).getFirst();
			
			if(Connectors.isIMPLIES(conn))
			{
				((ConnectedSentence) s).setFirst(new NotSentence(first));
				((ConnectedSentence) s).setConnector(Connectors.OR);
			}
		}
		
		else if (s instanceof NotSentence || s instanceof QuantifiedSentence) {
			for(Sentence s1: (List<Sentence>)s.getArgs()) {
				removeImpl(s1);
			}
		}
	}

	
	public static void pushNot(Sentence s) {
		if(s instanceof NotSentence) {
			pushNot(((NotSentence) s).getNegated());
			applyNegation(((NotSentence) s).getNegated());
		}
		
		else if (s instanceof ConnectedSentence || s instanceof QuantifiedSentence) {
			for(Sentence s1: (List<Sentence>)s.getArgs()) {
				pushNot(s1);
			}
		}
	}
	
	public static void applyNegation(Sentence s) {
		if(s instanceof QuantifiedSentence) {
			if(((QuantifiedSentence) s).getQuantifier().equals("ForAll")) {
				((QuantifiedSentence) s).setQuantifier("Exists");
			}
			
			else {
				((QuantifiedSentence) s).setQuantifier("ForAll");
			}
		}
		
		else if (s instanceof ConnectedSentence) {
			if(((ConnectedSentence) s).getConnector().equals(Connectors.AND)) {
				((ConnectedSentence) s).setConnector(Connectors.OR);
			}
			
			else if(((ConnectedSentence) s).getConnector().equals(Connectors.OR)) {
				((ConnectedSentence) s).setConnector(Connectors.AND);
			}
		}
		
		else if(s instanceof NotSentence) {
			Sentence negated = ((NotSentence) s).getNegated();
			if(negated instanceof ConnectedSentence) {
				Sentence first = ((ConnectedSentence) negated).getFirst();
				Sentence second = ((ConnectedSentence) negated).getSecond();
				String connector = ((ConnectedSentence) negated).getConnector();
				
				s = new ConnectedSentence(connector, first, second);
			}
		}
		
		else if(s instanceof Predicate) {
			s = new NotSentence(s);
		}
	}
	
	public static void standardizeApart(Sentence s) {
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

	
	public static void standardizeApart(Sentence s, Stack <String> stack) {
		if(s instanceof QuantifiedSentence) {
			for(Variable v: ((QuantifiedSentence) s).getVariables())
				v.setValue(stack.pop() + "");
			
			standardizeApart(((QuantifiedSentence) s).getQuantified(), stack);
		}
		
		else if(s instanceof ConnectedSentence || s instanceof NotSentence) {
			for(Sentence s1: (List<Sentence>)s.getArgs()) {
				standardizeApart(s1, stack);
			}
		}
	}
	
	public static Sentence skolemize(Sentence s) {
		
		List <Variable> universal = new ArrayList <Variable> ();
		Map <Variable, Term> existential = new LinkedHashMap<Variable, Term>();
		return skolemize(s, universal, existential, 1);
	}
	
	public static Sentence skolemize(Sentence s, List <Variable> universal, Map <Variable, Term> existential, int num) {
		if(s instanceof QuantifiedSentence) {
			if(((QuantifiedSentence) s).getQuantifier().equals("ForAll")) {
				universal.add(((QuantifiedSentence) s).getVariables().get(0));
			}
			else {
				if(universal.size() == 0) {
					existential.put(((QuantifiedSentence) s).getVariables().get(0), new Constant("a" + num++));
				}
				else {
					Function f = new Function("f" + num++);
					for(Variable v: universal) {
						f.addArg(v);
					}
					existential.put(((QuantifiedSentence) s).getVariables().get(0), f);
				}
			}
			Sentence quantified = ((QuantifiedSentence) s).getQuantified();
			((QuantifiedSentence) s).setQuantified(skolemize(quantified, universal, existential, num));
		}
		
		else if (s instanceof ConnectedSentence) {
			Sentence first = skolemize(((ConnectedSentence) s).getFirst(), universal, existential, num);
			Sentence second = skolemize(((ConnectedSentence) s).getSecond(), universal, existential, num);
			
			((ConnectedSentence) s).setFirst(first);
			((ConnectedSentence) s).setSecond(second);
		}
		
		else if (s instanceof NotSentence) {
			s = skolemize(((NotSentence) s).getNegated(), universal, existential, num);
		}
		
		else if(s instanceof Predicate) {
			Predicate s1 = new Predicate(((Predicate) s).getPredicateName());
			for(Term t: (ArrayList <Term>)s.getArgs()) {
				s1.addArg(skolemize(t, existential));
			}
			return s1;
		}
		return s;
	}
	
	public static Term skolemize(Term t,  Map <Variable, Term> existential) {
		if(t instanceof Variable) {
			if(existential.containsKey(t)) {
				t = existential.get(t);
			}
		}
		else if (t instanceof Function) {
			Function f1 = new Function(((Function) t).getFunctionName());
			for(Term t2: (ArrayList <Term>)t.getArgs()){
				f1.addArg(skolemize(t2, existential));
			}
		}
		return t;
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
	
//	public Sentence disToConj(Sentence s) {
//		if(s instanceof ConnectedSentence) {
//			if(((ConnectedSentence) s).getConnector().equals(Connectors.OR)) {
//				if(((ConnectedSentence) s).getSecond() instanceof ConnectedSentence 
//						&& ((ConnectedSentence) s).getFirst() instanceof Predicate) {
//					ConnectedSentence second = (ConnectedSentence) (((ConnectedSentence) s)
//							.getSecond());
//					if(second.getConnector().equals(Connectors.AND)) {
//						Predicate first = (Predicate)((ConnectedSentence) s).getFirst();
//						if(second.getFirst() instanceof Predicate 
//								&& second.getSecond() instanceof Predicate) {
//							ConnectedSentence firstdisj = 
//									new ConnectedSentence(Connectors.OR, first, second.getFirst());
//							ConnectedSentence seconddisj = 
//									new ConnectedSentence(Connectors.OR, first, second.getSecond());
//							s = new ConnectedSentence(Connectors.AND, firstdisj, seconddisj);
//						}
//					}
//				}
//			}
//		}
//	}
//	
//	public List <Sentence> disjToList(Sentence s)
//	{
//		List <Sentence> disj = new ArrayList <Sentence> ();
//		disjToList(disj, s);
//	}
//	
//	public List <Sentence> disjToList(List<Sentence> disj, Sentence s)
//	{
//		if(s instanceof ConnectedSentence)
//		{
//			ConnectedSentence temp = (ConnectedSentence) s;
//			if(temp.getConnector().equals(Connectors.AND))
//			{
//				disj.add(temp.getFirst());
//			}
//			return disjToList(disj, temp.getSecond());
//		}
//		return disj;
//	}
//	
//	public List<List<Sentence>> conjToList(List<Sentence> conj)
//	{
//		List<ArrayList<Sentence>> ret = new ArrayList< ArrayList <Sentence> >();
//		for(int i = 0; i < conj.size(); i++)
//		{
//			ArrayList<Sentence> sentences = new ArrayList<Sentence>();
//			if(conj.get(i) instanceof ConnectedSentence)
//			{
//				ConnectedSentence temp = (ConnectedSentence) conj.get(i);
//				sentences.add(temp.getFirst());
//				while(temp.getSecond() instanceof ConnectedSentence)
//				{
//					temp = (ConnectedSentence) temp.getSecond();
//					sentences.add(temp.getFirst());
//				}
//			}
//			ret.add(sentences);
//		}
//	}
	
	
	
}

