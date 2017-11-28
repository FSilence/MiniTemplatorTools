# MiniTemplatorTools
针对MiniTemplator添加扩展工具  

Minitemplator官网： http://www.source-code.biz/MiniTemplator/  

1.添加注解，支持从POJO直接映射到Templator
MTValue 对应 Value 
MTBlocks 对应 Blocks 

<pre>

<pre>
```java
public class A {
  @MTValue(name = "a')
  int a;
  @MTBlocks(name = "b")
  public List<B> bList;
  
}

public class B {
  @MTValue(name = "a")
  public int a;
}
```

生成模板

···
public static void main(String[] args) {
  //获取MiniTemplator对象
  ...
  //给MinitTemplator对象后追加映射 对象A
   MTAnnotionUtils.appendMT(mt, new A());
}
···




