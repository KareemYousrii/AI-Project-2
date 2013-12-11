package Unifier;

import FOL.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Unifier
{
	
	public Unifier() {
		
	}
	
	public Map<Variable, Term> unify(FOLNode x, FOLNode y)
	{
		return unify(x, y, new LinkedHashMap<Variable, Term>());
	}
	
	public Map<Variable, Term> unify (FOLNode x, FOLNode y, 
			Map<Variable, Term> theta)
	{
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
		
		// Attempt to unify two compound terms
		else if(x.isCompound() && y.isCompound())
			return unify(x.getArgs(), y.getArgs(), unifyOps(x.getSymbolicName(), y.getSymbolicName(), theta));
		
		else
			return null;
	}
	
	public Map<Variable, Term> unify (List <? extends FOLNode> x, List <? extends FOLNode> y, Map <Variable, Term> theta)
	{
		if (theta == null)
			return null;
		
		else if (x.size() != y.size())
			return null;
		
		else if (x.size() == 1 && y.size() == 1)
			return unify(x.get(0), y.get(0), theta);
		
		else 
			return unify(x.subList(1, x.size()), y.subList(1, y.size()), 
					unify(x.get(0), y.get(0), theta));
	}
	
	public Map<Variable, Term> unifyOps(String x, String y,
			Map<Variable, Term> theta) {
		
		if (theta == null)
			return null;
		
		else if (x.equals(y))
			return theta;
		
		else return null;
	}
	
	public Map<Variable, Term> unifyVar(Variable x, FOLNode y,  Map <Variable, Term> theta)
	{
		if(theta.containsKey(x))
			return unify(theta.get(x), y, theta);
		
		else if(theta.containsKey(y))
			return unify(x, theta.get(y), theta);
		
		else if(occurCheck(x, y, theta))
			return null;
		
		else {
			cascadeSub(x, y, theta);
			return theta;
		}
	}
	
	/* Check if the variable itself occurs inside the term: S(x) doesn't unify with S(S(x)) */
	public boolean occurCheck(Variable x, FOLNode y, Map <Variable, Term> theta)
	{
		if(x.equals(y))
			return true;
		
		else if (theta.containsKey(y))
			return occurCheck(x, theta.get(y), theta);
		
		else if(y instanceof Function) {
			for(Term t: ((Function)y).getArgs())
			{
				if(occurCheck(x, t, theta))
					return true;
			}
		}
		return false;
	}
	
	
	public void cascadeSub(Variable x, FOLNode y, Map<Variable, Term> theta)
	{
		// all variables
		for(Variable key: theta.keySet()) {
			if(x.equals(theta.get(key))){
				theta.put(key, (Term) y);
			}
		}
		theta.put(x, (Term) y);
	}
}
