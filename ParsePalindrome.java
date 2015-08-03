import java.io.*; 
import java.util.*; 


class ParsePalindrome{

	private static boolean testBoth(List<String> strs, List<String> intes){

		if (strs.size() != intes.size()){
			return false;
		}

		if (strs.size() > 50 || intes.size() > 50){
			return false;
		}

		for(int i = 0; i < strs.size(); i++){

			if (i == 0 && strs.get(i).length() > 50) return false;
			if (i > 0 && strs.get(i).length() != strs.get(i-1).length()) return false;
			if (Integer.parseInt(intes.get(i)) > 1000000) return false;
		}

		return true;
	}


	private static boolean checkPalindrome(String str){

		String reverse = new StringBuffer(str).reverse().toString();

		if (reverse.equals(str)) return true;

		return false;
	}


	public static void main(String[] args) throws Exception{

		System.out.println("Please input strings: ");
        	BufferedReader in = new BufferedReader( 
            		new InputStreamReader(System.in));

        	String str;
		String inte;
		List<String> strs;
		List<String> intes;


		int flagOut = 0;

		do{
			int flagIn = 0;

			if(flagOut > 0){

			System.out.println("Numbers of strings and number must be same / No more than 50 elements / Each string within 50 char and same number of char / Each number no more than 1000000.");

			System.out.println("Please reinput:");

			}

			do{
				if(flagIn > 0) System.out.println("Must be in form like aaa,bbb,ccc");
				str = in.readLine();
				flagIn++;
			}
			while (str.matches("^[a-z]+(,[a-z]+)+") == false);

			strs = Arrays.asList(str.split("\\s*,\\s*"));
 
			flagIn = 0;


			do{

				if(flagIn > 0) System.out.println("Must be in form like 111,222,333");
				inte = in.readLine();
				flagIn++;
			}
			while (!inte.matches("^[1-9][0-9]*(,[1-9][0-9]*)+"));

			intes = Arrays.asList(inte.split("\\s*,\\s*"));

			flagOut++;

		}
		while(!testBoth(strs, intes));
		


/*----------------------------------------------------------------------------------------*/

		List<String> frontNonPalindrome = new ArrayList<>();
		List<String> backNonPalindrome = new ArrayList<>();

		List<String> frontPalindrome = new ArrayList<>();
		List<String> backPalindrome = new ArrayList<>();

//Divide input into two categories

		for(int i = 0; i < strs.size(); i++){
			String tmpStrs = strs.get(i);
			String tmpInte = intes.get(i);

			if (checkPalindrome(tmpStrs)){
				frontPalindrome.add(tmpStrs);
				backPalindrome.add(tmpInte);
			}

			else{
				frontNonPalindrome.add(tmpStrs);
				backNonPalindrome.add(tmpInte);
			}
		}


		int max = 0;

/*----------------------------------------------------------------------------------------*/

//Dealing with List frontPalindrome, each element of which is Palindrome

		if(frontPalindrome!=null && !frontPalindrome.isEmpty()){

			Map<String, Integer> Palindrome = new HashMap<>(); //Map<elements, number of occurence>

			for(int i = 0; i < frontPalindrome.size(); i++){

				String tmpStrs = frontPalindrome.get(i);

				if (Palindrome.containsKey(tmpStrs)) Palindrome.put(tmpStrs, Palindrome.get(tmpStrs) + 1);

				else Palindrome.put(tmpStrs, 1);

			}


			List<Integer> oddMin = new ArrayList<>();


			for (String key : Palindrome.keySet()){

				List<Integer> index = new ArrayList<>();

				for(int i = 0; i < frontPalindrome.size(); i++){

					String tmpStrs = frontPalindrome.get(i);

					if (tmpStrs.equals(key)) index.add(Integer.parseInt(backPalindrome.get(i)));

				}


				int temp = 0;

				for(int i = 0; i < index.size(); i++) temp += index.get(i);

				max += temp;


				if(Palindrome.get(key) % 2 == 1){

					max -= Collections.min(index);

					oddMin.add(Collections.min(index));
				}
			}

			if (oddMin!=null && !oddMin.isEmpty()){
				max += Collections.max(oddMin);
			}

		}
/*----------------------------------------------------------------------------------------*/

//Dealing with List frontNonPalindrome, each element of which is not Palindrome

		if(frontNonPalindrome!=null && !frontNonPalindrome.isEmpty()){

			Map<String, Integer> cis = new HashMap<>();
			Map<String, Integer> trans = new HashMap<>();

			for(int i = 0; i < frontNonPalindrome.size(); i++){

				String key = frontNonPalindrome.get(i);
				String reverse = new StringBuffer(key).reverse().toString();

				if (cis.containsKey(key)) cis.put(key, cis.get(key) + 1);

				else if (cis.containsKey(reverse)) trans.put(reverse, trans.get(reverse) + 1);

				else{
					cis.put(key, 1);
					trans.put(key, 0);
				}
			}

			Map<String, Integer> cisCopy = new HashMap<>(cis);


			for (String key : cisCopy.keySet()){

				int C = cis.get(key);
				int T = trans.get(key);
				String reverse = new StringBuffer(key).reverse().toString();

				if (T == 0){
					cis.remove(key);
					trans.remove(key);
				}

				else if (C < T){

					cis.put(reverse, T);
					trans.put(reverse, C);
					cis.remove(key);
					trans.remove(key);
				}
			}



			for (String key : cis.keySet()){

				List<Integer> index = new ArrayList<>();
				int C = cis.get(key);
				int T = trans.get(key);
				String reverse = new StringBuffer(key).reverse().toString();

				if(C == T){

					for(int i = 0; i < frontNonPalindrome.size(); i++){

						String tmpStrs = frontNonPalindrome.get(i);

						if (tmpStrs.equals(key) || tmpStrs.equals(reverse)) index.add(Integer.parseInt(backNonPalindrome.get(i)));
					}
				}


				else{
					List<Integer> indexCis = new ArrayList<>();

					for(int i = 0; i < frontNonPalindrome.size(); i++){

						String tmpStrs = frontNonPalindrome.get(i);

						if (tmpStrs.equals(reverse)) index.add(Integer.parseInt(backNonPalindrome.get(i)));

						else if (tmpStrs.equals(key)) indexCis.add(Integer.parseInt(backNonPalindrome.get(i)));
					}

					for(int i = 0; i < T; i++){

						index.add(Collections.max(indexCis));

						indexCis.remove(Collections.max(indexCis));
					}

				}

				int temp = 0;

				for(int i = 0; i < index.size(); i++) temp += index.get(i);

				max += temp;
			}
		}


	System.out.println(max);
    } 
}
