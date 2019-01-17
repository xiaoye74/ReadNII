# ReadNII
这是一个NTFI格式（.nii）读取的java端实现。采用了java调用python脚本的方法来实现的折中方案
## python读取处理nii文件
在命令行中使用如下命令即可调用
```
python -readnii.py -i inputFileName -o outputDir
```
其中inputFileName应该是xxx.nii的格式的文件，outputDir是要生成文件的路径。在该路径下会生成包含数据维度信息的.txt文件，例如60-70-90.txt即表示数据维度是（60，70，90）
## java端调用
1、调用ReadNII.produce方法转换nii格式文件
2、调用ReadNII.getValues,ReadNII.getDims方法获取数据及维度
3、调用ReadNII.changeValuesInt3读取数据
