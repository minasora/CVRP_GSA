import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

/*********************************************
 * c-w节约算法的实现
 * 传入solution，返回一个solution，solution是节约后的solution
 * 顺序遍历，每次只生成一条循环
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
        public int compare(C_w_value o1, C_w_value o2) {
            if (o1.value > o2.value) return -1;
            else return 1;
        }
    };//重写比较器，返回大的
    static Queue<C_w_value> c_w_values = new PriorityQueue<>(c_w_valueComparator);//用优先队列存储节约值
    static Queue<C_w_value> c_w_values_back = new PriorityQueue<>(c_w_valueComparator);//另一个储存不用的
    static Queue<C_w_value> c_w_values_test = new PriorityQueue<>(c_w_valueComparator);
    public static void c_w_init(int customer_number)//初始化C_W的优先队列
    {
        for (int i = 1; i <= customer_number; i++)
            for (int j = i; j <= customer_number; j++) {
                if (i == j) continue;
                C_w_value c_w_value = new C_w_value();
                c_w_value.from_id = i;
                c_w_value.to_id = j;
                c_w_value.setValue(i, j);//更新节约值
                c_w_values.add(c_w_value);//加入到优先队列中

            }
    }
    public static int  check()
    {
        int i=0;
        for(C_w_value k :c_w_values)
            if(k.to_id==30||k.from_id==30)
                i++;
        for(C_w_value k :c_w_values_back)
            if(k.to_id==30||k.from_id==30)
                i++;

        return i;
    }

    public static void c_w_al(Solution solution) {

        Boolean If_can = false;
        Boolean If_in = false;
        Vehicle vehicle = new Vehicle();
        while (c_w_values.size() != 0) {



            If_in = false;
            C_w_value cur_c_w = c_w_values.poll();
            for(Customer c:vehicle.route_number) {
                if (c.id == 23) {
                    if (cur_c_w.from_id == 23 && solution.route_Vehicle.size() == 1)

                        System.out.println(1);
                }
            }
            if(c_w_values.size() == 0 && c_w_values_back.size() == 0)
                if(vehicle.route_number.size()!=0) {

                    vehicle.route_number.get(vehicle.route_number.size()-1).to_id=0;
                    solution.route_Vehicle.add(vehicle);
                    continue;
                }

            if (vehicle.route_number.size() == 0) {
                vehicle.route_number.add(C_VRP.customers.get(cur_c_w.from_id));
                vehicle.route_number.add(C_VRP.customers.get(cur_c_w.to_id));
                vehicle.route_number.get(0).to_id = cur_c_w.to_id;
                vehicle.set_all();


            } else {
                int first_id = vehicle.route_number.get(0).id;
                int last_id = vehicle.route_number.get(vehicle.route_number.size() - 1).id;//沿着开始和结束找

                if (first_id == cur_c_w.from_id || first_id == cur_c_w.to_id) {
                    Customer c;
                    if (first_id == cur_c_w.from_id) {
                        for(Customer c1 :vehicle.route_number)
                            if(c1.id == cur_c_w.to_id)
                            {
                                If_in = true;
                                break;
                            }
                        if(If_in)
                        {
                            If_in = false;
                            continue;
                        }
                        c = C_VRP.customers.get(cur_c_w.to_id);
                    }
                    else{

                            for(Customer c1 :vehicle.route_number)
                                if(c1.id == cur_c_w.from_id)
                                {
                                    If_in = true;
                                    break;
                                }
                            if(If_in)
                            {

                                continue;
                            }
                        c = C_VRP.customers.get(cur_c_w.from_id);
                    }
                    if (vehicle.route_weight + c.goods_need < C_VRP.CAPACITY) {

                        c.to_id = vehicle.route_number.get(0).id;
                        vehicle.route_number.add(0, c);
                        vehicle.set_all();
                        Iterator<C_w_value> itor = c_w_values.iterator();
                        while (itor.hasNext()) {
                            C_w_value cw = itor.next();
                            if (cw.from_id == vehicle.route_number.get(1).id || cw.to_id == vehicle.route_number.get(1).id) {

                                itor.remove();
                            }
                        }
                        Iterator<C_w_value> itor1 = c_w_values_back.iterator();
                        while (itor1.hasNext()) {
                            C_w_value cw = itor1.next();
                            if (cw.from_id == vehicle.route_number.get(1).id || cw.to_id == vehicle.route_number.get(1).id) {

                                itor1.remove();
                            }
                        }
                    } else {
                        If_can = true;
                    }
                } else if (last_id == cur_c_w.to_id || last_id == cur_c_w.from_id)//沿着结束找
                {

                    Customer c;
                    if (last_id == cur_c_w.from_id) c = C_VRP.customers.get(cur_c_w.to_id);
                    else c = C_VRP.customers.get(cur_c_w.from_id);
                    if (vehicle.route_weight + c.goods_need < C_VRP.CAPACITY) {
                        vehicle.route_number.get(vehicle.route_number.size()-1).to_id = c.id;
                        vehicle.route_number.add(c);
                        vehicle.set_all();
                        Iterator<C_w_value> itor = c_w_values.iterator();
                        while (itor.hasNext()) {
                            C_w_value cw = itor.next();

                            if (cw.from_id == vehicle.route_number.get(vehicle.route_number.size() - 2).id || cw.to_id == vehicle.route_number.get(vehicle.route_number.size() - 2).id)
                                itor.remove();

                        }
                        Iterator<C_w_value> itor1 = c_w_values_back.iterator();
                        while (itor1.hasNext()) {
                            C_w_value cw = itor1.next();
                            if (cw.from_id == vehicle.route_number.get(vehicle.route_number.size() - 2).id || cw.to_id == vehicle.route_number.get(vehicle.route_number.size() - 2).id) {

                                itor1.remove();
                            }
                        }
                    }
                    else {

                        If_can = true;
                    }

                }
                else//剩下的应该是不在之中的？
                {
                    c_w_values_back.add(cur_c_w);
                    //vehicle.print();
                    //System.out.println();
                }
                if (If_can)//新建路线
                {

                    vehicle.route_number.get(vehicle.route_number.size() - 1).to_id = 0;
                    solution.route_Vehicle.add(vehicle);


                    c_w_values.addAll(c_w_values_back);
                    c_w_values_back.clear();
                    Iterator<C_w_value> itor = c_w_values.iterator();
                    while (itor.hasNext()) {

                        C_w_value cw = itor.next();
                        if (cw.from_id == vehicle.route_number.get(vehicle.route_number.size() - 1).id || cw.to_id == vehicle.route_number.get(vehicle.route_number.size() - 1).id || cw.to_id == vehicle.route_number.get(0).id || cw.from_id == vehicle.route_number.get(0).id) {
                            itor.remove();
                        }
                    }


                    vehicle = new Vehicle();


                    If_can = false;
                    continue;
                }

            }
            if (c_w_values.size() == 0 && c_w_values_back.size() != 0) {

                //System.out.println(c_w_values_back.size());
                c_w_values.addAll(c_w_values_back);
                vehicle.route_number.get(vehicle.route_number.size()-1).to_id=0;
                solution.route_Vehicle.add(vehicle);
                vehicle = new Vehicle();
                c_w_values_back.clear();
                If_can = false;
            }
            if(c_w_values.size() == 0 && c_w_values_back.size() == 0)
                if(vehicle.route_number.size()!=0) {

                    vehicle.route_number.get(vehicle.route_number.size()-1).to_id=0;
                    solution.route_Vehicle.add(vehicle);
                }
        }

      solution.setTotal_length();
      solution.setTotal_weight();

    }
    static Solution C_W(int customer_number)
    {

        c_w_init(customer_number);
        Solution solution = new Solution();
        c_w_al(solution);
        solution.setTotal_length();
        return solution;
    }

}
