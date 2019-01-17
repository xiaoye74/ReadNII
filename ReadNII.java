package com.qianxiang.practice;

import java.io.*;

public class ReadNII {
    /**
     *  调用python脚本执行nii数据转换
     * @param scriptName python 脚本所在路径
     * @param inputFileName -i的标志的输入，表示输入的nii格式数据
     * @param outputFileDir -o的标志的输出路径，会在该路径下创建xx-xx-xx-xx.txt的文件
     * @return 判断是否执行成功，0表示成功，1表示脚本调用失败，-1表示文件读写失败
     */
    public static int produce(String scriptName, String inputFileName, String outputFileDir)
    {
        File file1 = new File(inputFileName);
        if (!file1.exists())
        {
            System.out.println("文件不存在，请核实文件是否有效");
            return -1;
        }
        else if (inputFileName.endsWith("\\.nii"))
        {
            System.out.println("文件不符合要求，请提供NII格式的文件");
            return -1;
        }
        file1 = new File(outputFileDir);
        if (!file1.exists()) {
            System.out.println("输出路径不存在，将自动创建");
            file1.mkdirs();
        }
        int success = 1;
        String command = "python "+scriptName+" -i "+inputFileName+" -o "+outputFileDir;
        String line;
        try {
            Process pr = Runtime.getRuntime().exec(command);
            success = pr.waitFor();
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            while ((line = in.readLine()) != null)
                System.out.println(line);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 用于从生成的txt文件中读取真实数据
     * @param inputFileName nii格式转换成的txt文件的路径
     * @return 从txt文件中读取出的数字字符串数组
     * @throws IOException 在文件读写时可能出现文件失败，读取失败的可能。
     * @warning 注意本方法采用一次读取文件进入一个字节数组，所以该文件最大只能是int的最大值个字节
     */
    public static String[] getValues(String inputFileName) throws IOException {
        File file = new File(inputFileName);
        if (!file.exists())
        {
            System.out.println("文件无效，请核实文件");
            return null;
        }
        long len = file.length();
        System.out.println(len);
        byte[] values = new byte[(int)(len+10)];
        FileInputStream in = new FileInputStream(file);
        int lenFile = in.read(values);
        String s = new String(values,0,lenFile);
        String[] res = s.split(",");
        return res;
    }

    /**
     * 从文件名中解析出数据的维度，因为txt文件中没有保存原来的数据维度，而是把维度保存在文件名中
     * @param inputFileName nii格式转换成的txt文件的路径
     * @return 一个元素个数为3或4的数组，按顺序为数组的各个维度
     */
    public static int[] getDims(String inputFileName){
        String s1 = null;
        String res[] = null;
        int dim3[] = new int[3];
        int dim4[] = new int[4];
        for (String str : inputFileName.split("\\\\"))
            if (str.matches("(\\d{2}-){2,3}\\d{2}\\.txt"))
                s1 = str;
        if (s1 != null)
            res = s1.split("\\.")[0].split("-");
        if (res.length == 3){
            for (int x = 0; x < 3; ++x)
                dim3[x] = Integer.parseInt(res[x]);
            return dim3;
        }
        else if (res.length == 4){
            for (int x = 0; x < 4; ++x)
                dim4[x] = Integer.parseInt(res[x]);
            return dim4;
            }
        return null;
    }

    /**
     * 从三维nii文件中读取出int型数组
     * @param datas 数字字符串数组
     * @param dims 数据维度数组
     * @return 生成的int型数组
     */
    public static int[][][] changeValuesInt3(String[] datas, int[] dims){
        int[][][] result = new int[dims[0]][dims[1]][dims[2]];
        if (datas.length != dims[0]*dims[1]*dims[2]){
            System.out.println("dims not right");
            return null;
        }
        for (int x = 0; x < datas.length; ++x)
            result[x/(dims[1]*dims[2])][x%(dims[1]*dims[2])/dims[2]][x%dims[2]] = Integer.parseInt(datas[x]);
        System.out.println("change sucessfully");
        return result;
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int suc = ReadNII.produce("C:\\Users\\98282\\Desktop\\readnii.py","C:\\Users\\98282\\Desktop\\0.nii","C:\\Users\\98282\\Desktop\\data");
        if (suc == 0)
            System.out.println("sucess");
        else
            System.out.println("error");


        String s = "C:\\Users\\98282\\Desktop\\data\\79-95-79.txt";
        //System.out.println("79-95-79.txt".matches("(\\d{2}-){2,3}\\d{2}\\.txt"));
        String[] ss = null;
        try {
            ss = ReadNII.getValues(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[] v = ReadNII.getDims(s);

        int[][][] res = ReadNII.changeValuesInt3(ss,v);
        long end = System.currentTimeMillis();
        System.out.println("cost(ms):"+(end-start));

    }
}
