package kakkun61.wajavassr.log;

/**
 * ログ出力インターフェースの標準エラー出力実装。
 * 
 * @author Kazuki Okamoto
 */
public class StandardErrorLog implements Log {
    @Override
    public void v(String message) {
        System.err.println("verbose: " + message);
    }

    @Override
    public void v(String message, Throwable thr) {
        System.err.println("verbose: " + message);
        thr.printStackTrace();
    }

    @Override
    public void d(String message) {
        System.err.println("debug: " + message);
    }

    @Override
    public void d(String message, Throwable thr) {
        System.err.println("debug: " + message);
        thr.printStackTrace();
    }

    @Override
    public void i(String message) {
        System.err.println("information: " + message);
    }

    @Override
    public void i(String message, Throwable thr) {
        System.err.println("information: " + message);
        thr.printStackTrace();
    }

    @Override
    public void w(String message) {
        System.err.println("warning: " + message);
    }

    @Override
    public void w(String message, Throwable thr) {
        System.err.println("warning: " + message);
        thr.printStackTrace();
    }

    @Override
    public void e(String message) {
        System.err.println("error: " + message);
    }

    @Override
    public void e(String message, Throwable thr) {
        System.err.println("error: " + message);
        thr.printStackTrace();
    }
}
