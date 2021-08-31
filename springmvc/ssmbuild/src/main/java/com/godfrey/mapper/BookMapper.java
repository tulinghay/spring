package com.godfrey.mapper;

import com.godfrey.pojo.Books;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description : BookMapper
 *
 * @author godfrey
 * @since 2020-05-23
 */
public interface BookMapper {

    //增加一个Book
    int addBook(Books book);

    //根据id删除一个Book
    int deleteBookById(@Param("booID") int id);

    //更新Book
    int updateBook(Books books);

    //根据id查询,返回一个Book
    Books queryBookById(@Param("booID") int id);

    //查询全部Book,返回list集合
    List<Books> queryAllBook();

    //根据书名查询书籍
    List<Books> queryBookByName(@Param("bookName") String bookName);
}
