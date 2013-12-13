import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Unifier.Unifier;
import FOL.*;
import CNF.*;
/*
 *  P(x, g(x), g(f (a))) and P (f (u), v, v) 
 *  P(a,y,f(y)) and P(z,z,u)
 *	f(x, g(x), x) f(g(u), g(g(z)), z)
 */

public class Main {
	
	public static void main(String [] args){
/*		Unifier u = new Unifier();
		
		Predicate p1 = new Predicate("P");
		p1.addArg(new Variable("X"));
		p1.addArg(new Function("g", new LinkedList<Term>(Arrays.asList(new Variable("X")))));
		p1.addArg(new Function("g", new LinkedList<Term>(
				Arrays.asList(new Function("f", new LinkedList<Term>(
						Arrays.asList(new Constant("a"))))))));
		
		Predicate p2 = new Predicate("P");
		p2.addArg(new Function("f", new LinkedList<Term>(
				Arrays.asList(new Constant("a")))));
		p2.addArg(new Variable("v"));
		p2.addArg(new Variable("v"));
		
		Map<Variable, Term> m1 = u.unify(p1, p2);
		
		p1 = new Predicate("P");
		p1.addArg(new Constant("a"));
		p1.addArg(new Variable("y"));
		p1.addArg(new Function("f", new LinkedList<Term>(
				Arrays.asList(new Variable("y")))));
		
		p2 = new Predicate("P");
		p2.addArg(new Variable("z"));
		p2.addArg(new Variable("z"));
		p2.addArg(new Variable("u"));
		
		Map<Variable, Term> m2 = u.unify(p1, p2);
		
		Function f1 = new Function("f");
		f1.addArg(new Variable("v"));
		f1.addArg(new Function("g", new LinkedList<Term>(
				Arrays.asList(new Variable("x")))));
		f1.addArg(new Variable("x"));
		
		Function f2 = new Function("f");
		f1.addArg(new Function("g", new LinkedList<Term>(
				Arrays.asList(new Variable("u")))));
		f1.addArg(new Function("g", new LinkedList<Term>(
				Arrays.asList(new Function("g", new LinkedList<Term>(
						Arrays.asList(new Variable("z"))))))));
		f1.addArg(new Variable("z"));
		
		Map<Variable, Term> m3 = u.unify(f1, f2);
		
		System.out.println("m1: " +  m1);
		System.out.println("m2: " +  m2);
		System.out.println("m3: " +  m3);*/
		
		/*************************** First CNF Test Case ***************************/
		//Q(X) ==> !P(X)
		ConnectedSentence s2 = new ConnectedSentence(Connectors.IMPLIES);
		s2.setFirst(new Predicate("Q", new ArrayList<Term>(Arrays.asList(new Variable("x")))));
		s2.setSecond(new NotSentence(new Predicate("P", new ArrayList<Term>(Arrays.asList(new Variable("x"))))));
		
		// ForAll x, Q(X) ==> !P(X)
		QuantifiedSentence s3 = new QuantifiedSentence("ForAll", 
				new ArrayList<Variable>(Arrays.asList(new Variable("x"))), s2);
		
		// P(X) /\ ForAll x, Q(X) ==> !P(X)
		ConnectedSentence s1 = new ConnectedSentence(Connectors.AND);
		s1.setFirst(new Predicate("P", new ArrayList<Term>(Arrays.asList(new Variable("x")))));
		s1.setSecond(s3);
		
		// Exists x [P(X) /\ ForAll x [Q(X) ==> !P(X)]]
		Sentence s = new QuantifiedSentence("Exists", new ArrayList<Variable>(Arrays.asList(new Variable("x"))), s1);
		
		CNF.removeEquiv(s);
		CNF.removeImpl(s);
		CNF.pushNot(s);
		CNF.standardizeApart(s);
		s = CNF.skolemize(s);
		s = CNF.removeUniversals(s);
		s = CNF.disToConj(s);
		s = CNF.pushOr(s);
		s = CNF.flatten(s);
		List<ArrayList<Sentence>> l = CNF.conjToList(s);
		//CNF.standardizeApartList(l);
		CNF.renameVariables(l);
		System.out.println(l);
		
		/*************************** Second CNF Test Case ***************************/
		//Q(y) /\ R(y,x)
		ConnectedSentence s4 = new ConnectedSentence(Connectors.AND,
				new Predicate("Q", new ArrayList<Term>(Arrays.asList(new Variable("x")))),
				new Predicate("R", new ArrayList<Term>(Arrays.asList(new Variable("y"), new Variable("x")))));
		
		//Exists y [Q(y) /\ R(y,x)]
		QuantifiedSentence s5 = new QuantifiedSentence("Exists", 
				new ArrayList<Variable>(Arrays.asList(new Variable("y"))), s4);
		
		//Q(x) /\ Exists y [Q(y) /\ R(y,x)]
		ConnectedSentence s6 = new ConnectedSentence(Connectors.AND,
				new Predicate("Q", new ArrayList<Term>(Arrays.asList(new Variable("x")))),
				s5);
		
		ConnectedSentence s7 = new ConnectedSentence(Connectors.BICOND,
				new Predicate("P", new ArrayList<Term>(Arrays.asList(new Variable("x")))),
				s6);
		
		Sentence s8 = new QuantifiedSentence("ForAll", 
				new ArrayList<Variable>(Arrays.asList(new Variable("x"))), 
				s7);
		
		CNF.removeEquiv(s8);
		CNF.removeImpl(s8);
		CNF.pushNot(s8);
		CNF.standardizeApart(s8);
		//System.out.println(s8);
		s8 = CNF.skolemize(s8);
		s8 = CNF.removeUniversals(s8);
		s8 = CNF.disToConj(s8);
		s8 = CNF.pushOr(s8);
		s8 = CNF.flatten(s8);
		List<ArrayList<Sentence>> l2 = CNF.conjToList(s8);
		//CNF.standardizeApartList(l2);
		CNF.renameVariables(l2);
		
		System.out.println(l2);
	}
}
