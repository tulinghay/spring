package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author HDTurk
 * @since 2021-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("awardorgandep")
public class Awardorgandep implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("badge")
    private String badge;

    @TableField("awardunit")
    private String awardunit;

    @TableField("awardname")
    private String awardname;

    @TableField("awardnametype")
    private String awardnametype;

    @TableField("awardlevel")
    private String awardlevel;

    @TableField("awarddate")
    private String awarddate;

    @TableField("awardstaffname")
    private String awardstaffname;

    @TableField("completedepname")
    private String completedepname;

    @TableField("certno")
    private String certno;

    @TableField("personcerti")
    private String personcerti;

    @TableField("unitcerti")
    private String unitcerti;

    @TableField("remark")
    private String remark;


}
