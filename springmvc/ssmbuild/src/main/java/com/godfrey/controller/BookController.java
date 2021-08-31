package com.godfrey.controller;

import com.godfrey.pojo.Books;
import com.godfrey.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description : Controller
 *
 * @author godfrey
 * @since 2020-05-23
 */
@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    @Qualifier("BookServiceImpl")
    private BookService bookService;

    //1.展示所有书籍
    @GetMapping("allBook")
    public String list(Model model) {
        List<Books> list = bookService.queryAllBook();
        model.addAttribute("list", list);
        return "allBook";
    }

    //2.1跳转到添加书籍页面
    @GetMapping("toAddBook")
    public String toAddPaper() {
        return "addBook";
    }

    //2.2添加书籍
    @PostMapping("addBook")
    public String addPaper(Books books) {
        bookService.addBook(books);
        return "redirect:/book/allBook";
    }

    //3.1跳转到修改书籍页面
    @GetMapping("toUpdateBook/{bookID}")
    public String toUpdateBook(Model model, @PathVariable("bookID") int id) {
        Books books = bookService.queryBookById(id);
        model.addAttribute("book", books);
        return "updateBook";
    }

    //3.2修改书籍信息
    @RequestMapping("updateBook")
    public String updateBook(Model model, Books book) {
        bookService.updateBook(book);
        return "redirect:/book/allBook";
    }

    //4.删除书籍
    @RequestMapping("del/{bookID}")
    public String deleteBook(@PathVariable("bookID") int id) {
        bookService.deleteBookById(id);
        return "redirect:/book/allBook";
    }

    //5.查询书籍
    @PostMapping("queryBook")
    public String queryBook(Model model, String queryBookName) {
        List<Books> list = bookService.queryBookByName(queryBookName);
        if (list.isEmpty()) {
            list = bookService.queryAllBook();
            model.addAttribute("error","未查到");
        }
        model.addAttribute("list", list);
        return "allBook";
    }
}
