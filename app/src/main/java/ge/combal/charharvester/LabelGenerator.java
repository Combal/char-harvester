package ge.combal.charharvester;

/**
 * Created by vano on 10/3/16.
 */

public class LabelGenerator {

	protected static String[] ALPHABET = {"ა", "ბ", "გ", "დ", "ე", "ვ", "ზ", "თ", "ი", "კ", "ლ", "მ", "ნ", "ო",
			"პ", "ჟ", "რ", "ს", "ტ", "უ", "ფ", "ქ", "ღ", "ყ", "შ", "ჩ", "ც", "ძ", "წ", "ჭ", "ხ", "ჯ", "ჰ"};

	public static String next() {
		int ind =  (int)(Math.random() * ALPHABET.length);
		return ALPHABET[ind];
	}

	public static void main(String[] args) {
		System.out.println(next());
	}
}
