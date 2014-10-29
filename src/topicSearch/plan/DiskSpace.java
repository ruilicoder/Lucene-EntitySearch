package topicSearch.plan;

import java.io.File;

import topicSearch.index.DocTopicIndex;
import topicSearch.index.DocumentTimeIndex;
import topicSearch.index.KeywordTopicDocumentIndex;
import topicSearch.index.TimeDocumentIndex;
import topicSearch.index.TimeKeywordDocumentIndex;
import topicSearch.index.TimeKeywordTopicIndex;
import entitySearch.Configure;
import entitySearch.index.KeywordDocumentIndex;

public class DiskSpace {
	public DiskSpace() {
		double kds = sizeCalculateSize(Configure.indexDir + KeywordDocumentIndex.index);
	
		System.out.println(kds);
		double des = this.sizeCalculateSize(Configure.indexDir + DocTopicIndex.index);

		System.out.println(des);
		double dti = this.sizeCalculateSize(Configure.indexDir + TimeDocumentIndex.index);
		
		System.out.println(dti);
		
				
		double dts = this.sizeCalculateSize(Configure.indexDir + DocumentTimeIndex.index);
		
		double teds  = this.sizeCalculateSize(Configure.indexDir + KeywordTopicDocumentIndex.index);
		

		System.out.println(teds);
		double tkds = this.sizeCalculateSize(Configure.indexDir + TimeKeywordDocumentIndex.index);

		System.out.println(tkds);

		double tkdts = this.sizeCalculateSize(Configure.indexDir + TimeKeywordTopicIndex.index);
		System.out.println(tkdts);
		
		double b1 = kds + des + dti;
		double b2 = kds + des + dts;
		double p1 = tkds + des;
		double p2 = tkdts + tkds;
		double p3 = tkdts;
		double p4 = tkds + teds;
		//double p5 = kdes + ets;
		System.out.println(b1 + "\t" + b2 + "\t" + p1 + "\t" + p2 + "\t" + p3 + "\t" + p4 +"\t");
	}

	public double sizeCalculateSize(String file) {
		File f = new File(file);
		return getDirSize(f) / 1024 / 1024;
	}

	long getDirSize(File dir) {
		long size = 0;
		if (dir.isFile()) {
			size = dir.length();
		} else {
			File[] subFiles = dir.listFiles();

			for (File file : subFiles) {
				if (file.isFile()) {
					size += file.length();
				} else {
					size += this.getDirSize(file);
				}

			}
		}

		return size;
	}
	public static void main(String[] args) {
		DiskSpace ds = new DiskSpace();
	}
}
