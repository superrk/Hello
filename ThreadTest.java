package r1;
/**
*@author Kai Rong
*@date 创建时间：May 28, 2017 10:35:08 AM 
*@version 1.0  
*/

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadTest {
	private int i=1;
    public static void main(String[] args) {
        ThreadTest pc = new ThreadTest();

        Storage s = pc.new Storage();

        ExecutorService service = Executors.newCachedThreadPool();
        Producer p = pc.new Producer("张三", s);
        Producer p2 = pc.new Producer("李四", s);
        Consumer c = pc.new Consumer("王五", s);
        Consumer c2 = pc.new Consumer("老刘", s);
        Consumer c3 = pc.new Consumer("老林", s);
        service.submit(p);
        service.submit(p2);
        service.submit(c);
        service.submit(c2);
        service.submit(c3);
        
    }

    /**
     * 消费者
     * 
     * @author 林计钦
     * @version 1.0 2013-7-24 下午04:53:30
     */
    class Consumer implements Runnable {
        private String name;
        private Storage s = null;

        public Consumer(String name, Storage s) {
            this.name = name;
            this.s = s;
        }

        public void run() {
            try {
                while (true) {
                    System.out.println(name + "准备消费产品.");
                    Product product = s.pop();
                    System.out.println(name + "已消费(" + product.toString() + ").");
                    System.out.println("===============");
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    class Producer implements Runnable {
        private String name;
        private Storage s = null;

        public Producer(String name, Storage s) {
            this.name = name;
            this.s = s;
        }

        public void run() {
            try {
                while (true) {
                    Product product = new Product(i++); // 产生0~9999随机整数
                    System.out.println(name + "准备生产(" + product.toString() + ").");
                    s.push(product);
                    System.out.println(name + "已生产(" + product.toString() + ").");
                    System.out.println("===============");
                    Thread.sleep(500);
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

        }
    }


    public class Storage {
        BlockingQueue<Product> queues = new LinkedBlockingQueue<Product>(10);

        public void push(Product p) throws InterruptedException {
            queues.put(p);
        }

        /**
         * 消费
         * 
         * @return 产品
         * @throws InterruptedException
         */
        public Product pop() throws InterruptedException {
            return queues.take();
        }
    }

    public class Product {
        private int id;

        public Product(int id) {
            this.id = id;
        }

        public String toString() {// 重写toString方法
            return "产品号：" + this.id;
        }
    }

}
