## 注解

- @Autowired：自动装配，通过类型，名字
  - 如果@Autowired不能唯一自动装配，则需要通过@Qualifier(value="xxx")来进行指定
- @Nullable ：参数标记了这个注解啧说明字段可为null

- @Resource：自动装配，通过名字类型