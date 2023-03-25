package rate_limited_printer;

public class RateLimitedPrinter implements Printable {
    private int interval;
    private long lastPrintTime;

    public RateLimitedPrinter(int interval) {
        this.interval = interval;
    }

    @Override
    public void print(String message) {
        long thisTime = System.currentTimeMillis();
        if (thisTime - lastPrintTime > interval) {
            System.out.println(message);
            lastPrintTime = thisTime;
        }
    }

}
