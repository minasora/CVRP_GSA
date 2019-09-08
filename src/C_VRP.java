import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;
/* VRP问题的模型*/
public class C_VRP {

    final double CAPACITY = 0;//容量约束
    final double DI

    public static double distance(Customer a,Customer b)
    {
        return Math.sqrt((a.x-b.x)*(a.x-b.x) +(a.y - b.y)*(a.y - b.y));//计算两个位置
    }

    public static void input_txt()//从txt文件里读取,输入文件格式参照TSPLIB95
    {
        File input_data = new File("./input");
        try {
            BufferedReader bf = new BufferedReader(new FileReader(input_data));
            String content = " ";
            while(content != null)
            {
                content = bf.readLine();

            }
        }
        catch(IOException e)
        {

        }
    }
    public static void input_io()//命令行读取
    {

    }




        class Route /*路径类，一个路径代表了一个解*/
    {
        ArrayList<Customer> route_number = new ArrayList<>();//用列表存放车辆路径
        double route_weight = 0; //车辆路径的容量
        double route_length = 0;
    }
    class Customer//顾客类，
    {
        int x;
        int y;//顾客位置
        int goods_need;//需求量
        public void setCustomer(int x,int y,int goods_need)//初始化方法
        {
            this.x = x;
            this.y = y;
            this.goods_need = goods_need;
        }

    }
    class  goods_center extends Customer //配送中心，继承顾客类，多态
    {
        int goods_need = 0;
    }

}
