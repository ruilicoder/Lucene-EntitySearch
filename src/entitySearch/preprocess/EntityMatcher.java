package entitySearch.preprocess;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import util.DictionaryMatcher;
import util.DictionaryMatcher.Status;
import entitySearch.Configure;
import entitySearch.index.EntityTypeIndex;

public class EntityMatcher {
	private EntityTypeIndex eti;
	private DictionaryMatcher dm;

	public EntityMatcher(EntityTypeIndex ei) {
		eti = ei;
		ArrayList<String> list = ei.entityList;
		dm = new DictionaryMatcher(list);
		// DictionaryMatcher.Status s = dm.get("key");
	}

	public ArrayList<String> matcher(String content) {
		content = content.toLowerCase();
		ArrayList<String> matched = new ArrayList<String>();
		String[] words = content.split(" +");
		for (int i = 0; i < words.length; i++) {
			Status s = dm.get(words[i]);
			if (s == null)
				continue;
			if (s.match) {
				matched.add(words[i]);
			}
			if (s.prefix) {
				String match = words[i];
				for (int j = i + 1; j < words.length; j++) {
					match = match + " " + words[j];
					Status ss = dm.get(match);
					if (ss == null)
						break;
					if (ss.match) {
						matched.add(match);
					}
					if (ss.prefix == false) {
						break;
					}
				}
			}
		}

		return matched;
	}

	public static void processDocumentSet(String fromDir, String toDir) {
		TextReader reader = new TextReader(fromDir);
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		EntityMatcher em = new EntityMatcher(eti);
		int docNum = 0;
		try {
			PrintWriter printer = new PrintWriter(new FileWriter(
					toDir + "string.txt"));
			PrintWriter printer2 = new PrintWriter(new FileWriter(
					toDir + "id.txt"));
			PrintWriter printer3 = new PrintWriter(new FileWriter(toDir +"text.txt"));

			while (reader.nextFile()) {
				while (reader.nextDoc()) {
					String content = reader.getDocContent();
					printer3.println(content);
					ArrayList<String> list = em.matcher(content);
					for (String entity : list) {
						Integer id = eti.entityIDMap.get(entity);
						printer2.print(id + "\t");
						printer.print(entity + "\t");
					}
					printer.println();
					printer2.println();

					if (docNum % 1000 == 0)
						System.out.println(docNum);
					if (docNum++ > Configure.DOCNUM) {
						break;
					}
					// writer.addDocument(doc);
				}
				if (docNum > Configure.DOCNUM) {
					break;
				}
			}
			printer.close();
			printer2.close();
			printer3.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		EntityMatcher em = new EntityMatcher(eti);

		ArrayList<String> list = em
				.matcher("test old time   baseball myth war online test movie");
		for (String str : list) {
			System.out.println(str);
		}
	}
}
