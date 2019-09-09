import java.util.Comparator;
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
public class CW_algrithm {
    static Comparator<c_w_value> c_w_valueComparator = new Comparator<>() {
        @Override
        public int  compare(c_w_value o1, c_w_value o2) {
           if(o1.value>o2.value)return  1;
           else return -1;
        }
    };//重写比较器，返回大的
    Queue<c_w_value> c_w_values = new PriorityQueue<>();//用优先队列存储节约值


    class c_w_value//在C_W节约算法中继承Customer类
    {
        int from_id;
        int to_id;//两点
        double value;//两点间的节约值

        public void setValue(int from_id,int to_id) {
            this.value = C_VRP.cost_matrix[0][to_id]+C_VRP.cost_matrix[0][from_id]-C_VRP.cost_matrix[from_id][to_id];
        }
    }
    public void c_w_init(int number)
    {
        for(int i=0;i<)
    }
    static Solution C_W(Solution solution)
    {
        return solution;
    }

}
