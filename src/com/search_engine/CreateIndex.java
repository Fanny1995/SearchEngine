package com.search_engine;

import java.sql.*;
import java.util.regex.Pattern;
import java.io.*;
import java.nio.*;
import java.nio.file.FileSystems;
import org.apache.lucene.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.function.valuesource.LongFieldSource;
import org.apache.lucene.queryparser.surround.query.AndQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class CreateIndex {
	private static String INDEXDIR = "/Users/wangshicheng/Desktop/index";
	
	public static String numberIntercept(String number) {
		return Pattern.compile("[^0-9]").matcher(number).replaceAll("");
	}
	
	public static void create() {
		DataBase dataBase = new DataBase();
		Analyzer analyzer = null;
		Directory directory  = null;
		IndexWriter indexWriter = null;
		
		try {
			analyzer = new IKAnalyzer(true);
			directory = FSDirectory.open(FileSystems.getDefault().getPath(INDEXDIR));
			IndexWriterConfig indexWriterConfig  = new IndexWriterConfig(analyzer);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			indexWriter.deleteAll();
			
			Connection connection = dataBase.getCon();
			String sql="select * from ir_news_2 limit 10";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
						
			while(rs.next()){
				Document document = new Document();
				
				//System.out.println(rs.getInt("id") + "");
				//System.out.println(rs.getString("url"));
				//System.out.println(rs.getString("name"));
				//System.out.println(rs.getString("time"));
				//System.out.println(numberIntercept(rs.getString("time")));
				//System.out.println(rs.getString("author"));
				//System.out.println(rs.getString("source"));
				//System.out.println(rs.getString("content"));
				
				document.add(new Field("id", rs.getInt("id") + "", TextField.TYPE_STORED));
				document.add(new Field("url", rs.getString("url"), TextField.TYPE_STORED));
				document.add(new Field("name", rs.getString("name"), TextField.TYPE_STORED));
				document.add(new Field("time", rs.getString("time"), TextField.TYPE_STORED));
				document.add(new NumericDocValuesField("longtime", Long.parseLong(numberIntercept(rs.getString("time")))));
				document.add(new StoredField("longtime", Long.parseLong(numberIntercept(rs.getString("time")))));
				//document.add(new NumericDocValuesField("hotdegree", ));
				//document.add(new StoredField("hotdegree", ));
				//document.add(new Field("author", rs.getString("author"), TextField.TYPE_STORED));
				document.add(new Field("source", rs.getString("source"), TextField.TYPE_STORED));
				document.add(new Field("content", rs.getString("content"), TextField.TYPE_STORED));
				indexWriter.addDocument(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (analyzer != null) analyzer.close();
				if (indexWriter != null) indexWriter.close();
				if (directory != null) directory.close();
			} catch (Exception e) {
			e.printStackTrace();}
		}	
	}
}
