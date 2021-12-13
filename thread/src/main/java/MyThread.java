
public class MyThread extends Thread{
    public void run(){
        throw new RuntimeException("线程中抛出异常会怎么样");
    }
}
