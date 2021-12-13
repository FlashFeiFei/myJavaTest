
public class Main {

    public static void main(String[] args) throws InterruptedException {

        try {
            Thread t = new MyThread();
            t.start();
        }catch (RuntimeException e){
            System.out.println("主线程能够捕获子线程的异常： "+ e.getMessage());
        }

        Thread.sleep(20000);
        System.out.println("到这里吗");
    }
}
