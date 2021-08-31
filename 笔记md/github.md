```bash
#项目级别，仅在当前本地库范围内有效
git config user.name tulinghay
git config user.email 18270213609@163.com
#系统级别：登录当前操作系统的用户范围
git config --global user.name tulinghay
git config --global user.email 18270213609@163.com

#可在.git/config中查看配置
cat .git/config
```



```bash
#查看当前git的状态
git status
#将文件加入暂存区
git add good.txt
#将当前目录所有文件加入暂存区
git add .
#将文件从暂存区中删除
git rm --cached good.txt

#提交暂存区中的good.txt文件
git commit -m "这是第一次提交" good.txt

#查看git日志，每条日志显示一行
git log --pretty=oneline 
#显示简略的索引值
git log --oneline
git reflog

#返回后退版本,--hard后面接的是索引值的一部分，可在log中查看
git reset --hard 9aace91
#当前git所处的版本位置后退一步，一个^符号退1步
git reset --hard HEAD^
#后退3步
git reset --hard HEAD~3


#比较文件内容的区别，不加参数默认是与暂存区的同名文件进行比较
git diff good.txt
#将历史版本的文件同现有的文件比较区分，一个^号代表后退一个版本
git diff HEAD^ good.txt

#创建分支
git branch [分支名]
#查看分支
git branch -v 
#切换分支
git checkout dev-hdt
#合并分支，将dev-hdt分支合并到当前分支
git checkout master 
git merge dev-hdt
#解决分支冲突

#拉取
git pull <远程主机名> <远程分支名>:<本地分支名>



#创建一个新的圆
git remote add origin https://github.com/tulinghay/demo1.git 
#推送项目
git push -u origin master
```







git config user.name tulinghay

git config user.email 18270213609@163.com

- 