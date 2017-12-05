package com.search_engine;

import java.nio.file.FileSystems;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedNumericSortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Search {
	private static String INDEXDIR = "/Users/fanny/Documents/workspace/SearchEngine/index";
	
	public static JSONArray searchByRelevence(String keyWord) throws Exception{
		Analyzer analyzer = null;
		Directory directory = null;
		DirectoryReader directoryReader = null;
		
		String prefixHTML = "<font color='red'>";
		String suffixHTML = "<//font>";
		
		JSONObject jsonObj=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		
		System.out.println("search");
		try {
			analyzer = new IKAnalyzer(true);
			directory = FSDirectory.open(FileSystems.getDefault().getPath(INDEXDIR));
			directoryReader = DirectoryReader.open(directory);
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
			QueryParser queryParser = new QueryParser("content", analyzer);
			Query query = queryParser.parse(keyWord);
			TopDocs topDocs = indexSearcher.search(query, 100); 
			
			ScoreDoc[] hits = topDocs.scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = indexSearcher.doc(hits[i].doc);
				//2.高亮显示、摘要生成
				//SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(prefixHTML, suffixHTML);    
		        //Highlighter highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(query));
				//String hlt = highlighter.getBestFragment(analyzer, "content", hitDoc.get("content"));
				//System.out.println(hlt);
				jsonObj.put("url", hitDoc.get("url"));  
				jsonObj.put("title", hitDoc.get("name"));  
				jsonObj.put("contents", hitDoc.get("content"));    
				jsonObj.put("add_time", hitDoc.get("time"));  
				jsonArray.add(jsonObj);
				
				//3.相似新闻查找
				/*
				int numDocs = indexReader.maxDoc();
				MoreLikeThis moreLikeThis = new MoreLikeThis(indexReader);
				
				moreLikeThis.setMinTermFreq(2);
				moreLikeThis.setMinDocFreq(1);
				moreLikeThis.setFieldNames(new String[] {"name", "content"});
				moreLikeThis.setAnalyzer(analyzer);
				System.out.println();
				//Document document = indexReader.document(i);
				//System.out.println(hitDoc.get("content"));
				System.out.println(Integer.parseInt(hitDoc.get("id")));
				System.out.println();

				Query simlarquery = moreLikeThis.like(Integer.parseInt(hitDoc.get("id")) - 1);
				System.out.println("similarquery = " + simlarquery);
				System.out.println();

				TopDocs simlartopDocs = indexSearcher.search(simlarquery, 2);
				if (simlartopDocs.totalHits == 0)
					System.out.println("None like this");
				else {
					for (int j = 0; j < simlartopDocs.scoreDocs.length; j++) {
						if (simlartopDocs.scoreDocs[j].doc != Integer.parseInt(hitDoc.get("id")) - 1) {
							Document doc = indexReader.document(simlartopDocs.scoreDocs[j].doc);
							System.out.println(doc.getField("id").stringValue());
							System.out.println(doc.getField("content").stringValue());
							System.out.println();
							}
						}
					}
					*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (analyzer != null) analyzer.close();
				if (directoryReader != null) directoryReader.close();
				if (directory != null) directory.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}
	
	public static JSONArray searchByPattern(String keyWord, String pattern) throws Exception{
		Analyzer analyzer = null;
		Directory directory = null;
		DirectoryReader directoryReader = null;
		TopDocs topDocs = null;
		
		String prefixHTML = "<font color='red'>";
		String suffixHTML = "<//font>";
		
		JSONObject jsonObj=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		
		try {
			analyzer = new IKAnalyzer(true);
			directory = FSDirectory.open(FileSystems.getDefault().getPath(INDEXDIR));
			directoryReader = DirectoryReader.open(directory);
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
			QueryParser queryParser = new QueryParser("content", analyzer);
			Query query = queryParser.parse(keyWord);
			
			if (pattern == "hot") {
				//按热度排序
				
			} else if (pattern.equals("time")) {
				//按时间排序
				topDocs = indexSearcher.search(query,100,new Sort(new SortedNumericSortField("longtime",SortField.Type.LONG,true)));
			}
			
			System.out.println("匹配" + keyWord + "，总共查询到" + topDocs.totalHits + "个文档");
			ScoreDoc[] hits = topDocs.scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = indexSearcher.doc(hits[i].doc);
				//2.高亮显示、摘要生成
				//SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(prefixHTML, suffixHTML);    
		        //Highlighter highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(query));
				//String hlt = highlighter.getBestFragment(analyzer, "content", hitDoc.get("content"));
				//System.out.println(hlt);
				jsonObj.put("url", hitDoc.get("url"));  
				jsonObj.put("title", hitDoc.get("name"));  
				jsonObj.put("contents", hitDoc.get("content"));    
				jsonObj.put("add_time", hitDoc.get("time"));  
				jsonArray.add(jsonObj);
				
				//3.相似新闻查找
				int numDocs = indexReader.maxDoc();
				MoreLikeThis moreLikeThis = new MoreLikeThis(indexReader);
				
				moreLikeThis.setMinTermFreq(2);
				moreLikeThis.setMinDocFreq(1);
				moreLikeThis.setFieldNames(new String[] {"name", "content"});
				moreLikeThis.setAnalyzer(analyzer);
				System.out.println();
				//Document document = indexReader.document(i);
				//System.out.println(hitDoc.get("content"));
				System.out.println(Integer.parseInt(hitDoc.get("id")));
				System.out.println();

				Query simlarquery = moreLikeThis.like(Integer.parseInt(hitDoc.get("id")) - 1);
				System.out.println("similarquery = " + simlarquery);
				System.out.println();

				TopDocs simlartopDocs = indexSearcher.search(simlarquery, 2);
				if (simlartopDocs.totalHits == 0)
					System.out.println("None like this");
				else {
					for (int j = 0; j < simlartopDocs.scoreDocs.length; j++) {
						if (simlartopDocs.scoreDocs[j].doc != Integer.parseInt(hitDoc.get("id")) - 1) {
							Document doc = indexReader.document(simlartopDocs.scoreDocs[j].doc);
							System.out.println(doc.getField("id").stringValue());
							System.out.println(doc.getField("content").stringValue());
							System.out.println();
							}
						}
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (analyzer != null) analyzer.close();
				if (directoryReader != null) directoryReader.close();
				if (directory != null) directory.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}
}