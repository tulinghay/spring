import com.godfrey.pojo.Books;
import com.godfrey.service.BookService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author godfrey
 * @since 2020-05-23
 */
public class MyTest {

    @Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookService bookService = context.getBean("BookServiceImpl", BookService.class);
        for (Books books : bookService.queryAllBook()) {
            System.out.println(books);
        }
    }
}
