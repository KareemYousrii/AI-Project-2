package Unifier;

class Pair <E> {
	public E first;
	public E second;
	
	public Pair(E first, E second) {
		this.first = first;
		this.second = second;
	}
	
	public E getFirst() {
		return first;
	}
	
	public E getSecond() {
		return second;
	}
}