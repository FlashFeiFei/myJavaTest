import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Application {

    public static void main(String args[]) throws IOException, ParseException {

        //创建分析器,写入和查询，同一个倒排索引处理方式

        //默认的中文分析器

        Analyzer analyzer = new IKAnalyzer();


        //针对不同字段用的不同分析器
        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        analyzerPerField.put("namePingYin", new IndexPinyinAnalyzer(false));

        //默认分析器 + 指定字段分析器
        PerFieldAnalyzerWrapper perFieldAnalyzerWrapper = new PerFieldAnalyzerWrapper(analyzer, analyzerPerField);


        //索引保存路径
        Path indexPath = Paths.get("D:\\javaProject\\myJavaTest\\luceneTest\\src\\main\\resources\\lucene\\indexDir");

        //索引配置类
        Directory directory = FSDirectory.open(indexPath);


        //默认采用的分析器
        IndexWriterConfig config = new IndexWriterConfig(perFieldAnalyzerWrapper);


        //创建索引具体
        IndexWriter indexWriter = new IndexWriter(directory, config);

        //数据源
        Product[] data = {
                new Product().setId(1).setName("雀巢咖啡").setCategory("雀巢").setPrice(30.2f),
                new Product().setId(2).setName("咖啡伴侣").setCategory("雀巢").setPrice(20.2f),
                new Product().setId(3).setName("香柚莫奇托风味咖啡").setCategory("雀巢").setPrice(20.2f),
                new Product().setId(4).setName("南方芝麻糊").setCategory("芝麻糊").setPrice(20.2f),
                new Product().setId(5).setName("删除文档测试").setCategory("lucene").setPrice(20.2f),
                new Product().setId(7).setName("雀巢全脂奶").setCategory("雀巢").setPrice(22.2f),
                //ik分词器扩展词典测试
                new Product().setId(8).setName("中国有嘻哈朱宗学的溉歌").setCategory("中国有嘻哈").setPrice(22.2f)
        };
        //文档添加

        for (Product product : data) {

            Document document = createDocument(product.getId(), product.getName(), product.getCategory(), product.getPrice());

            //添加文档
            indexWriter.addDocument(document);
        }

        /**
         * 更新文档，局部更新只能更新原子操作的自增自减操作，
         * 要想更新某个非 NUMERIC or BINARY类型的数据，只能删除整个文档，然后重新录入整个文档
         */
        //更新文档,如果没有则创建
        indexWriter.updateDocument(new Term("id", "6"), createDocument(6, "南方黑芝麻糊", "芝麻糊", 60.4f));

        //删除一个文档
        indexWriter.deleteDocuments(new Term("id", "5"));

//        //commit，将内从中的数据提交到磁盘，防止断点数据丢失
//        indexWriter.commit();

        //文档数据

        System.out.println("文档数据-------------");

        System.out.println("如果此索引有删除（包括缓冲删除），则返回 true。:" + indexWriter.hasDeletions());
        System.out.println("返回当前在 RAM 中缓冲的文档数:" + indexWriter.numRamDocs());


        //关闭写
        indexWriter.close();


        //文档搜索测试搜索功能

        DirectoryReader ireader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(ireader);

        //查询:
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        //模糊匹配查询，es那句话，searKeyWord会用同一个分析器进去特征提取
//        builder.add(new QueryParser("name",perFieldAnalyzerWrapper).parse("咖啡伴侣"), BooleanClause.Occur.MUST);


        //拼音搜索
//        builder.add(new QueryParser("namePingYin", perFieldAnalyzerWrapper).parse("giao"), BooleanClause.Occur.MUST);
        builder.add(new QueryParser("namePingYin", perFieldAnalyzerWrapper).parse("zhima"), BooleanClause.Occur.MUST);

//        //精确查找
//        builder.add(new TermQuery(new Term("category","雀巢")), BooleanClause.Occur.MUST);

//        //价格范围查询
//        builder.add(FloatPoint.newRangeQuery("price",10f,30f), BooleanClause.Occur.MUST);


//        //排序
//        Sort sort = new Sort();
//        //按照price asc 升序排序
//        sort.setSort(new SortField("price",SortField.Type.FLOAT,false));

        //搜索，返回前n个结果
        ScoreDoc[] hits = indexSearcher.search(builder.build(), 10).scoreDocs;


        System.out.println("搜索测试---------------------------");

        //查看搜索结果:
        for (int i = 0; i < hits.length; i++) {

            Document hitDoc = indexSearcher.doc(hits[i].doc);

            System.out.println(String.format(" %s, 相似度得分 %f", hitDoc.toString(), hits[i].score));


        }


        System.out.println("单条文档id查询-------------");


        //程序结束，关闭各种资源

        //读关闭
        ireader.close();
        directory.close();

        //删除索引库
        IOUtils.rm(indexPath);

    }

    private static Document createDocument(long id, String name, String category, float price) {
        //文档（定义一个表）
        Document doc = new Document();

        //StringField 构建一个字符串的Field,但不会进行分词,将整串字符串存入索引中,适合存储固定(id,身份证号,订单号等)
        doc.add(new StringField("id", id + "", Field.Store.YES));

        //TextField 分词、索引 ；一般此对字段需要进行检索查询
        doc.add(new TextField("name", name, Field.Store.YES));
        doc.add(new TextField("namePingYin", name, Field.Store.YES));
        doc.add(new StringField("category", category, Field.Store.YES));

        //FloatPoint 倒排索引, 浮点形范围查询索引
        doc.add(new FloatPoint("price", price));

        //FloatDocValuesField 正排索引用于排序、聚合
        doc.add(new FloatDocValuesField("price", price));

        return doc;
    }
}
