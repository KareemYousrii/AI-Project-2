package Unifier;

import FOL.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Unifier
{
	
	public Unifier() {
		
	}
	
	/* Attempts to unify two First Order Logic sentences.
	 * Return a MGU theta which unifies the two sentences.
	 */
	public Map<Variable, Term> unify(FOLNode x, FOLNode y)
	{
		return unify(x, y, new LinkedHashMap<Variable, Term>());
	}
	
	
	public Map<Variable, Term> unify (FOLNode x, FOLNode y, 
			Map<Variable, Term> theta)
	{
		// If no substitution was found, the unification failed.
		if (theta == null)
			return null;
		
		// Attempt to equate the two terms
		else if (x.equals(y))
			return theta;
		
		// Attempt to unify variable x with the term y
		else if (x instanceof Variable)
			return unifyVar((Variable) x, y, theta);
		
		// Attempt to unify variable y with term x
		else if (y instanceof Variable)
			return unifyVar((Variable) y, x, theta);
		
		// If x and y are Predicates, attempt to unify the arguments of the two predicates.
		else if(x.isCompound() && y.isCompound())
			
			// Predicate names must be equivalent for the two terms to unify.
			return unify(x.getArgs(), y.getArgs(), unifyOps(x.getSymbolicName(), y.getSymbolicName(), theta));
		
		else
			return null;
	}
	
	/* Attempts to unify the arguments of two Predicates */
	public Map<Variable, Term> unify (List <? extends FOLNode> x, List <? extends FOLNode> y, Map <Variable, Term> theta)
	{
		// If no substitution was found, the unification failed.
		if (theta == null)
			return null;
		
		/* If the two predicates are of different arrity, they will fail to unify.
		 * i.e. There is no valid substitution theta, such that P(x,y) unifies with P(z).
	     */
		else if (x.size() != y.size())
			return null;
		
		/* If both Predicates are unary predicates,
		 *  attempt to unify their arguments.
		 */
		else if (x.size() == 1 && y.size() == 1)
			return unify(x.get(0), y.get(0), theta);
		
		/* If both Predicates are n-ary predicates,
		 *  attempt to unify their arguments, which is
		 *  the result of unifying arguments n-1 to n
		 *  using a substitution theta which unfies the
		 *  first argment of both predicates.
		 */
		else 
			return unify(x.subList(1, x.size()), y.subList(1, y.size()), 
					unify(x.get(0), y.get(0), theta));
	}
	
	/* Attempts to unify the Predicate symbols of two predicates */
	public Map<Variable, Term> unifyOps(String x, String y,
			Map<Variable, Term> theta) {
		
		if (theta == null)
			return null;
		
		else if (x.equals(y))
			return theta;
		
		else return null;
	}
	
	/* Attempts to unify a Variable x with a Term y */
	public Map<Variable, Term> unifyVar(Variable x, FOLNode y,  Map <Variable, Term> theta)
	{
		/* If theta contains a valid substitution for x, Term t
		 * attempt to unify t with y.
		 */
		if(theta.containsKey(x))
			return unify(theta.get(x), y, theta);
		
		/* If theta contains a valid substitution for y, Term t
		 * attempt to unify x with t.
		 */
		else if(theta.containsKey(y))
			return unify(x, theta.get(y), theta);
		
		/* Ensure that Variable x does not occur within predicate y
		 * i.e. Attempting to unify x with S(x) should fail.
		 */
		else if(occurCheck(x, y, theta))
			return null;
		
		/* Substitute all instances of x with y */
		else {
			cascadeSub(x, y, theta);
			return theta;
		}
	}
	
	/* Check if the variable itself occurs inside the term: S(x) doesn't unify with S(S(x)) */
	public boolean occurCheck(Variable x, FOLNode y, Map <Variable, Term> theta)
	{
		/* Base Case: if y is a variable, and x equals y,
		 * then x can not unify with a Predicate containing
		 * a Variable x.
		 * 
		 * P.S. It is guaranteed that y will be deeper than x, 
		 * since unify(FOLNode x, FOLNode y,theta) calls unifyVar(x,y)
		 * only after assuring that x and y are not equivalent.
		 */
		if(x.equals(y))
			return true;
		/* If y is a Variable, and theta contains a substitution for y, Term t
		 * attempt to check if x occurs in t */
		else if (theta.containsKey(y))
			return occurCheck(x, theta.get(y), theta);
		
		/* If y is a Function, check if x occurs as either an argument of y,
		 * or within one of its arguments.
		 */
		else if(y instanceof Function) {
			for(Term t: ((Function)y).getArgs())
			{
				if(occurCheck(x, t, theta))
					return true;
			}
		}
		return false;
	}
	
	/* if P(z,x) and {z/x, x/a}, the result should be P(a,a) and not P(x,a) */ 
	public void cascadeSub(Variable x, FOLNode y, Map<Variable, Term> theta)
	{
		/*
		 * i.e. casecadeSub(x, a, {z/x}) ==> theta = {z/a, x/a}
		 */
		for(Variable key: theta.keySet()) { // z and x will be checked, the only keys present in the map
			if(x.equals(theta.get(key))){ // theta.get(z) == x
				theta.put(key, (Term) y); // cascade: put z ==> a
			}
		}
		theta.put(x, (Term) y);
		
		
		/*
		 * i.e. casecadeSub(u, f(y), {z/a, y/a}) ==> theta = {z/a, y/a, u/f(a)}
		 */
		if(y instanceof Function) {
			Function f = new Function(y.getSymbolicName());
			for(Term t: (List<Term>)y.getArgs()) {
				if(t instanceof Variable && theta.containsKey(t)) {
					f.addArg(theta.get(t));
				}
				else {
					f.addArg(t);
				}
			}
			theta.put(x, f);
		}
	}
}
