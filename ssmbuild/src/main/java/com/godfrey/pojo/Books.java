package com.godfrey.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * description : 实体类
 *
 * @author godfrey
 * @since 2020-05-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Books implements Serializable {
    private static final long serialVersionUID = -52398968511286469L;
    private Integer bookID;
    private String bookName;
    private Integer bookCounts;
    private String detail;
}
