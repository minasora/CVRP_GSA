
import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.*;

/* VRP问题的模型*/
class Vehicle
{
    ArrayList<Customer> route_number = new ArrayList<>();//用列表存放车辆路径
    double route_weight = 0; //车辆路径的当前容量
    double route_length = 0; //长度
    double CAPACITY = 0;//容量约束
    public void setRoute_weight() {
        this.route_weight = 0;
        for(Customer c : route_number)
        {
            this.route_weight += c.goods_need;
        }
    }
    public void setRoute_length()//获取当前路径的长度
    {

        //开头和结尾
        this.route_length = 0;
        this.route_length += C_VRP.cost_matrix[C_VRP.depot.id][route_number.get(0).id];
        this.route_length += C_VRP.cost_matrix[C_VRP.depot.id][route_number.get(route_number.size()-1).id];

        for(Customer c : route_number)
        {
            if(c.to_id == 0)break;
            this.route_length += C_VRP.cost_matrix[c.id][c.to_id];
        }
    }
}


class Solution
{
    ArrayList<Vehicle> route_Vehicle = new ArrayList<>();//用列表存放车辆路径
    int vehicle_number = 0;//车辆数量
    int total_length = 0 ;//路径总长
    public void setTotal_length()//获取总路径长度
    {
        this.total_length = 0;
        for(Vehicle v:route_Vehicle) this.total_length += v.route_length;
    }

}


class Customer//顾客类，
{
    int id;//id
    int x;
    int y;//顾客位置
    int goods_need;//需求量
    int to_id;//要去的下一个目标
    public void setCustomer(int x,int y,int id,int goods_need)//初始化方法
    {
        this.x = x;
        this.y = y;
        this.id = id;
        this.goods_need = goods_need;
        this.to_id = id;
    }

}
class  Depot extends Customer //配送中心，继承顾客类，多态
{
    int goods_need = 0;
}
public class C_VRP {
    static int CAPACITY = 0;
    static int vehicleNumber = 0;//车辆数量
    static int customerNumber = 0;//顾客数量
    static double[][] cost_matrix; // 距离矩阵，方便调用
    static ArrayList<Customer> customers = new ArrayList<>();
    static Depot depot;

