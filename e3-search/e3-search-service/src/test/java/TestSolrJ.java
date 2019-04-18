import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/19 15:36
 */
public class TestSolrJ {
    @Test
    public void addDocument()throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr/");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "doc01");
        document.addField("item_title", "测试商品02");
        document.addField("item_price", 2000);
        solrServer.add(document);
        solrServer.commit();
    }

    @Test
    public void testAddDocument()throws Exception {
        CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.128:2182,192.168.128.154:2183,192.168.25.128:2184");
        cloudSolrServer.setDefaultCollection("collection2");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "doc01");
        document.addField("item_title", "测试商品01");
        document.addField("item_price", 2000);
        cloudSolrServer.add(document);
        cloudSolrServer.commit();
    }
}
