import java.io.*;
import java.util.*;


class DictEntry2 {

    public int doc_freq = 0; // number of documents that contain the term
    public int term_freq = 0; //number of times the term is mentioned in the collection
    public HashSet<Integer> postingList;

    DictEntry2() {
        postingList = new HashSet<>();
    }
}
public class InvertedIndex {
            //--------------------------------------------
	    Map<Integer, String> sources;  // store the doc_id and the file name
	    HashMap<String, DictEntry2> index; // THe inverted index
	    //--------------------------------------------

	    InvertedIndex() {
	        sources = new HashMap<Integer, String>();
	        index = new HashMap<String, DictEntry2>();
	    }

	    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
	    {
	        // Create a list from elements of HashMap
	        List<Map.Entry<String, Integer> > list =
	               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

	        // Sort the list
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
	            public int compare(Map.Entry<String, Integer> o1,
	                               Map.Entry<String, Integer> o2)
	            {
	                return (o1.getValue()).compareTo(o2.getValue());
	            }
	        });

	        // put data from sorted list to hashmap
	        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
	        for (Map.Entry<String, Integer> aa : list) {
	            temp.put(aa.getKey(), aa.getValue());
	        }
	        return temp;
	    }
	    //---------------------------------------------
	    public void printDictionary() {
	        Iterator it = index.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry pair = (Map.Entry) it.next();
	            DictEntry2 dd = (DictEntry2) pair.getValue();
	            HashSet<Integer> hset = dd.postingList;// (HashSet<Integer>) pair.getValue();
	            System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "] <" + dd.term_freq + "> =--> ");
	            Iterator<Integer> it2 = hset.iterator();
	            while (it2.hasNext()) {
	                System.out.print(it2.next() + ", ");
	            }
	            System.out.println("");
	            //it.remove(); // avoids a ConcurrentModificationException
	        }
	        System.out.println("------------------------------------------------------");
	        System.out.println("*** Number of terms = " + index.size());
	    }

	    //-----------------------------------------------
	    public void buildIndex(String[] files) {
	        int i = 0;
	        for (String fileName : files) {
	            try ( BufferedReader file = new BufferedReader(new FileReader(fileName))) {
	                sources.put(i, fileName);
	                String ln;
	                while ((ln = file.readLine()) != null) {
	                    String[] words = ln.split("\\W+");
	                    for (String word : words) {
	                        word = word.toLowerCase();
	                        // check to see if the word is not in the dictionary
	                        if (!index.containsKey(word)) {
	                            index.put(word, new DictEntry2());
	                        }
	                        // add document id to the posting list
	                        if (!index.get(word).postingList.contains(i)) {
	                            index.get(word).doc_freq += 1; //set doc freq to the number of doc that contain the term
	                            index.get(word).postingList.add(i); // add the posting to the posting:ist
	                        }
	                        //set the term_freq in the collection
	                        index.get(word).term_freq += 1;
	                    }
	                }
	                printDictionary();
	            } catch (IOException e) {
	                System.out.println("File " + fileName + " not found. Skip it");
	            }
	            i++;
	        }
	    }

	    //--------------------------------------------------------------------------
	    // query inverted index
	    // takes a string of terms as an argument


	    //----------------------------------------------------------------------------
	    HashSet<Integer> intersect(HashSet<Integer> pL1, HashSet<Integer> pL2) {
		    HashSet<Integer> answer = new HashSet();
		    Iterator it1 = pL1.iterator();
		    Iterator it2 = pL2.iterator();

		    Integer n1 = (Integer) it1.next();
		    Integer n2 = (Integer) it2.next();

		    while(n1 != null && n2 != null)//this loop will end when one of the lists ends
	            {
		    	if(n1.compareTo(n2) == 0) //n1 = n2 -> will move the two iterator to the coming position
	                {
		    		answer.add(n1);
		    	    n1 = (it1.hasNext())? (Integer) it1.next(): null;
		    	    n2 = (it2.hasNext())? (Integer) it2.next(): null;
		    	}
		    	else if(n1.compareTo(n2) < 0) //n1 < n2 -> Will move the first iterator
	                {
		    		n1 = (it1.hasNext())? (Integer) it1.next(): null;
		    	}
		    	else//n1>n2 -> Will move the second iterator
	                {
		    		n2 = (it2.hasNext())? (Integer) it2.next(): null;
		    	}
		    }
		    return answer;//answer will be holding the set of common document number for both two posting lists
	    }
	    //-----------------------------------------------------------------------



	public String find(String phrase) {
		StringBuilder result = new StringBuilder();
		String[] words = phrase.split("\\W+");

		HashSet<Integer> res = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);

		for (String word : words) {
			res.retainAll(index.get(word).postingList);
		}
		if (res.size() == 0) {
			System.out.println("Not found");
			return "";
		}
		ArrayList<Integer> Sorted_Printed_List = new ArrayList<Integer>(res);
		Collections.sort(Sorted_Printed_List);
		String file_name[] ;
		result.append("Found in: \t [");
		for( int id : Sorted_Printed_List) {
			if (sources.get(id)!=null){
				file_name =sources.get(id).split("\\\\");
				result.append(" ").append(file_name[file_name.length - 1]).append(" , ");
			}
		}
		result.replace(result.length()-3,result.length()-1,"");
		result.append("]");
		return result.toString();
	}

	//----------------------------------------------------------------------------
	HashSet<Integer> intersect_add(HashSet<Integer> pL1, HashSet<Integer> pL2) {
		HashSet<Integer> answer = new HashSet();
		Iterator it1 = pL1.iterator();
		Iterator it2 = pL2.iterator();

		Integer n1 = (Integer) it1.next();
		Integer n2 = (Integer) it2.next();

		while(n1 != null && n2 != null)//this loop will end when one of the lists ends
		{
			if(n1.compareTo(n2) == 0) //n1 = n2 -> will move the two iterator to the coming position
			{
				answer.add(n1);
				n1 = (it1.hasNext())? (Integer) it1.next(): null;
				n2 = (it2.hasNext())? (Integer) it2.next(): null;
			}
			else if(n1.compareTo(n2) < 0) //n1 < n2 -> Will move the first iterator
			{
				n1 = (it1.hasNext())? (Integer) it1.next(): null;
			}
			else//n1>n2 -> Will move the second iterator
			{
				n2 = (it2.hasNext())? (Integer) it2.next(): null;
			}
		}
		return answer;//answer will be holding the set of common document number for both two posting lists
	}
	//-----------------------------------------------------------------------

	public String find_01(String phrase) { // 2 term phrase  2 postingsLists
		StringBuilder result = new StringBuilder();
		String[] words = phrase.split("\\W+");
		// 1- get first posting list
		HashSet<Integer> pL1 = new HashSet<>(index.get(words[0].toLowerCase()).postingList);
		// 2- get second posting list
		HashSet<Integer> pL2 = new HashSet<>(index.get(words[1].toLowerCase()).postingList);
		// 3- apply the algorithm
		HashSet<Integer> answer = intersect_add(pL1, pL2);
		ArrayList<Integer> Sorted_Printed_List = new ArrayList<>(answer);
		Collections.sort(Sorted_Printed_List);
		String file_name[] ;
		result.append("Found in: \t [");
		for( int id : Sorted_Printed_List) {
			if (sources.get(id)!=null){
				file_name =sources.get(id).split("\\\\");
				result.append(" ").append(file_name[file_name.length - 1]).append(" , ");
			}
		}
		result.replace(result.length()-3,result.length()-1,"");
		result.append("]");
		return result.toString();
	}
	//-----------------------------------------------------------------------

	public String find_02(String phrase) { //  lists
		StringBuilder result = new StringBuilder();
		if(phrase.equals(""))
			return result.toString();

		String[] words = phrase.split("\\W+");
		HashSet<Integer> res = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
		for(int i = 1 ; i < words.length ; i++) {
			res = intersect_add(res,index.get(words[i].toLowerCase()).postingList);
		}
		ArrayList<Integer> Sorted_Printed_List = new ArrayList<Integer>(res);
		Collections.sort(Sorted_Printed_List);
		String file_name[] ;
		result.append("Found in: \t [");
		for( int id : Sorted_Printed_List) {
			if (sources.get(id)!=null){
				file_name =sources.get(id).split("\\\\");
				result.append(" ").append(file_name[file_name.length - 1]).append(" , ");
			}
		}
		result.replace(result.length()-3,result.length()-1,"");
		result.append("]");
		return result.toString();

	}
	//-----------------------------------------------------------------------



	//---------------------------or-operator-code-----------------------------------------
	HashSet<Integer> intersect_or(HashSet<Integer> pL1, HashSet<Integer> pL2) {
		HashSet<Integer> answer = new HashSet();
		Iterator it1 = pL1.iterator();
		Iterator it2 = pL2.iterator();

		Integer n1 = (Integer) it1.next();
		Integer n2 = (Integer) it2.next();

		while(n1 != null && n2 != null)//this loop will end when one of the lists ends
		{
			if(n1.compareTo(n2) == 0) //n1 = n2 -> will move the two iterator to the coming position
			{
				answer.add(n1);
				n1 = (it1.hasNext())? (Integer) it1.next(): null;
				n2 = (it2.hasNext())? (Integer) it2.next(): null;
			}
			else if(n1.compareTo(n2) < 0) //n1 < n2 -> Will move the first iterator and add to the answer
			{
				answer.add(n1);
				n1 = (it1.hasNext())? (Integer) it1.next(): null;
			}
			else//n1>n2 -> Will move the second iterator and ad to the answer list
			{
				answer.add(n2);
				n2 = (it2.hasNext())? (Integer) it2.next(): null;
			}
		}
		return answer;//answer will be holding the set of common document number for both two posting lists
	}

	public String find_01_or(String phrase) { // 2 term phrase  2 postingsLists
		StringBuilder result = new StringBuilder();
		String[] words = phrase.split("\\W+");
		// 1- get first posting list
		HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
		// 2- get second posting list
		HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
		// 3- apply the algorithm
		HashSet<Integer> answer = intersect_or(pL1, pL2);
		ArrayList<Integer> Sorted_Printed_List = new ArrayList<Integer>(answer);
		Collections.sort(Sorted_Printed_List);
		String file_name[] ;
		result.append("Found in: \t [");
		for( int id : Sorted_Printed_List) {
			if (sources.get(id)!=null){
				file_name =sources.get(id).split("\\\\");
				result.append(" ").append(file_name[file_name.length - 1]).append(" , ");
			}
		}
		result.replace(result.length()-3,result.length()-1,"");
		result.append("]");
		return result.toString();
	}

	public String find_02_or(String phrase) { //  lists
		StringBuilder result = new StringBuilder();
		if(phrase.equals(""))
			return result.toString();

		String[] words = phrase.split("\\W+");
		HashSet<Integer> res = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
		for(int i = 1 ; i < words.length ; i++) {
			res = intersect_or(res,index.get(words[i].toLowerCase()).postingList);
		}
		ArrayList<Integer> Sorted_Printed_List = new ArrayList<Integer>(res);
		Collections.sort(Sorted_Printed_List);
		String file_name[] ;
		result.append("Found in: \t [");
		for( int id : Sorted_Printed_List) {
			if (sources.get(id)!=null){
				file_name =sources.get(id).split("\\\\");
				result.append(" ").append(file_name[file_name.length - 1]).append(" , ");
			}
		}
		result.replace(result.length()-3,result.length()-1,"");
		result.append("]");
		return result.toString();

	}

	//-----------------------------------------------------------------------

	//-------------------------not-operator-code-----------------------------------------
	HashSet<Integer> intersect_not(HashSet<Integer> pL1, HashSet<Integer> pL2) {
		HashSet<Integer> answer = new HashSet();
		Iterator it1 = pL1.iterator();
		Iterator it2 = pL2.iterator();

		Integer n1 = (Integer) it1.next();
		Integer n2 = (Integer) it2.next();

		while(n1 != null && n2 != null)//this loop will end when one of the lists ends
		{
			if(n1.compareTo(n2) == 0) //n1 = n2 -> will move the two iterator to the coming position and not add any of them
			{
				n1 = (it1.hasNext())? (Integer) it1.next(): null;
				n2 = (it2.hasNext())? (Integer) it2.next(): null;
			}
			else if(n1.compareTo(n2) < 0) //n1 < n2 -> Will move the first iterator and add to the answer for the first phrase
			{
				answer.add(n1);
				n1 = (it1.hasNext())? (Integer) it1.next(): null;
			}
			else//n1>n2 -> Will move the second iterator and ad to the answer list and not add any to the list
			{

				n2 = (it2.hasNext())? (Integer) it2.next(): null;
			}
		}
		return answer;//answer will be holding the set of common document number for the first posting list posting lists
	}

	public String find_01_not(String phrase) { // 2 term phrase  2 postingsLists
		StringBuilder result = new StringBuilder();
		String[] words = phrase.split("\\W+");
		// 1- get first posting list
		HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
		// 2- get second posting list
		HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
		// 3- apply the algorithm
		HashSet<Integer> answer = intersect_not(pL1, pL2);
		ArrayList<Integer> Sorted_Printed_List = new ArrayList<Integer>(answer);
		Collections.sort(Sorted_Printed_List);
		String file_name[] ;
		result.append("Found in: \t [");
		for( int id : Sorted_Printed_List) {
			if (sources.get(id)!=null){
				file_name =sources.get(id).split("\\\\");
				result.append(" ").append(file_name[file_name.length - 1]).append(" , ");
			}
		}
		result.replace(result.length()-3,result.length()-1,"");
		result.append("]");
		return result.toString();
	}



	public String find_02_not(String phrase) { //  lists
		StringBuilder result = new StringBuilder();
		if(phrase == "")
			return result.toString();

		String[] words = phrase.split("\\W+");
		HashSet<Integer> res = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
		for(int i = 1 ; i < words.length ; i++) {
			res = intersect_not(res,index.get(words[i].toLowerCase()).postingList);
		}
		ArrayList<Integer> Sorted_Printed_List = new ArrayList<Integer>(res);
		Collections.sort(Sorted_Printed_List);
		String file_name[] ;
		result.append("Found in: \t [");

		for( int id : Sorted_Printed_List) {
			if (sources.get(id)!=null){
				file_name =sources.get(id).split("\\\\");
				result.append(" ").append(file_name[file_name.length - 1]).append(" , ");
			}
		}
		result.replace(result.length()-3,result.length()-1,"");
		result.append(" ]");
		// write you code here
		return result.toString();

	}

	//-----------------------------------------------------------------------

	public String find_Free_Query(String phrase) { //  lists
		StringBuilder result = new StringBuilder();
		if(phrase == "")
			return result.toString();
		String[] words = phrase.split("\\W+");
		HashSet<Integer> res = new HashSet<>(index.get(words[0].toLowerCase()).postingList);
		for(int i = 0 ; i < words.length ; i++) {
			if(words[i].equals("not")){
				res = intersect_not(res,index.get(words[i-1].toLowerCase()).postingList);
				res = intersect_not(res,index.get(words[i+1].toLowerCase()).postingList);

			}else if(words[i].equals("and")){
				res = intersect_add(res,index.get(words[i-1].toLowerCase()).postingList);
				res = intersect_add(res,index.get(words[i+1].toLowerCase()).postingList);
			}else if(words[i].equals("or")){
				res = intersect_or(res,index.get(words[i-1].toLowerCase()).postingList);
				res = intersect_or(res,index.get(words[i+1].toLowerCase()).postingList);
			}

		}
		ArrayList<Integer> Sorted_Printed_List = new ArrayList<Integer>(res);
		Collections.sort(Sorted_Printed_List);
		String file_name[] ;
		result.append("Found in: \t [");

		for( int id : Sorted_Printed_List) {
			if (sources.get(id)!=null){
				file_name =sources.get(id).split("\\\\");
				result.append(" ").append(file_name[file_name.length - 1]).append(" , ");
			}
		}
		result.replace(result.length()-3,result.length()-1,"");
		result.append("]");
		// write you code here
		return result.toString();

	}

	public String find_N_Terms(String phrase) { // optimized search
		StringBuilder result = new StringBuilder();

		if(phrase == "")
			return result.toString();

		String[] words = phrase.split("\\W+");
		List<String> wordsList = new ArrayList<String>(Arrays.asList(words));

		HashSet<Integer> res = new HashSet<Integer> ();
		HashSet<Integer> not_res =new HashSet<Integer>();
		if (wordsList.get(0).equals("not")){
			wordsList.remove(wordsList.get(0));
			not_res = new HashSet<>(index.get(wordsList.get(0).toLowerCase()).postingList);
			wordsList.remove(wordsList.get(0));
		}else {
			res = new HashSet<Integer>(index.get(wordsList.get(0).toLowerCase()).postingList);
			wordsList.remove(wordsList.get(0));
		}
		res.add(50);
		while (!wordsList.isEmpty()) {

				if(wordsList.get(0).equals("not")){
					wordsList.remove(wordsList.get(0));
					not_res.addAll(index.get(wordsList.get(0).toLowerCase()).postingList);
					wordsList.remove(wordsList.get(0));
				}else if(wordsList.get(0).equals("and")){
					wordsList.remove(wordsList.get(0));
					res = intersect_add(res,index.get(wordsList.get(0).toLowerCase()).postingList);
					wordsList.remove(wordsList.get(0));

				}else if(wordsList.get(0).equals("or")){
					wordsList.remove(wordsList.get(0));
					res.addAll(index.get(wordsList.get(0).toLowerCase()).postingList);
					wordsList.remove(wordsList.get(0));

				}else {
					res.addAll(index.get(wordsList.get(0).toLowerCase()).postingList);
					wordsList.remove(wordsList.get(0));

				}
		}
		ArrayList<Integer> not_list = new ArrayList(not_res);
		while (!(not_list.size()==0)){
			for (int i = 0; i <not_list.size() ; i++) {
				res.remove(not_list.get(i));
				not_list.remove(not_list.get(i));
			}

		}
		res.remove(null);
		ArrayList<Integer> Sorted_Printed_List = new ArrayList<>(res);
		Collections.sort(Sorted_Printed_List);
		String file_name[] ;
		result.append("Found in: \t [");

		for( int id : Sorted_Printed_List) {
			if (sources.get(id)!=null){
				file_name =sources.get(id).split("\\\\");
				result.append(" ").append(file_name[file_name.length - 1]).append(" , ");
			}
		}
		result.replace(result.length()-3,result.length()-1,"");
		result.append("]");
		// write you code here
		return result.toString();
	}
}