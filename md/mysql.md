```
SELECT * FROM table LIMIT [offset,] rows | rows OFFSET offset

```





## 分页LIMIT

```sql
#求前5行数据
select * from demo limit 5
#求第6行到最后所有数据,-1代表最后的所有数据
select * from demo limit 6,-1
#求第6行到第15行数据，从第5行开始后面10行
select * from demo limit 5,10
```

