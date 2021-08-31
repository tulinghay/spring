package com.godfrey.service;

import com.godfrey.pojo.Books;

import java.util.List;

/**
 * description : 业务层接口
 *
 * @author godfrey
 * @since 2020-05-23
 */
public interface BookService {
    //增加一个Book
    int addBook(Books book);

    //根据id删除一个Book
    int deleteBookById(int id);

    //更新Book
    int updateBook(Books book);

    //根据id查询,返回一个Book
    Books queryBookById(int id);

    //查询全部Book,返回list集合
    List<Books> queryAllBook();

    //根据书名查询书籍
    List<Books> queryBookByName(String bookName);
}
