package renderer;

record Pixel(int row, int col) {
    private static int maxRows = 0;
    private static int maxCols = 0;
    private static long totalPixels = 0l;
    private static volatile int cRow = 0;
    private static volatile int cCol = -1;
    private static volatile long pixels = 0l;
    private static volatile int lastPrinted = 0;
    private static boolean print = false;
    private static long printInterval = 100l;
    private static final String PRINT_FORMAT = "%5.1f%%\r";
    private static Object mutexNext = new Object();
    private static Object mutexPixels = new Object();

    static void initialize(int maxRows, int maxCols, double interval) {
        Pixel.maxRows = maxRows;
        Pixel.maxCols = maxCols;
        Pixel.totalPixels = (long) maxRows * maxCols;
        printInterval = (int) (interval * 10);
        if (print = printInterval != 0) System.out.printf(PRINT_FORMAT, 0d);
    }

    static Pixel nextPixel() {
        synchronized (mutexNext) {
            if (cRow == maxRows) return null;
            ++cCol;
            if (cCol < maxCols) return new Pixel(cRow, cCol);
            cCol = 0;
            ++cRow;
            if (cRow < maxRows) return new Pixel(cRow, cCol);
        }
        return null;
    }

    static void pixelDone() {
        boolean flag = false;
        int percentage = 0;
        synchronized (mutexPixels) {
            ++pixels;
            if (print) {
                percentage = (int) (1000l * pixels / totalPixels);
                if (percentage - lastPrinted >= printInterval) {
                    lastPrinted = percentage;
                    flag = true;
                }
            }
        }
        if (flag) System.out.printf(PRINT_FORMAT, percentage / 10d);
    }
}
