package kakkun61.wajavassr.log;

/**
 * ログ出力インタフェース。
 * 
 * @author Kazuki Okamoto
 */
public interface Log {
    /**
     * Level verbose. Level 1 of 5. Most unimportant.
     */
    public void v(String message);

    /**
     * Level verbose. Level 1 of 5. Most unimportant.
     */
    public void v(String message, Throwable thr);

    /**
     * Level debug. Level 2 of 5.
     */
    public void d(String message);

    /**
     * Level debug. Level 2 of 5.
     */
    public void d(String message, Throwable thr);

    /**
     * Level information. Level 3 of 5.
     */
    public void i(String message);

    /**
     * Level information. Level 3 of 5.
     */
    public void i(String message, Throwable thr);

    /**
     * Level warning. Level 4 of 5.
     */
    public void w(String message);

    /**
     * Level warning. Level 4 of 5.
     */
    public void w(String message, Throwable thr);

    /**
     * Level error. Level 5 of 5.
     */
    public void e(String message);

    /**
     * Level error. Level 5 of 5.
     */
    public void e(String message, Throwable thr);
}
