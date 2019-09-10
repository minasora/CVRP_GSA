import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

/*********************************************
 * c-w节约算法的实现
 * 传入solution，返回一个solution，solution是节约后的solution
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * *******************************************/
class C_w_value//
{

    int from_id;
    int to_id;//两点
    double value;//两点间的节约值

    public void setValue(int from_id,int to_id) {
        this.value = C_VRP.cost_matrix[0][to_id]+C_VRP.cost_matrix[0][from_id]-C_VRP.cost_matrix[from_id][to_id];
    }
}
public class CW_algrithm {
    static Comparator<C_w_value> c_w_valueComparator = new Comparator<>() {
        @Override
        public int  compare(C_w_value o1, C_w_value o2) {
           if(o1.value>o2.value)return  1;
           else return -1;
        }
    };//重写比较器，返回大的
    static Queue<C_w_value> c_w_values = new PriorityQueue<>(c_w_valueComparator);//用优先队列存储节约值

    public static void c_w_init(int customer_number)//初始化C_W的优先队列
    {
        for(int i=1;i<=customer_number;i++)
            for(int j =i;j<=customer_number;j++)
            {
                if(i == j)continue;
                C_w_value c_w_value = new C_w_value();
                c_w_value.from_id = i;
                c_w_value.to_id = j;
                c_w_value.setValue(i,j);//更新节约值
                c_w_values.add(c_w_value);//加入到优先队列中

            }
    }
    public static void c_w_al(Solution solution)
    {
        Vehicle vehicle = new Vehicle();
        while(c_w_values.size()!=0)//不断取点直到没点
        {
            if(vehicle == null) {
                 vehicle = new Vehicle();
            }

            C_w_value cur_c_w = new C_w_value();
            cur_c_w = c_w_values.poll();
            if(C_VRP.customers.get(cur_c_w.from_id).goods_need+C_VRP.customers.get(cur_c_w.to_id).goods_need+vehicle.route_weight<=C_VRP.CAPACITY)//小于容量约束
            {
                C_VRP.customers.get(cur_c_w.from_id).to_id = cur_c_w.to_id;
                C_VRP.customers.get(cur_c_w.to_id).to_id = 0;
                vehicle.route_number.add(C_VRP.customers.get(cur_c_w.from_id));
                vehicle.route_number.add(C_VRP.customers.get(cur_c_w.to_id));
                Iterator<C_w_value> itor = c_w_values.iterator();
                while(itor.hasNext())
                {
                    C_w_value c_w_value = itor.next();
                    if(c_w_value.from_id == cur_c_w.from_id )
                        itor.remove();
                }
                vehicle.setRoute_weight();
                vehicle.setRoute_length();
            }
            else
            {
                solution.route_Vehicle.add(vehicle);
                vehicle = null;//不满足约束就新开路线
            }
            solution.setTotal_length();
        }

    }
    static Solution C_W(int customer_number)
    {

        c_w_init(customer_number);
        Solution solution = new Solution();
        c_w_al(solution);
        return solution;
    }

}
