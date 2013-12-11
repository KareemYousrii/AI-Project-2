import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

import Unifier.Unifier;
import FOL.*;

/*
 *  P(x, g(x), g(f (a))) and P (f (u), v, v) 
 *  P(a,y,f(y)) and P(z,z,u)
 *	f(x, g(x), x) f(g(u), g(g(z)), z)
 */

public class Main {
	public static void main(String [] args){
		Unifier u = new Unifier();
		
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
		System.out.println("m3: " +  m3);
	}
}
