import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.analysis.PinyinConfig;
import org.elasticsearch.index.analysis.PinyinTokenFilter;
import org.wltea.analyzer.lucene.IKTokenizer;

/**
 * ik中文分词+拼音中文分词做自定义拼音分析器
 * 拼音分析器
 */
public class IndexPinyinAnalyzer extends Analyzer {

    private boolean userSmart;

    public IndexPinyinAnalyzer(boolean userSmart) {
        this.userSmart = userSmart;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {

        IKTokenizer ikTokenizer = new IKTokenizer(userSmart);

        PinyinTokenFilter pinyinTokenFilter = new PinyinTokenFilter(ikTokenizer, new PinyinConfig());

        return new Analyzer.TokenStreamComponents(ikTokenizer, pinyinTokenFilter);
    }
}
