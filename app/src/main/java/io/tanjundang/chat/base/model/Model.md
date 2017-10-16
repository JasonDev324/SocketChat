## Model的作用
* Model用于对数据库的数据进行备份，用于数据缓存。

Model类的实现：

* 定义一个静态泛型类,用于封装读写方法。
* 创建一个BUFFER目录，使文件保存在/data/data/包名/files/BUFFER目录下
* 如何定义一个保存数据的方法 -- 需要一个上下文Context、一个目录folder、一个保存数据的文件、待保存的数据
* 对文件的读写操作，一般都是对List、Object进行读写