    public static double distance(Customer a, Customer b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));//计算两个位置
    }

    public static void input_txt(String input)//从txt文件里读取,输入文件格式参照TSPLIB95
    {
        File input_data = new File(input);

        try {
            BufferedReader br = new BufferedReader(new FileReader(input_data));
            String line = " ";
            int i = 1;
            System.out.println("————————————————————输入中——————————————————");
            while (true) {

                line = br.readLine();
                System.out.println(line);
                if (line.equals("END")) {
                    System.out.println("输入结束");
                    break;
                }
                String str[] = line.split(" ");


                if (i == 1) {
                    customerNumber = Integer.valueOf(line)-1;
                    vehicleNumber = customerNumber;//没有车辆约束，车辆数量就等于顾客数量
                } else if (i == 2) {
                    CAPACITY = Integer.valueOf(line);
                } else if (i == 3) {
                    depot = new Depot();
                    depot.setCustomer(Integer.valueOf(str[1]), Integer.valueOf(str[2]), Integer.valueOf(str[0])-1, 0);
                    customers.add(depot);
                } else {
                    Customer cur_customer = new Customer();
                    cur_customer.setCustomer(Integer.valueOf(str[1]), Integer.valueOf(str[2]), Integer.valueOf(str[0]) - 1, Integer.valueOf(str[3]));
                    customers.add(cur_customer);
                }
                i++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void solutionPrinter(Boolean If_io, Solution solution)//输出解，可以输出到文件和其他
    {
        if (If_io)//
        {
            print(solution);
        } else {
            try {
                PrintStream ps = new PrintStream(new FileOutputStream("result.txt"));
                System.setOut(ps);
                print(solution);
            } catch (IOException e) {

            }

        }

    }

    public static void print(Solution solution) {

        for (int i = 0; i < solution.route_Vehicle.size(); i++) {
            System.out.println("Vehicle" + i + ":");
            Vehicle cur_Vehicle = solution.route_Vehicle.get(i);
            System.out.print("0-");
            for (Customer number : cur_Vehicle.route_number) {
                System.out.print(number.id);
                if(number.to_id!=0) System.out.print("-");

            }
            System.out.print("-0");
            System.out.println("车辆"+i+"的长度:"+cur_Vehicle.route_length);
            System.out.println("车辆"+i+"的承载货物:"+cur_Vehicle.route_weight);
        }
        System.out.println("该解总花费为" + solution.total_length);


    }

    public static void Initialization()//初始化距离矩阵
    {
        cost_matrix = new double[customerNumber+1][customerNumber+1];//指向引用
        for (int i = 0; i < customerNumber+1; i++)
            for (int j = 0; j < customerNumber+1; j++) {
                if (i == 0)//配送中心的情况
                {
                    cost_matrix[i][j] = distance(depot, customers.get(j));
                    continue;
                }
                if (j == 0) {
                    cost_matrix[i][j] = distance(customers.get(j), depot);
                }
                cost_matrix[i][j] = distance(customers.get(i), customers.get(j));

            }

    }
    public static Solution ini_solution()//返回将每个点单独和depot连接的solution
    {
        Solution solution = new Solution();
        for(int i=0;i<customerNumber;i++)
        {
            Vehicle vehicle = new Vehicle();
            customers.get(i+1).to_id = 0;
            vehicle.route_number.add(customers.get(i+1));
            vehicle.setRoute_length();
            vehicle.setRoute_weight();
            solution.route_Vehicle.add(vehicle);
            solution.setTotal_length();
        }
        return  solution;

    }
    public static Solution random_solution()//随机生成一个solution
    {
        Solution solution = new Solution();
        ArrayList<Customer> Candidate_customer = new ArrayList<>();//候选解
        for (Customer customer: customers)
        {
            Candidate_customer.add(customer);//开始时所有顾客都是候选解：
            Candidate_customer.remove(depot);//先把中心移除

        }
        Random r = new Random();
        Vehicle vehicle = new Vehicle();
        while(Candidate_customer.size()!=0)//循环直到候选顾客数变为0
        {

            int i = r.nextInt(Candidate_customer.size());

            if(vehicle == null)
            {
                 vehicle = new Vehicle();//循环之后
            }

            if((vehicle.route_weight + Candidate_customer.get(i).goods_need) <=CAPACITY) //贪婪算法，尽可量的服务顾客
            {
                if(vehicle.route_number.size()!=0) vehicle.route_number.get(vehicle.route_number.size()-1).to_id = Candidate_customer.get(i).id;
                vehicle.route_number.add(Candidate_customer.get(i));
                Candidate_customer.remove(i);//加入的就移除

                vehicle.setRoute_length();
                vehicle.setRoute_weight();//更新路径
            }
            else {
                vehicle.route_number.get(vehicle.route_number.size()-1).to_id = 0;//最后一个设回起点

                solution.route_Vehicle.add(vehicle);

                vehicle = null;
            }
        }
        solution.setTotal_length();


        return solution;
    }

    public static Vehicle random_Vechile()//随机生成一个Vehicle
    {
        Vehicle vehicle = new Vehicle();
        return vehicle;
    }

    public static Solution neibor_solution(Solution last_solution)//邻域操作
    {
        Solution solution = new Solution();


        return solution;
    }

    public static void main(String args[]) {
        input_txt("input.vrp");
        Initialization();
        Solution solution = ini_solution();
        solutionPrinter(true,solution);
        for(Customer each :solution.route_Vehicle.get(0).route_number)
        {
            System.out.print(each.id+ " ");
            System.out.print(each.goods_need);
            System.out.println();
        }
        }


    }
