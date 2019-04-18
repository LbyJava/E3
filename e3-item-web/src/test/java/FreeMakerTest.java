import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/22 1:32
 */
public class FreeMakerTest {
    @Test
    public void testFreeMakeTest() throws Exception {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("D:\\学习\\练手项目\\e3\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("hello.ftl");
        Map data = new HashMap();
        data.put("hello", "hello freeMaker");
        Writer out = new FileWriter("D:\\学习\\练手项目\\e3\\e3-item-web\\src\\test\\java\\Eriri.txt");
        template.process(data, out);
        out.close();
    }
}
