package Unifier;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import FOL.*;

/* http://www.cs.man.ac.uk/~hoderk/ubench/unification_full.pdf */

public class Robinsion {
	
	public List<Variable> getVars(Term t)
	{
		List <Variable> l = new LinkedList <Variable> ();
		getVars(t, l);
		return l;
	}
	
	public void getVars(Term t, List <Variable> l)
	{
		if(t instanceof Variable){
			l.add((Variable)t);
			return;
		}
		
		for(Term y: t.getArgs()) {
			getVars(y, l);
		}
	}
	
	public boolean robOccursCheck(Variable x, Term t, Map <Variable, Term> theta){
		Stack <Term> s = new Stack <Term>();
		s.push(t);
		
		while(!s.isEmpty()){
			t = s.pop();
			for(Term y: getVars(t))
			{
				if(y instanceof Variable)
				{
					if(x.equals(y))
						return false;
					
					if(theta.containsKey(y))
					{
						s.push(theta.get(y));
					}
				}
			}
		}
		return true;
	}
	
	public Map<Variable, Term> ROB(Term x, Term y)
	{
		Stack <Pair<Term>> s = new Stack <Pair<Term>>();
		Map <Variable, Term> theta = new LinkedHashMap<Variable, Term>();
		
		s.push(new Pair <Term> (x,y));
		
		while(!s.isEmpty())
		{
			Pair <Term> p = s.pop();
			
			x = p.getFirst();
			y = p.getSecond();
			
			if(x instanceof Variable && theta.containsKey(x)){
				x = theta.get(x);
			}
			
			if(y instanceof Variable && theta.containsKey(y)){
				y = theta.get(y);
			}
			
			if(!x.equals(y))
			{
				if(x instanceof Variable && y instanceof Variable)
					theta.put((Variable)x, y);
				
				if(x instanceof Variable)
					if(robOccursCheck((Variable)x,y,theta))
						theta.put((Variable) x, y);
					else
						return null;
				
				if(y instanceof Variable)
					if(robOccursCheck((Variable)y,x,theta))
						theta.put((Variable) y, x);
					else
						return null;
				
				if(x instanceof Function && y instanceof Function) {
					if(x.getOp().equals(y.getOp())) {
						if(x.getArgs().size() == y.getArgs().size()) {
							List <Term> x_args = x.getArgs();
							List <Term> y_args = y.getArgs();
							for(int i = 0; i < x_args.size(); i++) {
								s.push(new Pair <Term> (x_args.get(i), y_args.get(i)));
							}
						}
					}
					else {
						return null;
					}
				}
			}
		}
		return theta;
	}
}
