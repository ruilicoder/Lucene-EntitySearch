package entitySearch.plan;

import java.io.File;

import entitySearch.Configure;
import entitySearch.index.DocumentEntityIndex;
import entitySearch.index.KeywordDocumentEntityIndex;
import entitySearch.index.KeywordDocumentIndex;
import entitySearch.index.TypeDocumentEntityIndex;
import entitySearch.index.TypeEntityDocumentIndex;
import entitySearch.index.TypeKeywordDocumentIndex;

public class DiskSpace {
	public DiskSpace() {
		double kds = sizeCalculateSize(Configure.indexDir + KeywordDocumentIndex.index);
	
		double des = this.sizeCalculateSize(Configure.indexDir + DocumentEntityIndex.index);
		
		double ets = this.sizeCalculateSize(Configure.entityList);
		
		
		double teds  = this.sizeCalculateSize(Configure.indexDir + TypeEntityDocumentIndex.index);
		
		double tkds = this.sizeCalculateSize(Configure.indexDir + TypeKeywordDocumentIndex.index);
		
		double tdes = this.sizeCalculateSize(Configure.indexDir + TypeDocumentEntityIndex.index);
		
		double tkdes = this.sizeCalculateSize(Configure.indexDir + KeywordDocumentIndex.index);
		
		double kdes  = this.sizeCalculateSize(Configure.indexDir + KeywordDocumentEntityIndex.index);
		System.out.println(kds);
		double b1 = kds + des + ets;
		double b2 = kds + teds;
		double p1 = tkds + tdes;
		double p2 = tkdes + tkds;
		double p3 = tkdes;
		double p4 = kds + tdes;
		double p5 = kdes + ets;
		System.out.println(b1 + "\t" + b2 + "\t" + p1 + "\t" + p2 + "\t" + p3 + "\t" + p4 + "\t" +p5);
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
