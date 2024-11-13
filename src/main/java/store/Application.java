package store;

public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext();
        ConvenienceStore convenienceStore = new ConvenienceStore(applicationContext);
        convenienceStore.run();
    }
}
