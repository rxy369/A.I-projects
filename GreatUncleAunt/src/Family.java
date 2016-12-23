import java.util.HashMap;

public class Family {

	//enums for all the people in the family
	public enum Person{
		MALE1,MALE2,MALE3,MALE4,MALE5,MALE6,MALE7,MALE8,
		FEMALE1,FEMALE2,FEMALE3,FEMALE4,FEMALE5,FEMALE6,FEMALE7,FEMALE8,FEMALE9,FEMALE10
	}
	
	//hashmaps to store father, mothers, and married
	private HashMap<Person,Person> married =  new HashMap();
	private HashMap<Person,Person> father =  new HashMap();
	private HashMap<Person,Person> mother =  new HashMap();
	
	//family class that establishes all the hashmaps relationships 
	
	public Family(){
		//establishes married hashmap
		married.put(Person.MALE3, Person.FEMALE4);
		married.put(Person.MALE6, Person.FEMALE2);
		married.put(Person.MALE4, Person.FEMALE6);
		married.put(Person.MALE8, Person.FEMALE8);
		married.put(Person.MALE7, Person.FEMALE10);
		married.put(Person.MALE2, Person.FEMALE3);
		married.put(Person.MALE1, Person.FEMALE1);
		
		married.put(Person.FEMALE4, Person.MALE3);
		married.put(Person.FEMALE2, Person.MALE6);
		married.put(Person.FEMALE6, Person.MALE4);
		married.put(Person.FEMALE8, Person.MALE8);
		married.put(Person.FEMALE10, Person.MALE7);
		married.put(Person.FEMALE3, Person.MALE2);
		married.put(Person.FEMALE1, Person.MALE1);
		
		//establishes father hashmap (goes by (child,parent)
		//avoid key duplicates)
		father.put(Person.MALE2, Person.MALE1);
		father.put(Person.MALE3, Person.MALE1);
		father.put(Person.FEMALE2, Person.MALE1);
		father.put(Person.MALE4, Person.MALE2);
		father.put(Person.MALE8, Person.MALE2);
		father.put(Person.FEMALE5, Person.MALE3);
		father.put(Person.MALE5, Person.MALE3);
		father.put(Person.FEMALE7, Person.MALE4);
		father.put(Person.MALE7, Person.MALE4);
		father.put(Person.FEMALE9, Person.MALE8);
		
		//establishes mother hashmap (goes by (child,parent)
				//avoid key duplicates)
		mother.put(Person.MALE4, Person.FEMALE3);
		mother.put(Person.MALE8, Person.FEMALE3);
		mother.put(Person.FEMALE5, Person.FEMALE4);
		mother.put(Person.MALE5, Person.FEMALE4);
		mother.put(Person.FEMALE7, Person.FEMALE6);
		mother.put(Person.MALE7, Person.FEMALE6);
		mother.put(Person.FEMALE9, Person.FEMALE8);
		mother.put(Person.FEMALE2, Person.FEMALE1);
		mother.put(Person.MALE3, Person.FEMALE1);
		mother.put(Person.MALE2, Person.FEMALE1);
	}
	
	//gets the grand parents of the child sent in
	private Person[] findGrandParents(Person child){
		Person Mommy;
		Person Daddy;
		Person[] grandParents = new Person[2];
		
		//checks if the child passed in has a father
		if(father.get(child) != null){
			Mommy = mother.get(child);
			Daddy = father.get(child);
			//checks if the child passed in has a grand parents, from father's side
			if(father.get(Daddy) != null){
				grandParents[0] = father.get(Daddy);
				grandParents[1] = mother.get(Daddy);
			}
			//checks if the child passed in has a grand parents, from mother's side
			else
				grandParents[0] = father.get(Mommy);
				grandParents[1] = mother.get(Mommy);
		}
		//if there are no grandparents we leave the array values as null
		//otherwise we fill them with the information above
		return grandParents;
	}
	
	//return whether or not these two people are siblings
	boolean areSiblings(Person grandParent, Person maybeSib){
		boolean isASib = false;
		if(father.get(grandParent) != null){
			//if there is a parent that has grand parent and a maybeSib as children, on father's side
			if(father.get(grandParent) == father.get(maybeSib))
				isASib = true;
			//if there is a parent that has grand parent and a maybeSib as children, on mother's side
			else if(father.get(grandParent) == father.get(married.get(maybeSib)))
				isASib = true;
		}
		return isASib;
	}
	
	// returns whether or not the child has a great uncle or aunt
	public boolean greatAuntUncle(Person posSib, Person child){
		Person[] grandParent = findGrandParents(child);
		return areSiblings(grandParent[0], posSib) || areSiblings(grandParent[1], posSib);
	}
}
  